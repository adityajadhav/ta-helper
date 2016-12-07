package com.cs442.team4.tahelper.student;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
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

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Mohammed on 11/2/2016.
 */

public class StudentModulesFragment extends ListFragment {

    String studentId;
    String courseName;

    double currentTotalPointsGained = 0.0;
    double currentTotalPointsPossible = 0.0;
    TextView totalPercentageTextView;
    TextView totalGradeTextView;

    double finalTotalPointsGained = 0.0;
    double finalTotalPointsPossible = 0.0;
    TextView totalScoreTextView;

    double finalPossibleWeightage = 0.0;
    double finalGainedWeightage = 0.0;


    double totalPoints = 0.0;

    private DatabaseReference mDatabase;
    View myFragmentView;
    TextView modulesTextView;
    //StudentModulesFragment.OnModuleClickListener moduleClickListener;

    public static ArrayList<TotalGrade> modulesArraylist;
    //public static ArrayList<String> modulesArraylist;
    public static StudentModulesListAdapter modulesListAdapter;

    /*
    public interface OnModuleClickListener{
        public void showSelectedModuleContent(String courseName, String studentId, String moduleName);
    }
    */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        finalTotalPointsPossible = 0.0;
        finalTotalPointsGained = 0.0;

        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Later, will have to check if bundle is there or not and then only assign the value...
        final String idFromActivity = getArguments().getString(IntentConstants.STUDENT_ID);
        final String courseNameFromActivity = getArguments().getString(IntentConstants.COURSE_NAME);
        studentId = idFromActivity;
        courseName = courseNameFromActivity;

        myFragmentView = inflater.inflate(R.layout.student_modules_fragment, container, false);
        modulesTextView = (TextView) myFragmentView.findViewById(R.id.textView11);
        totalScoreTextView = (TextView) myFragmentView.findViewById(R.id.totalScoreTextView);
        totalPercentageTextView = (TextView) myFragmentView.findViewById(R.id.totalPercentageTextView);
        totalGradeTextView = (TextView) myFragmentView.findViewById(R.id.totalGradeTextView);

        modulesTextView.setText(courseName+" | "+studentId);

        int resID = R.layout.student_list_textview;

        modulesArraylist = new ArrayList<TotalGrade>();

        modulesListAdapter = new StudentModulesListAdapter(getContext(), resID, modulesArraylist);
        setListAdapter(modulesListAdapter);

        loadFromDatabase();

        //getTotalGainedScoresForEachModule();

        //getTotalPossibleScoresForEachModule();

        //getTotalMarksFromAllModules();

