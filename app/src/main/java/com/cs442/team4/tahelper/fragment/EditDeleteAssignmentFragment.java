package com.cs442.team4.tahelper.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sowmyaparameshwara on 11/1/16.
 */

public class EditDeleteAssignmentFragment extends Fragment {


    public Button addSplitButton;
    public EditText assignmentName;
    public EditText assignmentTotalScore;
    public EditText assignmentWeightage;
    public EditText splitName;
    public EditText splitScore;
    private ListView splitList;
    private ArrayList<AssignmentSplit> assignmentSplitsList;
    private EditDeleteAssignmentListItemAdapter assignmentAdapter;
    public Button editAssignment;
    public Button deleteAssignment;
    private DatabaseReference mDatabase;
    private String moduleName;
    private EditDeleteAssignmentsFragmentListener editDeleteAssignmentFragmentListener;
    private String originalAssignmentName;
    //private Button backButton;
    private String courseCode;
    private ArrayList<String> assignmentList;
    private boolean isGraded;
    public static final double MAX_WEIGHTAGE = 100.0;


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
        assignmentList = getArguments().getStringArrayList(IntentConstants.ASSIGNMENT_LIST);

        /*backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                editDeleteAssignmentFragmentListener.notifyBackButtonEvent(moduleName);
            }
        });*/

        addSplitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(splitName.getText()!=null && splitName.getText().length()>0 && splitScore.getText()!=null && splitScore.getText().length()>0 ){
                    boolean isUnique = isUnique(splitName.getText().toString());
                    boolean isValidSplitScore = isValidSplitScore(splitScore.getText().toString());
                    boolean isValidSplitName = isValidSplitName(splitName.getText().toString());
                    if(isUnique && isValidSplitScore && isValidSplitName){
                        assignmentSplitsList.add(new AssignmentSplit(splitName.getText().toString(),Double.parseDouble(splitScore.getText().toString())));
                        splitName.setText("");
                        splitScore.setText("");
                        assignmentAdapter.notifyDataSetChanged();
                    }

                }else{
                    if(splitName.getText().toString().trim().length()<=0) {
                        splitName.setError("Split name cannot be empty.");
                    }
                    if(splitScore.getText().toString().trim().length()<=0) {
                        splitScore.setError("Split score cannot be empty.");
                    }

                    Toast.makeText(getActivity(),"Please correct the errors and try again.",Toast.LENGTH_LONG).show();

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

        assignmentName.setHint("Enter sub "+moduleName+" name.");

        return layout;
    }

