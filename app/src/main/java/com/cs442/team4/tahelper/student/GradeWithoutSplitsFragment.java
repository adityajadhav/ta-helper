package com.cs442.team4.tahelper.student;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.contants.IntentConstants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mohammed on 11/9/2016.
 */

public class GradeWithoutSplitsFragment extends Fragment {

    String studentId, courseName, moduleName, moduleItem;
    Double finalScore;

    String moduleItemMaxScore;
    String moduleItemGainedScore;

    View myFragmentView;
    private DatabaseReference mDatabase;

    //TextView courseTextView;
    TextView studentIdTextView;
    //TextView moduleItemTextView;
    TextView maxPointsTextView;
    EditText totalEditText;

    Button submitButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Later, will have to check if bundle is there or not and then only assign the value...
        final String idFromActivity = getArguments().getString(IntentConstants.STUDENT_ID);
        final String courseNameFromActivity = getArguments().getString(IntentConstants.COURSE_NAME);
        final String moduleNameFromActivity = getArguments().getString(IntentConstants.MODULE_NAME);
        final String moduleItemFromActivity = getArguments().getString(IntentConstants.MODULE_ITEM);
        studentId = idFromActivity;
        courseName = courseNameFromActivity;
        moduleName = moduleNameFromActivity;
        moduleItem = moduleItemFromActivity;

        myFragmentView = inflater.inflate(R.layout.grade_without_splits_fragment, container, false);

        //courseTextView = (TextView) myFragmentView.findViewById(R.id.gradeWithoutSplitsCourseName);
        studentIdTextView = (TextView) myFragmentView.findViewById(R.id.gradeWithoutSplitsStudentId);
        //moduleItemTextView = (TextView) myFragmentView.findViewById(R.id.gradeWithoutSplitsModuleItem);
        maxPointsTextView = (TextView) myFragmentView.findViewById(R.id.maxPointsTextView2);

        totalEditText = (EditText) myFragmentView.findViewById(R.id.gradeWithoutSplitsTotalEditText);

        submitButton = (Button) myFragmentView.findViewById(R.id.gradeWithoutSplitsSubmitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finalScore = Double.parseDouble(totalEditText.getText().toString());

                if(finalScore > Double.parseDouble(moduleItemMaxScore))
                {
                    Toast.makeText(getActivity(), "Please grade below maximum marks", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Toast.makeText(getActivity(),moduleItem+ " marks updated for "+studentId+" for course "+courseName, Toast.LENGTH_SHORT).show();
                    updateGradesToDatabase();
                    Toast.makeText(getActivity(),moduleItem+ " marks updated for "+studentId+" for course "+courseName, Toast.LENGTH_SHORT).show();
                }

            }
        });

        //courseTextView.setText(courseName);
        studentIdTextView.setText(studentId);
        //moduleItemTextView.setText(moduleItem);

        int resID = R.layout.grade_split_item_layout;

        loadModuleItemDetailsFromDatabase();

        return myFragmentView;
    }

    private void updateGradesToDatabase()
    {
        mDatabase.child("modules").child(courseName).child(moduleName).child("isGraded").setValue(true);
        mDatabase.child("modules").child(courseName).child(moduleName).child(moduleItem).child("isGraded").setValue(true);
        mDatabase.child("students").child(courseName).child(studentId).child(moduleName).child(moduleItem).child("Total").setValue(finalScore.toString());
    }

    private void loadModuleItemDetailsFromDatabase()
    {
        mDatabase.child("students").child(courseName).child(studentId).child(moduleName).push();

        mDatabase.child("students").child(courseName).child(studentId).child(moduleName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {
                    String key = (String) postSnapshot.getKey();

                    if(key.equals(moduleItem))
                    {
                        String score = (String) postSnapshot.child("Total").getValue();

                        moduleItemGainedScore = score;
                        totalEditText.setText(moduleItemGainedScore);

                        //For fetching maximum points
                        mDatabase.child("modules").child(courseName).child(moduleName).push();
                        mDatabase.child("modules").child(courseName).child(moduleName).addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                                {
                                    String key = (String) postSnapshot.getKey();

                                    if(key.equals(moduleItem))
                                    {
                                        String score2 = (String) postSnapshot.child("Total").getValue();
                                        moduleItemMaxScore = score2;
                                        maxPointsTextView.setText("/ "+moduleItemMaxScore);
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.d("ModuleListFragment : "," Read cancelled due to "+databaseError.getMessage());
                            }
                        });
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ModuleListFragment : "," Read cancelled due to "+databaseError.getMessage());
            }
        });
    }

}
