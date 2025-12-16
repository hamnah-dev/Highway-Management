package com.motorway.model;

import com.motorway.constants.Constants;
import com.motorway.enums.RoadBlockageLevel;
import com.motorway.enums.Severity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Accident extends Incident implements Serializable {
    private static final long serialVersionUID = 2L;

    private Severity severity;
    private int numberOfVehicles;
    private boolean injuriesReported;
    private List<String> emergencyServicesNeeded;
    private RoadBlockageLevel roadBlockage;

    public Accident(int id, String description, Location location,
                    Severity severity, int numberOfVehicles,
                    boolean injuriesReported, List<String> emergencyServicesNeeded,
                    RoadBlockageLevel roadBlockage) {
        super(id, Constants.TYPE_ACCIDENT, description, location);
        this.severity = severity != null ? severity : Severity.MEDIUM;
        this.numberOfVehicles = numberOfVehicles;
        this.injuriesReported = injuriesReported;
        this.emergencyServicesNeeded = emergencyServicesNeeded == null ? new ArrayList<>() : new ArrayList<>(emergencyServicesNeeded);
        this.roadBlockage = roadBlockage == null ? RoadBlockageLevel.NONE : roadBlockage;
    }

    @Override
    public void applyBusinessRules() {
        if (injuriesReported && numberOfVehicles >= 3 && roadBlockage == RoadBlockageLevel.FULL) {
            if (severity != null && severity != Severity.CRITICAL) {
                System.out.println("⚠ AUTO-ESCALATING accident to CRITICAL");
                this.severity = Severity.CRITICAL;
            }
        }
    }

    @Override
    public int getPriorityScore() {
        int score = severity != null ? severity.getPriorityScore() : 30;
        if (injuriesReported) score += 15;
        if (numberOfVehicles >= 3) score += 10;
        score += roadBlockage.getPriorityBoost();
        return Math.min(score, 100);
    }

    // --- Getters & Setters ---
    public Severity getSeverity() { return severity; }
    public void setSeverity(Severity severity) { this.severity = severity; }

    public int getNumberOfVehicles() { return numberOfVehicles; }
    public void setNumberOfVehicles(int numberOfVehicles) { this.numberOfVehicles = numberOfVehicles; }

    public boolean isInjuriesReported() { return injuriesReported; }
    public void setInjuriesReported(boolean injuriesReported) { this.injuriesReported = injuriesReported; }

    public List<String> getEmergencyServicesNeeded() { return new ArrayList<>(emergencyServicesNeeded); }
    public void setEmergencyServicesNeeded(List<String> emergencyServicesNeeded) {
        this.emergencyServicesNeeded = emergencyServicesNeeded == null ? new ArrayList<>() : new ArrayList<>(emergencyServicesNeeded);
    }

    public RoadBlockageLevel getRoadBlockage() { return roadBlockage; }
    public void setRoadBlockage(RoadBlockageLevel roadBlockage) { this.roadBlockage = roadBlockage; }

    @Override
    public String toString() {
        return super.generateReport();
    }
}