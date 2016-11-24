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
 * Created by Mohammed on 11/2/2016.
 */

public class StudentModulesListAdapter extends ArrayAdapter<TotalGrade> {

    int resource;

    public StudentModulesListAdapter(Context context, int resource, ArrayList<TotalGrade> modules) {
        super(context, resource, modules);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final TotalGrade module = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.assignment_score_item_layout, parent, false);
        }

        final TextView moduleName = (TextView) convertView.findViewById(R.id.assignmentName);
        final TextView moduleScore = (TextView) convertView.findViewById(R.id.assignmentScore);

        moduleName.setTag(position);
        moduleScore.setTag(position);

        //final TextView itemName = (TextView) convertView.findViewById(R.id.studentListTextview);
        //itemName.setTag(position);
        //itemName.setText(module.getModuleName());

        moduleName.setText(module.getModuleName());
        moduleScore.setText(module.getTotalGainedMarks() + "/" + module.getTotalPossibleMarks());

        return convertView;
    }


}
