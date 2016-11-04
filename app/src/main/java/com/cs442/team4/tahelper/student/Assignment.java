package com.cs442.team4.tahelper.student;

/**
 * Created by Mohammed on 11/3/2016.
 */

public class Assignment {

    private String name;
    private Long maximumPoints;
    private Long gainedPoints;
    private String grade;

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Assignment(String name, Long maximumPoints, Long gainedPoints) {
        this.name = name;
        this.maximumPoints = maximumPoints;
        this.gainedPoints = gainedPoints;
    }

    public Assignment() {
    }

    public Long getGainedPoints() {
        return gainedPoints;
    }

    public void setGainedPoints(Long gainedPoints) {
        this.gainedPoints = gainedPoints;
    }

    public Long getMaximumPoints() {
        return maximumPoints;
    }

    public void setMaximumPoints(Long maximumPoints) {
        this.maximumPoints = maximumPoints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
