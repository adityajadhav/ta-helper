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
            public void callAddCourseFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        return inflater.inflate(R.layout.courses_list_fragment, container, false);


    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


        FloatingActionButton myFab = (FloatingActionButton)  view.findViewById(R.id.add_course_fab_layout);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                mClick.callAddCourseFragment();
            }
        });




        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference players = database.getReference("courses");

        final Course_Entity ce = new Course_Entity("MAD","CS442","Zhang","Simon","email","zsimon","ta@ta");


        final ArrayList<Course_Entity> course_array = new ArrayList<>();



        ListView course_list_list_view = (ListView) getView().findViewById(R.id.course_list_view);


        final Course_list_array_adapter adapter = new Course_list_array_adapter(course_array,getContext());
        course_list_list_view.setAdapter(adapter);

        players.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot items : dataSnapshot.getChildren()) {

                    //    Log.i("player", player.child(s + "/courseCode").getValue().toString());
                    Log.i("player", items.child("courseName").getValue().toString());
                    String s = items.child("courseName").getValue().toString();
                    course_array.add(new Course_Entity(s,"n","n","N","n","n","n"));
                }
                adapter.notifyDataSetChanged();
            }


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

        mClick = (OnActionButtonClickListener) context;
    }
}
