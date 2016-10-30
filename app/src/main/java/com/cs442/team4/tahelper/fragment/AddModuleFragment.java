package com.cs442.team4.tahelper.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Button;

import com.cs442.team4.tahelper.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by sowmyaparameshwara on 10/30/16.
 */

public class AddModuleFragment extends Fragment{

    private EditText enterModuleNameFragmentView;
    private Button addModuleFragmentView;
    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout= (LinearLayout) inflater.inflate(R.layout.add_module_fragment,container,false);
        enterModuleNameFragmentView = (EditText) layout.findViewById(R.id.enterModuleNameFragmentView);
        addModuleFragmentView = (Button)layout.findViewById(R.id.addModuleButtonFragmentView);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        addModuleFragmentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String moduleName = enterModuleNameFragmentView.getText().toString();
                if(moduleName!=null && moduleName.length()>0){
                    mDatabase.child("modules").push().child("name").setValue("InClass Assignments");
                    mDatabase.child("modules").push().child("name").setValue("HW Assignments");
                    mDatabase.child("modules").push().child("name").setValue("Project");
                    mDatabase.child("modules").push().child("name").setValue("Exam");
                    mDatabase.child("modules").push().child("name").setValue("Final Score");

                    mDatabase.child("modules").push().child("name").setValue(moduleName);
                }

            }
        });
        return layout;
    }

}
