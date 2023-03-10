package org.example;

public class MyThread extends Thread {
    private ConnectionPool connectionPool;

    public MyThread() {
    }

    public MyThread(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public void run() {
        Connection con = connectionPool.getConnection();
        System.out.printf("%s has got a connection: %d\n", getName(), con.getId());
        task();
        System.out.printf("%s is about to release the connection: %d\n", getName(), con.getId());
        connectionPool.releaseConnection(con);
    }

    private void task() {
        try {
            System.out.printf("%s starts doing some work\n", getName());
            Thread.sleep(800 * (int) ((Math.random() * (5))));
            System.out.printf("%s has finished working\n", getName());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread Interrupted");
        }
    }

    public ConnectionPool getConnectionPool() {
        return connectionPool;
    }

    public void setConnectionPool(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }
}
