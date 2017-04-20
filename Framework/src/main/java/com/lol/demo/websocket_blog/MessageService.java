package com.lol.demo.websocket_blog;

public class MessageService {

    public static Response sendMessage(Client client, String message) {
        Response res = new Response();
        res.getData().put("id", client.getId());
        res.getData().put("message", message);
        res.getData().put("ts", System.currentTimeMillis());// 返回毫秒数
        return res;
    }
}
