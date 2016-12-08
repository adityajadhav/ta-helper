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

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sowmyaparameshwara on 10/31/16.
 */

public class AddAssignmentsFragment extends Fragment {


    private Button addSplitButton;
    private EditText assignmentName;
    private EditText assignmentTotalScore;
    private EditText assignmentWeightage;
    private EditText splitName;
    private EditText splitScore;
    private ListView splitList;
    private ArrayList<AssignmentSplit> assignmentSplitsList;
    private AddAssignmentListItemAdapter assignmentAdapter;
    private Button addAssignment;
    private DatabaseReference mDatabase;
    private String moduleName;
    private AddAssignmentsFragmentListener addAssignmentFragmentListener;
    //private Button backButton;
    private String courseCode;
    private ArrayList<String> assignmentsList;


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
                     assignmentSplitsList.add(new AssignmentSplit(splitName.getText().toString(),Double.parseDouble(splitScore.getText().toString())));
                     splitName.setText("");
                     splitScore.setText("");

                     assignmentAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(getActivity(),"Please enter both split name, score and try again.",Toast.LENGTH_LONG).show();
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
        return layout;
    }

    private void handleAddAssignment() {
        mDatabase = FirebaseDatabase.getInstance().getReference("modules").child(courseCode);
        if(assignmentName.getText()!=null && assignmentName.getText().length()>0 && assignmentTotalScore.getText()!=null && assignmentTotalScore.getText().length()>0
                && assignmentWeightage.getText()!=null && assignmentWeightage.getText().length()>0){
            boolean validTotal = validateTotal();
            boolean validName = validName(assignmentName.getText().toString());
            if(validTotal && validName){
                mDatabase.child(moduleName).child(assignmentName.getText().toString()).child("Total").setValue(assignmentTotalScore.getText().toString());
                mDatabase.child(moduleName).child(assignmentName.getText().toString()).child("weightage").setValue(assignmentWeightage.getText().toString());

                for (int i = 0; i < assignmentSplitsList.size(); i++) {
                    AssignmentSplit split = assignmentSplitsList.get(i);
                    mDatabase.child(moduleName).child(assignmentName.getText().toString()).child("Splits").child(split.getSplitName()).setValue(String.valueOf(split.getSplitScore()));
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
            }else if(!validTotal){
                Toast.makeText(getActivity(),"Total count does not match with Sum of Splits.Please correct it and try again.",Toast.LENGTH_LONG).show();
            }else if(!validName){
                Toast.makeText(getActivity(),"Sub module name cannot be duplicate.",Toast.LENGTH_LONG).show();
                assignmentName.setError("Sub module name cannot be duplicate.");

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

    private boolean validName(String assignmentName) {
        boolean isValid = true;
        for(String existingName : assignmentsList){
            if(existingName.equals(assignmentName)){
                return false;
            }
        }
        return isValid;
    }

    private boolean validateTotal() {
        boolean isValid = false;
        double total = Double.parseDouble(assignmentTotalScore.getText().toString());
        if(assignmentSplitsList.size()==0){
            isValid = true;
        }else {
            double splitTotal = 0;
            for (int i = 0; i < assignmentSplitsList.size(); i++) {
                AssignmentSplit split = assignmentSplitsList.get(i);
                splitTotal+= split.getSplitScore();

            }
            isValid = (total==splitTotal)?true:false;
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
