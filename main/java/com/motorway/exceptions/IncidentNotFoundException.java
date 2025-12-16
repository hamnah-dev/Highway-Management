
    package com.motorway.exceptions;

    public class IncidentNotFoundException extends Exception {
        public IncidentNotFoundException(int id) {
            super("Incident with ID " + id + " not found");
        }

        public IncidentNotFoundException(String message) {
            super(message);
        }
    }

