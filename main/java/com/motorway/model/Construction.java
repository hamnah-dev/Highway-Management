package com.motorway.model;

import com.motorway.constants.Constants;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class Construction extends Incident implements Serializable {
    private static final long serialVersionUID = 3L;
    private long expectedEndDate;

    public Construction(int id, String description, Location location, long expectedEndDate) {
        super(id, Constants.TYPE_CONSTRUCTION, description, location);
        this.expectedEndDate = expectedEndDate;
    }

    @Override
    public void applyBusinessRules() {
        long daysRemaining = getDaysRemaining();
        if (daysRemaining < 0) {
            System.out.println("Construction overdue by " + Math.abs(daysRemaining) + " days");
        } else if (daysRemaining <= 7) {
            System.out.println("Construction ending soon: " + daysRemaining + " days");
        }
    }

    @Override
    public int getPriorityScore() {
        return isOverdue() ? 50 : 30;
    }

    // --- Getters & Setters ---
    public long getExpectedEndDate() { return expectedEndDate; }
    public void setExpectedEndDate(long expectedEndDate) { this.expectedEndDate = expectedEndDate; }

    public long getDaysRemaining() {
        long diff = expectedEndDate - System.currentTimeMillis();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public boolean isOverdue() { return System.currentTimeMillis() > expectedEndDate; }

    @Override
    public String toString() { return super.generateReport(); }
}