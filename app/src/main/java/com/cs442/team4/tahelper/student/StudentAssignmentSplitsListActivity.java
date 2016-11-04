package com.cs442.team4.tahelper.student;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.contants.IntentConstants;

/**
 * Created by Mohammed on 11/3/2016.
 */

public class StudentAssignmentSplitsListActivity extends AppCompatActivity {

    String studentId, courseName, moduleName, moduleItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_assignment_splits_list_activity);

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

        StudentAssignmentSplitsListFragment studentAssignmentSplitsListFragment = new StudentAssignmentSplitsListFragment();

        Bundle bundle = new Bundle();
        bundle.putString(IntentConstants.STUDENT_ID, studentId);
        bundle.putString(IntentConstants.COURSE_NAME, courseName);
        bundle.putString(IntentConstants.MODULE_NAME, moduleName);
        bundle.putString(IntentConstants.MODULE_ITEM, moduleItem);

        studentAssignmentSplitsListFragment.setArguments(bundle);

        ft.replace(R.id.student_assignment_splits_list_activity_frame_layout,studentAssignmentSplitsListFragment,"student_assignment_splits_list_fragment");
        ft.commit();
    }

}
