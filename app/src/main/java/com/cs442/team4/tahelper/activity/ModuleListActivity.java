package com.cs442.team4.tahelper.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cs442.team4.tahelper.CourseActivity;
import com.cs442.team4.tahelper.MainActivity;
import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.contants.IntentConstants;
import com.cs442.team4.tahelper.fragment.AddModuleFragment;
import com.cs442.team4.tahelper.fragment.ManageAssignmentsFragment;
import com.cs442.team4.tahelper.fragment.ModuleListFragment;
import com.cs442.team4.tahelper.model.UserEntity;
import com.cs442.team4.tahelper.preferences.MyPreferenceActivity;
import com.cs442.team4.tahelper.preferences.MyPreferenceFragment;
import com.cs442.team4.tahelper.showcase.ActionItemsSampleActivity;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.SimpleShowcaseEventListener;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

/**
 * Created by sowmyaparameshwara on 10/30/16.
 */

public class ModuleListActivity  extends AppCompatActivity implements ModuleListFragment.ModuleListFragmentListener {
    private ListView mDrawerList;
    private String[] drawerList;
    public DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private static final int SHOW_PREFERENCES = 0;
    private String courseCode;
    UserEntity user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_list_activity);
        drawerCode();
        android.app.FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ModuleListFragment moduleListFragment = new ModuleListFragment();


        if(getIntent().getStringExtra(IntentConstants.COURSE_ID)!=null){
            courseCode = getIntent().getStringExtra(IntentConstants.COURSE_ID);
            user = (UserEntity) getIntent().getSerializableExtra("USER_DETAILS");
            Bundle bundle = new Bundle();
            bundle.putString(IntentConstants.COURSE_ID,courseCode);
            moduleListFragment.setArguments(bundle);
        }
        ft.replace(R.id.ModuleListActivityFrameLayout, moduleListFragment, "ModuleListFragment");
        //ft.addToBackStack("ModuleListFragment");
        ft.commit();
    }

    private void drawerCode() {
        mDrawerList = (ListView) findViewById(R.id.left_drawer_module_list);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_module_list);


        drawerList = new String[3];
        drawerList[0] = " Home ";
        drawerList[1] = " Settings ";
        drawerList[2] = " Sign Out ";


        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, drawerList));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                // getActionBar().setTitle("Ta-Helper");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                // getActionBar().setTitle("Ta-Helper Shortcuts");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isNotification = sharedPref.getBoolean("PREF_CHECK_BOX", false);
        Log.i("", "isNotification : " + isNotification);
    }

   /* @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, CourseActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(IntentConstants.COURSE_ID,courseCode);
        intent.putExtra("USER_DETAILS",user);
        startActivity(intent);
    }*/


    @Override
    public void addNewModuleEvent(View view) {
        Intent intent = new Intent(this, AddModuleActivity.class);
        intent.putExtra(IntentConstants.COURSE_ID,courseCode);
        startActivity(intent);
        overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out);
        finish();
    }

    public void onModuleItemClickEditDelete(String moduleName) {
        Intent intent = new Intent(this, EditDeleteModuleActivity.class);
        intent.putExtra(IntentConstants.MODULE_NAME, moduleName);
        intent.putExtra(IntentConstants.COURSE_ID,courseCode);
        startActivity(intent);
        overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
        finish();
    }

    public void onModuleItemClickedManage(String moduleName) {
        Intent intent = new Intent(this, ManageAssignmentsActivity.class);
        intent.putExtra("USER_DETAILS",user);
        intent.putExtra(IntentConstants.MODULE_NAME, moduleName);
        intent.putExtra(IntentConstants.COURSE_ID,courseCode);
        intent.putExtra("USER_DETAILS",user);
        startActivity(intent);
        overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out);
    }

   /* public void notifyBackButtonEvent(View view) {
        Intent intent = new Intent(this, CourseActivity.class);
        intent.putExtra(IntentConstants.COURSE_ID,courseCode);

     *//*   ActivityOptions options = ActivityOptions.makeScaleUpAnimation(view, 0,
                0, view.getWidth(), view.getHeight());*//*
        startActivity(intent);
        overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
    }*/

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        if (position == 2) {
            Intent loginscreen = new Intent(this, MainActivity.class);
            loginscreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginscreen);
            this.finish();
        } else if (position == 0) {
            Intent loginscreen = new Intent(this, CourseActivity.class);
            startActivity(loginscreen);
        } else if (position == 1) {
            Class<?> c = Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ?
                    MyPreferenceActivity.class : MyPreferenceActivity.class;

            Intent i = new Intent(this, c);
            startActivityForResult(i, SHOW_PREFERENCES);

        }
    }





}




