package org.example;

public class Connection {
    private static int counter = 0;
    private final int id;
    private boolean isAvailable;

    public Connection() {
        id = ++counter;
        isAvailable = true;
    }

    public int getId() {
        return id;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
