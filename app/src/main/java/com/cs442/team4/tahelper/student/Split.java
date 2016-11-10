package com.cs442.team4.tahelper.student;

/**
 * Created by Mohammed on 11/3/2016.
 */

public class Split {

    private String assignmentName;
    private String splitName;
    private Double splitMaximumPoints;
    private Double splitGainedPoints;

    public Split(String assignmentName, String splitName, Double splitMaximumPoints, Double splitGainedPoints) {
        this.assignmentName = assignmentName;
        this.splitName = splitName;
        this.splitMaximumPoints = splitMaximumPoints;
        this.splitGainedPoints = splitGainedPoints;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    public String getSplitName() {
        return splitName;
    }

    public void setSplitName(String splitName) {
        this.splitName = splitName;
    }

    public Double getSplitMaximumPoints() {
        return splitMaximumPoints;
    }

    public void setSplitMaximumPoints(Double splitMaximumPoints) {
        this.splitMaximumPoints = splitMaximumPoints;
    }

    public Double getSplitGainedPoints() {
        return splitGainedPoints;
    }

    public void setSplitGainedPoints(Double splitGainedPoints) {
        this.splitGainedPoints = splitGainedPoints;
    }

}
