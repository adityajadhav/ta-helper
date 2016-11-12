package com.cs442.team4.tahelper.student;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.contants.IntentConstants;
import com.cs442.team4.tahelper.model.AssignmentSplit;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

/**
 * Created by Mohammed on 11/9/2016.
 */

public class GradeWithSplitsFragment extends Fragment {

    String studentId, courseName, moduleName, moduleItem;
    Double finalScore;
    ArrayList<String> finalSplitValues;

    String moduleItemMaxScore;
    String moduleItemGainedScore;

    View myFragmentView;
    View itemView;
    private DatabaseReference mDatabase;

    ListView splitListView;

    TextView courseTextView;
    TextView studentIdTextView;
    TextView moduleItemTextView;
    TextView maxPointsTextView;

    EditText totalEditText;
    EditText splitEditText;

    Button calculateTotalButton;
    Button submitButton;

    public static ArrayList<Split> splitsArrayList;
    public static GradeWithSplitsAdapter gradeWithSplitsAdapter;

    public Double calculateTotalScore()
    {
        Double total = 0.0;

        HashMap<String, String> splitScoreHashmap = gradeWithSplitsAdapter.textValues;

        for(Map.Entry<String,String> s :splitScoreHashmap.entrySet())
        {
            String score = s.getValue();
            Double splitScore = Double.parseDouble(score);
            total = total + splitScore;
        }

        return total;

    }

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

        myFragmentView = inflater.inflate(R.layout.grade_with_splits_fragment, container, false);
        splitListView = (ListView) myFragmentView.findViewById(R.id.gradeWithListListView);

        courseTextView = (TextView) myFragmentView.findViewById(R.id.gradeWithSplitsCourseName);
        studentIdTextView = (TextView) myFragmentView.findViewById(R.id.gradeWithSplitsStudentId);
        moduleItemTextView = (TextView) myFragmentView.findViewById(R.id.gradeWithSplitsModuleItem);
        maxPointsTextView = (TextView) myFragmentView.findViewById(R.id.maxPointsTextView);

        totalEditText = (EditText) myFragmentView.findViewById(R.id.gradeWithSplitsTotalEditText);
        totalEditText.setFocusable(false);

        calculateTotalButton = (Button) myFragmentView.findViewById(R.id.gradeWithSplitsCalculateTotalButton);
        submitButton = (Button) myFragmentView.findViewById(R.id.gradeWithSplitsSubmitButton);

        calculateTotalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Double totalScore = calculateTotalScore();

                HashMap<String, String> splitScoreHashmap = gradeWithSplitsAdapter.textValues;

                boolean somethingWrong = false;
                int i=0;

                for(Map.Entry<String,String> s :splitScoreHashmap.entrySet())
                {
                    String score = s.getValue();
                    Double splitScore = Double.parseDouble(score);

                    if(splitScore > splitsArrayList.get(i).getSplitMaximumPoints())
                    {
                        somethingWrong = true;
                    }

                    if(somethingWrong)
                    {
                        break;
                    }

                    i++;

                }

