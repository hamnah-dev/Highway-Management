package com.motorway.interfaces;
import com.motorway.model.Incident;

public interface IncidentFilter {
    boolean test(Incident incident);
}
