package com.cs442.team4.tahelper.student;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.activity.AddModuleActivity;
import com.cs442.team4.tahelper.contants.IntentConstants;
import com.cs442.team4.tahelper.fragment.ModuleListFragment;

/**
 * Created by Mohammed on 10/30/2016.
 */

public class StudentListActivity extends AppCompatActivity implements StudentListFragment.OnStudentClickListener {

    String courseId = "CS442";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_list_activity);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft =fm.beginTransaction();

        Intent intent = getIntent();
        if(intent.getStringExtra("course_id")!=null){
            courseId = intent.getStringExtra("course_id");
            Log.d("Course Id : "," In StudentListActivity: "+courseId);
        }


        StudentListFragment studentListFragment = new StudentListFragment();

        Bundle bundle = new Bundle();
        Log.d("Course Id : "," In StudentListActivity 2nd time: "+courseId);
        bundle.putString(IntentConstants.COURSE_ID, courseId);

        studentListFragment.setArguments(bundle);

        ft.replace(R.id.student_list_activity_frame_layout,studentListFragment,"student_list_fragment");
        ft.commit();

    }

    public void showStudentModules(String courseName, String studentId) {
        Intent intent = new Intent(this,StudentModulesActivity.class);
        intent.putExtra(IntentConstants.STUDENT_ID, studentId);
        intent.putExtra(IntentConstants.COURSE_NAME, courseName);
        startActivity(intent);
    }

}
