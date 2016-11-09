package com.cs442.team4.tahelper.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by sowmyaparameshwara on 11/1/16.
 */

public class AssignmentEntity implements Parcelable{

    private String assignmentName;
    private String totalScore;
    private ArrayList<AssignmentSplit> splits;

    public AssignmentEntity(String assignmentName, String totalScore, ArrayList<AssignmentSplit> splits){
        this.assignmentName = assignmentName;
        this.totalScore = totalScore;
        this.splits = splits;
    }

    public AssignmentEntity(){

    }

    protected AssignmentEntity(Parcel in) {
        assignmentName = in.readString();
        totalScore = in.readString();
        splits = in.createTypedArrayList(AssignmentSplit.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(assignmentName);
        dest.writeString(totalScore);
        dest.writeTypedList(splits);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AssignmentEntity> CREATOR = new Creator<AssignmentEntity>() {
        @Override
        public AssignmentEntity createFromParcel(Parcel in) {
            return new AssignmentEntity(in);
        }

        @Override
        public AssignmentEntity[] newArray(int size) {
            return new AssignmentEntity[size];
        }
    };

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public ArrayList<AssignmentSplit> getAssignmentSplits(){
        return splits;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    public void setAssignmentSplits(ArrayList<AssignmentSplit> assignmentSplits) {
        this.splits = assignmentSplits;
    }

    @Override
    public int hashCode() {
        return assignmentName.length();
    }

    @Override
    public boolean equals(Object o) {
        if ((o instanceof AssignmentEntity) && (((AssignmentEntity) o).getAssignmentName().equals(this.assignmentName))) {
            return true;
        } else {
            return false;
        }
    }




}

