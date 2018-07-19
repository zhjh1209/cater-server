package com.mokylin.cater_server.socket.message;

import com.mokylin.cater_server.service.WorldService;
import com.mokylin.cater_server.socket.Worker;
import com.mokylin.cater_server.socket.WorkerFactory;
import com.mokylin.cater_server.socket.handler.MsgHandlerManager;

import org.jboss.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("workerFactory")
public class MessageWorkerFactory implements WorkerFactory {
    @Autowired
    private WorldService worldService;
    @Autowired
    private MsgHandlerManager msgHandlerManager;

    public Worker newWorker(Channel channel) {
        return new MessageWorker(channel, worldService, msgHandlerManager);
    }

}
