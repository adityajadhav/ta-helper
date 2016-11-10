package com.cs442.team4.tahelper.student;

/**
 * Created by Mohammed on 11/3/2016.
 */

public class Assignment {

    private String name;
    private Double maximumPoints;
    private Double gainedPoints;
    //grade is just for the 'Grade' module...
    private String grade;
    private Boolean hasSplits = true;

    public Boolean getHasSplits() {
        return hasSplits;
    }

    public void setHasSplits(Boolean hasSplits) {
        this.hasSplits = hasSplits;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Assignment(String name, Double maximumPoints, Double gainedPoints) {
        this.name = name;
        this.maximumPoints = maximumPoints;
        this.gainedPoints = gainedPoints;
    }

    public Assignment() {
    }

    public Double getGainedPoints() {
        return gainedPoints;
    }

    public void setGainedPoints(Double gainedPoints) {
        this.gainedPoints = gainedPoints;
    }

    public Double getMaximumPoints() {
        return maximumPoints;
    }

    public void setMaximumPoints(Double maximumPoints) {
        this.maximumPoints = maximumPoints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
