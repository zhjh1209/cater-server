package com.mokylin.cater_server.service;

import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ThreadExecutorService implements Closeable {
    private ExecutorService common;
    private ScheduledExecutorService schedule;
    private ThreadPoolExecutor[] poolExecutors;

    public ThreadExecutorService() {
        common = Executors.newFixedThreadPool(4, new NamedThreadFactory("common-pool-thread-"));
        schedule = Executors
                .newScheduledThreadPool(8, new NamedThreadFactory("schedule-pool-thread-"));
        poolExecutors = new ThreadPoolExecutor[4];
        for (int i = 0; i < poolExecutors.length; i++) {
            poolExecutors[i] = new ThreadPoolExecutor(1, 1, 1000, TimeUnit.MILLISECONDS,
                    new ArrayBlockingQueue<>(1024), new NamedThreadFactory("user-pool-" + i + "-"));
        }
    }

    public ExecutorService getCommon() {
        return common;
    }

    public ScheduledExecutorService getSchedule() {
        return schedule;
    }

    public ThreadPoolExecutor get(long index) {
        int hash = (int) (index & 0xffffffffL);
        int i = hash & (poolExecutors.length - 1);
        return poolExecutors[i];
    }

    @Override
    public void close() {
        common.shutdown();
        schedule.shutdown();
        for (int i = 0; i < poolExecutors.length; i++) {
            poolExecutors[i].shutdown();
        }
    }

    public static class NamedThreadFactory implements ThreadFactory {
        private final String namePrefix;
        private final AtomicInteger counter;

        public NamedThreadFactory(String namePrefix) {
            this.namePrefix = namePrefix;
            this.counter = new AtomicInteger(0);
        }

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, namePrefix + counter.incrementAndGet());
        }
    }
}
