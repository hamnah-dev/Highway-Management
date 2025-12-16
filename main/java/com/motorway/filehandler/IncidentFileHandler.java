package com.motorway.filehandler;

import com.motorway.model.Incident;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class IncidentFileHandler {

    private static final String INCIDENTS_FILE = "incidents.dat";

    public static synchronized void saveIncidents(List<com.motorway.model.Incident> incidents) {
        File file = new File(INCIDENTS_FILE);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file, false))) {

            oos.writeObject(new ArrayList<>(incidents));
            oos.flush();
            System.out.println("[IncidentFileHandler] Saved " + incidents.size() + " incidents to " + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("[IncidentFileHandler] Error saving incidents: " + e.getMessage());
        }
    }



    @SuppressWarnings("unchecked")
    public static synchronized List<com.motorway.model.Incident> loadIncidents() {
        File file = new File(INCIDENTS_FILE);
        if (!file.exists() || file.length() == 0) {

            System.out.println("[IncidentFileHandler] incidents file not found or empty; returning empty list");
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object o = ois.readObject();
            if (o instanceof List) {
                List<com.motorway.model.Incident> list = (List<com.motorway.model.Incident>) o;
                System.out.println("[IncidentFileHandler] Loaded " + list.size() + " incidents from file");
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("[IncidentFileHandler] Error loading incidents: " + e.getMessage());
        }
        return new ArrayList<>();
    }

}