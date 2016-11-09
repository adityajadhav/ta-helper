package com.cs442.team4.tahelper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.contants.IntentConstants;
import com.cs442.team4.tahelper.fragment.EditDeleteModuleFragment;
import com.cs442.team4.tahelper.fragment.ManageAssignmentsFragment;

/**
 * Created by sowmyaparameshwara on 10/31/16.
 */

public class ManageAssignmentsActivity extends AppCompatActivity implements ManageAssignmentsFragment.ManageAssignmentFragmentListener {


    String moduleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_assignments_activity);
        Intent intent = getIntent();
        if(intent.getStringExtra(IntentConstants.MODULE_NAME)!=null){
            moduleName = intent.getStringExtra(IntentConstants.MODULE_NAME);
        }
        ManageAssignmentsFragment manageAssignmentsFragment = (ManageAssignmentsFragment) getFragmentManager().findFragmentById(R.id.ManageAssignmentsFragmentView);
        manageAssignmentsFragment.initialise(getIntent());
    }

    @Override
    public void notifyAddAssignmentEvent() {
        Intent intent = new Intent(this,AddAssignmentsActivity.class);
        intent.putExtra(IntentConstants.MODULE_NAME,moduleName);
        startActivity(intent);
    }

    public void onAssignmentItemClickEditDelete(String assignmentName){
        Intent intent = new Intent(this, EditDeleteAssignmentActivity.class);
        intent.putExtra(IntentConstants.MODULE_NAME,moduleName);
        intent.putExtra(IntentConstants.ASSIGNMENT_NAME,assignmentName);
        startActivity(intent);
    }

    public void notifyBackButton(){
        Intent intent = new Intent(this,ModuleListActivity.class);
        startActivity(intent);
    }
}
