package com.cs442.team4.tahelper;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ullas on 10/29/2016.
 */

public class add_course_fragment extends Fragment {

    OnFinishAddCourseInterface mFinish;
    String smode = null;
    String courseId = null;
    String fragmentHeading = "Add Course";
    String buttonLabel = "ADd Course";
    String old_course_id = null;
    ArrayList<String> ta_memebers = new ArrayList<>();
    int flag =0 ;
    String user;
    int exists_flag =0;
    public interface OnFinishAddCourseInterface {
        public void closeAddCourseFragment();
        public void callAddTAs_to_activity(ArrayList<String> ta_members);
    }

    public void callManageCourseFragment() {
        Log.i("Here", "here");
    }

    View global_view;
    ArrayAdapter<String> adapter;
    public void setTAMembers(ArrayList<String> getMembers)
    {
        final ListView ta_display__lv = (ListView) global_view.findViewById(R.id.ta_display_lv_layout);
        ta_memebers = getMembers;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.ta_display_list_view,R.id.ta_email_tv_layout,ta_memebers);
        ta_display__lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        Log.i("got",ta_memebers.toString());
        final TextView course_name_tv = (TextView) global_view.findViewById(R.id.course_name_tv_layout);

        course_name_tv.setText(getMembers.get(0));
    }



    public boolean checkValues()
    {
        boolean result = true;
        final TextView course_name_tv = (TextView) getView().findViewById(R.id.course_name_tv_layout);
        final TextView course_id_tv = (TextView) getView().findViewById(R.id.course_id_tv_layout);
        final TextView professor_FN_tv = (TextView) getView().findViewById(R.id.professor_FN_tv_layout);
        final TextView professor_LN_tv = (TextView) getView().findViewById(R.id.professor_LN_tv_layout);
        final TextView professor_UN_tv = (TextView) getView().findViewById(R.id.professor_UN_tv_layout);
        final TextView professor_email_tv = (TextView) getView().findViewById(R.id.professor_email_tv_layout);



        final String course_name = course_name_tv.getText().toString();
        if(course_name.trim().length() <=0)
        {
            course_name_tv.setError("Course Name cannot be empty");
            result = false;
        }
        final String course_id = course_id_tv.getText().toString();
        if(course_id.trim().length() <=0)
        {
            course_id_tv.setError("Course Id cannot be empty");
            result = false;
        }

        final String professor_FN = professor_FN_tv.getText().toString();

        if(professor_FN.trim().length() <=0)
        {
            professor_FN_tv.setError("First Name cannot be empty");
            result = false;
        }
        final String professor_LN = professor_LN_tv.getText().toString();

        if(professor_LN.trim().length() <=0)
        {
            professor_LN_tv.setError("Last Name cannot be empty");
            result = false;
        }
        final String professor_UN = professor_UN_tv.getText().toString();

        if(professor_UN.trim().length() <=0)
        {
            professor_UN_tv.setError("User Name cannot be empty");
            result = false;
        }
        final String professor_email = professor_email_tv.getText().toString();

        if(professor_email.trim().length() <=0)
        {
            professor_email_tv.setError("Email cannot be empty");
            result = false;
        }

        return result;

    }


    @Override
    public void onResume() {
        super.onResume();

        final ListView ta_display__lv = (ListView) global_view.findViewById(R.id.ta_display_lv_layout);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.ta_display_list_view,R.id.ta_email_tv_layout,ta_memebers);
        ta_display__lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {





        Bundle args = getArguments();
        if (args != null) {
            String mode = getArguments().getString("mode");
            smode = mode;

            try {
                courseId = args.getString("course_code");
            }
            catch(Exception e)
            {
                Log.i("Exception",e.toString());
            }


            Log.i("Mode is ", mode);
        }
        return inflater.inflate(R.layout.add_course, container, false);

    }



    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {

        SharedPreferences pref = getContext().getSharedPreferences("CurrentUser", MODE_PRIVATE);
        user = pref.getString("UserEntity","");
        Log.i("Username",user);


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("courses");
        global_view = view;

        final TextView course_name_tv = (TextView) getView().findViewById(R.id.course_name_tv_layout);
        final TextView course_id_tv = (TextView) getView().findViewById(R.id.course_id_tv_layout);
        final TextView professor_FN_tv = (TextView) getView().findViewById(R.id.professor_FN_tv_layout);
        final TextView professor_LN_tv = (TextView) getView().findViewById(R.id.professor_LN_tv_layout);
        final TextView professor_UN_tv = (TextView) getView().findViewById(R.id.professor_UN_tv_layout);
        final TextView professor_email_tv = (TextView) getView().findViewById(R.id.professor_email_tv_layout);
        //  final TextView ta_email_tv = (TextView) getView().findViewById(R.id.ta_email_tv_layout);
        final ListView ta_display__lv = (ListView) getView().findViewById(R.id.ta_display_lv_layout);


        Button add_course_btn = (Button) getView().findViewById(R.id.add_course_btn_layout);
        Button add_ta_btn = (Button) getView().findViewById(R.id.add_ta_btn_layout);





        if (smode.length() > 0) {
            if (smode.equals("edit")) {
                TextView add_course_heading_tv = (TextView) view.findViewById(R.id.add_course_heading_tv_layout);
                add_course_btn.setText("DONE");
                add_course_heading_tv.setText("Edit Course");
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        DataSnapshot items = dataSnapshot.child(courseId);

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
                            old_course_id =  c_id;
                            professor_FN_tv.setText(c_p_first);
                            professor_LN_tv.setText(c_p_last);
                            professor_UN_tv.setText(c_p_un);
                            professor_email_tv.setText(c_p_email);
                            //  ta_email_tv.setText(c_p_un);

                            GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};

                            if(flag == 0) {
                                ta_memebers = items.child("ta_members").getValue(t);

                                if (ta_memebers.size() > 0) {
                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.ta_display_list_view, R.id.ta_email_tv_layout, ta_memebers);
                                    ta_display__lv.setAdapter(adapter);

                                }
                                flag =1;
                            }





                        } catch (Exception e) {
                            Log.i("Exception", e.toString());
                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError e) {

                    }
                });
            }
            else
            {
                if(!ta_memebers.contains(user) && flag ==0)
                {
                    ta_memebers.add(user);

                }
            }
        }

        add_ta_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFinish.callAddTAs_to_activity(ta_memebers);

            }
        });


        add_course_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean check = checkValues();

                if(!check)
                {
                    Toast.makeText(getContext(),"Please fill all the fields",Toast.LENGTH_SHORT).show();
                    return;
                }

                final String course_name = course_name_tv.getText().toString();
                final String course_id = course_id_tv.getText().toString();
                final String professor_FN = professor_FN_tv.getText().toString();
                final String professor_LN = professor_LN_tv.getText().toString();
                final  String professor_UN = professor_UN_tv.getText().toString();
                final String professor_email = professor_email_tv.getText().toString();
                // final String ta_email = ta_email_tv.getText().toString();
                if(smode.equals("edit"))
                {
                    myRef.child(old_course_id).removeValue();
                }

                else
                {

                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        if (snapshot.hasChild(course_id)) {

                            Toast.makeText(getContext(),"Course already exists",Toast.LENGTH_SHORT).show();
                            course_id_tv.setError("Course ID already exists");
                        }
                        else
                        {
                            exists_flag = 1;
                            Course_Entity ce = new Course_Entity(course_name, course_id, professor_FN, professor_LN, professor_email, professor_UN, "","false");

                            final ProgressDialog dialog = ProgressDialog.show(getContext(), "",
                                    "Loading. Please wait...", true);
                            myRef.child(course_id).setValue(ce);
                            exists_flag = 0;


                            if (ta_memebers.size() > 0) {
                                myRef.child(course_id).child("ta_members").setValue(ta_memebers);

                            } else {
                                Toast.makeText(getContext(), "Add TA members by clicking on Add TAs button", Toast.LENGTH_SHORT).show();
                            }

                            myRef.child(course_id).child("imported").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    Log.i("Comp","Now Completed");
                                    dialog.dismiss();
                                    Toast.makeText(getContext(),"Course Added successfully", Toast.LENGTH_SHORT).show();
                                    mFinish.closeAddCourseFragment();
                                }
                            });
                            Log.i("Comp2","Now Completed");
                            //mFinish.closeAddCourseFragment();
                        }
                    }
                        @Override
                        public void onCancelled(DatabaseError e) {

                        }
                });

