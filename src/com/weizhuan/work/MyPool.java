package com.weizhuan.work;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MyPool {

    private static ExecutorService instance = null;

    public synchronized static ExecutorService getInstance() {
        if (instance == null) {
            AtomicInteger processCount = new AtomicInteger(Math.max(Runtime.getRuntime().availableProcessors(), 1));

            System.out.println("This CPU Processors:" + processCount.toString());

            instance = new ThreadPoolExecutor(processCount.get(), 20,
                    6L, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(),
                    Executors.defaultThreadFactory(),
                    new ThreadPoolExecutor.CallerRunsPolicy());
        }
        return instance;
    }

}
