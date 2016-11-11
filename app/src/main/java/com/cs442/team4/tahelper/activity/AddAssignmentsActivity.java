package com.cs442.team4.tahelper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.contants.IntentConstants;
import com.cs442.team4.tahelper.fragment.AddAssignmentsFragment;
import com.cs442.team4.tahelper.model.AssignmentSplit;

/**
 * Created by sowmyaparameshwara on 10/31/16.
 */

public class AddAssignmentsActivity extends AppCompatActivity implements AddAssignmentsFragment.AddAssignmentsFragmentListener {

    private String courseCode;

    @Override
    protected void onCreate(Bundle onSavedInstance){
        super.onCreate(onSavedInstance);
        setContentView(R.layout.add_assignments_activity);
        if(getIntent().getStringExtra(IntentConstants.COURSE_ID)!=null){
            courseCode = getIntent().getStringExtra(IntentConstants.COURSE_ID);
        }
        AddAssignmentsFragment addAssignmentsFragment = (AddAssignmentsFragment) getFragmentManager().findFragmentById(R.id.AddAssignmentsFragmentView);
        addAssignmentsFragment.initialise(getIntent());
    }

    public void deleteSplit(AssignmentSplit split) {
        AddAssignmentsFragment editDeleteModuleFragment = (AddAssignmentsFragment) getFragmentManager().findFragmentById(R.id.AddAssignmentsFragmentView);
        editDeleteModuleFragment.deleteSplit(split);

    }

    @Override
    public void notifyAddAssignmentEvent(String moduleName) {
        Intent intent = new Intent(this,ManageAssignmentsActivity.class);
        intent.putExtra(IntentConstants.MODULE_NAME,moduleName);
        intent.putExtra(IntentConstants.COURSE_ID,courseCode);
        startActivity(intent);
    }

    public void notifyBackEvent(String moduleName){
        Intent intent = new Intent(this,ManageAssignmentsActivity.class);
        intent.putExtra(IntentConstants.MODULE_NAME,moduleName);
        intent.putExtra(IntentConstants.COURSE_ID,courseCode);
        startActivity(intent);
    }
}
