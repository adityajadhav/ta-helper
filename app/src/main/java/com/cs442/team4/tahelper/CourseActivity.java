package com.cs442.team4.tahelper;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.cs442.team4.tahelper.activity.ModuleListActivity;
import com.cs442.team4.tahelper.fragment.ManageCourseFragment;

import java.util.ArrayList;

public class CourseActivity extends AppCompatActivity implements add_course_fragment.OnFinishAddCourseInterface {

    String mode = "null";
    final public static String COURCE_ID_KEY = "COURSE_ID";
    static String COURSE_ID = null;
    String courseId = null;
    final Add_ta_fragment newAddTAFragment = new Add_ta_fragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);


        final ManageCourseFragment newManageCourseFragment = new ManageCourseFragment();

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
                                bundle.putString("course_id", courseCode);
                                COURSE_ID = courseCode;
                                Log.i("Code in activity : ", courseCode);

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

                    public void closeAddTAFragment()
                    {
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        Fragment id = fm.findFragmentByTag("add_ta_fragment_tag");
                        ft.remove(id);
                        fm.popBackStack();
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
    public void callAddTAs_to_activity(ArrayList<String> ta_memebers){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.replace(R.id.course_activity_frame_layout, newAddTAFragment, "add_ta_fragment_tag");
        ft.addToBackStack("add_course_fragment_tag");
        ft.commit();

        newAddTAFragment.getExisitingMembers(ta_memebers);
       // Add_ta_fragment getInstance = (Add_ta_fragment) getFragmentManager().findFragmentByTag("add_ta_fragment_tag");
      // getInstance.getExisitingMembers(ta_memebers);
    }


}
