package com.cs442.team4.tahelper;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class CourseActivity extends AppCompatActivity implements course_list_fragment.OnActionButtonClickListener, add_course_fragment.OnFinishAddCourseInterface {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft =fm.beginTransaction();
        ft.replace(R.id.course_activity_frame_layout,new course_list_fragment(),"course_list_fragment");
        ft.commit();


    }
    @Override
    public void callAddCourseFragment()
    {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft =fm.beginTransaction();
        ft.replace(R.id.course_activity_frame_layout,new add_course_fragment(),"add_course_fragment_tag");
        ft.addToBackStack("course_list_fragment");
        ft.commit();
    }

    @Override
    public void closeAddCourseFragment()
    {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft =fm.beginTransaction();
        Fragment id = fm.findFragmentByTag("add_course_fragment_tag");
        ft.remove(id);
        fm.popBackStack();
        ft.commit();
    }
}
