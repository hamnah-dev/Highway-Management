package com.motorway.enums;

public enum RoadBlockageLevel {
    NONE("No Blockage", 0),
    PARTIAL("Partially Blocked", 5),
    FULL("Fully Blocked", 15);

    private final String displayName;
    private final int priorityBoost;

    RoadBlockageLevel(String displayName, int priorityBoost) {
        this.displayName = displayName;
        this.priorityBoost = priorityBoost;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getPriorityBoost() {
        return priorityBoost;
    }

    @Override
    public String toString() {
        return displayName;
    }
}