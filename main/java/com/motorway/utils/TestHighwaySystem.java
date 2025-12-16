package com.motorway.utils;

import com.motorway.enums.*;
import com.motorway.exceptions.*;
import com.motorway.manager.*;
import com.motorway.model.*;
import com.motorway.utils.TestDataGenerator;

import java.util.List;

public class TestHighwaySystem {

    public static void main(String[] args) {
        System.out.println(" HIGHWAY MANAGEMENT SYSTEM TEST \n");


        IncidentManager manager = new IncidentManager();


        System.out.println("=== TEST 1: Adding Teams ===");
        Team[] teams = TestDataGenerator.createSampleTeams();
        for (Team team : teams) {
            manager.addTeam(team);
        }
        manager.printAllTeams();


        manager.printAllIncidents();

        System.out.println("=== TEST 3: System Status ===");
        manager.printSystemStatus();

        System.out.println("=== TEST 4: Priority Report ===");
        PriorityScorer.printPriorityReport(manager.getActiveIncidents());

        System.out.println("=== TEST 5: Assigning Teams ===");
        try {
            manager.assignTeamToIncident(1, 1); // Assign team 1 to incident 1
            manager.assignTeamToIncident(2, 2); // Assign team 2 to incident 2
        } catch (IncidentNotFoundException | TeamUnavailableException e) {
            System.err.println("Error: " + e.getMessage());
        }

        System.out.println("\n=== TEST 6: Testing Exception Handling ===");
        try {
            manager.assignTeamToIncident(3, 1); // Team 1 is already busy
        } catch (TeamUnavailableException e) {
            System.out.println("Exception caught: " + e.getMessage());
        } catch (IncidentNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }


        System.out.println("\n=== TEST 7: Resolving Incidents ===");
        try {
            manager.resolveIncident(1);
        } catch (IncidentNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }

        System.out.println("=== TEST 8: Updated System Status ===");
        manager.printSystemStatus();

        System.out.println("=== TEST 9: Generic Method - Filter by Type ===");
        List<Accident> accidents = manager.getIncidentsByType(Accident.class);
        System.out.println("Total Accidents: " + accidents.size());
        for (Accident acc : accidents) {
            System.out.println("  - " + acc.toString());
        }


        System.out.println("\n=== TEST 10: Lambda Filtering - High Priority ===");
        List<Incident> highPriority = manager.findIncidents(i -> i.getPriorityScore() > 70);
        System.out.println("High Priority Incidents (>70): " + highPriority.size());
        for (Incident inc : highPriority) {
            System.out.println("  - Priority " + inc.getPriorityScore() + ": " + inc.toString());
        }


        System.out.println("\n=== TEST 11: Polymorphism Demo ===");
        System.out.println("All incidents stored in List<Incident>, but each behaves differently:");
        for (Incident inc : manager.getAllIncidents()) {
            System.out.println("\nType: " + inc.getClass().getSimpleName());
            System.out.println(inc.toString());
            System.out.println("Priority: " + inc.getPriorityScore());
        }

        System.out.println("\n=== TEST 12: Full Report Generation ===");
        Incident firstIncident = manager.getAllIncidents().get(0);
        System.out.println(firstIncident.generateReport());

        System.out.println("\nALL TESTS COMPLETE! ");
    }
}