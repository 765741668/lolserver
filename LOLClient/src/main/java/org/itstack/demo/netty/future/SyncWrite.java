package org.itstack.demo.netty.future;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.itstack.demo.netty.msg.Request;
import org.itstack.demo.netty.msg.Response;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by fuzhengwei1 on 2016/10/20.
 */
public class SyncWrite {

    public Request writeAndSync(final Channel channel, final Response request, final long timeout) throws Exception {

        if (channel == null) {
            throw new NullPointerException("channel");
        }
        if (request == null) {
            throw new NullPointerException("request");
        }
        if (timeout <= 0) {
            throw new IllegalArgumentException("timeout <= 0");
        }

        String requestId = UUID.randomUUID().toString();
        request.setRequestId(requestId);

        WriteFuture<Request> future = new SyncWriteFuture(request.getRequestId());
        SyncWriteMap.syncKey.put(request.getRequestId(), future);

        Request response = doWriteAndSync(channel, request, timeout, future);

        SyncWriteMap.syncKey.remove(request.getRequestId());
        return response;
    }

    private Request doWriteAndSync(final Channel channel, final Response request, final long timeout, final WriteFuture<Request> writeFuture) throws Exception {

        channel.writeAndFlush(request).addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                writeFuture.setWriteResult(future.isSuccess());
                writeFuture.setCause(future.cause());
                //失败移除
                if (!writeFuture.isWriteSuccess()) {
                    SyncWriteMap.syncKey.remove(writeFuture.requestId());
                }
            }
        });

        Request response = writeFuture.get(timeout, TimeUnit.MILLISECONDS);
        if (response == null) {
            if (writeFuture.isTimeout()) {
                throw new TimeoutException();
            } else {
                // write exception
                throw new Exception(writeFuture.cause());
            }
        }
        return response;
    }

}
