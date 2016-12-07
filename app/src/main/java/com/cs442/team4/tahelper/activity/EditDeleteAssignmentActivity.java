package com.cs442.team4.tahelper.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cs442.team4.tahelper.CourseActivity;
import com.cs442.team4.tahelper.MainActivity;
import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.contants.IntentConstants;
import com.cs442.team4.tahelper.fragment.AddAssignmentsFragment;
import com.cs442.team4.tahelper.fragment.EditDeleteAssignmentFragment;
import com.cs442.team4.tahelper.model.AssignmentSplit;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by sowmyaparameshwara on 11/1/16.
 */

public class EditDeleteAssignmentActivity extends AppCompatActivity implements EditDeleteAssignmentFragment.EditDeleteAssignmentsFragmentListener{

    private ListView mDrawerList;
    private String[] drawerList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private String courseCode;
    private String moduleName;
    private String assignmentName;
    private boolean isGraded;
    EditDeleteAssignmentFragment editDeleteAssignmentFragment;


    @Override
    protected void onCreate(Bundle onSavedInstance){
        super.onCreate(onSavedInstance);
        setContentView(R.layout.editdelete_assignments_activity);
        android.app.FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        editDeleteAssignmentFragment = new EditDeleteAssignmentFragment();

        if(getIntent().getStringExtra(IntentConstants.COURSE_ID)!=null){
            courseCode = getIntent().getStringExtra(IntentConstants.COURSE_ID);
            moduleName = getIntent().getStringExtra(IntentConstants.MODULE_NAME);
            assignmentName = getIntent().getStringExtra(IntentConstants.ASSIGNMENT_NAME);
            Bundle bundle = new Bundle();
            bundle.putString(IntentConstants.COURSE_ID,courseCode);
            bundle.putString(IntentConstants.MODULE_NAME,moduleName);
            bundle.putString(IntentConstants.ASSIGNMENT_NAME,getIntent().getStringExtra(IntentConstants.ASSIGNMENT_NAME));
            editDeleteAssignmentFragment.setArguments(bundle);
        }
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

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

        validateEditButton();

        ft.replace(R.id.EditDeleteAssignmentsFragmentFrameLayout, editDeleteAssignmentFragment, "editdelete_assignment_fragment");
        ft.commit();
    }

    private void validateEditButton() {
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("modules").child(courseCode).child(moduleName).child(assignmentName).push();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    if(postSnapshot.getKey().equals("isGraded")){
                        isGraded = postSnapshot.getValue(Boolean.class);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void deleteSplit(AssignmentSplit split) {
       // EditDeleteAssignmentFragment editDeleteAssignmentsFragment = (EditDeleteAssignmentFragment) getFragmentManager().findFragmentById(R.id.EditDeleteAssignmentsFragmentView);
        editDeleteAssignmentFragment.deleteSplit(split);

    }

    @Override
    public void notifyEditDeleteAssignmentEvent(String moduleName) {
        Intent intent = new Intent(this,ManageAssignmentsActivity.class);
        intent.putExtra(IntentConstants.MODULE_NAME,moduleName);
        intent.putExtra(IntentConstants.COURSE_ID,courseCode);

        startActivity(intent);
        finish();

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ManageAssignmentsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(IntentConstants.MODULE_NAME,moduleName);
        intent.putExtra(IntentConstants.COURSE_ID,courseCode);
        startActivity(intent);
        finish();
    }

   /* public void notifyBackButtonEvent(String moduleName){
        Intent intent = new Intent(this,ManageAssignmentsActivity.class);
        intent.putExtra(IntentConstants.MODULE_NAME,moduleName);
        intent.putExtra(IntentConstants.COURSE_ID,courseCode);

        startActivity(intent);
    }*/

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
       if(position==2){
           Intent loginscreen=new Intent(this,MainActivity.class);
           loginscreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
           startActivity(loginscreen);
           this.finish();
       }else if(position==0){
           Intent loginscreen=new Intent(this,CourseActivity.class);
           startActivity(loginscreen);
       }
    }


}
