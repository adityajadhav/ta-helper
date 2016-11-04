package com.cs442.team4.tahelper.student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cs442.team4.tahelper.R;

import java.util.ArrayList;

/**
 * Created by Mohammed on 11/3/2016.
 */

public class StudentAssignmentListAdapter extends ArrayAdapter<Assignment> {

    int resource;

    public StudentAssignmentListAdapter(Context context, int resource, ArrayList<Assignment> assignments) {
        super(context, resource, assignments);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Assignment assignment = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.assignment_score_item_layout, parent, false);
        }

        final TextView assignmentName = (TextView) convertView.findViewById(R.id.assignmentName);
        final TextView assignmentScore = (TextView) convertView.findViewById(R.id.assignmentScore);

        assignmentName.setTag(position);
        assignmentScore.setTag(position);

        if(assignment.getGrade()== null)
        {
            assignmentName.setText(assignment.getName());
            assignmentScore.setText(assignment.getGainedPoints() + "/" + assignment.getMaximumPoints());
        }
        else
        {
            assignmentName.setText(assignment.getName());
            assignmentScore.setText(assignment.getGrade());
        }

        return convertView;
    }

}
