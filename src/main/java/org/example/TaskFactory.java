package org.example;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;

public class TaskFactory {
    private static int taskCounter = 0;
    private int numberOfTasks;
    private ConnectionPool connectionPool;

    public TaskFactory() {
    }

    public TaskFactory(int numberOfTasks, ConnectionPool connectionPool) {
        this.numberOfTasks = numberOfTasks;
        this.connectionPool = connectionPool;
    }

    public void getTasks() {
        BlockingQueue<CompletableFuture<Void>> l = new ArrayBlockingQueue<>(numberOfTasks);
        System.out.printf("Task Factory (%s) starts creating tasks\n", Thread.currentThread().getName());
        for (int i = 0; i < numberOfTasks; i++) {
            l.add(getTask());
        }
        System.out.printf("Task Factory (%s) ends creating tasks\n", Thread.currentThread().getName());
        CompletableFuture.allOf(l.toArray(new CompletableFuture[l.size()])).join();
    }

    private CompletableFuture<Void> getTask() {
        int taskId = ++taskCounter;
        return CompletableFuture.supplyAsync(() -> {
                    Connection conn = connectionPool.getConnection();
                    System.out.printf("Task %d Step 1: %s has got a connection: %d\n", taskId,
                            Thread.currentThread().getName(), conn.getId());
                    return conn;
                })
                .thenApply(p -> {
                    System.out.printf("Task %d Step 2a: %s is doing some work\n",
                            taskId, Thread.currentThread().getName());
                    try {
                        Thread.sleep(800 * (int) ((Math.random() * (5))));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.err.println("Thread Interrupted");
                    }
                    System.out.printf("Task %d Step 2b: %s has finished working\n",
                            taskId, Thread.currentThread().getName());
                    return p;
                }).thenAccept(p -> {
                    System.out.printf("Task %d Step 3: %s is about to release the connection: %d\n",
                            taskId, Thread.currentThread().getName(), p.getId());
                    connectionPool.releaseConnection(p);
                });
    }

    public int getNumberOfTasks() {
        return numberOfTasks;
    }

    public void setNumberOfTasks(int numberOfTasks) {
        this.numberOfTasks = numberOfTasks;
    }

    public ConnectionPool getConnectionPool() {
        return connectionPool;
    }

    public void setConnectionPool(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }
}
