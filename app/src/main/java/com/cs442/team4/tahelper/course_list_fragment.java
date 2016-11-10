package com.cs442.team4.tahelper;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by ullas on 10/29/2016.
 */

public class course_list_fragment extends Fragment {
    @Nullable


    OnActionButtonClickListener mClick;

    public interface OnActionButtonClickListener{
            public void callAddCourseFragment(String mode_from_fragment);
            public void callManageCourseFragment_to_activity(String course_id);
            public void editCourseFragment_to_activity(String mode, String course_id);
            public void callModuleActivity_to_activity(String courseCode);
    }

    public void setInterface(OnActionButtonClickListener oa)
    {
        this.mClick = oa;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        return inflater.inflate(R.layout.courses_list_fragment, container, false);


    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {



        Button myFab = (Button)  view.findViewById(R.id.add_course_fab_layout);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                mClick.callAddCourseFragment("add");
            }
        });






        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {


        FirebaseDatabase database = FirebaseDatabase.getInstance();



        final DatabaseReference players = database.getReference("courses");




        final ArrayList<Course_Entity> course_array = new ArrayList<>();



        ListView course_list_list_view = (ListView) getView().findViewById(R.id.course_list_view);


        final Course_list_array_adapter adapter = new Course_list_array_adapter(course_array,getContext());

        course_list_list_view.setAdapter(adapter);
        adapter.set_course_list_adapter_interface(new Course_list_array_adapter.Course_list_adapter_interface() {
            @Override
            public void callManageCourseFragment(String courseCode) {

                mClick.callManageCourseFragment_to_activity(courseCode);

            }
            @Override
            public void editCourseFragment(String mode, String courseCode){
                mClick.editCourseFragment_to_activity(mode,courseCode);
            }

            @Override
            public void callModulesActivity_to_fragment(String courseCode)
            {
                mClick.callModuleActivity_to_activity(courseCode);
            }
        });
        players.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               // if(dataSnapshot.hasChildren()) {
                    for (DataSnapshot items : dataSnapshot.getChildren()) {

                        //    Log.i("player", player.child(s + "/courseCode").getValue().toString());
                        try {

                            Log.i("player", items.child("courseName").getValue().toString());
                            String c_name = items.child("courseName").getValue().toString();
                            String c_id = items.child("courseCode").getValue().toString();
                            String c_p_email = items.child("professorEmailId").getValue().toString();
                            String c_p_first = items.child("professorFirstName").getValue().toString();
                            String c_p_last = items.child("professorLastName").getValue().toString();
                            //String c_p_full = items.child("professorFullName").getValue().toString();
                            String c_ta = items.child("taemailIds").getValue().toString();
                            String c_p_un = items.child("professorUserName").getValue().toString();

                            course_array.add(new Course_Entity(c_name, c_id, c_p_first, c_p_last, c_p_email, c_p_un, c_ta));
                        }
                        catch(Exception e)
                        {
                            Log.i("Exception",e.toString());
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            //}


            @Override
            public void onCancelled(DatabaseError e) {

            }
        });


        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //mClick = (OnActionButtonClickListener) context;
    }
}
