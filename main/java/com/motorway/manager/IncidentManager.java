package com.motorway.manager;

import com.motorway.exceptions.IncidentNotFoundException;
import com.motorway.exceptions.TeamUnavailableException;
import com.motorway.filehandler.IncidentFileHandler;
import com.motorway.model.Incident;
import com.motorway.model.Team;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;

public class IncidentManager {

    private final List<Incident> incidents = new ArrayList<>();
    private final List<Team> teams = new ArrayList<>();
    private int nextIncidentId = 1;


    public IncidentManager() {
        List<Incident> loaded = IncidentFileHandler.loadIncidents();
        incidents.addAll(loaded);

        nextIncidentId = incidents.stream()
                .mapToInt(Incident::getId)
                .max()
                .orElse(0) + 1;

        System.out.println("[IncidentManager] Loaded " + incidents.size() + " incidents");
    }


    public void createIncident(Incident newIncident) {
        if (newIncident == null) return;

        String fpNew = fingerprint(newIncident);

        for (Incident existing : incidents) {
            if (fpNew.equals(fingerprint(existing))) {
                System.out.println("[createIncident] duplicate incident detected, skipping.");
                return;
            }
        }

        newIncident.setId(nextIncidentId++);
        incidents.add(newIncident);

        IncidentFileHandler.saveIncidents(incidents);

        System.out.println("[createIncident] incident added. Total = " + incidents.size());
    }


    public boolean assignTeamToIncident(int incidentId, int teamId)
            throws IncidentNotFoundException, TeamUnavailableException {

        Incident incident = findIncidentById(incidentId);
        if (incident == null) {
            throw new IncidentNotFoundException(incidentId);
        }

        Team team = findTeamById(teamId);
        if (team == null) {
            throw new IncidentNotFoundException("Team #" + teamId + " not found");
        }

        if (!team.isAvailable()) {
            throw new TeamUnavailableException(team.getName());
        }

        incident.assignTeam(teamId);
        team.dispatch();

        IncidentFileHandler.saveIncidents(incidents);

        return true;
    }


    public boolean resolveIncident(int incidentId) throws IncidentNotFoundException {

        Incident incident = findIncidentById(incidentId);
        if (incident == null) {
            throw new IncidentNotFoundException(incidentId);
        }

        incident.resolve();

        if (incident.getAssignedTeamId() != null) {
            Team team = findTeamById(incident.getAssignedTeamId());
            if (team != null) {
                team.makeAvailable();
            }
        }

        IncidentFileHandler.saveIncidents(incidents);

        return true;
    }


    public List<Incident> getIncidentsByStatus(String status) {
        List<Incident> filtered = new ArrayList<>();
        for (Incident incident : incidents) {
            if (incident.getStatus().name().equals(status)) {
                filtered.add(incident);
            }
        }
        return filtered;
    }

    public List<Incident> getAllIncidents() {
        return new ArrayList<>(incidents);
    }

    public List<Incident> getActiveIncidents() {
        List<Incident> active = new ArrayList<>();
        for (Incident incident : incidents) {
            if (incident.isActive()) {
                active.add(incident);
            }
        }
        return active;
    }

    public List<Team> getAvailableTeams() {
        List<Team> available = new ArrayList<>();
        for (Team team : teams) {
            if (team.isAvailable()) {
                available.add(team);
            }
        }
        return available;
    }


    public void addTeam(Team team) {
        if (team == null) return;
        for (Team t : teams) {
            if (t.getId() == team.getId()) return;
        }
        teams.add(team);
    }


    public List<Team> getAllTeams() {
        return new ArrayList<>(teams);
    }

    private Incident findIncidentById(int id) {
        for (Incident incident : incidents) {
            if (incident.getId() == id) return incident;
        }
        return null;
    }

    private Team findTeamById(int id) {
        for (Team team : teams) {
            if (team.getId() == id) return team;
        }
        return null;
    }


    public void printAllTeams() {
        System.out.println("\n--- All Teams ---");
        if (teams.isEmpty()) {
            System.out.println("No teams available.");
            return;
        }
        for (Team team : teams) {
            System.out.println(team);
        }
    }

    public void printAllIncidents() {
        System.out.println("\n--- All Incidents ---");
        if (incidents.isEmpty()) {
            System.out.println("No incidents reported.");
            return;
        }
        for (Incident incident : incidents) {
            System.out.println(incident);
        }
    }

    public void printSystemStatus() {
        System.out.println("\n--- System Status ---");
        System.out.println("Total Incidents: " + incidents.size());
        System.out.println("Active Incidents: " + getActiveIncidents().size());
        System.out.println("Total Teams: " + teams.size());
        System.out.println("Available Teams: " + getAvailableTeams().size());

        System.out.println("\nIncidents by Status:");
        System.out.println("  REPORTED: " + getIncidentsByStatus("REPORTED").size());
        System.out.println("  IN_PROGRESS: " + getIncidentsByStatus("IN_PROGRESS").size());
        System.out.println("  RESOLVED: " + getIncidentsByStatus("RESOLVED").size());
    }


    public <T extends Incident> List<T> getIncidentsByType(Class<T> type) {
        List<T> filtered = new ArrayList<>();
        for (Incident incident : incidents) {
            if (type.isInstance(incident)) {
                filtered.add(type.cast(incident));
            }
        }
        return filtered;
    }

    public List<Incident> findIncidents(java.util.function.Predicate<Incident> predicate) {
        List<Incident> filtered = new ArrayList<>();
        for (Incident incident : incidents) {
            if (predicate.test(incident)) {
                filtered.add(incident);
            }
        }
        return filtered;
    }

    private String fingerprint(Object obj) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.close();

            byte[] bytes = baos.toByteArray();
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(bytes);
            return Base64.getEncoder().encodeToString(digest);
        } catch (Exception e) {
            return "id:" + System.identityHashCode(obj);
        }
    }

}
