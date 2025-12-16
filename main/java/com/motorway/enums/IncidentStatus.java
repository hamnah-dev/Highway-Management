package com.motorway.enums;

public enum IncidentStatus {
    REPORTED("Reported",1),
    DISPATCHED("Dispatched",2),
    RESOLVED("Resolved",3);

    private final String displayName;
    private final int order;

    IncidentStatus(String displayName,int order){
        this.displayName= displayName;
        this.order= order;
    }

    public String getDisplayName(){
        return displayName;
    }

    public int getOrder(){
        return order;
    }

    @Override
    public String toString(){
        return displayName;
    }
}
