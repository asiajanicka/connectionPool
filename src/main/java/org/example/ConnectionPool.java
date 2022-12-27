package org.example;

import java.util.Optional;
import java.util.Vector;

public class ConnectionPool {
    int size;
    private Vector<Connection> pool;

    public ConnectionPool(int size) {
        this.size = size;
        pool = new Vector<>();
    }

    public synchronized Connection getConnection() {
        if (findAvailableConnection().isPresent()) {
            Connection con = findAvailableConnection().get();
            con.setAvailable(false);
            return con;
        }
        if (pool.size() < size) {
            Connection con = new Connection();
            con.setAvailable(false);
            pool.add(con);
            return con;
        } 
        return null;
    }

    public synchronized void releaseConnection(Connection con) {
        con.setAvailable(true);
    }

    private Optional<Connection> findAvailableConnection() {
        return pool.stream().filter(Connection::isAvailable).findFirst();
    }
}
