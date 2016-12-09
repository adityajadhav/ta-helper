package com.cs442.team4.tahelper.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.activity.AddModuleActivity;
import com.cs442.team4.tahelper.contants.IntentConstants;
import com.cs442.team4.tahelper.listItem.AddAssignmentListItemAdapter;
import com.cs442.team4.tahelper.model.AssignmentEntity;
import com.cs442.team4.tahelper.model.AssignmentSplit;
import com.cs442.team4.tahelper.model.ModuleEntity;
import com.cs442.team4.tahelper.services.AssignmentsDatabaseUpdationService;
import com.cs442.team4.tahelper.services.ModuleDatabaseUpdationIntentService;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.SimpleShowcaseEventListener;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sowmyaparameshwara on 10/31/16.
 */

public class AddAssignmentsFragment extends Fragment {


    public Button addSplitButton;
    public EditText assignmentName;
    public EditText assignmentTotalScore;
    public EditText assignmentWeightage;
    public EditText splitName;
    public EditText splitScore;
    private ListView splitList;
    private ArrayList<AssignmentSplit> assignmentSplitsList;
    private AddAssignmentListItemAdapter assignmentAdapter;
    public Button addAssignment;
    private DatabaseReference mDatabase;
    private String moduleName;
    private AddAssignmentsFragmentListener addAssignmentFragmentListener;
    //private Button backButton;
    private String courseCode;
    private ArrayList<String> assignmentsList;
    public static final double MAX_WEIGHTAGE = 100.0;



    public interface AddAssignmentsFragmentListener{
        public void notifyAddAssignmentEvent(String moduleName);
        //public void notifyBackEvent(String moduleName);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout= (LinearLayout) inflater.inflate(R.layout.add_assignment_fragment,container,false);
        addSplitButton = (Button) layout.findViewById(R.id.addAssignmentsFragmentButton);
        splitName = (EditText) layout.findViewById(R.id.addAssignmentFragmentSplitName);
        splitScore = (EditText) layout.findViewById(R.id.addAssignmentFragmentSplitScore);
        splitList = (ListView) layout.findViewById(R.id.addAssignmentsFragmentListView);
        addAssignment = (Button) layout.findViewById(R.id.addAssignmentsFragmentAddButton);
        assignmentName = (EditText) layout.findViewById(R.id.addAssignmentsFragmentTextView);
        assignmentTotalScore = (EditText) layout.findViewById(R.id.addAssignmentFragmentTotalScore);
        assignmentWeightage = (EditText) layout.findViewById(R.id.addAssignmentFragmentWeightageValue);
        //backButton = (Button) layout.findViewById(R.id.addAssignmentsFragmentBackButton);
        assignmentSplitsList = new ArrayList<AssignmentSplit>();
        assignmentAdapter = new AddAssignmentListItemAdapter(getActivity(),R.layout.add_assignments_item_layout,assignmentSplitsList);
        splitList.setAdapter(assignmentAdapter);

        assignmentsList = getArguments().getStringArrayList(IntentConstants.ASSIGNMENT_LIST);

        /*backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 addAssignmentFragmentListener.notifyBackEvent(moduleName);;
            }
        });*/

        addSplitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(splitName.getText()!=null && splitName.getText().length()>0 && splitScore.getText()!=null && splitScore.getText().length()>0){
                    boolean isUnique = isUnique(splitName.getText().toString());
                    boolean isValidSplitScore = isValidSplitScore(splitScore.getText().toString());
                    boolean isValidSplitName = isValidSplitName(splitName.getText().toString());
                    if(isUnique && isValidSplitScore && isValidSplitName) {
                        assignmentSplitsList.add(new AssignmentSplit(splitName.getText().toString(), Double.parseDouble(splitScore.getText().toString())));
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

        addAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               handleAddAssignment();
            }
        });

        moduleName = getArguments().getString(IntentConstants.MODULE_NAME);
        //backButton.setText(" BACK TO "+moduleName+" MODULE LIST");
        courseCode = getArguments().getString(IntentConstants.COURSE_ID);
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



