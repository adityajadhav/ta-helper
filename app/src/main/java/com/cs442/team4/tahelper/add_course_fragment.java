package com.cs442.team4.tahelper;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by ullas on 10/29/2016.
 */

public class add_course_fragment extends Fragment {

    OnFinishAddCourseInterface mFinish;

    public interface OnFinishAddCourseInterface
    {
        public void closeAddCourseFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_course, container, false);



    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("courses");




        final TextView course_name_tv = (TextView) getView().findViewById(R.id.course_name_tv_layout);
        final TextView course_id_tv = (TextView) getView().findViewById(R.id.course_id_tv_layout);
        final TextView professor_FN_tv = (TextView) getView().findViewById(R.id.professor_FN_tv_layout);
        final TextView professor_LN_tv = (TextView) getView().findViewById(R.id.professor_LN_tv_layout);
        final TextView professor_UN_tv = (TextView) getView().findViewById(R.id.professor_UN_tv_layout);
        final TextView professor_email_tv = (TextView) getView().findViewById(R.id.professor_email_tv_layout);
        final TextView ta_email_tv = (TextView) getView().findViewById(R.id.ta_email_tv_layout);



        Button add_course_btn = (Button) getView().findViewById(R.id.add_course_btn_layout);
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


                final Course_Entity ce = new Course_Entity(course_name,course_id,professor_FN,professor_LN,professor_email,professor_UN,ta_email);

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
