package com.motorway.model;

import com.motorway.enums.VisibilityLevel;

import java.io.Serializable;

public class VisibilityInfo implements Serializable {
    private static final long serialVersionUID = 9L;
    private int visibilityMeters;
    private double pm25;
    private double pm10;
    private VisibilityLevel level;

    public VisibilityInfo(int visibilityMeters, double pm25, double pm10) {
        this.visibilityMeters = visibilityMeters; this.pm25 = pm25; this.pm10 = pm10;
    }

    public int getVisibilityMeters() { return visibilityMeters; }
    public void setVisibilityMeters(int visibilityMeters) { this.visibilityMeters = visibilityMeters; }

    public double getPm25() { return pm25; }
    public void setPm25(double pm25) { this.pm25 = pm25; }

    public double getPm10() { return pm10; }
    public void setPm10(double pm10) { this.pm10 = pm10; }

    public VisibilityLevel getVisibilityLevel() { return VisibilityLevel.fromMeters(visibilityMeters); }
    public boolean requiresWarning() { return getVisibilityLevel() == VisibilityLevel.HAZARDOUS; }
}