    private void handleAddAssignment() {
        mDatabase = FirebaseDatabase.getInstance().getReference("modules").child(courseCode);
        if(assignmentName.getText()!=null && assignmentName.getText().length()>0 && assignmentTotalScore.getText()!=null && assignmentTotalScore.getText().length()>0
                && assignmentWeightage.getText()!=null && assignmentWeightage.getText().length()>0){
            boolean validWeightage = validateWeightage(assignmentWeightage.getText().toString());
            boolean validTotal = validateTotal();
            boolean validName = validName(assignmentName.getText().toString());
            if(validTotal && validName && validWeightage){
                mDatabase.child(moduleName).child(assignmentName.getText().toString()).child("Total").setValue(assignmentTotalScore.getText().toString());
                mDatabase.child(moduleName).child(assignmentName.getText().toString()).child("weightage").setValue(assignmentWeightage.getText().toString());

                for (int i = 0; i < assignmentSplitsList.size(); i++) {
                    AssignmentSplit split = assignmentSplitsList.get(i);
                    mDatabase.child(moduleName).child(assignmentName.getText().toString()).child("Splits").child(String.valueOf(split.getSplitName())).setValue(String.valueOf(split.getSplitScore()));
                }

                Intent serviceIntent = new Intent(getActivity(), AssignmentsDatabaseUpdationService.class);
                serviceIntent.putExtra(IntentConstants.MODULE_NAME, moduleName);
                serviceIntent.putExtra(IntentConstants.COURSE_ID, courseCode);
                serviceIntent.putExtra(IntentConstants.ASSIGNMENT_NAME, assignmentName.getText().toString());
                serviceIntent.putExtra(IntentConstants.TOTAL, assignmentTotalScore.getText().toString());
                serviceIntent.putExtra(IntentConstants.ASSIGNMENT_WEIGHTAGE, assignmentWeightage.getText().toString());

                serviceIntent.putExtra(IntentConstants.SPLIT, assignmentSplitsList);
                serviceIntent.putExtra(IntentConstants.MODE, "Add");
                getActivity().startService(serviceIntent);

                ModuleEntity.addAssignments(moduleName,new AssignmentEntity(assignmentName.getText().toString(),assignmentTotalScore.getText().toString(),assignmentWeightage.getText().toString(),assignmentSplitsList));
                addAssignmentFragmentListener.notifyAddAssignmentEvent(moduleName);
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

    private boolean validName(String assignmentNameString) {
        boolean isValid = true;
        for(String existingName : assignmentsList){
            if(existingName.equals(assignmentNameString)){
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
            Toast.makeText(getActivity(),"Please correct the errors and try again.",Toast.LENGTH_LONG).show();

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
                    Toast.makeText(getActivity(), "Please correct the errors and try again.", Toast.LENGTH_LONG).show();
                    assignmentTotalScore.setError("Total count does not match with Sum of Splits.");

                }
            }
        }
        return isValid;
    }

    public void deleteSplit(AssignmentSplit split) {
        assignmentSplitsList.remove(split);
        assignmentAdapter.notifyDataSetChanged();
    }

/*    public void initialise(Intent intent) {
        if(intent!=null && intent.getStringExtra(IntentConstants.MODULE_NAME)!=null){
            moduleName = intent.getStringExtra(IntentConstants.MODULE_NAME);
            backButton.setText(" BACK TO "+moduleName+" MODULE LIST");
            courseCode = intent.getStringExtra(IntentConstants.COURSE_ID);
        }
    }*/


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            addAssignmentFragmentListener = (AddAssignmentsFragmentListener) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement OnNewItemAddedListener");
        }
    }

  /*  @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SharedPreferences prefs = getActivity().getSharedPreferences("com.cs442.team4.tahelper.AddAssignmentsFragment", MODE_PRIVATE);
        boolean isFirstRun = prefs.getBoolean("firstrun", true);
        if (isFirstRun)
        {
            prefs.edit().putBoolean("firstrun", false).commit();
            showFirstShowCase();
        }


    }*/

