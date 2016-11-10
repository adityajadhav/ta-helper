package com.cs442.team4.tahelper.student;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.contants.IntentConstants;

/**
 * Created by Mohammed on 11/9/2016.
 */

public class GradeWithSplitsActivity extends AppCompatActivity {

    String studentId = "nothing";
    String courseName = "nothing";
    String moduleName = "nothing";
    String moduleItem = "nothing";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grade_with_splits_activity);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft =fm.beginTransaction();

        Intent intent = getIntent();
        if(intent.getStringExtra(IntentConstants.STUDENT_ID)!=null){
            studentId = intent.getStringExtra(IntentConstants.STUDENT_ID);
        }
        if(intent.getStringExtra(IntentConstants.COURSE_NAME)!=null){
            courseName = intent.getStringExtra(IntentConstants.COURSE_NAME);
        }
        if(intent.getStringExtra(IntentConstants.MODULE_NAME)!=null){
            moduleName = intent.getStringExtra(IntentConstants.MODULE_NAME);
        }
        if(intent.getStringExtra(IntentConstants.MODULE_ITEM)!=null){
            moduleItem = intent.getStringExtra(IntentConstants.MODULE_ITEM);
        }

        GradeWithSplitsFragment gradeWithSplitsFragment = new GradeWithSplitsFragment();

        Bundle bundle = new Bundle();
        bundle.putString(IntentConstants.STUDENT_ID, studentId);
        bundle.putString(IntentConstants.COURSE_NAME, courseName);
        bundle.putString(IntentConstants.MODULE_NAME, moduleName);
        bundle.putString(IntentConstants.MODULE_ITEM, moduleItem);

        gradeWithSplitsFragment.setArguments(bundle);

        ft.replace(R.id.grade_with_splits_activity_frame_layout,gradeWithSplitsFragment,"grade_with_splits_fragment");
        ft.commit();

    }

}
