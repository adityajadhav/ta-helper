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

public class StudentModulesListAdapter extends ArrayAdapter<String> {

    int resource;

    public StudentModulesListAdapter(Context context, int resource, ArrayList<String> modules) {
        super(context, resource, modules);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final String module = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.student_list_textview, parent, false);
        }

        final TextView itemName = (TextView) convertView.findViewById(R.id.studentListTextview);
        itemName.setTag(position);
        itemName.setText(module);

        return convertView;
    }


}
