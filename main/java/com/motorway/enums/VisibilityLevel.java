package com.motorway.enums;

public enum VisibilityLevel {
    EXCELLENT("Excellent", 10, 10000),
    GOOD("Good", 20, 5000),
    MODERATE("Moderate", 40, 1000),
    POOR("Poor", 70, 200),
    HAZARDOUS("Hazardous", 90, 0);

    private final String displayName;
    private final int priorityScore;
    private final int minVisibilityMeters;

    VisibilityLevel(String displayName, int priorityScore, int minVisibilityMeters) {
        this.displayName = displayName;
        this.priorityScore = priorityScore;
        this.minVisibilityMeters = minVisibilityMeters;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getPriorityScore() {
        return priorityScore;
    }

    public int getMinVisibilityMeters() {
        return minVisibilityMeters;
    }

    public static VisibilityLevel fromMeters(int visibilityMeters) {
        if (visibilityMeters > 10000) return EXCELLENT;
        if (visibilityMeters > 5000) return GOOD;
        if (visibilityMeters > 1000) return MODERATE;
        if (visibilityMeters > 200) return POOR;
        return HAZARDOUS;
    }

    @Override
    public String toString() {
        return displayName;
    }
}