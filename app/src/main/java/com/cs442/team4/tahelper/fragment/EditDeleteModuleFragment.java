package com.cs442.team4.tahelper.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.contants.IntentConstants;
import com.cs442.team4.tahelper.model.ModuleEntity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Map;

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
                 FirebaseDatabase.getInstance().getReference("modules/"+ModuleEntity.getDBKey(moduleNameString)).removeValue();
                  ModuleEntity.removeKeyValue(moduleNameString);
                editDeleteButtonListner.clickButtonEvent();

            }
        });
        editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(moduleName.getText().toString()!=null && moduleName.getText().toString().length()>0) {

                    FirebaseDatabase.getInstance().getReference("modules/"+ModuleEntity.getDBKey(moduleNameString)).removeValue();
                    ModuleEntity.removeKeyValue(moduleNameString);

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("modules");
                    String key  = databaseReference.push().getKey();
                    databaseReference.child(key).child("name").setValue(moduleName.getText().toString());
                    ModuleEntity.addKeyValue(moduleName.getText().toString(),key);
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
