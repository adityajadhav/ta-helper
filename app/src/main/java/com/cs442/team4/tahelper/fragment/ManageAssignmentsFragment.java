package com.cs442.team4.tahelper.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.contants.IntentConstants;
import com.cs442.team4.tahelper.listItem.ManageAssignmentsListItemAdapter;
import com.cs442.team4.tahelper.listItem.ModuleListItemAdapter;
import com.cs442.team4.tahelper.model.AssignmentEntity;
import com.cs442.team4.tahelper.model.ModuleEntity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by sowmyaparameshwara on 10/31/16.
 */

public class ManageAssignmentsFragment extends Fragment {

    private ListView manageAssignmentsList;
    private DatabaseReference mDatabase;
    private ArrayList<String> assignmentsList;
    private ManageAssignmentsListItemAdapter manageAssignmentsAdapter;
    private String moduleName;
   // private TextView assignmentName;
    private Button addAssignmentButton;
    private ManageAssignmentFragmentListener manageAssignmentFragmentListener;
    private Button backButton;

    public interface ManageAssignmentFragmentListener{
        public void notifyAddAssignmentEvent();
        public void notifyBackButton();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout= (LinearLayout) inflater.inflate(R.layout.manage_assignments_fragment,container,false);
        manageAssignmentsList = (ListView) layout.findViewById(R.id.manageAssignmentsFragmentList);
       // assignmentName = (TextView) layout.findViewById(R.id.manageAssignmentsFragmentTextView);
        addAssignmentButton = (Button) layout.findViewById(R.id.manageAssignmentsFragmentAddAssignmentButton);
        backButton = (Button) layout.findViewById(R.id.manageAssignmentsFragmentBackButton);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        backButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                manageAssignmentFragmentListener.notifyBackButton();
            }
        });

        addAssignmentButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                manageAssignmentFragmentListener.notifyAddAssignmentEvent();
            }
        });
        return layout;
    }

    private void loadExistingAssignmentFromDatabase() {
        assignmentsList = new ArrayList<String>();
        loadFromDatabase();
        manageAssignmentsAdapter = new ManageAssignmentsListItemAdapter(getActivity(),R.layout.manage_assignments_item_layout,assignmentsList);
        manageAssignmentsList.setAdapter(manageAssignmentsAdapter);
    }

    private void loadFromDatabase() {
        mDatabase.child("modules/"+moduleName).push();

        mDatabase.child("modules/"+moduleName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                assignmentsList.removeAll(assignmentsList);
                Log.i("","Snaphot "+dataSnapshot+"  "+dataSnapshot.getChildren()+"  "+dataSnapshot.getValue());
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    if(!assignmentsList.contains(postSnapshot.getKey())) {
                        assignmentsList.add((String)postSnapshot.getKey());
                     /*   Iterator it = (Iterator) map.entrySet();
                        while(it.hasNext()){

                        }*/
                      //  ModuleSEntity.addAssignments(moduleName,new AssignmentEntity((String)postSnapshot.getKey(),));
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ModuleListFragment : "," Read cancelled due to "+databaseError.getMessage());
            }
        });
    }

    public void initialise(Intent intent) {
        if(intent!=null && intent.getStringExtra(IntentConstants.MODULE_NAME)!=null){
            moduleName = intent.getStringExtra(IntentConstants.MODULE_NAME);
         //   assignmentName.setText(moduleName+" Module ");
            addAssignmentButton.setText(" Add "+moduleName+" Sub Module ");
            loadExistingAssignmentFromDatabase();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            manageAssignmentFragmentListener = (ManageAssignmentFragmentListener) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement OnNewItemAddedListener");
        }
    }
}
