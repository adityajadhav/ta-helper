package com.cs442.team4.tahelper;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by ullas on 10/29/2016.
 */

public class add_course_fragment extends Fragment {

    OnFinishAddCourseInterface mFinish;
    String smode = null;
    String courseId = null;
    String fragmentHeading = "Add Course";
    String buttonLabel = "ADd Course";

    public interface OnFinishAddCourseInterface {
        public void closeAddCourseFragment();
    }

    public void callManageCourseFragment() {
        Log.i("Here", "here");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            String mode = getArguments().getString("mode");
            smode = mode;

            if (args.getString("course_code") != null) {
                courseId = args.getString("course_code");
            }

            Log.i("Mode is ", mode);
        }
        return inflater.inflate(R.layout.add_course, container, false);

    }


    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("courses");


        final TextView course_name_tv = (TextView) getView().findViewById(R.id.course_name_tv_layout);
        final TextView course_id_tv = (TextView) getView().findViewById(R.id.course_id_tv_layout);
        final TextView professor_FN_tv = (TextView) getView().findViewById(R.id.professor_FN_tv_layout);
        final TextView professor_LN_tv = (TextView) getView().findViewById(R.id.professor_LN_tv_layout);
        final TextView professor_UN_tv = (TextView) getView().findViewById(R.id.professor_UN_tv_layout);
        final TextView professor_email_tv = (TextView) getView().findViewById(R.id.professor_email_tv_layout);
        final TextView ta_email_tv = (TextView) getView().findViewById(R.id.ta_email_tv_layout);

        Button add_course_btn = (Button) getView().findViewById(R.id.add_course_btn_layout);

        if (smode.length() > 0) {
            if (smode.equals("edit")) {
                TextView add_course_heading_tv = (TextView) view.findViewById(R.id.add_course_heading_tv_layout);
                add_course_btn.setText("EDIT");
                add_course_heading_tv.setText("Edit Course");
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        DataSnapshot items = dataSnapshot.child("CS551");

                        //for (DataSnapshot items : values.getChildren()) {


                        try {

                            Log.i("player", items.child("courseName").getValue().toString());
                            String c_name = items.child("courseName").getValue().toString();
                            String c_id = items.child("courseCode").getValue().toString();
                            String c_p_email = items.child("professorEmailId").getValue().toString();
                            String c_p_first = items.child("professorFirstName").getValue().toString();
                            String c_p_last = items.child("professorLastName").getValue().toString();
                            String c_p_full = items.child("professorFullName").getValue().toString();
                            String c_ta = items.child("taemailIds").getValue().toString();
                            String c_p_un = items.child("professorUserName").getValue().toString();

                            course_name_tv.setText(c_name);
                            course_id_tv.setText(c_id);
                            professor_FN_tv.setText(c_p_first);
                            professor_LN_tv.setText(c_p_last);
                            professor_UN_tv.setText(c_p_un);
                            professor_email_tv.setText(c_p_email);
                            ta_email_tv.setText(c_p_un);





                        } catch (Exception e) {
                            Log.i("Exception", e.toString());
                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError e) {

                    }
                });
            }
        }



                add_course_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String course_name = course_name_tv.getText().toString();
                final String course_id = course_id_tv.getText().toString();
                final String professor_FN = professor_FN_tv.getText().toString();
                final String professor_LN = professor_LN_tv.getText().toString();
                final String professor_UN = professor_UN_tv.getText().toString();
                final String professor_email = professor_email_tv.getText().toString();
                final String ta_email = ta_email_tv.getText().toString();


                Course_Entity ce = new Course_Entity(course_name, course_id, professor_FN, professor_LN, professor_email, professor_UN, ta_email);

                myRef.child(course_id).setValue(ce);
                mFinish.closeAddCourseFragment();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFinish = (OnFinishAddCourseInterface) context;
    }
}
