package com.cs442.team4.tahelper.student;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.contants.IntentConstants;

/**
 * Created by Mohammed on 11/2/2016.
 */

public class StudentModulesActivity extends AppCompatActivity implements StudentModulesFragment.OnModuleClickListener {

    String studentId = "nothing";
    String courseName = "nothing";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_modules_activity);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft =fm.beginTransaction();

        Intent intent = getIntent();
        if(intent.getStringExtra(IntentConstants.STUDENT_ID)!=null){
            studentId = intent.getStringExtra(IntentConstants.STUDENT_ID);
        }
        if(intent.getStringExtra(IntentConstants.COURSE_NAME)!=null){
            courseName = intent.getStringExtra(IntentConstants.COURSE_NAME);
        }

        StudentModulesFragment studentModulesFragment = new StudentModulesFragment();

        Bundle bundle = new Bundle();
        bundle.putString(IntentConstants.STUDENT_ID, studentId);
        bundle.putString(IntentConstants.COURSE_NAME, courseName);

        studentModulesFragment.setArguments(bundle);

        ft.replace(R.id.studentModulesActivityFrameLayout,studentModulesFragment,"student_modules_fragment");
        ft.commit();

    }

    public void showSelectedModuleContent(String courseName, String studentId, String moduleName) {
        Intent intent = new Intent(this,StudentAssignmentListActivity.class);
        intent.putExtra(IntentConstants.STUDENT_ID, studentId);
        intent.putExtra(IntentConstants.COURSE_NAME, courseName);
        intent.putExtra(IntentConstants.MODULE_NAME, moduleName);
        startActivity(intent);
    }

}
