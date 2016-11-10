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

public class GradeStudentListActivity extends AppCompatActivity implements GradeStudentListFragment.OnStudentClickListener {

    String courseName, moduleName, moduleItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grade_student_list_activity);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft =fm.beginTransaction();

        Intent intent = getIntent();
        if(intent.getStringExtra(IntentConstants.COURSE_NAME)!=null){
            courseName = intent.getStringExtra(IntentConstants.COURSE_NAME);
        }
        if(intent.getStringExtra(IntentConstants.MODULE_NAME)!=null){
            moduleName = intent.getStringExtra(IntentConstants.MODULE_NAME);
        }
        if(intent.getStringExtra(IntentConstants.MODULE_ITEM)!=null){
            moduleItem = intent.getStringExtra(IntentConstants.MODULE_ITEM);
        }

        GradeStudentListFragment gradeStudentListFragment = new GradeStudentListFragment();

        Bundle bundle = new Bundle();
        bundle.putString(IntentConstants.COURSE_NAME, courseName);
        bundle.putString(IntentConstants.MODULE_NAME, moduleName);
        bundle.putString(IntentConstants.MODULE_ITEM, moduleItem);

        gradeStudentListFragment.setArguments(bundle);

        ft.replace(R.id.grade_student_list_activity_frame_layout,gradeStudentListFragment,"grade_student_list_fragment");
        ft.commit();
    }

    public void showGradeWithSplitsFragment(String studentId, String courseName, String moduleName, String moduleItem) {
        Intent intent = new Intent(this,GradeWithSplitsActivity.class);
        intent.putExtra(IntentConstants.STUDENT_ID, studentId);
        intent.putExtra(IntentConstants.COURSE_NAME, courseName);
        intent.putExtra(IntentConstants.MODULE_NAME, moduleName);
        intent.putExtra(IntentConstants.MODULE_ITEM, moduleItem);
        startActivity(intent);
    }

    public void showGradeWithoutSplitsFragment(String studentId, String courseName, String moduleName, String moduleItem) {
        Intent intent = new Intent(this,GradeWithoutSplitsActivity.class);
        intent.putExtra(IntentConstants.STUDENT_ID, studentId);
        intent.putExtra(IntentConstants.COURSE_NAME, courseName);
        intent.putExtra(IntentConstants.MODULE_NAME, moduleName);
        intent.putExtra(IntentConstants.MODULE_ITEM, moduleItem);
        startActivity(intent);
    }

}
