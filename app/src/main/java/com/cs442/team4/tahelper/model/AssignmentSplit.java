package com.cs442.team4.tahelper.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.EditText;

/**
 * Created by sowmyaparameshwara on 10/31/16.
 */

public class AssignmentSplit implements Parcelable{

    private String splitName;
    private double splitScore;

    public AssignmentSplit(String splitName, double splitScore) {
        this.splitName = splitName;
        this.splitScore = splitScore;
    }

    protected AssignmentSplit(Parcel in) {
        splitName = in.readString();
        splitScore = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(splitName);
        dest.writeDouble(splitScore);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AssignmentSplit> CREATOR = new Creator<AssignmentSplit>() {
        @Override
        public AssignmentSplit createFromParcel(Parcel in) {
            return new AssignmentSplit(in);
        }

        @Override
        public AssignmentSplit[] newArray(int size) {
            return new AssignmentSplit[size];
        }
    };

    public String getSplitName() {
        return splitName;
    }

    public void setSplitName(String splitName) {
        this.splitName = splitName;
    }

    public double getSplitScore() {
        return splitScore;
    }

    public void setSplitScore(int splitScore) {
        this.splitScore = splitScore;
    }
}
