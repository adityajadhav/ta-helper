package com.cs442.team4.tahelper.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.activity.AddModuleActivity;
import com.cs442.team4.tahelper.contants.IntentConstants;
import com.cs442.team4.tahelper.model.AssignmentEntity;
import com.cs442.team4.tahelper.model.AssignmentSplit;
import com.cs442.team4.tahelper.model.ModuleEntity;
import com.cs442.team4.tahelper.services.AssignmentsDatabaseUpdationService;
import com.cs442.team4.tahelper.services.ModuleDatabaseUpdationIntentService;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.SimpleShowcaseEventListener;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sowmyaparameshwara on 10/30/16.
 */

public class EditDeleteModuleFragment extends Fragment {

    private EditText moduleName;
    private EditText moduleWeightage;
    private Button editButton;
    private Button deleteButton;
    private String moduleNameString;
    private String moduleWeightageString;
    private String courseCode;
    private EditDeleteButtonListner editDeleteButtonListner;
    private ArrayList<String> moduleList;
    private boolean isGraded;

    //private Button backButton;

    public interface EditDeleteButtonListner{
        public void clickButtonEvent();
        //public void backToModuleList();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout= (LinearLayout) inflater.inflate(R.layout.edit_delete_module_fragment,container,false);
        moduleName = (EditText) layout.findViewById(R.id.editDeleteModuleNameFragmentEditTextView);
        moduleWeightage = (EditText) layout.findViewById(R.id.editDeleteModuleWeightageFragmentEditTextView);

        editButton = (Button)layout.findViewById(R.id.editModuleButtonFragmentView);
        deleteButton = (Button)layout.findViewById(R.id.deleteModuleButtonFragmentView);
        moduleList = getArguments().getStringArrayList(IntentConstants.MODULE_LIST);


        //backButton = (Button) layout.findViewById(R.id.editDeleteModuleFragmentListViewBackButton);

       /* backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDeleteButtonListner.backToModuleList();
            }
        });*/
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 FirebaseDatabase.getInstance().getReference("modules/"+courseCode+"/"+moduleNameString).removeValue();
                ModuleEntity.removeModule(moduleNameString);
                Intent serviceIntent = new Intent(getActivity(), ModuleDatabaseUpdationIntentService.class);
                serviceIntent.putExtra(IntentConstants.MODULE_NAME,moduleNameString);
                serviceIntent.putExtra(IntentConstants.COURSE_ID,courseCode);
                serviceIntent.putExtra(IntentConstants.MODE,"Delete");
                getActivity().startService(serviceIntent);

