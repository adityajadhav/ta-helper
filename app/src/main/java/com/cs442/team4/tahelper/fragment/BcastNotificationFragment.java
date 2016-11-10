package com.cs442.team4.tahelper.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.student.Student_Entity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by neo on 05-11-2016.
 */

public class BcastNotificationFragment extends Fragment {

    private DatabaseReference mDatabase;
    private String TAG = "team4";
    private ArrayList<Student_Entity> studentList;

    public final static String MODULE_NAME = "BcastNotification";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.broadcast_email, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();


        final EditText toEdtTxt = (EditText) view.findViewById(R.id.toEdtTxt);
        final EditText ccEdtTxt = (EditText) view.findViewById(R.id.ccEdtTxt);
        final EditText subjectEdtTxt = (EditText) view.findViewById(R.id.subjectEdtTxt);
        final EditText bodyEdtTxt = (EditText) view.findViewById(R.id.bodyEdtTxt);

        Button sendBtn = (Button) view.findViewById(R.id.sendBtn);

        sendBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                // TODO Get all students email ids

                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"ajadhav4@hawk.iit.edu"});
                intent.putExtra(Intent.EXTRA_SUBJECT, subjectEdtTxt.getText());
                intent.putExtra(Intent.EXTRA_TEXT, bodyEdtTxt.getText());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, ""));

            }
        });
        return view;

    }




    private void fetchStudents(String courceId) {
        // TODO filter by  courceId
        DatabaseReference ref = mDatabase.child("students");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                studentList = new ArrayList<Student_Entity>();
                Log.e("Count ", "" + snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Student_Entity studentEntity = postSnapshot.getValue(Student_Entity.class);
                    studentList.add(studentEntity);
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: ", firebaseError.getMessage());
            }
        });


    }

}
