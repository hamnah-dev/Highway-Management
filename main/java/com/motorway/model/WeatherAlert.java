package com.motorway.model;

import com.motorway.constants.Constants;

public class WeatherAlert extends Incident {
    private VisibilityInfo visibilityInfo;

    public WeatherAlert(int id, String description, Location location,
                        VisibilityInfo visibilityInfo) {
        super(id, Constants.TYPE_WEATHER_ALERT, description, location);
        this.visibilityInfo = visibilityInfo;
    }

    @Override
    public void applyBusinessRules() {
        switch (visibilityInfo.getVisibilityLevel()) {
            case HAZARDOUS:
                System.out.println(" HAZARDOUS VISIBILITY - Close motorway immediately!");
                System.out.println("   Visibility: " + visibilityInfo.getVisibilityMeters() + "m");
                System.out.println("   Location: " + getLocation().getAddress());
                System.out.println("   PM2.5: " + visibilityInfo.getPm25() + " | PM10: " + visibilityInfo.getPm10());
                break;
            case POOR:
                System.out.println(" POOR VISIBILITY - Activate warning signs and reduce speed limits");
                System.out.println("   Visibility: " + visibilityInfo.getVisibilityMeters() + "m");
                System.out.println("   Location: " + getLocation().getAddress());
                break;
            case MODERATE:
                System.out.println(" MODERATE VISIBILITY - Monitor conditions closely");
                System.out.println("   Visibility: " + visibilityInfo.getVisibilityMeters() + "m");
                break;
            default:
                // Good or excellent - no action needed
                break;
        }
    }

    @Override
    public int getPriorityScore() {
        return visibilityInfo.getVisibilityLevel().getPriorityScore();
    }

    public VisibilityInfo getVisibilityInfo() {
        return visibilityInfo;
    }

    public void setVisibilityInfo(VisibilityInfo visibilityInfo) {
        this.visibilityInfo = visibilityInfo;
    }

    @Override
    public String toString() {
        return super.toString() + " | " + visibilityInfo.toString();
    }

    @Override
    public String generateReport() {
        String baseReport = super.generateReport();
        int insertIndex = baseReport.lastIndexOf("=====");
        StringBuilder report = new StringBuilder(baseReport.substring(0, insertIndex));

        report.append("--- Weather Alert Details ---\n");
        report.append("Visibility Level: ").append(visibilityInfo.getVisibilityLevel()).append("\n");
        report.append("Visibility Distance: ").append(visibilityInfo.getVisibilityMeters()).append("m\n");
        report.append("PM2.5: ").append(visibilityInfo.getPm25()).append("\n");
        report.append("PM10: ").append(visibilityInfo.getPm10()).append("\n");
        report.append("Requires Warning: ").append(visibilityInfo.requiresWarning() ? "YES" : "NO").append("\n");
        report.append("=====================================\n");
        return report.toString();
    }
}