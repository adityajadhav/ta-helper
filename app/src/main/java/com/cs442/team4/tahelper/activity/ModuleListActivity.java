package com.cs442.team4.tahelper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.contants.IntentConstants;
import com.cs442.team4.tahelper.fragment.AddModuleFragment;
import com.cs442.team4.tahelper.fragment.ModuleListFragment;

/**
 * Created by sowmyaparameshwara on 10/30/16.
 */

public class ModuleListActivity  extends AppCompatActivity implements ModuleListFragment.ModuleListFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_list_activity);
    }

    @Override
    public void addNewModuleEvent() {
        Intent intent = new Intent(this,AddModuleActivity.class);
        startActivity(intent);
    }

    public void onModuleItemClickEditDelete(String moduleName){
        Intent intent = new Intent(this, EditDeleteModuleActivity.class);
        intent.putExtra(IntentConstants.MODULE_NAME,moduleName);
        startActivity(intent);
    }
}
