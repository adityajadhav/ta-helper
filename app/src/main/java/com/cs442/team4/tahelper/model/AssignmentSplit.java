package com.cs442.team4.tahelper.model;

import android.widget.EditText;

/**
 * Created by sowmyaparameshwara on 10/31/16.
 */

public class AssignmentSplit {

    private String splitName;
    private int splitScore;

    public AssignmentSplit(String splitName, int splitScore) {
        this.splitName = splitName;
        this.splitScore = splitScore;
    }

    public String getSplitName() {
        return splitName;
    }

    public void setSplitName(String splitName) {
        this.splitName = splitName;
    }

    public int getSplitScore() {
        return splitScore;
    }

    public void setSplitScore(int splitScore) {
        this.splitScore = splitScore;
    }
}
