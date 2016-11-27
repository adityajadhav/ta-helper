package com.cs442.team4.tahelper.student;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.contants.IntentConstants;

/**
 * Created by Mohammed on 11/3/2016.
 */

public class StudentAssignmentListActivity extends AppCompatActivity {

    String studentId, courseName, moduleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_assignment_list_activity);

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

        StudentAssignmentListFragment studentAssignmentListFragment = new StudentAssignmentListFragment();

        Bundle bundle = new Bundle();
        bundle.putString(IntentConstants.STUDENT_ID, studentId);
        bundle.putString(IntentConstants.COURSE_NAME, courseName);
        bundle.putString(IntentConstants.MODULE_NAME, moduleName);

        studentAssignmentListFragment.setArguments(bundle);

        ft.replace(R.id.student_assignment_list_activity_frame_layout,studentAssignmentListFragment,"student_assignment_list_fragment");
        ft.commit();
    }

    public void showAssignmentSplits(String courseName, String studentId, String moduleName, String moduleItem) {
        Intent intent = new Intent(this,StudentAssignmentSplitsListActivity.class);
        intent.putExtra(IntentConstants.STUDENT_ID, studentId);
        intent.putExtra(IntentConstants.COURSE_NAME, courseName);
        intent.putExtra(IntentConstants.MODULE_NAME, moduleName);
        intent.putExtra(IntentConstants.MODULE_ITEM, moduleItem);
        startActivity(intent);
    }

}
