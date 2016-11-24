package com.cs442.team4.tahelper.student;

/**
 * Created by Mohammed on 11/23/2016.
 */

public class TotalGrade {

    String moduleName;
    double totalGainedMarks;
    double totalPossibleMarks;
    double totalGainedPercentage;
    double totalPossiblePercentage;
    String FinalGrade;

    public TotalGrade() {
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public double getTotalGainedMarks() {
        return totalGainedMarks;
    }

    public void setTotalGainedMarks(double totalGainedMarks) {
        this.totalGainedMarks = totalGainedMarks;
    }

    public double getTotalPossibleMarks() {
        return totalPossibleMarks;
    }

    public void setTotalPossibleMarks(double totalPossibleMarks) {
        this.totalPossibleMarks = totalPossibleMarks;
    }

    public double getTotalGainedPercentage() {
        return totalGainedPercentage;
    }

    public void setTotalGainedPercentage(double totalGainedPercentage) {
        this.totalGainedPercentage = totalGainedPercentage;
    }

    public double getTotalPossiblePercentage() {
        return totalPossiblePercentage;
    }

    public void setTotalPossiblePercentage(double totalPossiblePercentage) {
        this.totalPossiblePercentage = totalPossiblePercentage;
    }

    public String getFinalGrade() {
        return FinalGrade;
    }

    public void setFinalGrade(String finalGrade) {
        FinalGrade = finalGrade;
    }
}
