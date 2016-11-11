package com.cs442.team4.tahelper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.contants.IntentConstants;
import com.cs442.team4.tahelper.fragment.AddModuleFragment;
import com.cs442.team4.tahelper.fragment.EditDeleteModuleFragment;

/**
 * Created by sowmyaparameshwara on 10/30/16.
 */

public class EditDeleteModuleActivity extends AppCompatActivity implements  EditDeleteModuleFragment.EditDeleteButtonListner{

    private String courseCode;

    @Override
    protected void onCreate(Bundle onSavedInstance){
        super.onCreate(onSavedInstance);
        setContentView(R.layout.edit_delete_module_activity);
        EditDeleteModuleFragment editDeleteModuleFragment = (EditDeleteModuleFragment) getFragmentManager().findFragmentById(R.id.editDeleteModuleActivityView);
        editDeleteModuleFragment.initialise(getIntent());
        if(getIntent().getStringExtra(IntentConstants.COURSE_ID)!=null){
            courseCode = getIntent().getStringExtra(IntentConstants.COURSE_ID);
        }

    }

    @Override
    public void clickButtonEvent() {
        Intent intent = new Intent(this,ModuleListActivity.class);
        intent.putExtra(IntentConstants.COURSE_ID,courseCode);
        startActivity(intent);
    }

    public void backToModuleList(){
        Intent intent = new Intent(this,ModuleListActivity.class);
        intent.putExtra(IntentConstants.COURSE_ID,courseCode);
        startActivity(intent);
    }
}