//                    myRef.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//
////                            for (DataSnapshot items : dataSnapshot.getChildren()) {
////
////                                if(items.getKey().equals(course_id)) {
////                                    exists_flag = 1;
////
////                                    break;
////                                }
////
////                            }
////                            if(exists_flag == 1)
////
////                            {
////                                Toast.makeText(getContext(), "Course id " + course_id + " already exists", Toast.LENGTH_SHORT).show();
////                            }
//
//
////                            if(dataSnapshot.hasChild(courseId))
////                             {
////                                 Toast.makeText(getContext(),"Course id " + course_id + " exists",Toast.LENGTH_SHORT).show();
////                                 exists_flag = 1;
////                             }
//
//
//
//
//                            try {
//
//                            } catch (Exception e) {
//                                Log.i("Exception", e.toString());
//                            }
//                        }
//
//
//                        @Override
//                        public void onCancelled(DatabaseError e) {
//
//                        }
//                    });
                }



           //    if(exists_flag == 1)
            //   {
//                Course_Entity ce = new Course_Entity(course_name, course_id, professor_FN, professor_LN, professor_email, professor_UN, "","false");
//
//                final ProgressDialog dialog = ProgressDialog.show(getContext(), "",
//                        "Loading. Please wait...", true);
//                myRef.child(course_id).setValue(ce);
//                exists_flag = 0;
//
//
//                if (ta_memebers.size() > 0) {
//                    myRef.child(course_id).child("ta_members").setValue(ta_memebers);
//
//                } else {
//                    Toast.makeText(getContext(), "Add TA members by clicking on Add TAs button", Toast.LENGTH_SHORT).show();
//                }
//
//                myRef.child(course_id).child("imported").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//
//                        Log.i("Comp","Now Completed");
//                        dialog.dismiss();
//                        Toast.makeText(getContext(),"Course Added successfully", Toast.LENGTH_SHORT).show();
//                        mFinish.closeAddCourseFragment();
//                    }
//                });
//                Log.i("Comp2","Now Completed");
//                //mFinish.closeAddCourseFragment();
               //  }
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
