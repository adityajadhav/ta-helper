package com.cs442.team4.tahelper.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
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
import com.cs442.team4.tahelper.fragment.EditDeleteModuleFragment;
import com.cs442.team4.tahelper.fragment.ManageAssignmentsFragment;
import com.cs442.team4.tahelper.preferences.MyPreferenceActivity;
import com.cs442.team4.tahelper.student.GradeStudentListActivity;

/**
 * Created by sowmyaparameshwara on 10/31/16.
 */

public class ManageAssignmentsActivity extends AppCompatActivity implements ManageAssignmentsFragment.ManageAssignmentFragmentListener {
    private ListView mDrawerList;
    private String[] drawerList;
    public DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private static final int SHOW_PREFERENCES = 0;
    private String courseCode;

    String moduleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_assignments_activity);
        Intent intent = getIntent();
        if(intent.getStringExtra(IntentConstants.MODULE_NAME)!=null){
            moduleName = intent.getStringExtra(IntentConstants.MODULE_NAME);
            courseCode = intent.getStringExtra(IntentConstants.COURSE_ID);
        }
        drawerCode();
        ManageAssignmentsFragment manageAssignmentsFragment = (ManageAssignmentsFragment) getFragmentManager().findFragmentById(R.id.ManageAssignmentsFragmentView);
        manageAssignmentsFragment.initialise(getIntent());
    }

    @Override
    public void notifyAddAssignmentEvent() {
        Intent intent = new Intent(this,AddAssignmentsActivity.class);
        intent.putExtra(IntentConstants.MODULE_NAME,moduleName);
        intent.putExtra(IntentConstants.COURSE_ID,courseCode);
        startActivity(intent);
        overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out);

    }

    public void onAssignmentItemClickEditDelete(String assignmentName){
        Intent intent = new Intent(this, EditDeleteAssignmentActivity.class);
        intent.putExtra(IntentConstants.MODULE_NAME,moduleName);
        intent.putExtra(IntentConstants.ASSIGNMENT_NAME,assignmentName);
        intent.putExtra(IntentConstants.COURSE_ID,courseCode);
        startActivity(intent);
        overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);

    }


    public void onAssignmentItemClickScore(String assignmentName){
        Intent intent = new Intent(this, GradeStudentListActivity.class);
        intent.putExtra(IntentConstants.MODULE_NAME,moduleName);
        intent.putExtra(IntentConstants.MODULE_ITEM,assignmentName);
        intent.putExtra(IntentConstants.COURSE_ID,courseCode);

        startActivity(intent);
        overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out);

    }

    public void notifyBackButton(){
        Intent intent = new Intent(this,ModuleListActivity.class);
        intent.putExtra(IntentConstants.COURSE_ID,courseCode);
        startActivity(intent);
        overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);

    }

    private void drawerCode() {
        mDrawerList = (ListView) findViewById(R.id.left_drawer_manage_assignment_list);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_manage_assignment_list);


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
