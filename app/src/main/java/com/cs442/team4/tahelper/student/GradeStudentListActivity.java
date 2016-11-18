package com.cs442.team4.tahelper.student;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.activity.ManageAssignmentsActivity;
import com.cs442.team4.tahelper.activity.ModuleListActivity;
import com.cs442.team4.tahelper.contants.IntentConstants;
import com.cs442.team4.tahelper.model.UserEntity;

/**
 * Created by Mohammed on 11/9/2016.
 */

public class GradeStudentListActivity extends AppCompatActivity implements GradeStudentListFragment.OnStudentClickListener {

    String courseName, moduleName, moduleItem;
    UserEntity user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grade_student_list_activity);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft =fm.beginTransaction();

        Intent intent = getIntent();
        if(intent.getStringExtra(IntentConstants.COURSE_ID)!=null){
            courseName = intent.getStringExtra(IntentConstants.COURSE_ID);
        }
        if(intent.getStringExtra(IntentConstants.MODULE_NAME)!=null){
            moduleName = intent.getStringExtra(IntentConstants.MODULE_NAME);
        }
        if(intent.getStringExtra(IntentConstants.MODULE_ITEM)!=null){
            moduleItem = intent.getStringExtra(IntentConstants.MODULE_ITEM);
        }
        if(getIntent().getSerializableExtra("USER_DETAILS")!=null){
            user = (UserEntity) getIntent().getSerializableExtra("USER_DETAILS");
        }

        GradeStudentListFragment gradeStudentListFragment = new GradeStudentListFragment();

        Bundle bundle = new Bundle();
        bundle.putString(IntentConstants.COURSE_ID, courseName);
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

    /*@Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ManageAssignmentsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(IntentConstants.MODULE_NAME,moduleName);
        intent.putExtra(IntentConstants.COURSE_ID,courseName);
        intent.putExtra(IntentConstants.MODULE_ITEM, moduleItem);
        intent.putExtra("USER_DETAILS",user);
        startActivity(intent);
    }*/

}
