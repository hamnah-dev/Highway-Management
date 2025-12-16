package com.motorway.model;

import java.io.Serializable;

public class Team implements Serializable {
    private static final long serialVersionUID = 6L;

    private int id;
    private String name;
    private int capacity;
    private boolean available;

    public Team(int id, String name, int capacity) {
        this.id = id; this.name = name; this.capacity = capacity; this.available = true;
    }

    // --- Behavioural methods ---
    public void dispatch() { this.available = false; }
    public void makeAvailable() { this.available = true; }

    // --- Getters & Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override public String toString() {
        return "Team #" + id + " " + name + " (Capacity:" + capacity + ") " + (available ? "YES" : "NO");
    }
}