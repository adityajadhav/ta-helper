package com.cs442.team4.tahelper;

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
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs442.team4.tahelper.activity.ModuleListActivity;
import com.cs442.team4.tahelper.fragment.AddModuleFragment;
import com.cs442.team4.tahelper.fragment.ManageCourseFragment;
import com.cs442.team4.tahelper.model.UserEntity;
import com.cs442.team4.tahelper.preferences.MyPreferenceActivity;
import com.cs442.team4.tahelper.utils.ObjectUtils;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.SimpleShowcaseEventListener;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.ArrayList;

public class CourseActivity extends AppCompatActivity implements add_course_fragment.OnFinishAddCourseInterface, NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    String mode = "null";
    final public static String COURCE_ID_KEY = "COURSE_ID";
    static String COURSE_ID = null;
    String courseId = null;
    GoogleApiClient mGoogleApiClient;
    final Add_ta_fragment newAddTAFragment = new Add_ta_fragment();
    UserEntity user;
    private static final int SHOW_PREFERENCES = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if(savedInstanceState != null)
//        {
//            final ExportFragment newExportFragment = new ExportFragment();
//
//            newExportFragment.setExportFragmentInterface(new ExportFragment.ExportFragmentInterface() {
//                @Override
//                public void closeExportFragmentInterface() {
//                    FragmentManager fm = getFragmentManager();
//                    FragmentTransaction ft = fm.beginTransaction();
//                    Fragment id = fm.findFragmentByTag("export_fragment_tag");
//                    ft.remove(id);
//                    fm.popBackStack();
//                    ft.commit();
//                }
//            });
//
//            final ManageCourseFragment newManageCourseFragment = new ManageCourseFragment();
//            newManageCourseFragment.setManageCourseFragmentInterface(new ManageCourseFragment.ManageCourseFragmentInterface() {
//                @Override
//                public void callExportFragment(String courseId) {
//                    FragmentManager fm = getFragmentManager();
//                    FragmentTransaction ft = fm.beginTransaction();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("course_code", courseId);
//                    newExportFragment.setArguments(bundle);
//
//                    ft.replace(R.id.course_activity_frame_layout, newExportFragment, "export_fragment_tag");
//                    ft.addToBackStack("manage_course_fragment_tag");
//                    ft.commit();
//
//                }
//            });
//
//            newAddTAFragment.setAddTAFragmentInterface(new Add_ta_fragment.addTAToFirebaseInterface() {
//
//                public void sendTAdata(ArrayList<String> al) {
//                    add_course_fragment getInstance = (add_course_fragment) getFragmentManager().findFragmentByTag("add_course_fragment_tag");
//                    getInstance.setTAMembers(al);
//                }
//
//                public void closeAddTAFragment() {
//                    FragmentManager fm = getFragmentManager();
//                    FragmentTransaction ft = fm.beginTransaction();
//                    Fragment id = fm.findFragmentByTag("add_ta_fragment_tag");
//                    ft.remove(id);
//                    fm.popBackStack();
//                    ft.commit();
//                }
//            });
//
//            FragmentManager fm = getFragmentManager();
//            FragmentTransaction ft = fm.beginTransaction();
//            course_list_fragment cl = new course_list_fragment();
//            cl.setInterface(new course_list_fragment.OnActionButtonClickListener()
//
//                            {
//
//                                @Override
//                                public void callAddCourseFragment(String mode_from_fragment) {
//                                    mode = mode_from_fragment;
//                                    Bundle bundle = new Bundle();
//                                    bundle.putString("mode", mode);
//
//                                    add_course_fragment course_add_or_edit = new add_course_fragment();
//                                    course_add_or_edit.setArguments(bundle);
//
//                                    FragmentManager fm = getFragmentManager();
//                                    FragmentTransaction ft = fm.beginTransaction();
//
//                                    ft.replace(R.id.course_activity_frame_layout, course_add_or_edit, "add_course_fragment_tag");
//                                    ft.addToBackStack("course_list_fragment");
//                                    ft.commit();
//                                }
//
//                                @Override
//                                public void callManageCourseFragment_to_activity(String courseCode) {
//                                    // courseId = courseCode;
//                                    Bundle bundle = new Bundle();
//                                    bundle.putString(COURCE_ID_KEY, courseCode);
//                                    COURSE_ID = courseCode;
//                                    Log.i("Code in activity : ", courseCode);
//                                    newManageCourseFragment.setArguments(bundle);
//
//                                    newManageCourseFragment.setArguments(bundle);
//                                    FragmentManager fm = getFragmentManager();
//                                    FragmentTransaction ft = fm.beginTransaction();
//
//                                    ft.replace(R.id.course_activity_frame_layout, newManageCourseFragment, "manage_course_fragment_tag");
//                                    ft.addToBackStack("course_list_fragment");
//                                    ft.commit();
//                                }
//
//                                @Override
//                                public void editCourseFragment_to_activity(String mode_from_fragment, String
//                                        courseCode) {
//                                    mode = mode_from_fragment;
//                                    Bundle bundle = new Bundle();
//                                    bundle.putString("mode", mode);
//                                    bundle.putString("course_code", courseCode);
//
//                                    add_course_fragment course_add_or_edit = new add_course_fragment();
//                                    course_add_or_edit.setArguments(bundle);
//
//                                    FragmentManager fm = getFragmentManager();
//                                    FragmentTransaction ft = fm.beginTransaction();
//
//                                    ft.replace(R.id.course_activity_frame_layout, course_add_or_edit, "add_course_fragment_tag");
//                                    ft.addToBackStack("course_list_fragment");
//                                    ft.commit();
//                                }
//
//                                @Override
//                                public void callModuleActivity_to_activity(String courseCode) {
//                                    Intent intent = new Intent(getApplicationContext(), ModuleListActivity.class);
//                                    intent.putExtra("course_id", courseCode);
//                                    intent.putExtra("USER_DETAILS", user);
//                                    startActivity(intent);
//                                }
//                            }
//
//
//            );
//            ft.replace(R.id.course_activity_frame_layout, cl, "course_list_fragment");
//            ft.commit();
//
//        }
//        else {

            Intent intent = getIntent();
            user = (UserEntity) intent.getSerializableExtra("USER_DETAILS");
            Log.d("taem4", user.toString());

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);


            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
            OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            View hView = navigationView.getHeaderView(0);
            ImageView imageView = (ImageView) hView.findViewById(R.id.profilePic);
            TextView userName = (TextView) hView.findViewById(R.id.userName);
            TextView emailIdtextView = (TextView) hView.findViewById(R.id.emailIdtextView);


            emailIdtextView.setText(user.getEmail());
            userName.setText(user.getDisplayName());

            if (ObjectUtils.isNotEmpty(user.getPhotoUrl()))
                new DownloadImageTask(imageView)
                        .execute(user.getPhotoUrl());

            final ExportFragment newExportFragment = new ExportFragment();

            newExportFragment.setExportFragmentInterface(new ExportFragment.ExportFragmentInterface() {
                @Override
                public void closeExportFragmentInterface() {
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    Fragment id = fm.findFragmentByTag("export_fragment_tag");
                    ft.remove(id);
                    fm.popBackStack();
                    ft.commit();
                }
            });

            final ManageCourseFragment newManageCourseFragment = new ManageCourseFragment();
            newManageCourseFragment.setManageCourseFragmentInterface(new ManageCourseFragment.ManageCourseFragmentInterface() {
                @Override
                public void callExportFragment(String courseId) {
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putString("course_code", courseId);
                    newExportFragment.setArguments(bundle);

                    ft.replace(R.id.course_activity_frame_layout, newExportFragment, "export_fragment_tag");
                    ft.addToBackStack("manage_course_fragment_tag");
                    ft.commit();





                }
            });

            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            course_list_fragment cl = new course_list_fragment();
            cl.setInterface(new course_list_fragment.OnActionButtonClickListener()

                            {

                                @Override
                                public void callAddCourseFragment(String mode_from_fragment) {
                                    mode = mode_from_fragment;
                                    Bundle bundle = new Bundle();
                                    bundle.putString("mode", mode);

                                    add_course_fragment course_add_or_edit = new add_course_fragment();
                                    course_add_or_edit.setArguments(bundle);

                                    FragmentManager fm = getFragmentManager();
                                    FragmentTransaction ft = fm.beginTransaction();

                                    ft.replace(R.id.course_activity_frame_layout, course_add_or_edit, "add_course_fragment_tag");
                                    ft.addToBackStack("course_list_fragment");
                                    ft.commit();
                                }

                                @Override
                                public void callManageCourseFragment_to_activity(String courseCode) {
                                    // courseId = courseCode;
                                    Bundle bundle = new Bundle();
                                    bundle.putString(COURCE_ID_KEY, courseCode);
                                    COURSE_ID = courseCode;
                                    Log.i("Code in activity : ", courseCode);
                                    newManageCourseFragment.setArguments(bundle);

                                    newManageCourseFragment.setArguments(bundle);
                                    FragmentManager fm = getFragmentManager();
                                    FragmentTransaction ft = fm.beginTransaction();

                                    ft.replace(R.id.course_activity_frame_layout, newManageCourseFragment, "manage_course_fragment_tag");
                                    ft.addToBackStack("course_list_fragment");
                                    ft.commit();
                                }

                                @Override
                                public void editCourseFragment_to_activity(String mode_from_fragment, String
                                        courseCode) {
                                    mode = mode_from_fragment;
                                    Bundle bundle = new Bundle();
                                    bundle.putString("mode", mode);
                                    bundle.putString("course_code", courseCode);

                                    add_course_fragment course_add_or_edit = new add_course_fragment();
                                    course_add_or_edit.setArguments(bundle);

                                    FragmentManager fm = getFragmentManager();
                                    FragmentTransaction ft = fm.beginTransaction();

                                    ft.replace(R.id.course_activity_frame_layout, course_add_or_edit, "add_course_fragment_tag");
                                    ft.addToBackStack("course_list_fragment");
                                    ft.commit();
                                }

                                @Override
                                public void callModuleActivity_to_activity(String courseCode) {
                                    Intent intent = new Intent(getApplicationContext(), ModuleListActivity.class);
                                    intent.putExtra("course_id", courseCode);
                                    intent.putExtra("USER_DETAILS", user);
                                    startActivity(intent);
                                }
                            }

            );
            ft.replace(R.id.course_activity_frame_layout, cl, "course_list_fragment");
            //   ft.replace(R.id.course_activity_frame_layout,course_list,"course_list_fragment");
            ft.commit();


            newAddTAFragment.setAddTAFragmentInterface(new Add_ta_fragment.addTAToFirebaseInterface() {

                public void sendTAdata(ArrayList<String> al) {
                    add_course_fragment getInstance = (add_course_fragment) getFragmentManager().findFragmentByTag("add_course_fragment_tag");
                    getInstance.setTAMembers(al);
                }

                public void closeAddTAFragment() {
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    Fragment id = fm.findFragmentByTag("add_ta_fragment_tag");
                    ft.remove(id);
                    fm.popBackStack();
//                    FragmentManager manager = getFragmentManager();
//                    FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
//                    manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    ft.commit();
                }
            });


    }

    @Override
    public void closeAddCourseFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment id = fm.findFragmentByTag("add_course_fragment_tag");
        ft.remove(id);
        fm.popBackStack();
        ft.commit();
    }

    @Override
    public void callAddTAs_to_activity(ArrayList<String> ta_members) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();


        ft.replace(R.id.course_activity_frame_layout, newAddTAFragment, "add_ta_fragment_tag");
        ft.addToBackStack("add_course_fragment_tag");
        ft.commit();

        newAddTAFragment.getExisitingMembers(ta_members);
        // Add_ta_fragment getInstance = (Add_ta_fragment) getFragmentManager().findFragmentByTag("add_ta_fragment_tag");
        // getInstance.getExisitingMembers(ta_memebers);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
                    finish();
                }
            });

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
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
            startActivityForResult(i, SHOW_PREFERENCES);
        }else if(id == R.id.nav_notification_help){


            course_list_fragment frag = (course_list_fragment) getFragmentManager().findFragmentByTag("course_list_fragment");
            if (frag != null && frag.isVisible()) {
                // add your code here
                courseListShowCase();
            }

            Add_ta_fragment ta_frag = (Add_ta_fragment) getFragmentManager().findFragmentByTag("add_ta_fragment_tag");
            if (ta_frag != null && ta_frag.isVisible()) {
                // add your code here
                TAListShowCase();
            }




        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void courseListShowCase() {
        final course_list_fragment frag = (course_list_fragment) getFragmentManager().findFragmentByTag("course_list_fragment");
        new ShowcaseView.Builder(this)
                .withMaterialShowcase()
                .setStyle(R.style.CustomShowcaseTheme2)
                .setTarget(new ViewTarget(frag.getView().findViewById(R.id.add_course_fab_layout)))
                .hideOnTouchOutside()
                .setContentTitle("Add a course by clicking on this button")
                .setShowcaseEventListener(new SimpleShowcaseEventListener() {

                    @Override
                    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                       // showSecondShowCase();
                        View vw = frag.getView().findViewById(R.id.manage_course_btn_layout);
                        if(vw != null)
                        {
                            courseListSecondShowCase();
                        }
                    }

                })
                .build();
    }

    private void courseListSecondShowCase() {
        course_list_fragment frag = (course_list_fragment) getFragmentManager().findFragmentByTag("course_list_fragment");
        new ShowcaseView.Builder(this)
                .withMaterialShowcase()
                .setStyle(R.style.CustomShowcaseTheme2)
                .setTarget(new ViewTarget(frag.getView().findViewById(R.id.manage_course_btn_layout)))
                .hideOnTouchOutside()
                .setContentTitle("Click here to find course related operations. \n\nFor example: \n\n1)Import the student list \n\n2) Export assignment marks \n\n3)Generate groups for projects \n\n4)Send broadcast email to students")
                .setShowcaseEventListener(new SimpleShowcaseEventListener() {

                    @Override
                    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                         courseListThirdShowCase();
                    }

                })
                .build();

    }

    private void courseListThirdShowCase() {
        course_list_fragment frag = (course_list_fragment) getFragmentManager().findFragmentByTag("course_list_fragment");

        new ShowcaseView.Builder(this)
                .withMaterialShowcase()
                .setStyle(R.style.CustomShowcaseTheme2)
                .setTarget(new ViewTarget(frag.getView().findViewById(R.id.edit_course_btn_layout)))
                .hideOnTouchOutside()
                .setContentTitle("Click here to make changes to an existing course. \n\nFor example: Change course name, id, TA members")
                .setShowcaseEventListener(new SimpleShowcaseEventListener() {

                    @Override
                    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                         courseListFourthShowCase();
                    }

                })
                .build();
    }
    private void courseListFourthShowCase() {
        course_list_fragment frag = (course_list_fragment) getFragmentManager().findFragmentByTag("course_list_fragment");

        new ShowcaseView.Builder(this)
                .withMaterialShowcase()
                .setStyle(R.style.CustomShowcaseTheme2)
                .setTarget(new ViewTarget(frag.getView().findViewById(R.id.grade_course_btn_layout)))
                .hideOnTouchOutside()
                .setContentTitle("Click here to grade assignments, projects etc.")
                .setShowcaseEventListener(new SimpleShowcaseEventListener() {

                    @Override
                    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                         courseListFifthShowCase();
                    }

                })
                .build();
    }
    private void courseListFifthShowCase() {
        course_list_fragment frag = (course_list_fragment) getFragmentManager().findFragmentByTag("course_list_fragment");

        new ShowcaseView.Builder(this)
                .withMaterialShowcase()
                .setStyle(R.style.CustomShowcaseTheme2)
                .setTarget(new ViewTarget(frag.getView().findViewById(R.id.delete_course_btn_layout)))
                .hideOnTouchOutside()
                .setContentTitle("Click here to delete a course* \n\n\n*Note: Deleting a course will delete all the data (assignment scores, project scores) associated with the course")
                .setShowcaseEventListener(new SimpleShowcaseEventListener() {

                    @Override
                    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                        // showSecondShowCase();
                    }

                })
                .build();
    }
    private void TAListShowCase() {
        final Add_ta_fragment ta_frag = (Add_ta_fragment) getFragmentManager().findFragmentByTag("add_ta_fragment_tag");
        new ShowcaseView.Builder(this)
                .withMaterialShowcase()
                .setStyle(R.style.CustomShowcaseTheme2)
                .setTarget(new ViewTarget(ta_frag.getView().findViewById(R.id.ta_drop_down_layout)))
                .hideOnTouchOutside()
                .setContentTitle("Select the TA to be added from the drop down")
                .setShowcaseEventListener(new SimpleShowcaseEventListener() {

                    @Override
                    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                        TASecondShowCase();
//                        View vw = ta_frag.getView().findViewById(R.id.manage_course_btn_layout);
//                        if(vw != null)
//                        {
//                            courseListSecondShowCase();
//                        }
                    }

                })
                .build();
    }
    private void TASecondShowCase() {
        final Add_ta_fragment ta_frag = (Add_ta_fragment) getFragmentManager().findFragmentByTag("add_ta_fragment_tag");
        new ShowcaseView.Builder(this)
                .withMaterialShowcase()
                .setStyle(R.style.CustomShowcaseTheme2)
                .setTarget(new ViewTarget(ta_frag.getView().findViewById(R.id.add_to_list_btn_layout)))
                .hideOnTouchOutside()
                .setContentTitle("Click here to add the TA to the list")
                .setShowcaseEventListener(new SimpleShowcaseEventListener() {

                    @Override
                    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

                        View vw = ta_frag.getView().findViewById(R.id.select_ta_btn_layout);
                        if(vw != null)
                        {

                            TAThirdShowCase();

                        }
                        else
                        {
                            TAFourthShowCase();
                        }
                    }

                })
                .build();
    }

    private void TAFourthShowCase() {
        final Add_ta_fragment ta_frag = (Add_ta_fragment) getFragmentManager().findFragmentByTag("add_ta_fragment_tag");
        new ShowcaseView.Builder(this)
                .withMaterialShowcase()
                .setStyle(R.style.CustomShowcaseTheme2)
                .setTarget(new ViewTarget(ta_frag.getView().findViewById(R.id.add_ta_final_btn_layout)))
                .hideOnTouchOutside()
                .setContentTitle("Click here once you have added all the TAs needed for the course")
                .setShowcaseEventListener(new SimpleShowcaseEventListener() {

                    @Override
                    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                        //TASecondShowCase();
//                        View vw = ta_frag.getView().findViewById(R.id.manage_course_btn_layout);
//                        if(vw != null)
//                        {
//                            courseListSecondShowCase();
//                        }
                    }

                })
                .build();
    }
    private void TAThirdShowCase() {
        final Add_ta_fragment ta_frag = (Add_ta_fragment) getFragmentManager().findFragmentByTag("add_ta_fragment_tag");
        new ShowcaseView.Builder(this)
                .withMaterialShowcase()
                .setStyle(R.style.CustomShowcaseTheme2)
                .setTarget(new ViewTarget(ta_frag.getView().findViewById(R.id.select_ta_btn_layout)))
                .hideOnTouchOutside()
                .setContentTitle("Click here to remove the TA from the list")
                .setShowcaseEventListener(new SimpleShowcaseEventListener() {

                    @Override
                    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                        TAFourthShowCase();
//                        View vw = ta_frag.getView().findViewById(R.id.manage_course_btn_layout);
//                        if(vw != null)
//                        {
//                            courseListSecondShowCase();
//                        }
                    }

                })
                .build();
    }



    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d("team4", "onConnectionFailed:" + connectionResult);
    }

}
