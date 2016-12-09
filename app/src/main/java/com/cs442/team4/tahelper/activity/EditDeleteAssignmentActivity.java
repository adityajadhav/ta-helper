package com.cs442.team4.tahelper.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cs442.team4.tahelper.CourseActivity;
import com.cs442.team4.tahelper.DownloadImageTask;
import com.cs442.team4.tahelper.MainActivity;
import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.contants.IntentConstants;
import com.cs442.team4.tahelper.fragment.AddAssignmentsFragment;
import com.cs442.team4.tahelper.fragment.EditDeleteAssignmentFragment;
import com.cs442.team4.tahelper.model.AssignmentSplit;
import com.cs442.team4.tahelper.model.UserEntity;
import com.cs442.team4.tahelper.preferences.MyPreferenceActivity;
import com.cs442.team4.tahelper.utils.ObjectUtils;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.SimpleShowcaseEventListener;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by sowmyaparameshwara on 11/1/16.
 */

public class EditDeleteAssignmentActivity extends AppCompatActivity implements EditDeleteAssignmentFragment.EditDeleteAssignmentsFragmentListener, NavigationView.OnNavigationItemSelectedListener,GoogleApiClient.OnConnectionFailedListener{

/*    private ListView mDrawerList;
    private String[] drawerList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;*/
    private String courseCode;
    private String moduleName;
    private String assignmentName;
    private boolean isGraded;
    EditDeleteAssignmentFragment editDeleteAssignmentFragment;
    UserEntity user;
    GoogleApiClient mGoogleApiClient;




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
            user = (UserEntity) getIntent().getSerializableExtra("USER_DETAILS");