                //  ModuleEntity.removeKeyValue(moduleNameString);
                editDeleteButtonListner.clickButtonEvent();

            }
        });
        editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(moduleName.getText().toString()!=null && moduleName.getText().toString().length()>0 && moduleWeightage.getText().toString()!=null
                        && moduleWeightage.getText().toString().length()>0) {
                    if(isValidName(moduleName.getText().toString(),moduleWeightage.getText().toString())){
                        ModuleEntity.editModule(moduleNameString,moduleName.getText().toString(),moduleWeightage.getText().toString());

                        FirebaseDatabase.getInstance().getReference("modules/"+courseCode+"/"+moduleNameString).removeValue();

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("modules");
                        // String key  = databaseReference.push().getKey();
                        //databaseReference.child(key).child("name").setValue(moduleName.getText().toString());
                        databaseReference.child(courseCode).child(moduleName.getText().toString()).child("weightage").setValue(moduleWeightage.getText().toString());
                        ArrayList<AssignmentEntity> assignmentsList = ModuleEntity.getAssignmentList(moduleName.getText().toString());
                        for(int i = 0 ; i <assignmentsList.size(); i++){
                            AssignmentEntity assignmentEntity = assignmentsList.get(i);

                            databaseReference.child(courseCode).child(moduleName.getText().toString()).child(assignmentEntity.getAssignmentName()).child("Total").setValue(assignmentEntity.getTotalScore());
                            for (int j = 0; j < assignmentEntity.getAssignmentSplits().size(); j++) {
                                AssignmentSplit split = assignmentEntity.getAssignmentSplits().get(j);
                                databaseReference.child(courseCode).child(moduleName.getText().toString()).child(assignmentEntity.getAssignmentName()).child("Splits").child(split.getSplitName()).setValue(String.valueOf(split.getSplitScore()));
                            }

                        }

                        Intent serviceIntent = new Intent(getActivity(), ModuleDatabaseUpdationIntentService.class);
                        serviceIntent.putExtra(IntentConstants.MODULE_OLD_NAME,moduleNameString);
                        serviceIntent.putExtra(IntentConstants.MODULE_NEW_NAME,moduleName.getText().toString());
                        serviceIntent.putExtra(IntentConstants.ASSIGNMENT_list,assignmentsList);
                        serviceIntent.putExtra(IntentConstants.COURSE_ID,courseCode);
                        serviceIntent.putExtra(IntentConstants.MODULE_WEIGHTAGE,moduleWeightage.getText().toString());

                        serviceIntent.putExtra(IntentConstants.MODE,"Edit");
                        getActivity().startService(serviceIntent);

                        //ModuleEntity.addKeyValue(moduleName.getText().toString(),key);
                        editDeleteButtonListner.clickButtonEvent();
                    }

                }else{
                    Toast.makeText(getActivity()," Please correct the errors and try again.",Toast.LENGTH_LONG).show();
                    if(moduleName.getText().toString().trim().length() <=0)
                    {
                        moduleName.setError("Module Name cannot be empty");
                    }
                    if(moduleWeightage.getText().toString().trim().length() <=0 ){
                        moduleWeightage.setError("Module weightage cannot be empty");
                    }
                }
            }
        });

        moduleNameString = getArguments().getString(IntentConstants.MODULE_NAME);
        moduleWeightageString = getArguments().getString(IntentConstants.MODULE_WEIGHTAGE);
        moduleWeightage.setText(moduleWeightageString);
        moduleWeightage.setSelection(moduleWeightageString.length());
        moduleName.setText(moduleNameString);
        moduleName.setSelection(moduleName.getText().length());
        courseCode = getArguments().getString(IntentConstants.COURSE_ID);
        loadFromDatabase();

        return layout;
    }

    private void loadFromDatabase() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("modules").child(courseCode).child(moduleNameString).push();
        mDatabase.child("modules").child(courseCode).child(moduleNameString).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("EditDelete"," children : "+dataSnapshot.getChildren()+"  "+dataSnapshot.getChildrenCount());


                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    if(postSnapshot.getKey().equals("isGraded")){
                        isGraded = postSnapshot.getValue(Boolean.class);
                        editButton.setVisibility(View.GONE);

                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT, 2.0f);

                        deleteButton.setLayoutParams(param);
                    }
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ModuleListFragment : "," Read cancelled due to "+databaseError.getMessage());
            }
        });
    }

    private boolean isValidName(String newModuleName, String newModuleWeightage) {
        boolean isValid = true;
        for(String existingName : moduleList){
            if(existingName.equals(newModuleName) && !newModuleName.equals(moduleNameString)){
                moduleName.setError("Duplicate Module Name");
                return false;
            }
        }
        Pattern p = Pattern.compile("[^A-Za-z0-9]");
        Matcher m = p.matcher(newModuleName);
        boolean b = m.find();
        if (b == true){
            moduleName.setError("Module Name should not contain special characters");
            return false;
        }

        if(!Character.isDigit(newModuleWeightage.charAt(0))){
            moduleWeightage.setError("Module Weightage should begin with a number");
            return false;
        }
        return isValid;
    }


  /*  public void initialise(Intent intent) {
        if(intent!=null && intent.getStringExtra(IntentConstants.MODULE_NAME)!=null){
            moduleNameString = intent.getStringExtra(IntentConstants.MODULE_NAME);
            moduleName.setText(moduleNameString);
            moduleName.setSelection(moduleName.getText().length());
            courseCode = intent.getStringExtra(IntentConstants.COURSE_ID);
        }
    }*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            editDeleteButtonListner = (EditDeleteButtonListner) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement OnNewItemAddedListener");
        }
    }


  /*  @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SharedPreferences prefs = getActivity().getSharedPreferences("com.cs442.team4.tahelper.EditDeleteModuleFragment", MODE_PRIVATE);
        boolean isFirstRun = prefs.getBoolean("firstrun", true);
        if (isFirstRun)
        {
            prefs.edit().putBoolean("firstrun", false).commit();
            showZerothShowCase();
        }


    }*/


    private void showZerothShowCase(){
        new ShowcaseView.Builder(getActivity())
                .withMaterialShowcase()
                .setStyle(R.style.CustomShowcaseTheme2)
                .setTarget(new ViewTarget(moduleName))
                .hideOnTouchOutside()
                .setContentTitle("Edit module name here.")
                .setShowcaseEventListener(new SimpleShowcaseEventListener() {

                    @Override
                    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                        showFirstShowCase();
                    }

                })
                .build();
    }

    private void showFirstShowCase(){
        new ShowcaseView.Builder(getActivity())
                .withMaterialShowcase()
                .setStyle(R.style.CustomShowcaseTheme2)
                .setTarget(new ViewTarget(editButton))
                .hideOnTouchOutside()
                .setContentTitle("Click this button after editing module name changes.")
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
                .setTarget(new ViewTarget(deleteButton))
                .hideOnTouchOutside()
                .setContentTitle("Click this button to delete this module.")
                .setShowcaseEventListener(new SimpleShowcaseEventListener() {

                    @Override
                    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                        //showFourthShowCase();
                    }

                })
                .build();
    }


  /*  private void showFourthShowCase() {
        new ShowcaseView.Builder(getActivity())
                .withMaterialShowcase()
                .setStyle(R.style.CustomShowcaseTheme2)
                .setTarget(new ViewTarget(((AddModuleActivity)getActivity()).mDrawerLayout))
                .hideOnTouchOutside()
                .setContentTitle("Swipe from left to launch drawer with navigation options.")
                .build();
    }*/
}
