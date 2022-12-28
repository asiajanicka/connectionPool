package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ThreadFactory implements Runnable {
    private int numberOfThread;
    private ConnectionPool connectionPool;

    public ThreadFactory() {
    }

    public ThreadFactory(int numberOfThread, ConnectionPool connectionPool) {
        this.numberOfThread = numberOfThread;
        this.connectionPool = connectionPool;
    }

    @Override
    public void run() {
        List<CompletableFuture<Void>> l = new ArrayList<>();
        System.out.printf("Thread Factory (%s) starts creating threads\n", Thread.currentThread().getName());
        for (int i = 0; i < numberOfThread; i++) {
            l.add(CompletableFuture.runAsync(new MyThread(connectionPool)));
        }
        System.out.printf("Thread Factory (%s) ends creating threads\n", Thread.currentThread().getName());

        CompletableFuture.allOf(l.toArray(new CompletableFuture[l.size()])).join();
    }

    public int getNumberOfThread() {
        return numberOfThread;
    }

    public void setNumberOfThread(int numberOfThread) {
        this.numberOfThread = numberOfThread;
    }

    public ConnectionPool getConnectionPool() {
        return connectionPool;
    }

    public void setConnectionPool(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }
}
