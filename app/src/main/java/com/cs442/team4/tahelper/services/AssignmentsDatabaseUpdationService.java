package com.cs442.team4.tahelper.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.cs442.team4.tahelper.contants.IntentConstants;
import com.cs442.team4.tahelper.model.AssignmentSplit;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by sowmyaparameshwara on 11/8/16.
 */

public class AssignmentsDatabaseUpdationService extends IntentService{

    private DatabaseReference mDatabase;

    public AssignmentsDatabaseUpdationService() {
        super("AssignmentsDatabaseUpdationService");
    }


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AssignmentsDatabaseUpdationService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        mDatabase.child("students").push();
        DatabaseListener dbListsner = new DatabaseListener(intent);
        dbListsner.setInvokedByService(true);
        mDatabase.child("students").addValueEventListener(dbListsner);
    }

    class DatabaseListener implements ValueEventListener{
        Intent intent;
        boolean isInvokedByService;

        DatabaseListener(Intent intent){
            this.intent = intent;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(isInvokedByService) {
                isInvokedByService = false;
                String mode = intent.getStringExtra(IntentConstants.MODE);
                if (mode.equals("Add")) {
                    String moduleName = intent.getStringExtra(IntentConstants.MODULE_NAME);
                    String assignmentName = intent.getStringExtra(IntentConstants.ASSIGNMENT_NAME);
                    String total = intent.getStringExtra(IntentConstants.TOTAL);
                    ArrayList<AssignmentSplit> assignmentSplitsList = intent.getParcelableArrayListExtra(IntentConstants.SPLIT);

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        mDatabase.child("students").child(postSnapshot.getKey()).child("CS442").child(moduleName).child(assignmentName).child("Total").setValue(total);
                        for (int i = 0; i < assignmentSplitsList.size(); i++) {
                            AssignmentSplit split = assignmentSplitsList.get(i);
                            mDatabase.child("students").child(postSnapshot.getKey()).child("CS442").child(moduleName).child(assignmentName).child("Splits").child(split.getSplitName()).setValue(String.valueOf(split.getSplitScore()));
                        }
                    }
                } else if (mode.equals("Edit")) {
                    String moduleName = intent.getStringExtra(IntentConstants.MODULE_NAME);
                    String assignmentOldName = intent.getStringExtra(IntentConstants.ASSIGNMENT_OLD_NAME);
                    String assignmentNewName = intent.getStringExtra(IntentConstants.ASSIGNMENT_NEW_NAME);
                    String total = intent.getStringExtra(IntentConstants.TOTAL);
                    ArrayList<AssignmentSplit> assignmentSplitsList = intent.getParcelableArrayListExtra(IntentConstants.SPLIT);

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Log.i("AssignmentsUpdation", "Edit : " + postSnapshot.getKey());
                        mDatabase.child("students").child(postSnapshot.getKey()).child("CS442").child(moduleName).child(assignmentOldName).removeValue();
                        mDatabase.child("students").child(postSnapshot.getKey()).child("CS442").child(moduleName).child(assignmentNewName).child("Total").setValue(total);
                        for (int i = 0; i < assignmentSplitsList.size(); i++) {
                            AssignmentSplit split = assignmentSplitsList.get(i);
                            Log.i("AssignmentsUpdation", "i : " + i + "split  " + split.getSplitName());
                            mDatabase.child("students").child(postSnapshot.getKey()).child("CS442").child(moduleName).child(assignmentNewName).child("Splits").child(split.getSplitName()).setValue(String.valueOf(split.getSplitScore()));
                        }
                    }
                } else if (mode.equals("Delete")) {
                    String moduleName = intent.getStringExtra(IntentConstants.MODULE_NAME);
                    String assignmentName = intent.getStringExtra(IntentConstants.ASSIGNMENT_NAME);
                    boolean retainModuleName = intent.getBooleanExtra(IntentConstants.RETAIN_MODULE_NAME,false);
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        mDatabase.child("students").child(postSnapshot.getKey()).child("CS442").child(moduleName).child(assignmentName).removeValue();
                        if(retainModuleName){
                            mDatabase.child("students").child(postSnapshot.getKey()).child("CS442").child(moduleName).setValue("");
                        }
                    }
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

        public void setInvokedByService(boolean invokedByService) {
            this.isInvokedByService = invokedByService;
        }
    }
}