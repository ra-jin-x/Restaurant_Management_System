package com.restaurant.model;

public class table {

    public enum Avialable {
        occupied, free
    }

    private int capacity, id;
    private Avialable status;

    public table(int id, int cap) {
        this.id = id;
        this.capacity = cap;
        this.status = Avialable.free;
    }

    public int getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public Avialable getStatus() {
        return status;
    }

    public void booking() {
        status = Avialable.occupied;
    }
    public void freeNow(){
        status=Avialable.free;
    }
}