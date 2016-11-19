package com.cs442.team4.tahelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.ss.formula.functions.T;

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
        public void callModulesActivity_to_fragment(String coursecode);
        void deleteCourse(String courseCode,int index);
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
        final Button grade_course_btn = (Button) view.findViewById(R.id.grade_course_btn_layout);
//        if(course.getImportStatus().equals("false"))
//        {
//            grade_course_btn.setEnabled(false);
//
//        }
//        else
//        {
//            grade_course_btn.setEnabled(true);
//        }

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


        grade_course_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(course.getImportStatus().equals("false"))
                {

                    new AlertDialog.Builder(context)
                            .setTitle("Error!")
                            .setMessage("Import Students to the course first. Click on Manage and import the student list")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
//                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    // do nothing
//                                }
//                            })
//                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();


                }
                else {

                    obj.callModulesActivity_to_fragment(courseId);
                }
            }
        });

        final Button delete_course_btn = (Button) view.findViewById(R.id.delete_course_btn_layout);
        delete_course_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete Course")
                        .setMessage("Are you sure you want to delet the course. All the details related to the course will be deleted")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                obj.deleteCourse(courseId,position);
                            }
                        })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getContext(),"Operation Cancelled",Toast.LENGTH_SHORT).show();
                              }
                           })
  //                         .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();


            }
        });


        return view;
    }
}
