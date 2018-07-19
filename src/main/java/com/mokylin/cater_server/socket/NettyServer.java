package com.mokylin.cater_server.socket;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.DefaultChannelPipeline;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.netty.handler.ssl.SslContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class NettyServer implements Server {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    private static final int DEFAULT_MSG_SIZE_LIMIT = 65536;

    private final ChannelFactory factory;
    private final SslContext sslContext;
    private final ServerBootstrap serverBootstrap;
    private final ChannelGroup allChannels;
    private final int port;

    public NettyServer(int port, final WorkerFactory workerFactory, SslContext sslContext) {
        this(port, DEFAULT_MSG_SIZE_LIMIT, workerFactory, sslContext);
    }

    public NettyServer(int port, int msgSizeLimit, WorkerFactory workerFactory,
            SslContext sslContext) {
        this(port, msgSizeLimit, workerFactory, new DefaultChannelGroup(), sslContext);
    }

    NettyServer(int port, final WorkerFactory workerFactory, ChannelGroup group,
            SslContext sslContext) {
        this(port, DEFAULT_MSG_SIZE_LIMIT, workerFactory, group, sslContext);
    }

    NettyServer(int port, final int msgSizeLimit, final WorkerFactory workerFactory,
            ChannelGroup group, SslContext sslContext) {
        this.port = port;
        this.sslContext = sslContext;
        allChannels = group;

        factory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool());

        serverBootstrap = new ServerBootstrap(factory);

        serverBootstrap.setOption("child.tcpNoDelay", true);
        serverBootstrap.setOption("child.keepAlive", false);
        serverBootstrap.setOption("child.sendBufferSize", 8192 * 4);
        serverBootstrap.setOption("child.receiveBufferSize", 8192 * 2);

        serverBootstrap.setPipelineFactory(new ChannelPipelineFactory() {

            public ChannelPipeline getPipeline() throws Exception {
                DefaultChannelPipeline result = new DefaultChannelPipeline();
                if (sslContext != null) {
                    result.addLast("sslHandler", sslContext.newHandler());
                }
                result.addLast("decoder", new HttpRequestDecoder());
                result.addLast("aggregator", new HttpChunkAggregator(65536));
                result.addLast("encoder", new HttpResponseEncoder());
                result.addLast("websocketHandler", new WebSocketHandler("ws"));
                result.addLast("dataHandler",
                        new SingleDataDecoderHandler(allChannels, workerFactory, msgSizeLimit));
                return result;
            }
        });
    }

    public int start(String address) {
        Channel channel = serverBootstrap.bind(new InetSocketAddress(address, port));
        int listeningPort = ((InetSocketAddress) channel.getLocalAddress()).getPort();
        logger.debug("bind on port: {}", listeningPort);
        allChannels.add(channel);
        logger.info("Server started. Listening at {}:{}", address, listeningPort);

        return listeningPort;
    }

    public int start() {
        return start("0.0.0.0");
    }

    public void close() throws IOException {
        logger.info("start close allChannels");
        allChannels.close().awaitUninterruptibly();
        logger.info("allChannels closed ");

        logger.info("start close serverBootstrap");
        serverBootstrap.releaseExternalResources();
        logger.info("serverBootstrap closed");
    }

}
