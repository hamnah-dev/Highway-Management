package com.motorway.model;

import com.motorway.enums.IncidentStatus;
import com.motorway.enums.Severity;
import com.motorway.interfaces.Assignable;
import com.motorway.interfaces.Reportable;
import com.motorway.interfaces.Resolvable;
import com.motorway.model.Location;

import java.io.Serializable;
import java.util.Date;

public abstract class Incident implements Reportable, Assignable, Resolvable, Comparable<Incident>, Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String type;
    private String description;
    private long timestamp;
    private IncidentStatus status;
    private Location location;
    private Integer assignedTeamId;
    private Severity severity;

    public Incident(int id, String type, String description, Location location) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.location = location;
        this.timestamp = System.currentTimeMillis();
        this.status = IncidentStatus.REPORTED;
        this.assignedTeamId = null;
        this.severity=severity;
    }

    public abstract void applyBusinessRules();
    public abstract int getPriorityScore();


    @Override
    public void assignTeam(int teamId) {
        this.assignedTeamId = teamId;
        this.status = IncidentStatus.DISPATCHED;
    }

    @Override
    public Integer getAssignedTeamId() { return assignedTeamId; }

    @Override
    public boolean hasTeamAssigned() { return assignedTeamId != null; }

    @Override
    public void resolve() { this.status = IncidentStatus.RESOLVED; }

    @Override
    public boolean isResolved() { return status == IncidentStatus.RESOLVED; }

    @Override
    public boolean isActive() { return !isResolved(); }

    @Override
    public String generateReport() {
        StringBuilder r = new StringBuilder();
        r.append("===== Incident Report =====\n");
        r.append("ID: ").append(id).append("\n");
        r.append("Type: ").append(type).append("\n");
        r.append("Description: ").append(description).append("\n");
        r.append("Status: ").append(status).append("\n");
        r.append("Location: ").append(location == null ? "N/A" : location.getAddress()).append("\n");
        if (location != null) r.append("Coordinates: ").append(location.getLat()).append(",").append(location.getLng()).append("\n");
        r.append("Timestamp: ").append(new Date(timestamp)).append("\n");
        r.append("Priority Score: ").append(getPriorityScore()).append("\n");
        if (hasTeamAssigned()) r.append("Assigned Team: ").append(assignedTeamId).append("\n");
        r.append("===========================\n");
        return r.toString();
    }

    @Override
    public int compareTo(Incident other) { return Long.compare(this.timestamp, other.timestamp); }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public IncidentStatus getStatus() { return status; }
    public void setStatus(IncidentStatus status) { this.status = status; }

    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public void setAssignedTeamId(Integer assignedTeamId) { this.assignedTeamId = assignedTeamId; }
}