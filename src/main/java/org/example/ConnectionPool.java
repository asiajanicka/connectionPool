package org.example;

import java.util.Vector;

public class ConnectionPool {
    int size;
    private Vector<Connection> pool;

    public ConnectionPool() {
        pool = new Vector<>();
    }

    public ConnectionPool(int size) {
        this.size = size;
        pool = new Vector<>();
    }

    public synchronized Connection getConnection() {
        Connection con = null;

        while (con == null) {
            con = findAvailableConnection();
            if (con == null && pool.size() < size) {
                con = new Connection();
                pool.add(con);
            }
            if (con == null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Thread Interrupted");
                }
            } else {
                con.setAvailable(false);
            }
        }
        return con;
    }

    public synchronized void releaseConnection(Connection con) {
        notifyAll();
        con.setAvailable(true);
    }

    private synchronized Connection findAvailableConnection() {
        return pool.stream().filter(Connection::isAvailable).findFirst().orElse(null);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
