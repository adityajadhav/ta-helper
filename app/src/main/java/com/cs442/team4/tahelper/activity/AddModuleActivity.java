package com.cs442.team4.tahelper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.fragment.AddModuleFragment;

/**
 * Created by sowmyaparameshwara on 10/30/16.
 */

public class AddModuleActivity extends AppCompatActivity implements AddModuleFragment.AddModuleFragmentListener{

    @Override
    protected void onCreate(Bundle onSavedInstance){
        super.onCreate(onSavedInstance);
        setContentView(R.layout.add_module_activity);
    }

    @Override
    public void addModuleEvent() {
        Intent intent = new Intent(this,ModuleListActivity.class);
        startActivity(intent);
    }
}
