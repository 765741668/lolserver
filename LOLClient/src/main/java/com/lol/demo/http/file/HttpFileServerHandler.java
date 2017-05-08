package com.lol.demo.http.file;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderUtil.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaderUtil.setContentLength;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @author Administroter
 * @version 1.0
 * @FileName HttpFileServerHandler.java
 * @Description:
 * @Date 2016年3月8日
 */
public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private String url;

    public HttpFileServerHandler(String url) {
        this.url = url;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        // 如果出现编码错误，跳转404路径错误页面
        if (!request.decoderResult().isSuccess()) {
            HttpUrlKit.sendError(ctx, BAD_REQUEST);
            return;
        }
        // 如果不是浏览器或者表单发送get请求，跳转405错误
        if (request.method() != GET) {
            HttpUrlKit.sendError(ctx, METHOD_NOT_ALLOWED);
        }
        final String uri = request.uri();
        // 对具体的包装具体的url路径
//        final String path = HttpUrlKit.sanitizeUri(uri, url);
        final String path = HttpUrlKit.sanitizeCustomUri(uri, url.substring(2), url.substring(0, 2));
        if (path == null) {
            HttpUrlKit.sendError(ctx, FORBIDDEN);
            return;
        }
        // 获取文件对象
        File file = new File(path);
        // 文件属于隐藏文件或者不存在
        if (file.isHidden() || !file.exists()) {
            HttpUrlKit.sendError(ctx, NOT_FOUND);
            return;
        }
        // 查看路径名表示的是否是一个目录，如果是，则发送目录的链接给客户端
        if (file.isDirectory()) {
            if (uri.endsWith("/")) {
                HttpUrlKit.sendListing(ctx, file);
            } else {
                HttpUrlKit.sendRedirect(ctx, uri + "/");
            }
            return;
        }
        // 查看路径名表示的文件是否是一个标准文件
        if (!file.isFile()) {
            HttpUrlKit.sendError(ctx, FORBIDDEN);
            return;
        }
        // 构建随机访问文件的读取和写入
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "r");
        } catch (FileNotFoundException e) {
            HttpUrlKit.sendError(ctx, NOT_FOUND);
            return;
        }
        // 获取文件的长度，构造http答应消息
        long fileLength = raf.length();
        HttpResponse hresp = new DefaultHttpResponse(HTTP_1_1, OK);
        setContentLength(request, fileLength);
        // 设置响应文件的mime类型，即文件的扩展名，而客户端浏览器获取这个类型以后，根绝mime来决定采用什么应用程序来处理数据
        HttpUrlKit.setContentTypeHeader(hresp, file);
        if (isKeepAlive(request)) {
            hresp.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        // 发送消息
        ctx.write(hresp);
        ChannelFuture sendFileFuture;
        // 将文件写入到发送缓冲区
        sendFileFuture = ctx.write(new ChunkedFile(raf, 0, fileLength, 8192), ctx.newProgressivePromise());
        // 增加ChannelFuture的监听
        sendFileFuture.addListener(new ChannelProgressiveFutureListener() {
            @Override
            public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) {
                if (total < 0) {
                    System.err.println("Transfer progress: " + progress);
                } else {
                    System.err.println("Transfer progress: " + progress + " / " + total);
                }
            }

            // 发送完成信息后触发
            @Override
            public void operationComplete(ChannelProgressiveFuture future) throws Exception {
                System.out.println("Transfer complete.");
            }
        });
        // 发送编码结束的空消息体，标识消息体发送完成，同时小勇flush方法将发送消息缓冲区的消息刷新到SocketChannel
        ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        // 如果非keepalive则标识发送完成，关闭连接
        if (!isKeepAlive(request)) {
            lastContentFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if (ctx.channel().isActive()) {
            HttpUrlKit.sendError(ctx, INTERNAL_SERVER_ERROR);
        }
    }
}