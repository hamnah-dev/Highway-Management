package com.motorway.manager;

import com.motorway.model.Incident;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class PriorityScorer {
    private PriorityScorer() {}

    public static int computePriority(Incident incident) {
        int score = incident.getPriorityScore();

        if (incident.getStatus().name().equals("REPORTED")) {
            score += 10;
        }

        long ageMinutes = getIncidentAgeMinutes(incident);
        if (ageMinutes > 120) score += 20;
        else if (ageMinutes > 60) score += 10;

        return Math.min(score, 100);
    }

    private static long getIncidentAgeMinutes(Incident incident) {
        long diff = System.currentTimeMillis() - incident.getTimestamp();
        return TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static List<Incident> sortByPriority(List<Incident> incidents) {
        List<Incident> sorted = new ArrayList<>(incidents);
        Collections.sort(sorted, Comparator.comparingInt(PriorityScorer::computePriority).reversed());
        return sorted;
    }

    public static void printPriorityReport(List<Incident> incidents) {
        System.out.println("\n--- Priority Report ---");
        if (incidents.isEmpty()) {
            System.out.println("No active incidents.");
            return;
        }

        List<Incident> sorted = sortByPriority(incidents);
        System.out.println("Incidents sorted by priority (highest first):\n");

        for (Incident incident : sorted) {
            int priority = computePriority(incident);
            System.out.printf("Priority %d | ID: %d | Status: %s | Type: %s%n",
                    priority,
                    incident.getId(),
                    incident.getStatus(),
                    incident.getClass().getSimpleName());
        }
    }
}