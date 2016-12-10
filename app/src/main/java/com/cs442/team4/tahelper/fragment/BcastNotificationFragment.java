package com.cs442.team4.tahelper.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
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

import static com.cs442.team4.tahelper.R.id.bodyEdtTxt;
import static com.cs442.team4.tahelper.R.id.subjectEdtTxt;

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
    EditText subjectEdt;
    EditText bodyEdt;
    ProgressDialog dialog;
    Spinner toEdtTxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.broadcast_email, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            courseId = getArguments().getString(CourseActivity.COURCE_ID_KEY);
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
        taList = new ArrayList<>();
        toEdtTxt = (Spinner) view.findViewById(R.id.toSpinner);
        List<String> list = new ArrayList<String>();
        list.add("All");
        list.add("TA's");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toEdtTxt.setAdapter(dataAdapter);

        emails = new ArrayList<>();
        toEdtTxt.setOnItemSelectedListener(this);

        subjectEdt = (EditText) view.findViewById(subjectEdtTxt);
        bodyEdt = (EditText) view.findViewById(bodyEdtTxt);
        Button sendBtn = (Button) view.findViewById(R.id.sendBtn);


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fetchStudents(courseId);


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

    private void fetchStudents(final String courseId) {

        String selectedItem = toEdtTxt.getSelectedItem().toString();

        dialog = ProgressDialog.show(getContext(), "",
                "Fetching " + selectedItem + " email recipients. Please wait...", true);

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
                    fetchTAs(courseId);

                }

                @Override
                public void onCancelled(DatabaseError firebaseError) {
                    Log.e("The read failed: ", firebaseError.getMessage());
                    dialog.dismiss();
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
                    dialog.dismiss();
                    openEmailClient();

                }

                @Override
                public void onCancelled(DatabaseError firebaseError) {
                    Log.e("The read failed: ", firebaseError.getMessage());
                    dialog.dismiss();
                }
            });
        }
    }

    private void openEmailClient() {
        String type = toEdtTxt.getSelectedItem().toString();
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


        toEdtTxt.setSelection(toEdtTxt.getSelectedItemPosition());
        StringBuilder body = new StringBuilder(bodyEdt.getText().toString());
        body.append("\n\n\n --Sent From TA Helper--");
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, getSimpleList());
        intent.putExtra(Intent.EXTRA_TEXT, body.toString()/*bodyEdtTxt.getText()*/);
        intent.putExtra(Intent.EXTRA_SUBJECT, subjectEdt.getText().toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, "Send Email"));
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //String type = parent.getItemAtPosition(position).toString();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
