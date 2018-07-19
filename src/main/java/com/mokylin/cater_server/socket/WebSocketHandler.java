package com.mokylin.cater_server.socket;

import com.alibaba.fastjson.JSONObject;
import com.mokylin.cater_server.socket.message.MessagePacket;

import org.jboss.netty.channel.ChannelDownstreamHandler;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.DownstreamMessageEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.UpstreamMessageEvent;
import org.jboss.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import org.jboss.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketHandler extends WebSocketServerProtocolHandler
        implements ChannelDownstreamHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());

    public WebSocketHandler(String websocketPath) {
        super(websocketPath);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        if (e.getMessage() instanceof WebSocketFrame) {
            WebSocketFrame frame = (WebSocketFrame) e.getMessage();
            if (frame instanceof CloseWebSocketFrame) {
                WebSocketServerHandshaker handshaker = getHandshaker(ctx);
                handshaker.close(ctx.getChannel(), (CloseWebSocketFrame) frame);
            } else if (frame instanceof PingWebSocketFrame) {
                ctx.getChannel().write(new PongWebSocketFrame(frame.getBinaryData()));
            } else if (frame instanceof TextWebSocketFrame) {
                ctx.sendUpstream(new UpstreamMessageEvent(ctx.getChannel(),
                        ((TextWebSocketFrame) frame).getText(),
                        ctx.getChannel().getRemoteAddress()));
            } else if (frame instanceof BinaryWebSocketFrame) {
                logger.error("收到二进制包，不处理");
            }
        } else {
            ctx.sendUpstream(e);
        }
    }

    static WebSocketServerHandshaker getHandshaker(ChannelHandlerContext ctx) {
        return (WebSocketServerHandshaker) ctx.getAttachment();
    }

    @Override
    public void handleDownstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
        if (e instanceof DownstreamMessageEvent) {
            DownstreamMessageEvent de = (DownstreamMessageEvent) e;
            if (((DownstreamMessageEvent) e).getMessage() instanceof MessagePacket) {
                MessagePacket messagePacket =
                        (MessagePacket) ((DownstreamMessageEvent) e).getMessage();
                ctx.sendDownstream(new DownstreamMessageEvent(de.getChannel(), de.getFuture(),
                        new TextWebSocketFrame(messagePacket.getType() +
                                (messagePacket.getMessage() == null ? "" :
                                        ":" + JSONObject.toJSONString(messagePacket.getMessage()))),
                        de.getRemoteAddress()));
            } else {
                ctx.sendDownstream(e);
            }
        } else {
            ctx.sendDownstream(e);
        }
    }
}
