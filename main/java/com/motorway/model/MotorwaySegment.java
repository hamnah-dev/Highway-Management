package com.motorway.model;

import java.io.Serializable;
import java.util.Objects;

public class MotorwaySegment implements Serializable {
    private static final long serialVersionUID = 8L;

    private int id;
    private String name;
    private boolean isOpen;
    private String notes;

    public MotorwaySegment(int id, String name, boolean isOpen, String notes) {
        this.id = id; this.name = name; this.isOpen = isOpen; this.notes = notes == null ? "" : notes;
    }

    public MotorwaySegment(int id, String name) { this(id, name, true, ""); }

    // --- Getters & Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isOpen() { return isOpen; }
    public void setOpen(boolean open) { this.isOpen = open; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes == null ? "" : notes; }

    // convenience
    public void close(String reason) { setOpen(false); setNotes(reason); }
    public void open() { setOpen(true); setNotes(""); }

    @Override public String toString() { return name + " - " + (isOpen ? "OPEN" : "CLOSED") + (notes.isEmpty() ? "" : " (" + notes + ")"); }
    @Override public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof MotorwaySegment)) return false; return ((MotorwaySegment) o).id == id; }
    @Override public int hashCode() { return Objects.hash(id); }
}