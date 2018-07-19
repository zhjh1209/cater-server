package com.mokylin.cater_server;

import com.mokylin.cater_server.service.ThreadExecutorService;
import com.mokylin.cater_server.socket.ConnectionServer;
import com.mokylin.cater_server.socket.NettyServer;
import com.mokylin.cater_server.socket.Server;
import com.mokylin.cater_server.socket.WorkerFactory;
import com.mokylin.cater_server.socket.handler.MsgHandlerManager;

import org.jboss.netty.handler.ssl.SslContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;

@SpringBootApplication
public class ServerApplication implements CommandLineRunner {
    @Value("${wss.ip}")
    private String wssIp;
    @Value("${wss.port}")
    private int wssPort;
    @Value("${ssl.pem}")
    private String sslPem;
    @Value("${ssl.key}")
    private String sslKey;
    @Autowired
    @Qualifier("workerFactory")
    private WorkerFactory workerFactory;
    @Autowired
    private ConnectionServer gameServer;
    @Autowired
    private MsgHandlerManager handlerManager;
    @Autowired
    private ThreadExecutorService executorService;

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> shutdownTask()));
        handlerManager.initMsgHandlers();
        this.gameServer.start();
    }

    public void shutdownTask() {
        gameServer.close();
        executorService.close();
    }

    @Bean
    public ConnectionServer getConnectionServer() throws Exception {
        Server server = new NettyServer(wssPort, workerFactory,
                SslContext.newServerContext(new File(sslPem), new File(sslKey)));
        return new ConnectionServer(server);
    }
}
