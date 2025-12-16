package com.motorway.exceptions;

public class TeamUnavailableException extends Exception {
    public TeamUnavailableException(String teamName) {
        super("Team '" + teamName + "' is currently unavailable");
    }
}