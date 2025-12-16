package com.motorway.enums;

public enum Severity {
    LOW("Low",20),
    MEDIUM("Medium",50),
    HIGH("HIGH",75),
    CRITICAL("Critical",100);

    private String displayName;
    private int priorityScore;

    Severity(String displayName, int priorityScore){
        this.displayName=displayName;
        this.priorityScore=priorityScore;
    }

    public String getDisplayName(){
        return displayName;
    }

    public int getPriorityScore(){
        return priorityScore;
    }

    @Override
    public String toString(){
        return displayName;
    }
}
