package com.cs442.team4.tahelper.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cs442.team4.tahelper.CourseActivity;
import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.student.Student_Entity;
import com.cs442.team4.tahelper.utils.ObjectUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neo on 05-11-2016.
 */

public class BcastNotificationFragment extends Fragment {

    private DatabaseReference mDatabase;
    private String TAG = "team4";
    private ArrayList<Student_Entity> studentList;
    private String emails = "ajadhav4@hawk.iit.edu";
    public final static String MODULE_NAME = "BcastNotification";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.broadcast_email, container, false);
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);


        String courseId = "";
        Bundle bundle = getArguments();
        if (bundle != null) {
            courseId = getArguments().getString(CourseActivity.COURCE_ID_KEY);
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //fetchStudents(courseId);

        final Spinner toEdtTxt = (Spinner) view.findViewById(R.id.toSpinner);
        List<String> list = new ArrayList<String>();
        list.add("All");
        //TODO fetch TA's email Ids
        list.add("abc_ta1@hawk.iit.edu");
        list.add("abc_ta2@hawk.iit.edu");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toEdtTxt.setAdapter(dataAdapter);
        toEdtTxt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Toast.makeText(parent.getContext(),
                        "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
                        Toast.LENGTH_SHORT).show();
                emails = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        //final EditText ccEdtTxt = (EditText) view.findViewById(R.id.ccEdtTxt);
        final EditText subjectEdtTxt = (EditText) view.findViewById(R.id.subjectEdtTxt);
        final EditText bodyEdtTxt = (EditText) view.findViewById(R.id.bodyEdtTxt);

        Button sendBtn = (Button) view.findViewById(R.id.sendBtn);

        sendBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                // TODO Get all students email ids
                Log.d("check", studentList.toString());
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"ajadhav4@hawk.iit.edu"});
                intent.putExtra(Intent.EXTRA_SUBJECT, subjectEdtTxt.getText());
                intent.putExtra(Intent.EXTRA_TEXT, bodyEdtTxt.getText());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, ""));

            }
        });
        return view;

    }

    private void fetchStudents(String courseId) {
        // TODO filter by  courseId
        DatabaseReference ref = mDatabase.child("students");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count ", "" + snapshot.getChildrenCount());
                if (ObjectUtils.isNotEmpty(snapshot.getChildren())) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Student_Entity studentEntity = postSnapshot.getValue(Student_Entity.class);
                        studentList.add(studentEntity);
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
