package com.lol.demo.http.file;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Pattern;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderNames.LOCATION;
import static io.netty.handler.codec.http.HttpResponseStatus.FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @author Administroter
 * @version 1.0
 * @FileName HttpFileServer.java
 * @Description:
 * @Date 2016年3月7日
 */
@SuppressWarnings("restriction")
public class HttpUrlKit {

    private static final Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");
    private static final Pattern ALLOWED_FILE_NAME = Pattern.compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*");

    /**
     * @param uri
     * @param url
     * @return
     * @Title: sanitizeUri
     * @Description:uri路径合法性校验
     * @author Administroter
     * @date 2016年3月11日
     */
    public static String sanitizeUri(String uri, String url) {
        try {
            // 对URL进行解码
            uri = URLDecoder.decode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            try {
                uri = URLDecoder.decode(uri, "ISO-8859-1");
            } catch (UnsupportedEncodingException e1) {
                throw new Error();
            }
        }
        /**
         * 对uri进行合法性的判断
         */
        if (!uri.startsWith(url)) {
            return null;
        }
        if (!uri.startsWith("/")) {
            return null;
        }
        // 将硬编码的文件路径分隔符替换为本地操作系统文件路径分隔符，比如C:\Users\hfgff\Desktop就是将其中的“\”替换成“/”
        uri = uri.replace('/', File.separatorChar);
        // 对uri进行第二次合法性验证验证请求的路径当中是否含有"\."或者".\"或者以"."开头或者结尾,再匹配正则规则。
        if (uri.contains(File.separator + '.') || uri.contains('.' + File.separator) || uri.startsWith(".") || uri.endsWith(".")
                || INSECURE_URI.matcher(uri).matches()) {
            return null;
        }
        // 构造项目目录+uri构造绝对路径返回(System.getProperty("player.dir")表用户当前的工作目录)
        return System.getProperty("player.dir") + File.separator + uri;
    }

    public static String sanitizeCustomUri(String uri, String url, String urlPrefix) {
        try {
            // 对URL进行解码
            uri = URLDecoder.decode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            try {
                uri = URLDecoder.decode(uri, "ISO-8859-1");
            } catch (UnsupportedEncodingException e1) {
                throw new Error();
            }
        }
        /**
         * 对uri进行合法性的判断
         */
        if (!uri.startsWith(url)) {
            return null;
        }
        if (!uri.startsWith("/")) {
            return null;
        }
        // 将硬编码的文件路径分隔符替换为本地操作系统文件路径分隔符，比如C:\Users\hfgff\Desktop就是将其中的“\”替换成“/”
        uri = uri.replace('/', File.separatorChar);
        // 对uri进行第二次合法性验证验证请求的路径当中是否含有"\."或者".\"或者以"."开头或者结尾,再匹配正则规则。
        if (uri.contains(File.separator + '.') || uri.contains('.' + File.separator) || uri.startsWith(".") || uri.endsWith(".")
                || INSECURE_URI.matcher(uri).matches()) {
            return null;
        }
        // 构造项目目录+uri构造绝对路径返回(System.getProperty("player.dir")表用户当前的工作目录)
        return urlPrefix + File.separator + uri;
    }

    /**
     * @param ctx
     * @param dir
     * @Title: sendListing
     * @Description:构建请求路径的文件目录
     * @author Administroter
     * @date 2016年3月11日
     */

    public static void sendListing(ChannelHandlerContext ctx, File dir) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK);
        // 这里显示在浏览器，采用html的格式，设置消息头的类型
        response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
        // 构建消息响应信息体
        StringBuilder buf = new StringBuilder();
        String dirPath = dir.getPath();
        buf.append("<!DOCTYPE html>\r\n");
        buf.append("<html><head><title>");
        buf.append(dirPath);
        buf.append(" 目录：");
        buf.append("</title></head><body>\r\n");
        buf.append("<h3>");
        buf.append("Netty-http文件服务器：");
        buf.append("</h3>\r\n");
        buf.append("<ul>");
        buf.append("<li><a href=\"../\">返回上一级</a></li>\r\n");
        for (File f : dir.listFiles()) {
            if (f.isHidden() || !f.canRead()) {
                continue;
            }
            String name = f.getName();
            if (!ALLOWED_FILE_NAME.matcher(name).matches()) {
                continue;
            }
            buf.append("<li>链接：<a href=\"");
            buf.append(name);
            buf.append("\">");
            buf.append(name);
            buf.append("</a></li>\r\n");
        }
        buf.append("</ul></body></html>\r\n");
        // 分配对应消息的缓冲对象
        ByteBuf buffer = Unpooled.copiedBuffer(buf, CharsetUtil.UTF_8);
        // 将缓冲区的消息存放到http答应消息中
        response.content().writeBytes(buffer);
        // 释放缓冲区
        buffer.release();
        // 将响应消息发送到缓冲区并刷新到SocketChannel中
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * @param ctx
     * @param newUri
     * @Title: sendRedirect
     * @Description:
     * @author Administroter
     * @date 2016年3月11日
     */
    public static void sendRedirect(ChannelHandlerContext ctx, String newUri) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, FOUND);
        response.headers().set(LOCATION, newUri);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    public static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status, Unpooled.copiedBuffer("Failure: " + status.toString()
                + "\r\n", CharsetUtil.UTF_8));
        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    public static void setContentTypeHeader(HttpResponse response, File file) {
        MimetypesFileTypeMap mft = new MimetypesFileTypeMap();
        // 获取指定文件的扩展类型，并设置成消息消息头的类型，用于响应客户端后，浏览器根据这个，来决定采用
        // 浏览器嵌入的哪个应用程序模块处理响应的数据
        response.headers().set(CONTENT_TYPE, mft.getContentType(file.getPath()));
    }
}