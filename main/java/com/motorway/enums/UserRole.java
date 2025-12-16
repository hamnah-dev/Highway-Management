package com.motorway.enums;

public enum UserRole {

    ADMIN("Administrator",1),
    USER("Regular",2);

    private String displayName;
    private int level;

    UserRole(String displayName,int level) {
        this.displayName = displayName;
        this.level = level;
    }

    public String getDisplayName(){
        return displayName;
    }

    public int getLevel(){
        return level;
    }

    @Override
    public String toString(){
        return displayName;
    }
}