    private void showFirstShowCase(){
        new ShowcaseView.Builder(getActivity())
                .withMaterialShowcase()
                .setStyle(R.style.CustomShowcaseTheme2)
                .setTarget(new ViewTarget(assignmentName))
                .hideOnTouchOutside()
                .setContentTitle("Please enter assignment name here.")
                .setShowcaseEventListener(new SimpleShowcaseEventListener() {

                    @Override
                    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                        showSecondShowCase();
                    }

                })
                .build();
    }

    private void showSecondShowCase() {
        new ShowcaseView.Builder(getActivity())
                .withMaterialShowcase()
                .setStyle(R.style.CustomShowcaseTheme2)
                .setTarget(new ViewTarget(splitName))
                .hideOnTouchOutside()
                .setContentTitle("Please enter split name here.")
                .setShowcaseEventListener(new SimpleShowcaseEventListener() {

                    @Override
                    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                        showThirdShowCase();
                    }

                })
                .build();
    }

    private void showThirdShowCase() {
        new ShowcaseView.Builder(getActivity())
                .withMaterialShowcase()
                .setStyle(R.style.CustomShowcaseTheme2)
                .setTarget(new ViewTarget(splitScore))
                .hideOnTouchOutside()
                .setContentTitle("Please enter score corresponding to the split here.")
                .setShowcaseEventListener(new SimpleShowcaseEventListener() {

                    @Override
                    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                        showFourthShowCase();
                    }

                })
                .build();
    }

    private void showFourthShowCase() {
        new ShowcaseView.Builder(getActivity())
                .withMaterialShowcase()
                .setStyle(R.style.CustomShowcaseTheme2)
                .setTarget(new ViewTarget(addSplitButton))
                .hideOnTouchOutside()
                .setContentTitle("Please click here to add the new split details.")
                .setShowcaseEventListener(new SimpleShowcaseEventListener() {

                    @Override
                    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                        showFifthShowCase();
                    }

                })
                .build();
    }

    private void showFifthShowCase() {
        new ShowcaseView.Builder(getActivity())
                .withMaterialShowcase()
                .setStyle(R.style.CustomShowcaseTheme2)
                .setTarget(new ViewTarget(assignmentTotalScore))
                .hideOnTouchOutside()
                .setContentTitle("Please enter total score for assignment here.")
                .setShowcaseEventListener(new SimpleShowcaseEventListener() {

                    @Override
                    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                        showSixthShowCase();
                    }

                })
                .build();
    }

    private void showSixthShowCase() {
        new ShowcaseView.Builder(getActivity())
                .withMaterialShowcase()
                .setStyle(R.style.CustomShowcaseTheme2)
                .setTarget(new ViewTarget(addAssignment))
                .hideOnTouchOutside()
                .setContentTitle("Please click here to add the new sub module.")
                .setShowcaseEventListener(new SimpleShowcaseEventListener() {

                    @Override
                    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                        //showEightShowCase();
                    }

                })
                .build();
    }

    /*private void showSeventhShowCase() {
        new ShowcaseView.Builder(getActivity())
                .withMaterialShowcase()
                .setStyle(R.style.CustomShowcaseTheme2)
                .setTarget(new ViewTarget(backButton))
                .hideOnTouchOutside()
                .setContentTitle("Please click here to go back to module list.")
                .setShowcaseEventListener(new SimpleShowcaseEventListener() {

                    @Override
                    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                        showEightShowCase();
                    }

                })
                .build();
    }*/


   /* private void showEightShowCase() {
        new ShowcaseView.Builder(getActivity())
                .withMaterialShowcase()
                .setStyle(R.style.CustomShowcaseTheme2)
                .setTarget(new ViewTarget(((AddModuleActivity)getActivity()).mDrawerLayout))
                .hideOnTouchOutside()
                .setContentTitle("Swipe from left to launch drawer with navigation options.")
                .build();
    }*/

}
