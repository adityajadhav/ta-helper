package com.cs442.team4.tahelper.student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.cs442.team4.tahelper.R;

import java.util.ArrayList;

/**
 * Created by Mohammed on 10/30/2016.
 */

public class StudentListAdapter extends ArrayAdapter<Student_Entity> implements Filterable {

    int resource;
    public Context context;
    public ArrayList<Student_Entity> students;
    public ArrayList<Student_Entity> orig;

    public StudentListAdapter(Context context, int resource, ArrayList<Student_Entity> students) {
        super(context, resource, students);
        this.resource = resource;
        this.students = students;
        this.context = context;
    }

    public class StudentHolder
    {
        TextView userName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        StudentHolder holder;
        final Student_Entity student = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.student_list_textview, parent, false);
            holder = new StudentHolder();
            holder.userName=(TextView) convertView.findViewById(R.id.studentListTextview);
            convertView.setTag(holder);
        }
        else
        {
            holder=(StudentHolder) convertView.getTag();
        }

        //final TextView itemName = (TextView) convertView.findViewById(R.id.studentListTextview);
        //itemName.setTag(position);
        holder.userName.setText(student.getStudentUserName());
        holder.userName.setText(students.get(position).getStudentUserName());

        return convertView;
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Student_Entity> results = new ArrayList<Student_Entity>();
                if (orig == null)
                    orig = students;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Student_Entity g : orig) {
                            if (g.getStudentUserName().toLowerCase()
                                    .contains(constraint.toString().toLowerCase()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                students = (ArrayList<Student_Entity>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return students.size();
    }

    @Override
    public Student_Entity getItem(int position) {
        return students.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



}
