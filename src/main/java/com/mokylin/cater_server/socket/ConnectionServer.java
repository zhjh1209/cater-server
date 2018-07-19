package com.mokylin.cater_server.socket;

import com.jayway.jsonpath.internal.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.util.concurrent.atomic.AtomicReference;

public class ConnectionServer implements Closeable {

	private static final Logger logger = LoggerFactory.getLogger(ConnectionServer.class);
    private final AtomicReference<Stage> stage = new AtomicReference<Stage>(Stage.INIT);
    private final Server nettyServer;
    
    public ConnectionServer(Server server) {
        this.nettyServer = server;
    }

    public void start() {
        if (!stage.compareAndSet(Stage.INIT, Stage.STARTED)) {
            throw new IllegalStateException("GameServer illegal stage: " + stage.get());
        }
        nettyServer.start();
    }

    public void close() {
        if (!stage.compareAndSet(Stage.STARTED, Stage.STOPPED)) {
            throw new IllegalStateException("GameServer illegal stage: " + stage.get());
        }

        logger.info("start close nettyServer");
        Utils.closeQuietly(nettyServer);
        logger.info("nettyServer closed");
    }

    private enum Stage {
        INIT, STARTED, STOPPED
    }

}
