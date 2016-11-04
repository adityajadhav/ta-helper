package com.cs442.team4.tahelper.student;

import android.app.Activity;
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

import java.util.ArrayList;

/**
 * Created by Mohammed on 11/2/2016.
 */

public class StudentModulesFragment extends ListFragment {

    String studentId;
    String courseName;

    private DatabaseReference mDatabase;
    View myFragmentView;
    TextView modulesTextView;
    StudentModulesFragment.OnModuleClickListener moduleClickListener;

    public static ArrayList<String> modulesArraylist;
    public static StudentModulesListAdapter modulesListAdapter;

    public interface OnModuleClickListener{
        public void showSelectedModuleContent(String courseName, String studentId, String moduleName);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Later, will have to check if bundle is there or not and then only assign the value...
        final String idFromActivity = getArguments().getString(IntentConstants.STUDENT_ID);
        final String courseNameFromActivity = getArguments().getString(IntentConstants.COURSE_NAME);
        studentId = idFromActivity;
        courseName = courseNameFromActivity;

        myFragmentView = inflater.inflate(R.layout.student_modules_fragment, container, false);
        modulesTextView = (TextView) myFragmentView.findViewById(R.id.textView11);

        modulesTextView.setText(studentId+"'s Modules");

        int resID = R.layout.student_list_textview;

        modulesArraylist = new ArrayList<String>();

        modulesListAdapter = new StudentModulesListAdapter(getContext(), resID, modulesArraylist);
        setListAdapter(modulesListAdapter);

        loadFromDatabase();

        return myFragmentView;
    }

    private void loadFromDatabase() {
        mDatabase.child("students").child(studentId).child(courseName).push();

        mDatabase.child("students").child(studentId).child(courseName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                modulesArraylist.removeAll(modulesArraylist);
                //Log.i("","Snaphot "+dataSnapshot+"  "+dataSnapshot.getChildren()+"  "+dataSnapshot.getValue());
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {
                    if(!modulesArraylist.contains(postSnapshot.getKey()))
                    {
                        modulesArraylist.add((String)postSnapshot.getKey());
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

        String module =  (String)getListView().getItemAtPosition(position);

        //Toast.makeText(getActivity(), "Module Name: "+module, Toast.LENGTH_SHORT).show();

        moduleClickListener.showSelectedModuleContent(courseName, studentId, module);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            moduleClickListener = (StudentModulesFragment.OnModuleClickListener) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement OnStudentClickListener");
        }
    }

}
