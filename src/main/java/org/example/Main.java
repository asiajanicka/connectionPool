package org.example;

public class Main {

    public static void main(String[] args) {
        // Connection pool can be used by thread factory and task (future) factory separately or by both at once.
        // If you would like to run only one factory, please comment the line with the unnecessary factory
        ConnectionPool connectionPool = new ConnectionPool(5);
        System.out.println("Running via Threads");
        (new Thread(new ThreadFactory(7, connectionPool))).start();
        System.out.println("Running via Completable Future");
        (new TaskFactory(7, connectionPool)).getTasks();
    }
}