            Bundle bundle = new Bundle();
            bundle.putString(IntentConstants.COURSE_ID,courseCode);
            bundle.putString(IntentConstants.MODULE_NAME,moduleName);
            bundle.putString(IntentConstants.ASSIGNMENT_NAME,getIntent().getStringExtra(IntentConstants.ASSIGNMENT_NAME));
            bundle.putStringArrayList(IntentConstants.ASSIGNMENT_LIST,getIntent().getStringArrayListExtra(IntentConstants.ASSIGNMENT_LIST));
            editDeleteAssignmentFragment.setArguments(bundle);
        }
       /* mDrawerList = (ListView) findViewById(R.id.left_drawer);
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
                this,                  *//* host Activity *//*
                mDrawerLayout,         *//* DrawerLayout object *//*
                R.drawable.ic_drawer,  *//* nav drawer image to replace 'Up' caret *//*
                R.string.drawer_open,  *//* "open drawer" description for accessibility *//*
                R.string.drawer_close  *//* "close drawer" description for accessibility *//*
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
*/
        validateEditButton();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_edit_delete_assignment);
        android.support.v7.app.ActionBarDrawerToggle toggle = new android.support.v7.app.ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_edit_delete_assignment);
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);
        ImageView imageView = (ImageView) hView.findViewById(R.id.profilePic);
        TextView userName = (TextView) hView.findViewById(R.id.userName);
        TextView emailIdtextView = (TextView) hView.findViewById(R.id.emailIdtextView);

        if(user!=null) {

            emailIdtextView.setText(user.getEmail());
            userName.setText(user.getDisplayName());

            if (ObjectUtils.isNotEmpty(user.getPhotoUrl()))
                new DownloadImageTask(imageView)
                        .execute(user.getPhotoUrl());
        }


        ft.replace(R.id.EditDeleteAssignmentsFragmentFrameLayout, editDeleteAssignmentFragment, "editdelete_assignment_fragment");
        ft.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_share) {
            try {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:"));
                sendIntent.putExtra("sms_body", "TA Helper is a one stop solution to manage work related to teaching assistant's");
                startActivity(sendIntent);
                getFragmentManager().popBackStack();
            } catch (ActivityNotFoundException e) {
            }
        }else if(id == R.id.nav_notification_settings){
            Class<?> c = Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ?
                    MyPreferenceActivity.class : MyPreferenceActivity.class;

            Intent i = new Intent(this, c);
            startActivityForResult(i, 0);
        }else if(id == R.id.nav_notification_help){
            showCase();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_edit_delete_assignment);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {


            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    Log.d("team4", "onResult:Logout ");
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d("team4", "onConnectionFailed:" + connectionResult);
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
        intent.putExtra("USER_DETAILS",user);

        startActivity(intent);
        finish();

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ManageAssignmentsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(IntentConstants.MODULE_NAME,moduleName);
        intent.putExtra(IntentConstants.COURSE_ID,courseCode);
        intent.putExtra("USER_DETAILS",user);
        startActivity(intent);
        finish();
    }

   /* public void notifyBackButtonEvent(String moduleName){
        Intent intent = new Intent(this,ManageAssignmentsActivity.class);
        intent.putExtra(IntentConstants.MODULE_NAME,moduleName);
        intent.putExtra(IntentConstants.COURSE_ID,courseCode);

        startActivity(intent);
    }*/

    /*private class DrawerItemClickListener implements ListView.OnItemClickListener {
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
    }*/

    private void showCase() {
        EditDeleteAssignmentFragment editDeleteAssignmentFragment = (EditDeleteAssignmentFragment) getFragmentManager().findFragmentById(R.id.EditDeleteAssignmentsFragmentFrameLayout);
        if(editDeleteAssignmentFragment.editAssignment.getVisibility()==View.VISIBLE){
            new ShowcaseView.Builder(this)
                    .withMaterialShowcase()
                    .setStyle(R.style.CustomShowcaseTheme2)
                    .setTarget(new ViewTarget(editDeleteAssignmentFragment.assignmentName))
                    .hideOnTouchOutside()
                    .setContentTitle("The sub module name can be renamed here.\n")
                    .setShowcaseEventListener(new SimpleShowcaseEventListener() {

                        @Override
                        public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                            showSecondShowCase();
                        }

                    })
                    .build();
        }else{
            new ShowcaseView.Builder(this)
                    .withMaterialShowcase()
                    .setStyle(R.style.CustomShowcaseTheme2)
                    .setTarget(new ViewTarget(editDeleteAssignmentFragment.assignmentName))
                    .hideOnTouchOutside()
                    .setContentTitle("The sub module name.\nThis field is disabled since atleast one student has been graded for this sub module.")
                    .setShowcaseEventListener(new SimpleShowcaseEventListener() {

                        @Override
                        public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                            showSecondShowCase();
                        }

                    })
                    .build();
        }

    }

    private void showSecondShowCase() {
        EditDeleteAssignmentFragment editDeleteAssignmentFragment = (EditDeleteAssignmentFragment) getFragmentManager().findFragmentById(R.id.EditDeleteAssignmentsFragmentFrameLayout);
        if(editDeleteAssignmentFragment.editAssignment.getVisibility()==View.VISIBLE){
            new ShowcaseView.Builder(this)
                    .withMaterialShowcase()
                    .setStyle(R.style.CustomShowcaseTheme2)
                    .setTarget(new ViewTarget(editDeleteAssignmentFragment.assignmentTotalScore))
                    .hideOnTouchOutside()
                    .setContentTitle("The sub module total can be changed here.\n")
                    .setShowcaseEventListener(new SimpleShowcaseEventListener() {

                        @Override
                        public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                            showThirdShowCase();
                        }

                    })
                    .build();
        }else{
            new ShowcaseView.Builder(this)
                    .withMaterialShowcase()
                    .setStyle(R.style.CustomShowcaseTheme2)
                    .setTarget(new ViewTarget(editDeleteAssignmentFragment.assignmentTotalScore))
                    .hideOnTouchOutside()
                    .setContentTitle("Total score for this sub module.\nThis field is disabled since atleast one student has been graded for this sub module.")
                    .setShowcaseEventListener(new SimpleShowcaseEventListener() {

                        @Override
                        public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                            showThirdShowCase();
                        }

                    })
                    .build();
        }

    }

    private void showThirdShowCase() {
        EditDeleteAssignmentFragment editDeleteAssignmentFragment = (EditDeleteAssignmentFragment) getFragmentManager().findFragmentById(R.id.EditDeleteAssignmentsFragmentFrameLayout);
        if(editDeleteAssignmentFragment.editAssignment.getVisibility()==View.VISIBLE){
            new ShowcaseView.Builder(this)
                    .withMaterialShowcase()
                    .setStyle(R.style.CustomShowcaseTheme2)
                    .setTarget(new ViewTarget(editDeleteAssignmentFragment.assignmentWeightage))
                    .hideOnTouchOutside()
                    .setContentTitle("The sub module weightage can be changed here.\n")
                    .setShowcaseEventListener(new SimpleShowcaseEventListener() {

                        @Override
                        public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                            showFourthShowCase();
                        }

                    })
                    .build();
        }else{
            new ShowcaseView.Builder(this)
                    .withMaterialShowcase()
                    .setStyle(R.style.CustomShowcaseTheme2)
                    .setTarget(new ViewTarget(editDeleteAssignmentFragment.assignmentWeightage))
                    .hideOnTouchOutside()
                    .setContentTitle("Weightage for this sub module.\nThis field is disabled since atleast one student has been graded for this sub module.")
                    .setShowcaseEventListener(new SimpleShowcaseEventListener() {

                        @Override
                        public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                            showFourthShowCase();
                        }

                    })
                    .build();
        }

    }

    private void showFourthShowCase() {
        EditDeleteAssignmentFragment editDeleteAssignmentFragment = (EditDeleteAssignmentFragment) getFragmentManager().findFragmentById(R.id.EditDeleteAssignmentsFragmentFrameLayout);
        if(editDeleteAssignmentFragment.editAssignment.getVisibility()==View.VISIBLE){
            new ShowcaseView.Builder(this)
                    .withMaterialShowcase()
                    .setStyle(R.style.CustomShowcaseTheme2)
                    .setTarget(new ViewTarget(editDeleteAssignmentFragment.splitName))
                    .hideOnTouchOutside()
                    .setContentTitle("This is optional.\n" +
                            " This is used to define the splits through which sub module scoring can be divided.\n" +
                            " Name for the split of this sub module.\n")
                    .setShowcaseEventListener(new SimpleShowcaseEventListener() {

                        @Override
                        public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                            showFifthShowCase();
                        }

                    })
                    .build();
        }else{
            showFifthShowCase();
        }

    }

    private void showFifthShowCase() {
        EditDeleteAssignmentFragment editDeleteAssignmentFragment = (EditDeleteAssignmentFragment) getFragmentManager().findFragmentById(R.id.EditDeleteAssignmentsFragmentFrameLayout);
        if(editDeleteAssignmentFragment.editAssignment.getVisibility()==View.VISIBLE){
            new ShowcaseView.Builder(this)
                    .withMaterialShowcase()
                    .setStyle(R.style.CustomShowcaseTheme2)
                    .setTarget(new ViewTarget(editDeleteAssignmentFragment.splitScore))
                    .hideOnTouchOutside()
                    .setContentTitle("This is required if split name is defined.\n" +
                            " Score corresponding to the split name of this sub module.\n" +
                            "The sum of all the split score should be equal to the total sub module score.\n")
                    .setShowcaseEventListener(new SimpleShowcaseEventListener() {

                        @Override
                        public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                            showSixthShowCase();
                        }

                    })
                    .build();
        }else{
            showSixthShowCase();
        }

    }

    private void showSixthShowCase() {
        EditDeleteAssignmentFragment editDeleteAssignmentFragment = (EditDeleteAssignmentFragment) getFragmentManager().findFragmentById(R.id.EditDeleteAssignmentsFragmentFrameLayout);
        if(editDeleteAssignmentFragment.editAssignment.getVisibility()==View.VISIBLE){
            new ShowcaseView.Builder(this)
                    .withMaterialShowcase()
                    .setStyle(R.style.CustomShowcaseTheme2)
                    .setTarget(new ViewTarget(editDeleteAssignmentFragment.addSplitButton))
                    .hideOnTouchOutside()
                    .setContentTitle("Click this button for adding the split.\n")
                    .setShowcaseEventListener(new SimpleShowcaseEventListener() {

                        @Override
                        public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                            showSeventhShowCase();
                        }

                    })
                    .build();
        }else{
            showSeventhShowCase();
        }

    }

    private void showSeventhShowCase() {
        EditDeleteAssignmentFragment editDeleteAssignmentFragment = (EditDeleteAssignmentFragment) getFragmentManager().findFragmentById(R.id.EditDeleteAssignmentsFragmentFrameLayout);
        if(editDeleteAssignmentFragment.editAssignment.getVisibility()==View.VISIBLE){
            new ShowcaseView.Builder(this)
                    .withMaterialShowcase()
                    .setStyle(R.style.CustomShowcaseTheme2)
                    .setTarget(new ViewTarget(editDeleteAssignmentFragment.editAssignment))
                    .hideOnTouchOutside()
                    .setContentTitle("Click this button for saving the editted details for this sub module.\n")
                    .setShowcaseEventListener(new SimpleShowcaseEventListener() {

                        @Override
                        public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                            showEigthShowCase();
                        }

                    })
                    .build();
        }else{
            showEigthShowCase();
        }

    }

    private void showEigthShowCase() {
        EditDeleteAssignmentFragment editDeleteAssignmentFragment = (EditDeleteAssignmentFragment) getFragmentManager().findFragmentById(R.id.EditDeleteAssignmentsFragmentFrameLayout);
        new ShowcaseView.Builder(this)
                    .withMaterialShowcase()
                    .setStyle(R.style.CustomShowcaseTheme2)
                    .setTarget(new ViewTarget(editDeleteAssignmentFragment.deleteAssignment))
                    .hideOnTouchOutside()
                    .setContentTitle("Click this button for deleting this sub module.\n")
                    .setShowcaseEventListener(new SimpleShowcaseEventListener() {

                        @Override
                        public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

                        }

                    })
                    .build();

    }


}
