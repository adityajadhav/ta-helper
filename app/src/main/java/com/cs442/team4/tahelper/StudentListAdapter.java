package com.cs442.team4.tahelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mohammed on 10/30/2016.
 */

public class StudentListAdapter extends ArrayAdapter<Student_Entity> {

    int resource;

    public StudentListAdapter(Context context, int resource, ArrayList<Student_Entity> students) {
        super(context, resource, students);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Student_Entity student = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.student_list_textview, parent, false);
        }

        final TextView itemName = (TextView) convertView.findViewById(R.id.studentListTextview);
        itemName.setTag(position);
        itemName.setText(student.getStudentFirstName() + " " + student.getStudentLastName());

        return convertView;
    }
}
