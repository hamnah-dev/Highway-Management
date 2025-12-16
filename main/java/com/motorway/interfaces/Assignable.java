package com.motorway.interfaces;

public interface Assignable {
    void assignTeam(int teamId);
    Integer getAssignedTeamId();
    boolean hasTeamAssigned();
}
