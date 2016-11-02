package com.cs442.team4.tahelper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.contants.IntentConstants;
import com.cs442.team4.tahelper.fragment.AddAssignmentsFragment;
import com.cs442.team4.tahelper.fragment.EditDeleteAssignmentFragment;
import com.cs442.team4.tahelper.model.AssignmentSplit;

/**
 * Created by sowmyaparameshwara on 11/1/16.
 */

public class EditDeleteAssignmentActivity extends AppCompatActivity implements EditDeleteAssignmentFragment.EditDeleteAssignmentsFragmentListener{

    @Override
    protected void onCreate(Bundle onSavedInstance){
        super.onCreate(onSavedInstance);
        setContentView(R.layout.editdelete_assignments_activity);
        EditDeleteAssignmentFragment editDeleteAssignmentsFragment = (EditDeleteAssignmentFragment) getFragmentManager().findFragmentById(R.id.EditDeleteAssignmentsFragmentView);
        editDeleteAssignmentsFragment.initialise(getIntent());
    }

    public void deleteSplit(AssignmentSplit split) {
        EditDeleteAssignmentFragment editDeleteAssignmentsFragment = (EditDeleteAssignmentFragment) getFragmentManager().findFragmentById(R.id.EditDeleteAssignmentsFragmentView);
        editDeleteAssignmentsFragment.deleteSplit(split);

    }

    @Override
    public void notifyEditDeleteAssignmentEvent(String moduleName) {
        Intent intent = new Intent(this,ManageAssignmentsActivity.class);
        intent.putExtra(IntentConstants.MODULE_NAME,moduleName);
        startActivity(intent);
    }
}
