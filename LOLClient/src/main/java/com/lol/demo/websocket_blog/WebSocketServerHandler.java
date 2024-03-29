package com.lol.demo.websocket_blog;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderNames.HOST;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

    // websocket 服务的 uri
    private static final String WEBSOCKET_PATH = "/websocket";
    // 本次请求的 code
    private static final String HTTP_REQUEST_STRING = "request";
    // 登录用户表 如果用户不存在于该表,收到的消息不广播,也不入库
    private static Map<String, Client> loginClientMap = new HashMap<>();
    // 一个 ChannelGroup 代表一个直播频道
    private static Map<Integer, ChannelGroup> channelGroupMap = new HashMap<>();
    private Client client = null;

    private WebSocketServerHandshaker handshaker;

    private static void sendHttpResponse(
            ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
        // Generate an error page if response getStatus code is not OK (200).
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
            HttpHeaders.setContentLength(res, res.content().readableBytes());
        }

        // Send the response and close the connection if necessary.
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!HttpHeaders.isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private static String getWebSocketLocation(FullHttpRequest req) {
        String location = req.headers().get(HOST) + WEBSOCKET_PATH;
        System.out.println("location : " + location);
        if (WebSocketServer.SSL) {
            return "wss://" + location;
        } else {
            return "ws://" + location;
        }
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        // Handle a bad request.
        if (!req.decoderResult().isSuccess()) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST));
            return;
        }

        // Allow only GET methods.
        if (req.method() != GET) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN));
            return;
        }

        if ("/favicon.ico".equals(req.uri())) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND));
            return;
        }

        if ("/".equals(req.uri())) {
            String attr = getWebSocketLocation(req) + "?request=e2lkOjE7cmlkOjI2O3Rva2VuOiI0MzYwNjgxMWM3MzA1Y2NjNmFiYjJiZTExNjU3OWJmZCJ9";
            ByteBuf content = WebSocketServerIndexPage.getContent(attr);
            FullHttpResponse res = new DefaultFullHttpResponse(HTTP_1_1, OK, content);

            res.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
            HttpHeaders.setContentLength(res, content.readableBytes());

            sendHttpResponse(ctx, req, res);
            return;
        }

        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(req.uri());
        Map<String, List<String>> parameters = queryStringDecoder.parameters();

        if (parameters.size() == 0 || !parameters.containsKey(HTTP_REQUEST_STRING)) {
            System.err.printf(HTTP_REQUEST_STRING + "参数不可缺省");
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND));
            return;
        }

        client = RequestService.clientRegister(parameters.get(HTTP_REQUEST_STRING).get(0));
        if (client.getRoomId() == 0) {
            System.err.printf("房间号不可缺省");
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND));
            return;
        }

        // 房间列表中如果不存在则为该频道,则新增一个频道 ChannelGroup
        if (!channelGroupMap.containsKey(client.getRoomId())) {
            channelGroupMap.put(client.getRoomId(), new DefaultChannelGroup(GlobalEventExecutor.INSTANCE));
        }
        // 确定有房间号,才将客户端加入到频道中
        channelGroupMap.get(client.getRoomId()).add(ctx.channel());

        req.headers().set("Connection", "Upgrade");
        req.headers().set("Upgrade", "WebSocket");
        String path = getWebSocketLocation(req) + "?" + HTTP_REQUEST_STRING + "=" + parameters.get(HTTP_REQUEST_STRING).get(0);
        System.out.println(path);
        // Handshake
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(path, null, true);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            ChannelFuture channelFuture = handshaker.handshake(ctx.channel(), req);

            // 握手成功之后,业务逻辑 注册
            if (channelFuture.isSuccess()) {
                if (client.getId() == 0) {
                    System.out.println(ctx.channel() + " 游客");
                    return;
                }

                loginClientMap.put(ctx.channel().id().asShortText(), client);
            }
        }
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {

        // Check for closing frame
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        if (!(frame instanceof TextWebSocketFrame)) {
            throw new UnsupportedOperationException(String.format("%s frame types not supported", frame.getClass()
                    .getName()));
        }

        broadcast(ctx, frame);
    }

    private void broadcast(ChannelHandlerContext ctx, WebSocketFrame frame) {

        if (!loginClientMap.containsKey(ctx.channel().id().asShortText())) {
            Response response = new Response(1001, "没登录不能聊天哦");
            String msg = new JSONObject(response).toString();
            ctx.channel().write(new TextWebSocketFrame(msg));
            return;
        }

        String request = ((TextWebSocketFrame) frame).text();
        System.out.println(" 收到 " + ctx.channel() + request);


        Response response = MessageService.sendMessage(client, request);
        String msg = new JSONObject(response).toString();
        if (channelGroupMap.containsKey(client.getRoomId())) {
            channelGroupMap.get(client.getRoomId()).writeAndFlush(new TextWebSocketFrame(msg));
        }

    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        System.out.println("收到" + incoming.remoteAddress() + " 握手请求");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        String channelId = ctx.channel().id().asShortText();

        if (loginClientMap.containsKey(channelId)) {
            loginClientMap.remove(channelId);
        }

        if (client != null && channelGroupMap.containsKey(client.getRoomId())) {
            channelGroupMap.get(client.getRoomId()).remove(ctx.channel());
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
