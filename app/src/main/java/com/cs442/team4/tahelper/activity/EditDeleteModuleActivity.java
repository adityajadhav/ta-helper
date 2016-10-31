package com.cs442.team4.tahelper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.fragment.AddModuleFragment;
import com.cs442.team4.tahelper.fragment.EditDeleteModuleFragment;

/**
 * Created by sowmyaparameshwara on 10/30/16.
 */

public class EditDeleteModuleActivity extends AppCompatActivity implements  EditDeleteModuleFragment.EditDeleteButtonListner{

    @Override
    protected void onCreate(Bundle onSavedInstance){
        super.onCreate(onSavedInstance);
        setContentView(R.layout.edit_delete_module_activity);
        EditDeleteModuleFragment editDeleteModuleFragment = (EditDeleteModuleFragment) getFragmentManager().findFragmentById(R.id.editDeleteModuleActivityView);
        editDeleteModuleFragment.initialise(getIntent());

    }

    @Override
    public void clickButtonEvent() {
        Intent intent = new Intent(this,ModuleListActivity.class);
        startActivity(intent);
    }
}