                if(somethingWrong)
                {
                    Toast.makeText(getActivity(), "Please grade below maximum marks", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    totalEditText.setText(totalScore.toString());
                }

                //totalEditText.setText(totalScore.toString());

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Double totalScore = calculateTotalScore();

                HashMap<String, String> splitScoreHashmap = gradeWithSplitsAdapter.textValues;

                boolean somethingWrong = false;
                int i=0;

                for(Map.Entry<String,String> s :splitScoreHashmap.entrySet())
                {
                    String score = s.getValue();
                    Double splitScore = Double.parseDouble(score);

                    if(splitScore > splitsArrayList.get(i).getSplitMaximumPoints())
                    {
                        somethingWrong = true;
                    }

                    if(somethingWrong)
                    {
                        break;
                    }

                    i++;

                }

                if(somethingWrong)
                {
                    Toast.makeText(getActivity(), "Please grade below maximum marks", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    finalSplitValues = new ArrayList<String>();

                    for(Map.Entry<String,String> s :splitScoreHashmap.entrySet())
                    {
                        String score = s.getValue();
                        Double splitScore = Double.parseDouble(score);

                        finalSplitValues.add(score);

                        i++;
                    }

                    totalEditText.setText(totalScore.toString());
                    finalScore = totalScore;
                    updateGradesToDatabase();
                }

                //totalEditText.setText(totalScore.toString());

            }
        });

        courseTextView.setText(courseName);
        studentIdTextView.setText(studentId);
        moduleItemTextView.setText(moduleItem);

        int resID = R.layout.grade_split_item_layout;

        splitsArrayList = new ArrayList<Split>();

        gradeWithSplitsAdapter = new GradeWithSplitsAdapter(getContext(), resID, splitsArrayList);
        splitListView.setAdapter(gradeWithSplitsAdapter);

        loadModuleItemDetailsFromDatabase();
        loadFromDatabase();


        //totalEditText.setText(moduleItemGainedScore);
        //maxPointsTextView.setText("/ "+moduleItemMaxScore);

        return myFragmentView;
    }

    private void updateGradesToDatabase()
    {
        mDatabase.child("students").child(courseName).child(studentId).child(moduleName).child(moduleItem).child("Total").setValue(finalScore.toString());

        for (int i = 0; i < splitsArrayList.size(); i++) {
            Split split = splitsArrayList.get(i);
            mDatabase.child("students").child(courseName).child(studentId).child(moduleName).child(moduleItem).child("Splits").child(split.getSplitName()).setValue(finalSplitValues.get(i));
        }
    }

    private void loadModuleItemDetailsFromDatabase()
    {
        mDatabase.child("students").child(courseName).child(studentId).child(moduleName).push();

        mDatabase.child("students").child(courseName).child(studentId).child(moduleName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {
                    String score = (String) postSnapshot.child("Total").getValue();

                    moduleItemGainedScore = score;
                    totalEditText.setText(moduleItemGainedScore);

                    //Log.d("moduleItemGainedScore:"," "+moduleItemGainedScore);

                    //For fetching maximum points
                    mDatabase.child("modules").child(courseName).child(moduleName).push();
                    mDatabase.child("modules").child(courseName).child(moduleName).addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                            {
                                String score2 = (String) postSnapshot.child("Total").getValue();
                                moduleItemMaxScore = score2;
                                maxPointsTextView.setText("/ "+moduleItemMaxScore);
                                //Log.d("moduleItemMaxScore:"," "+moduleItemMaxScore);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d("ModuleListFragment : "," Read cancelled due to "+databaseError.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ModuleListFragment : "," Read cancelled due to "+databaseError.getMessage());
            }
        });
    }

    private void loadFromDatabase() {
        mDatabase.child("students").child(courseName).child(studentId).child(moduleName).child(moduleItem).child("Splits").push();

        mDatabase.child("students").child(courseName).child(studentId).child(moduleName).child(moduleItem).child("Splits").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                splitsArrayList.removeAll(splitsArrayList);
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {
                    if(!splitsArrayList.contains(postSnapshot.getKey()))
                    {
                        String splitName = (String) postSnapshot.getKey();
                        String gainedPoints = (String) postSnapshot.getValue();
                        //String gainedPoints = "0";

                        Split split = new Split(moduleName, splitName, 10.0, Double.parseDouble(gainedPoints));
                        splitsArrayList.add(split);

                        //For fetching maximum points
                        //mDatabase.child("modules").child(courseName).child(moduleName).child(moduleItem).child("Splits").push();
                        mDatabase.child("modules").child(courseName).child(moduleName).child(moduleItem).child("Splits").push();
                        mDatabase.child("modules").child(courseName).child(moduleName).child(moduleItem).child("Splits").addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                int i = 0;

                                for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                                {
                                    String score = "";
                                    score = (String) postSnapshot.getValue();

                                    splitsArrayList.get(i).setSplitMaximumPoints(Double.parseDouble(score));
                                    i++;
                                }

                                gradeWithSplitsAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.d("ModuleListFragment : "," Read cancelled due to "+databaseError.getMessage());
                            }
                        });

                    }
                }
                gradeWithSplitsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ModuleListFragment : "," Read cancelled due to "+databaseError.getMessage());
            }
        });

        //Log.d("checkForSplitsArray : "," for module:"+moduleItem+" : "+splitsArrayList);

    }

}