    private boolean isValidSplitName(String splitNameString) {
        if(!splitNameString.matches(".*[a-zA-Z]+.*")){
            splitName.setError("Split name cannot have special characters.");
            Toast.makeText(getActivity(),"Please correct all errors and try again.",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean isValidSplitScore(String splitScoreString) {
        if(!Character.isDigit(splitScoreString.charAt(0))){
            splitScore.setError("Split score should begin with a number.");
            Toast.makeText(getActivity(),"Please correct all errors and try again.",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean isUnique(String splitNameString) {
        for(AssignmentSplit assignmentSplit : assignmentSplitsList){
            if(assignmentSplit.getSplitName().equals(splitNameString)){
                splitName.setError("Split name needs to be unique.");
                Toast.makeText(getActivity(),"Please correct all errors and try again.",Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    private void handleEditAssignment() {
        if(assignmentName.getText()!=null && assignmentName.getText().length()>0 && assignmentTotalScore.getText()!=null && assignmentTotalScore.getText().length()>0
                && assignmentWeightage.getText()!=null && assignmentWeightage.getText().length()>0 ){
            boolean validWeightage = validateWeightage(assignmentWeightage.getText().toString());
            boolean isValidTotal = validateTotal();
            boolean isValidName = validateName(assignmentName.getText().toString());
            if(isValidTotal && isValidName && validWeightage){
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

    private boolean validateWeightage(String weightage) {
        boolean isValid = true;
        if(!Character.isDigit(weightage.charAt(0))){
            assignmentWeightage.setError("Sub Module Weightage should begin with a number");
            Toast.makeText(getActivity(),"Please correct all errors and try again.",Toast.LENGTH_LONG).show();

            isValid = false;
        }else {
            double weigthage = Double.parseDouble(weightage);
            if (weigthage > MAX_WEIGHTAGE) {
                assignmentWeightage.setError("Sub Module Weightage should be less than 100.0");
                Toast.makeText(getActivity(), "Please correct all errors and try again.", Toast.LENGTH_LONG).show();
                isValid = false;
            }
        }
        return isValid;
    }

    private boolean validateName(String assignmentNameString) {
        boolean isValid = true;
        for(String existingName : assignmentList){
            if(existingName.equals(assignmentNameString) && !existingName.equals(originalAssignmentName)){
                Toast.makeText(getActivity(),"Please correct all errors and try again.",Toast.LENGTH_LONG).show();
                assignmentName.setError("Sub module name cannot be duplicate.");
                isValid = false;
            }
        }
        Pattern p = Pattern.compile("[^A-Za-z0-9]");
        Matcher m = p.matcher(assignmentNameString);
        boolean b = m.find();
        if (isValid && b == true){
            assignmentName.setError("Sub Module Name should not contain special characters");
            Toast.makeText(getActivity(),"Please correct all errors and try again.",Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return isValid;
    }

    private boolean validateTotal() {
        boolean isValid = true;
        if(!Character.isDigit(assignmentTotalScore.getText().toString().charAt(0))){
            assignmentTotalScore.setError("Sub Module Total should begin with a number");
            Toast.makeText(getActivity(),"Please correct all errors and try again.",Toast.LENGTH_LONG).show();

            isValid = false;
        }else {
            double total = Double.parseDouble(assignmentTotalScore.getText().toString());
            if (assignmentSplitsList.size() != 0) {
                double splitTotal = 0;
                for (int i = 0; i < assignmentSplitsList.size(); i++) {
                    AssignmentSplit split = assignmentSplitsList.get(i);
                    splitTotal += split.getSplitScore();

                }
                if (total != splitTotal) {
                    isValid = false;
                    Toast.makeText(getActivity(), "Please correct all errors and try again.", Toast.LENGTH_LONG).show();
                    assignmentTotalScore.setError("Total count does not match with Sum of Splits.");
                }
            }
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
                          assignmentSplitsList.add(new AssignmentSplit(splits.getKey(),Double.parseDouble(splits.getValue().toString())));
                       }
                       assignmentAdapter.notifyDataSetChanged();
                    }
                    if(postSnapshot.getKey().equals("weightage")){
                        assignmentWeightage.setText(postSnapshot.getValue(String.class));
                        assignmentWeightage.setSelection(assignmentWeightage.length());
                    }
                    if(postSnapshot.getKey().equals("isGraded")){
                        isGraded = postSnapshot.getValue(Boolean.class);
                        editAssignment.setVisibility(View.GONE);
                        assignmentName.setFocusable(false);
                        assignmentName.setBackgroundColor(Color.DKGRAY);
                        assignmentTotalScore.setFocusable(false);
                        assignmentTotalScore.setBackgroundColor(Color.DKGRAY);
                        assignmentWeightage.setFocusable(false);
                        assignmentWeightage.setBackgroundColor(Color.DKGRAY);
                        splitName.setFocusable(false);
                        splitName.setBackgroundColor(Color.DKGRAY);
                        splitScore.setFocusable(false);
                        splitScore.setBackgroundColor(Color.DKGRAY);
                        addSplitButton.setVisibility(View.GONE);
                        assignmentAdapter.setGraded(isGraded);


                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT, 2.0f);

                        deleteAssignment.setLayoutParams(param);

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
