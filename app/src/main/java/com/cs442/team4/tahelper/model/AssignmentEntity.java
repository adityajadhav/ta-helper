package com.cs442.team4.tahelper.model;

/**
 * Created by sowmyaparameshwara on 11/1/16.
 */

public class AssignmentEntity {

    private String totalScore;
    private AssignmentSplit[] splits;

    AssignmentEntity(){

    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public AssignmentSplit[] getAssignmentSplits(){
        return splits;
    }
}

