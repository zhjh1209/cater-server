package com.mokylin.cater_server.socket;

import com.alibaba.fastjson.JSONObject;
import com.mokylin.cater_server.socket.message.MessageEntity;
import com.mokylin.cater_server.socket.message.MessagePacket;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.group.ChannelGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SingleDataDecoderHandler extends SimpleChannelUpstreamHandler {
    private static final Logger logger = LoggerFactory.getLogger(SingleDataDecoderHandler.class);
    private final ChannelGroup channelGroup;
    private final WorkerFactory workerFactory;
    private final int msgSizeLimit;

    public SingleDataDecoderHandler(ChannelGroup _channelGroup, WorkerFactory workerFactory,
            int msgSizeLimit) {
        this.channelGroup = _channelGroup;
        this.workerFactory = workerFactory;
        this.msgSizeLimit = msgSizeLimit;
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        Channel channel = ctx.getChannel();
        Worker worker = workerFactory.newWorker(channel);
        channel.setAttachment(worker);
        channelGroup.add(channel);
        worker.onConnected();
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e)
            throws Exception {
        Channel channel = ctx.getChannel();
        channelGroup.remove(channel);
        if (channel.getAttachment() instanceof Worker) {
            Worker worker = (Worker) channel.getAttachment();
            if (worker != null) {
                channel.setAttachment(null);
                worker.onDisconnect();
            }
        }
        super.channelDisconnected(ctx, e);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        e.getCause().printStackTrace();
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        if (e.getMessage() instanceof String) {
            if (ctx.getChannel().getAttachment() instanceof Worker) {
                String message = (String) e.getMessage();
                String[] data = message.split(":", 2);
                ((Worker) ctx.getChannel().getAttachment()).onMessage(new MessagePacket(data[0],
                        data.length > 1 ? JSONObject.parseObject(data[1], MessageEntity.class) :
                                null));
            }
        } else {
            logger.error("unknow message received！");
        }
    }

}
