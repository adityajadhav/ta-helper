package com.cs442.team4.tahelper.student;

/**
 * Created by Mohammed on 11/3/2016.
 */

public class Split {

    private String assignmentName;
    private String splitName;
    private Long splitMaximumPoints;
    private Long splitGainedPoints;

    public Split(String assignmentName, String splitName, Long splitMaximumPoints, Long splitGainedPoints) {
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

    public Long getSplitMaximumPoints() {
        return splitMaximumPoints;
    }

    public void setSplitMaximumPoints(Long splitMaximumPoints) {
        this.splitMaximumPoints = splitMaximumPoints;
    }

    public Long getSplitGainedPoints() {
        return splitGainedPoints;
    }

    public void setSplitGainedPoints(Long splitGainedPoints) {
        this.splitGainedPoints = splitGainedPoints;
    }

}
