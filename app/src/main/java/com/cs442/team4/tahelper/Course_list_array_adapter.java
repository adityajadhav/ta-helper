package com.cs442.team4.tahelper;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ullas on 10/29/2016.
 */

public class Course_list_array_adapter extends ArrayAdapter<Course_Entity> {

    ArrayList<Course_Entity> course = new ArrayList<>();
    Context context;

    public interface Course_list_adapter_interface
    {
         void callManageCourseFragment(String courseCode);
        void editCourseFragment(String mode, String coursecode);
    }
    private Course_list_adapter_interface obj;

    public void set_course_list_adapter_interface(Course_list_adapter_interface ci)
    {
        this.obj = ci;
    }

    public Course_list_array_adapter(ArrayList<Course_Entity> ce , Context context)
    {
        super(context, R.layout.courses_list_view,ce);
        this.course = ce;
        this.context = context;


    }
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View view = convertView;


        if (view == null) {

            view = View.inflate(context, R.layout.courses_list_view, null);
        } else {
            view = convertView;
        }

        final Course_Entity course = getItem(position);

        TextView course_name_tv = (TextView) view.findViewById(R.id.course_name_tv_layout);
        course_name_tv.setText(course.getCourseName());
        final String courseId = course.getCourseCode();

        Button manage_course_btn = (Button) view.findViewById(R.id.manage_course_btn_layout);
        manage_course_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obj.callManageCourseFragment(courseId);

            }
        });

        Button  edit_course_btn = (Button) view.findViewById(R.id.edit_course_btn_layout);
        edit_course_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obj.editCourseFragment("edit",courseId);
            }


        });


        return view;
    }
}
