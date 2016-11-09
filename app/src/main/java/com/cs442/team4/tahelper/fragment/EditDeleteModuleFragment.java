package com.cs442.team4.tahelper.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.contants.IntentConstants;
import com.cs442.team4.tahelper.model.AssignmentEntity;
import com.cs442.team4.tahelper.model.AssignmentSplit;
import com.cs442.team4.tahelper.model.ModuleEntity;
import com.cs442.team4.tahelper.services.AssignmentsDatabaseUpdationService;
import com.cs442.team4.tahelper.services.ModuleDatabaseUpdationIntentService;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by sowmyaparameshwara on 10/30/16.
 */

public class EditDeleteModuleFragment extends Fragment {

    private EditText moduleName;
    private Button editButton;
    private Button deleteButton;
    private String moduleNameString;
    private EditDeleteButtonListner editDeleteButtonListner;

    public interface EditDeleteButtonListner{
        public void clickButtonEvent();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout= (LinearLayout) inflater.inflate(R.layout.edit_delete_module_fragment,container,false);
        moduleName = (EditText) layout.findViewById(R.id.editDeleteModuleNameFragmentEditTextView);
        editButton = (Button)layout.findViewById(R.id.editModuleButtonFragmentView);
        deleteButton = (Button)layout.findViewById(R.id.deleteModuleButtonFragmentView);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 FirebaseDatabase.getInstance().getReference("modules/"+moduleNameString).removeValue();
                ModuleEntity.removeModule(moduleNameString);
                Intent serviceIntent = new Intent(getActivity(), ModuleDatabaseUpdationIntentService.class);
                serviceIntent.putExtra(IntentConstants.MODULE_NAME,moduleNameString);
                serviceIntent.putExtra(IntentConstants.MODE,"Delete");
                getActivity().startService(serviceIntent);

                //  ModuleEntity.removeKeyValue(moduleNameString);
                editDeleteButtonListner.clickButtonEvent();

            }
        });
        editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(moduleName.getText().toString()!=null && moduleName.getText().toString().length()>0) {
                    ModuleEntity.editModule(moduleNameString,moduleName.getText().toString());

                    FirebaseDatabase.getInstance().getReference("modules/"+moduleNameString).removeValue();

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("modules");
                   // String key  = databaseReference.push().getKey();
                    //databaseReference.child(key).child("name").setValue(moduleName.getText().toString());
                    databaseReference.child(moduleName.getText().toString()).setValue("");
                    ArrayList<AssignmentEntity> assignmentsList = ModuleEntity.getAssignmentList(moduleName.getText().toString());
                    for(int i = 0 ; i <assignmentsList.size(); i++){
                        AssignmentEntity assignmentEntity = assignmentsList.get(i);

                        databaseReference.child(moduleName.getText().toString()).child(assignmentEntity.getAssignmentName()).child("Total").setValue(assignmentEntity.getTotalScore());
                        for (int j = 0; j < assignmentEntity.getAssignmentSplits().size(); j++) {
                            AssignmentSplit split = assignmentEntity.getAssignmentSplits().get(j);
                            databaseReference.child(moduleName.getText().toString()).child(assignmentEntity.getAssignmentName()).child("Splits").child(split.getSplitName()).setValue(String.valueOf(split.getSplitScore()));
                        }

                    }

                    Intent serviceIntent = new Intent(getActivity(), ModuleDatabaseUpdationIntentService.class);
                    serviceIntent.putExtra(IntentConstants.MODULE_OLD_NAME,moduleNameString);
                    serviceIntent.putExtra(IntentConstants.MODULE_NEW_NAME,moduleName.getText().toString());
                    serviceIntent.putExtra(IntentConstants.ASSIGNMENT_list,assignmentsList);

                    serviceIntent.putExtra(IntentConstants.MODE,"Edit");
                    getActivity().startService(serviceIntent);

                    //ModuleEntity.addKeyValue(moduleName.getText().toString(),key);
                    editDeleteButtonListner.clickButtonEvent();
                }
            }
        });

        return layout;
    }

    public void initialise(Intent intent) {
        if(intent!=null && intent.getStringExtra(IntentConstants.MODULE_NAME)!=null){
            moduleNameString = intent.getStringExtra(IntentConstants.MODULE_NAME);
            moduleName.setText(moduleNameString);
            moduleName.setSelection(moduleName.getText().length());
        }
    }

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
}
