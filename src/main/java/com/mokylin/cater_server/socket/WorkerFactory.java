package com.mokylin.cater_server.socket;

import org.jboss.netty.channel.Channel;

public interface WorkerFactory {
	Worker newWorker(Channel var1);
}
