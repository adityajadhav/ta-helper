package com.cs442.team4.tahelper;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Mohammed on 10/30/2016.
 */

public class StudentListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_list_activity);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft =fm.beginTransaction();
        ft.replace(R.id.student_list_activity_frame_layout,new StudentListFragment(),"student_list_fragment");
        ft.commit();

    }

}
