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

public class StudentAssignmentSplitsListAdapter extends ArrayAdapter<Split> {

    int resource;

    public StudentAssignmentSplitsListAdapter(Context context, int resource, ArrayList<Split> splits) {
        super(context, resource, splits);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Split split = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.assignment_score_item_layout, parent, false);
        }

        final TextView splitName = (TextView) convertView.findViewById(R.id.assignmentName);
        final TextView splitScore = (TextView) convertView.findViewById(R.id.assignmentScore);

        splitName.setTag(position);
        splitScore.setTag(position);

        splitName.setText(split.getSplitName());
        splitScore.setText(split.getSplitGainedPoints() + "/" + split.getSplitMaximumPoints());

        return convertView;
    }

}
