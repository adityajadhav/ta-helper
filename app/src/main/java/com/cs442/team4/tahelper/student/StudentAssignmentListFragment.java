package com.cs442.team4.tahelper.student;

import android.app.Activity;
import android.app.ListFragment;
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

/**
 * Created by Mohammed on 11/3/2016.
 */

public class StudentAssignmentListFragment extends ListFragment {

    String studentId, courseName, moduleName;

    private DatabaseReference mDatabase;
    View myFragmentView;
    StudentAssignmentListFragment.OnAssignmentClickListener onAssignmentClickListener;

    private ArrayList<Split> checkForSplitsArray;

    TextView assignmentName;

    public static ArrayList<Assignment> assignmentsArraylist;
    public static StudentAssignmentListAdapter studentAssignmentListAdapter;

    public interface OnAssignmentClickListener {
        public void showAssignmentSplits(String courseName, String studentId, String moduleName, String moduleItem);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Later, will have to check if bundle is there or not and then only assign the value...
        final String idFromActivity = getArguments().getString(IntentConstants.STUDENT_ID);
        final String courseNameFromActivity = getArguments().getString(IntentConstants.COURSE_NAME);
        final String moduleNameFromActivity = getArguments().getString(IntentConstants.MODULE_NAME);
        studentId = idFromActivity;
        courseName = courseNameFromActivity;
        moduleName = moduleNameFromActivity;

        myFragmentView = inflater.inflate(R.layout.student_assignment_list_fragment, container, false);
        assignmentName = (TextView) myFragmentView.findViewById(R.id.moduleNameTextView);

        assignmentName.setText(moduleName + ": " + studentId);

        int resID = R.layout.assignment_score_item_layout;

        assignmentsArraylist = new ArrayList<Assignment>();

        studentAssignmentListAdapter = new StudentAssignmentListAdapter(getContext(), resID, assignmentsArraylist);
        setListAdapter(studentAssignmentListAdapter);

        loadFromDatabase();

        return myFragmentView;
    }

    private void loadFromDatabase() {
        mDatabase.child("students").child(studentId).child(courseName).child(moduleName).push();

        mDatabase.child("students").child(studentId).child(courseName).child(moduleName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                assignmentsArraylist.removeAll(assignmentsArraylist);
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {
                    if(!assignmentsArraylist.contains(postSnapshot.getKey()))
                    {
                        if(moduleName.equals("Assignments"))
                        {
                            Long score = (Long) postSnapshot.child("score").getValue();

                            Assignment assignment = new Assignment();
                            assignment.setName((String)postSnapshot.getKey());
                            assignment.setGainedPoints(score);

                            //This value we will have to fetch from other root database from firebase
                            //Or include in the 'students' root database
                            assignment.setMaximumPoints(10L);
                            assignmentsArraylist.add(assignment);
                        }
                        if(moduleName.equals("Exams"))
                        {
                            Assignment assignment = new Assignment();
                            assignment.setName((String)postSnapshot.getKey());
                            assignment.setGainedPoints((Long)postSnapshot.getValue());

                            //This value we will have to fetch from other root database from firebase
                            //Or include in the 'students' root database
                            assignment.setMaximumPoints(10L);

                            assignmentsArraylist.add(assignment);
                        }

                        if(moduleName.equals("Final Grades"))
                        {
                            Assignment assignment = new Assignment();

                            if(postSnapshot.getKey().equals("grade"))
                            {
                                assignment.setName((String)postSnapshot.getKey());
                                assignment.setGrade((String)postSnapshot.getValue());
                            }
                            else
                            {
                                assignment.setName((String)postSnapshot.getKey());
                                assignment.setGainedPoints((Long)postSnapshot.getValue());
                            }

                            //This value we will have to fetch from other root database from firebase
                            //Or include in the 'students' root database
                            assignment.setMaximumPoints(10L);

                            assignmentsArraylist.add(assignment);
                        }

                        if(moduleName.equals("In-Class Assignments"))
                        {
                            Long score = (Long) postSnapshot.child("score").getValue();

                            Assignment assignment = new Assignment();
                            assignment.setName((String)postSnapshot.getKey());
                            assignment.setGainedPoints(score);

                            //This value we will have to fetch from other root database from firebase
                            //Or include in the 'students' root database
                            assignment.setMaximumPoints(10L);
                            assignmentsArraylist.add(assignment);
                        }

                        if(moduleName.equals("Project"))
                        {
                            Long score = (Long) postSnapshot.child("score").getValue();

                            Assignment assignment = new Assignment();
                            assignment.setName((String)postSnapshot.getKey());
                            assignment.setGainedPoints(score);

                            //This value we will have to fetch from other root database from firebase
                            //Or include in the 'students' root database
                            assignment.setMaximumPoints(10L);
                            assignmentsArraylist.add(assignment);
                        }

                    }
                }
                studentAssignmentListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ModuleListFragment : "," Read cancelled due to "+databaseError.getMessage());
            }
        });


    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        Assignment assignment =  (Assignment) getListView().getItemAtPosition(position);
        String moduleItem = assignment.getName();

        if(checkForSplits(moduleItem)==true)
        {
            onAssignmentClickListener.showAssignmentSplits(courseName, studentId, moduleName, moduleItem);
        }
        else
        {
            Toast.makeText(getActivity(), "No Splits for "+moduleItem, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            onAssignmentClickListener = (StudentAssignmentListFragment.OnAssignmentClickListener) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement OnStudentClickListener");
        }
    }

    private boolean checkForSplits(String moduleItem)
    {
        checkForSplitsArray = new ArrayList<Split>();
        boolean hasSplits = true;

        mDatabase.child("students").child(studentId).child(courseName).child(moduleName).child(moduleItem).child("splits").push();

        mDatabase.child("students").child(studentId).child(courseName).child(moduleName).child(moduleItem).child("splits").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                checkForSplitsArray.removeAll(checkForSplitsArray);
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {
                    if(!checkForSplitsArray.contains(postSnapshot.getKey()))
                    {
                        String splitName = (String) postSnapshot.getKey();
                        Long gainedPoints = (Long) postSnapshot.getValue();

                        Split split = new Split(moduleName, splitName, 10L, gainedPoints);
                        checkForSplitsArray.add(split);

                        Log.d("checkForSplitsArray : "," "+checkForSplitsArray);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ModuleListFragment : "," Read cancelled due to "+databaseError.getMessage());
            }
        });

        if(checkForSplitsArray.isEmpty())
        {
            hasSplits=true;
        }

        Log.d("checkForSplitsArray : "," for module:"+moduleItem+" : "+checkForSplitsArray);
        return  hasSplits;
    }


}
