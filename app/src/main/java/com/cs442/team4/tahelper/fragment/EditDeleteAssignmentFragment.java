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
import android.widget.Toast;

import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.contants.IntentConstants;
import com.cs442.team4.tahelper.listItem.AddAssignmentListItemAdapter;
import com.cs442.team4.tahelper.listItem.EditDeleteAssignmentListItemAdapter;
import com.cs442.team4.tahelper.model.AssignmentEntity;
import com.cs442.team4.tahelper.model.AssignmentSplit;
import com.cs442.team4.tahelper.model.ModuleEntity;
import com.cs442.team4.tahelper.services.AssignmentsDatabaseUpdationService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by sowmyaparameshwara on 11/1/16.
 */

public class EditDeleteAssignmentFragment extends Fragment {


    private Button addSplitButton;
    private EditText assignmentName;
    private EditText assignmentTotalScore;
    private EditText assignmentWeightage;
    private EditText splitName;
    private EditText splitScore;
    private ListView splitList;
    private ArrayList<AssignmentSplit> assignmentSplitsList;
    private EditDeleteAssignmentListItemAdapter assignmentAdapter;
    private Button editAssignment;
    private Button deleteAssignment;
    private DatabaseReference mDatabase;
    private String moduleName;
    private EditDeleteAssignmentsFragmentListener editDeleteAssignmentFragmentListener;
    private String originalAssignmentName;
    //private Button backButton;
    private String courseCode;

    public interface EditDeleteAssignmentsFragmentListener{
        public void notifyEditDeleteAssignmentEvent(String moduleName);
        //public void notifyBackButtonEvent(String moduleName);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout= (LinearLayout) inflater.inflate(R.layout.editdelete_assignment_fragment,container,false);
        addSplitButton = (Button) layout.findViewById(R.id.editDeleteAssignmentsFragmentButton);
        splitName = (EditText) layout.findViewById(R.id.editDeleteAssignmentFragmentSplitName);
        splitScore = (EditText) layout.findViewById(R.id.editDeleteAssignmentFragmentSplitScore);
        splitList = (ListView) layout.findViewById(R.id.editDeleteAssignmentsFragmentListView);
        editAssignment = (Button) layout.findViewById(R.id.editDeleteAssignmentsFragmentEditButton);
        deleteAssignment = (Button) layout.findViewById(R.id.editDeleteAssignmentsFragmentDeleteButton);
        assignmentName = (EditText) layout.findViewById(R.id.editDeleteAssignmentsFragmentTextView);
        assignmentTotalScore = (EditText) layout.findViewById(R.id.editDeleteAssignmentFragmentTotalScore);
        assignmentWeightage = (EditText) layout.findViewById(R.id.editDeleteAssignmentFragmentWeightageValue);
        //backButton = (Button) layout.findViewById(R.id.editDeleteAssignmentsFragmentBackButton);

        assignmentSplitsList = new ArrayList<AssignmentSplit>();
        assignmentAdapter = new EditDeleteAssignmentListItemAdapter(getActivity(),R.layout.add_assignments_item_layout,assignmentSplitsList);
        splitList.setAdapter(assignmentAdapter);

        /*backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                editDeleteAssignmentFragmentListener.notifyBackButtonEvent(moduleName);
            }
        });*/

        addSplitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(splitName.getText()!=null && splitName.getText().length()>0 && splitScore.getText()!=null && splitScore.getText().length()>0){
                    assignmentSplitsList.add(new AssignmentSplit(splitName.getText().toString(),Integer.parseInt(splitScore.getText().toString())));
                    splitName.setText("");
                    splitScore.setText("");
                    assignmentAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(getActivity(),"Please enter both split name, score and try again.",Toast.LENGTH_LONG).show();
                }
            }
        });

        editAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleEditAssignment();
            }
        });

        deleteAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDeleteAssignment();
            }
        });

        moduleName = getArguments().getString(IntentConstants.MODULE_NAME);
        courseCode = getArguments().getString(IntentConstants.COURSE_ID);
        originalAssignmentName = getArguments().getString(IntentConstants.ASSIGNMENT_NAME);
        //backButton.setText(" BACK TO "+moduleName+" MODULE LIST");
        assignmentName.setText(originalAssignmentName);
        assignmentName.setSelection(assignmentName.getText().length());
        mDatabase = FirebaseDatabase.getInstance().getReference("modules").child(courseCode);
        loadFromDatabase();
        return layout;
    }

    private void handleEditAssignment() {
        if(assignmentName.getText()!=null && assignmentName.getText().length()>0 && assignmentTotalScore.getText()!=null && assignmentTotalScore.getText().length()>0
                && assignmentWeightage.getText()!=null && assignmentWeightage.getText().length()>0){
            if(validateTotal()){
                mDatabase.child(moduleName).child(originalAssignmentName).removeValue();
                ModuleEntity.removeAssignmentFromModule(moduleName,originalAssignmentName);

                mDatabase.child(moduleName).child(assignmentName.getText().toString()).child("Total").setValue(assignmentTotalScore.getText().toString());
                mDatabase.child(moduleName).child(assignmentName.getText().toString()).child("weightage").setValue(assignmentWeightage.getText().toString());

                // assignmentSplitsList.remove(assignmentSplitsList);
                for(int i = 0 ; i <assignmentSplitsList.size();i++){
                    AssignmentSplit split = assignmentSplitsList.get(i);
                    mDatabase.child(moduleName).child(assignmentName.getText().toString()).child("Splits").child(split.getSplitName()).setValue(String.valueOf(split.getSplitScore()));
                }

                Intent serviceIntent = new Intent(getActivity(), AssignmentsDatabaseUpdationService.class);
                serviceIntent.putExtra(IntentConstants.MODULE_NAME,moduleName);
                serviceIntent.putExtra(IntentConstants.ASSIGNMENT_OLD_NAME,originalAssignmentName);
                serviceIntent.putExtra(IntentConstants.COURSE_ID, courseCode);
                serviceIntent.putExtra(IntentConstants.ASSIGNMENT_NEW_NAME,assignmentName.getText().toString());
                serviceIntent.putExtra(IntentConstants.TOTAL,assignmentTotalScore.getText().toString());
                serviceIntent.putExtra(IntentConstants.SPLIT,assignmentSplitsList);
                serviceIntent.putExtra(IntentConstants.ASSIGNMENT_WEIGHTAGE,assignmentWeightage.getText().toString());
                serviceIntent.putExtra(IntentConstants.MODE,"Edit");
                getActivity().startService(serviceIntent);

                ModuleEntity.addAssignments(moduleName,new AssignmentEntity(assignmentName.getText().toString(),assignmentTotalScore.getText().toString(),assignmentWeightage.getText().toString(),assignmentSplitsList));

                assignmentAdapter.notifyDataSetChanged();

                editDeleteAssignmentFragmentListener.notifyEditDeleteAssignmentEvent(moduleName);
            }else{
                Toast.makeText(getActivity(),"Total count does not match with Sum of Splits.Please correct it and try again.",Toast.LENGTH_LONG).show();
            }

        }else{
            Toast.makeText(getActivity(),"Please correct all errors and try again.",Toast.LENGTH_LONG).show();
            if(assignmentName.getText().toString().trim().length() <=0)
            {
                assignmentName.setError("Assignment Name cannot be empty");
            }
            if(assignmentTotalScore.getText().toString().trim().length() <=0)
            {
                assignmentTotalScore.setError("Total Score cannot be empty");
            }
            if(assignmentWeightage.getText().toString().trim().length() <=0)
            {
                assignmentWeightage.setError("Assignment Weightage cannot be empty");
            }
        }
    }

    private boolean validateTotal() {
        boolean isValid = false;
        int total = Integer.parseInt(assignmentTotalScore.getText().toString());
        if(assignmentSplitsList.size()==0){
            isValid = true;
        }else {
            int splitTotal = 0;
            for (int i = 0; i < assignmentSplitsList.size(); i++) {
                AssignmentSplit split = assignmentSplitsList.get(i);
                splitTotal+= split.getSplitScore();

            }
            isValid = (total==splitTotal)?true:false;
        }
        return isValid;
    }

    private void handleDeleteAssignment() {


            mDatabase.child(moduleName).child(originalAssignmentName).setValue(null);


        ModuleEntity.removeAssignmentFromModule(moduleName,originalAssignmentName);

        Intent serviceIntent = new Intent(getActivity(), AssignmentsDatabaseUpdationService.class);
        serviceIntent.putExtra(IntentConstants.MODULE_NAME,moduleName);
        serviceIntent.putExtra(IntentConstants.ASSIGNMENT_NAME,originalAssignmentName);
        serviceIntent.putExtra(IntentConstants.COURSE_ID, courseCode);
        serviceIntent.putExtra(IntentConstants.MODE,"Delete");
        getActivity().startService(serviceIntent);

        editDeleteAssignmentFragmentListener.notifyEditDeleteAssignmentEvent(moduleName);
    }


    public void deleteSplit(AssignmentSplit split) {
        assignmentSplitsList.remove(split);
        assignmentAdapter.notifyDataSetChanged();
    }

  /*  public void initialise(Intent intent) {
        if(intent!=null && intent.getStringExtra(IntentConstants.MODULE_NAME)!=null){
            moduleName = intent.getStringExtra(IntentConstants.MODULE_NAME);
            courseCode = intent.getStringExtra(IntentConstants.COURSE_ID);
            originalAssignmentName = intent.getStringExtra(IntentConstants.ASSIGNMENT_NAME);
            backButton.setText(" BACK TO "+moduleName+" MODULE LIST");
            assignmentName.setText(originalAssignmentName);
            assignmentName.setSelection(assignmentName.getText().length());
            mDatabase = FirebaseDatabase.getInstance().getReference("modules").child(courseCode);
            loadFromDatabase();
        }
    }
*/
    private void loadFromDatabase() {
        mDatabase.child(moduleName).child(originalAssignmentName).push();
        mDatabase.child(moduleName+"/"+originalAssignmentName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("EditDelete"," children : "+dataSnapshot.getChildren()+"  "+dataSnapshot.getChildrenCount());


                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    if(postSnapshot.getKey().equals("Total")){
                        assignmentTotalScore.setText(postSnapshot.getValue(String.class));
                        assignmentTotalScore.setSelection(assignmentTotalScore.length());
                    }
                    if(postSnapshot.getKey().equals("Splits")){
                       assignmentSplitsList.removeAll(assignmentSplitsList);
                       for (DataSnapshot splits: postSnapshot.getChildren()) {
                          assignmentSplitsList.add(new AssignmentSplit(splits.getKey(),Integer.parseInt(splits.getValue().toString())));
                       }
                       assignmentAdapter.notifyDataSetChanged();
                    }
                    if(postSnapshot.getKey().equals("weightage")){
                        assignmentWeightage.setText(postSnapshot.getValue(String.class));
                        assignmentWeightage.setSelection(assignmentWeightage.length());
                    }
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ModuleListFragment : "," Read cancelled due to "+databaseError.getMessage());
            }
        });
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            editDeleteAssignmentFragmentListener = (EditDeleteAssignmentsFragmentListener) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement OnNewItemAddedListener");
        }
    }
}
