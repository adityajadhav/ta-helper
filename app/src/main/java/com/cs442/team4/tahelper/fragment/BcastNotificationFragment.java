package com.cs442.team4.tahelper.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.cs442.team4.tahelper.CourseActivity;
import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.student.Student_Entity;
import com.cs442.team4.tahelper.utils.ObjectUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neo on 05-11-2016.
 */

public class BcastNotificationFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private DatabaseReference mDatabase;
    private String TAG = "team4";
    ArrayList<String> taList;
    private ArrayList<Student_Entity> studentList;
    private ArrayList<String> emails;
    public final static String MODULE_NAME = "BcastNotification";
    private String courseId = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.broadcast_email, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            courseId = getArguments().getString(CourseActivity.COURCE_ID_KEY);
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
        taList = new ArrayList<>();
        final Spinner toEdtTxt = (Spinner) view.findViewById(R.id.toSpinner);
        List<String> list = new ArrayList<String>();
        list.add("All");
        list.add("TA's");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toEdtTxt.setAdapter(dataAdapter);

        emails = new ArrayList<>();
        toEdtTxt.setOnItemSelectedListener(this);

        final EditText subjectEdtTxt = (EditText) view.findViewById(R.id.subjectEdtTxt);
        final EditText bodyEdtTxt = (EditText) view.findViewById(R.id.bodyEdtTxt);
        Button sendBtn = (Button) view.findViewById(R.id.sendBtn);

        fetchStudents(courseId);
        fetchTAs(courseId);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(android.content.Intent.EXTRA_EMAIL, getSimpleList());
                intent.putExtra(Intent.EXTRA_SUBJECT, subjectEdtTxt.getText());
                intent.putExtra(Intent.EXTRA_TEXT, bodyEdtTxt.getText());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, "Send Email"));
                //getFragmentManager().popBackStack();
            }
        });
        return view;

    }

    @NonNull
    private String[] getSimpleList() {
        String sendTo[] = new String[emails.size()];
        if (ObjectUtils.isNotEmpty(emails)) {
            for (int i = 0; i < emails.size(); i++) {
                if (ObjectUtils.isNotEmpty(emails.get(i)))
                    sendTo[i] = emails.get(i);

            }
        }
        return sendTo;
    }

    private void fetchStudents(String courseId) {
        if (ObjectUtils.isNotEmpty(courseId)) {
            studentList = new ArrayList<>();
            DatabaseReference ref = mDatabase.child("students").child(courseId);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Log.e("Count ", "" + snapshot.getChildrenCount());
                    if (ObjectUtils.isNotEmpty(snapshot.getChildren())) {
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            Student_Entity studentEntity = postSnapshot.getValue(Student_Entity.class);
                            studentList.add(studentEntity);
                        }
                        if (ObjectUtils.isNotEmpty(studentList)) {
                            for (Student_Entity student : studentList) {
                                if (ObjectUtils.isNotEmpty(student.getStudentEmail()))
                                    emails.add(student.getStudentEmail());
                            }
                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError firebaseError) {
                    Log.e("The read failed: ", firebaseError.getMessage());
                }
            });

        }
    }


    private void fetchTAs(String courseId) {
        if (ObjectUtils.isNotEmpty(courseId)) {
            studentList = new ArrayList<>();
            DatabaseReference ref = mDatabase.child("courses").child(courseId);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {
                    };
                    taList = snapshot.child("ta_members").getValue(t);
                }

                @Override
                public void onCancelled(DatabaseError firebaseError) {
                    Log.e("The read failed: ", firebaseError.getMessage());
                }
            });
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String type = parent.getItemAtPosition(position).toString();
        if ("All".equals(type)) {
            emails.clear();
            if (ObjectUtils.isNotEmpty(studentList)) {
                for (Student_Entity student : studentList) {
                    if (ObjectUtils.isNotEmpty(student.getStudentEmail()))
                        emails.add(student.getStudentEmail());
                }
            }
            if (ObjectUtils.isNotEmpty(taList)) {
                for (String taEmailId : taList) {
                    if (ObjectUtils.isNotEmpty(taEmailId))
                        emails.add(taEmailId);
                }
            }

        } else {
            emails.clear();
            if (ObjectUtils.isNotEmpty(taList)) {
                for (String taEmailId : taList) {
                    if (ObjectUtils.isNotEmpty(taEmailId))
                        emails.add(taEmailId);
                }
            }

        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
