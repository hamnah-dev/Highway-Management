package com.motorway.utils;

import com.motorway.enums.*;
import com.motorway.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class TestDataGenerator {

    public static Team[] createSampleTeams() {
        return new Team[] {
                new Team(1, "Emergency Response Alpha", 8),
                new Team(2, "Highway Maintenance Beta", 6),
                new Team(3, "Traffic Control Gamma", 5),
                new Team(4, "Weather Response Delta", 4)
        };
    }

    public static MotorwaySegment[] createSampleSegments() {
        return new MotorwaySegment[] {
                new MotorwaySegment(1, "M1 North (J10-J15)", true, ""),
                new MotorwaySegment(2, "M1 South (J15-J20)", true, ""),
                new MotorwaySegment(3, "M25 East (J5-J10)", true, ""),
                new MotorwaySegment(4, "M25 West (J10-J15)", false, "Closed for maintenance"),
                new MotorwaySegment(5, "M6 Central (J8-J12)", true, ""),
                new MotorwaySegment(6, "M60 Ring Road", true, "")
        };
    }

    public static VisibilityInfo createModerateVisibility() {
        return new VisibilityInfo(2000, 35.0, 50.0);
    }

    public static VisibilityInfo createPoorVisibility() {
        return new VisibilityInfo(500, 65.0, 95.0);
    }

    public static VisibilityInfo createHazardousVisibility() {
        return new VisibilityInfo(100, 95.0, 135.0);
    }
}