        return myFragmentView;
    }

    private void loadFromDatabase() {
        mDatabase.child("students").child(courseName).child(studentId).push();

        mDatabase.child("students").child(courseName).child(studentId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                modulesArraylist.removeAll(modulesArraylist);
                //Log.i("","Snaphot "+dataSnapshot+"  "+dataSnapshot.getChildren()+"  "+dataSnapshot.getValue());
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {
                    if(!modulesArraylist.contains(postSnapshot.getKey()))
                    {
                        String key = (String)postSnapshot.getKey();
                        final Long count = postSnapshot.getChildrenCount();

                        Log.d("Key : "," Name: "+key);
                        Log.d("Count : "," Name: "+count);

                        final TotalGrade module = new TotalGrade();
                        module.setModuleName((String)postSnapshot.getKey());
                        final String moduleName = module.getModuleName();

                        //final double currentTotalPointsGained = 0.0;
                        //final double currentTotalPointsPossible = 0.0;

                        //***********************************************************************************

                        mDatabase.child("students").child(courseName).child(studentId).child(moduleName).push();
                        mDatabase.child("students").child(courseName).child(studentId).child(moduleName).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                double total = 0;
                                for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                                {
                                    if(!postSnapshot.getKey().equals("weightage"))
                                    {
                                        String score = (String) postSnapshot.child("Total").getValue();
                                        Double score1 = Double.parseDouble(score);
                                        total = total + score1;
                                        currentTotalPointsGained = currentTotalPointsGained+total;
                                    }
                                }
                                //currentTotalPointsGained = total;
                                finalTotalPointsGained = finalTotalPointsGained+total;
                                Log.d("Total Gained : "," finalTotalPointsGained: "+finalTotalPointsGained);
                                module.setTotalGainedMarks(total);

                                //For fetching maximum points
                                //mDatabase.child("modules").child(courseName).child(moduleName).push();
                                mDatabase.child("modules").child(courseName).child(moduleName).push();
                                mDatabase.child("modules").child(courseName).child(moduleName).addValueEventListener(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        int i = 0;
                                        double total1 = 0;
                                        double currentPossibleWeightage = 0.0;

                                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                                        {
                                            if(!postSnapshot.getKey().equals("weightage"))
                                            {
                                                String score = (String) postSnapshot.child("Total").getValue();
                                                Double score1 = Double.parseDouble(score);
                                                total1 = total1 + score1;
                                                currentTotalPointsPossible=total1;
                                                //i++;
                                            }
                                            else if(postSnapshot.getKey().equals("weightage"))
                                            {
                                                String weightage1 = (String) postSnapshot.getValue();
                                                currentPossibleWeightage = Double.parseDouble(weightage1);
                                                Log.d(""," currentPossibleWeightage Inside elseif: "+currentPossibleWeightage);

                                            }
                                        }

                                        finalTotalPointsPossible = finalTotalPointsPossible+total1;
                                        totalScoreTextView.setText(finalTotalPointsGained+"/"+finalTotalPointsPossible+"  ");
                                        Log.d(": "," finalTotalPointsPossible: "+finalTotalPointsPossible);
                                        module.setTotalPossibleMarks(total1);

                                        //****************************************************************

                                        /*
                                        finalPossibleWeightage = finalPossibleWeightage + currentPossibleWeightage;
                                        Log.d(""," finalPossibleWeightage: "+finalPossibleWeightage);

                                        double currentGainedWeightage = 0.0;

                                        currentGainedWeightage = (currentPossibleWeightage*currentTotalPointsGained)/currentTotalPointsPossible;
                                        Log.d(""," currentTotalPointsGained: "+currentTotalPointsGained);
                                        Log.d(""," currentTotalPointsPossible: "+currentTotalPointsPossible);
                                        Log.d(""," currentPossibleWeightage: "+currentPossibleWeightage);
                                        Log.d(""," currentGainedWeightage: "+currentGainedWeightage);

                                        finalGainedWeightage = finalGainedWeightage + currentGainedWeightage;
                                        Log.d(""," finalGainedWeightage: "+finalGainedWeightage);
                                        */

                                        DecimalFormat df = new DecimalFormat("####0.00");
                                        double percentage = 0.0;

                                        if(finalTotalPointsPossible!=0.0)
                                            percentage = (100*finalTotalPointsGained)/finalTotalPointsPossible;

                                        Log.d(""," percentage: "+percentage);


                                        totalPercentageTextView.setText(""+df.format(percentage)+"%  ");

                                        if(percentage>=90.0)
                                        {
                                            totalGradeTextView.setText("A  ");
                                        }
                                        else if(percentage>=80.0)
                                        {
                                            totalGradeTextView.setText("B  ");
                                        }
                                        else if(percentage>=70)
                                        {
                                            totalGradeTextView.setText("C  ");
                                        }
                                        else
                                        {
                                            totalGradeTextView.setText("F  ");
                                        }

                                        //*****************************************************************************


                                        if(count!=0)
                                            modulesArraylist.add(module);

                                        modulesListAdapter.notifyDataSetChanged();

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.d("ModuleListFragment : "," Read cancelled due to "+databaseError.getMessage());
                                    }
                                });


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.d("ModuleListFragment : "," Read cancelled due to "+databaseError.getMessage());
                            }
                        });

                        //***************************************************************************************

                    }
                }
                modulesListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ModuleListFragment : "," Read cancelled due to "+databaseError.getMessage());
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        TotalGrade moduleName = (TotalGrade)getListView().getItemAtPosition(position);

        String module =  moduleName.getModuleName();

        //Toast.makeText(getActivity(), "Module Name: "+module, Toast.LENGTH_SHORT).show();

        /*
        Intent intent = new Intent(getActivity(),StudentAssignmentListActivity.class);
        intent.putExtra(IntentConstants.STUDENT_ID, studentId);
        intent.putExtra(IntentConstants.COURSE_NAME, courseName);
        intent.putExtra(IntentConstants.MODULE_NAME, module);
        startActivity(intent);
        */

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        StudentAssignmentListFragment studentAssignmentListFragment = new StudentAssignmentListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(IntentConstants.STUDENT_ID, studentId);
        bundle.putString(IntentConstants.COURSE_NAME, courseName);
        bundle.putString(IntentConstants.MODULE_NAME, module);
        studentAssignmentListFragment.setArguments(bundle);
        ft.replace(R.id.course_activity_frame_layout,studentAssignmentListFragment,"student_assignment_list_fragment");

        //Trial code for maintaining instance variable state
        //ft.add(R.id.course_activity_frame_layout, studentAssignmentListFragment);
        //ft.hide(StudentModulesFragment.this);

        ft.addToBackStack("student_modules_fragment");
        ft.commit();



        //moduleClickListener.showSelectedModuleContent(courseName, studentId, module);

    }

    private void getTotalGainedScoresForEachModule()
    {
        Log.d("moduleName : ", "Inside method getTotalGainedScoresForEachModule");
        String moduleName = "InClass";
        Log.d("moduleName : ", moduleName);
        int i;
        for(i=0; i<modulesArraylist.size();i++)
        {
            moduleName = modulesArraylist.get(i).getModuleName();
            final TotalGrade moduleTotalGrade = modulesArraylist.get(i);
            Log.d("moduleName : ", moduleName);

            mDatabase.child("students").child(courseName).child(studentId).child(moduleName).push();
            mDatabase.child("students").child(courseName).child(studentId).child(moduleName).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    //modulesArraylist.removeAll(modulesArraylist);
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                    {
                        if(!modulesArraylist.contains(postSnapshot.getKey()) && !postSnapshot.getKey().equals("weightage"))
                        {
                            String score = (String) postSnapshot.child("Total").getValue();
                            Double score1 = Double.parseDouble(score);
                            totalPoints=0;
                            totalPoints = moduleTotalGrade.getTotalGainedMarks() + score1;

                            moduleTotalGrade.setTotalGainedMarks(totalPoints);
                            //modulesArraylist.add(assignment);
                        }
                    }
                    modulesListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("ModuleListFragment : "," Read cancelled due to "+databaseError.getMessage());
                }
            });

            //moduleTotalGrade.setTotalGainedMarks(totalPoints);

            i++;

        }



    }

    private void getTotalPossibleScoresForEachModule()
    {
        //final String moduleName = "InClass";
        final ArrayList<String> moduleNames = new ArrayList<String>();
        moduleNames.add("InClass");
        moduleNames.add("Thesis");

        final ArrayList<Double> moduleTotalScores = new ArrayList<Double>();


        for(String moduleName: moduleNames)
        {
            mDatabase.child("students").child(courseName).child(studentId).child(moduleName).push();
            mDatabase.child("students").child(courseName).child(studentId).child(moduleName).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    //modulesArraylist.removeAll(modulesArraylist);
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                    {
                        if(!modulesArraylist.contains(postSnapshot.getKey()) && !postSnapshot.getKey().equals("weightage"))
                        {
                            String score = (String) postSnapshot.child("Total").getValue();
                            Double score1 = Double.parseDouble(score);
                            totalPoints = totalPoints + score1;
                            Log.d("totalPoints : ", ": "+totalPoints);
                            //modulesArraylist.add(assignment);
                        }

                        moduleTotalScores.add(totalPoints);
                        Log.d("moduleTotalScores : ", ": "+moduleTotalScores);
                    }


                    //modulesListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("ModuleListFragment : "," Read cancelled due to "+databaseError.getMessage());
                }
            });
        }


    }

    /*
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            moduleClickListener = (StudentModulesFragment.OnModuleClickListener) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement OnModuleClickListener");
        }
    }
    */

    private void getTotalMarksFromAllModules()
    {
        final ArrayList<String> moduleNames = new ArrayList<String>();
        moduleNames.add("InClass");
        moduleNames.add("Thesis");

        final ArrayList<Double> moduleTotalScores = new ArrayList<Double>();


        for(String moduleName: moduleNames)
        {
            mDatabase.child("students").child(courseName).child(studentId).child(moduleName).push();
            mDatabase.child("students").child(courseName).child(studentId).child(moduleName).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    double total = 0;
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                    {
                        if(!postSnapshot.getKey().equals("weightage"))
                        {
                            String score = (String) postSnapshot.child("Total").getValue();
                            Double score1 = Double.parseDouble(score);
                            //moduleTotalScores.add(score1);

                            total = total + score1;
                            //Log.d("totalPoints : ", ": "+totalPoints);
                            //modulesArraylist.add(assignment);
                        }

                        //moduleTotalScores.add(totalPoints);
                        //Log.d("moduleTotalScores : ", ": "+moduleTotalScores);
                    }
                    moduleTotalScores.add(total);
                    Log.d("moduleTotalScores : ", ": "+total);

                    Log.d("moduleTotalScores : ", "Arraylist: "+moduleTotalScores);


                    Log.d("Outside for : ", ": ");
                    int i =0;
                    for(TotalGrade totalGrade: modulesArraylist)
                    {
                        Log.d("Inside for : ", ": ");
                        totalGrade.setTotalGainedMarks(moduleTotalScores.get(i));
                        i++;
                    }

                    modulesListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("ModuleListFragment : "," Read cancelled due to "+databaseError.getMessage());
                }
            });
        }
    }


}
