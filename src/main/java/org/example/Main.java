package org.example;

public class Main {

    public static void main(String[] args) {
        ConnectionPool connectionPool = new ConnectionPool(5);
        (new Thread(new ThreadFactory(7, connectionPool))).start();
    }
}
