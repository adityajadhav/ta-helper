package com.cs442.team4.tahelper.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.cs442.team4.tahelper.contants.IntentConstants;
import com.cs442.team4.tahelper.model.AssignmentEntity;
import com.cs442.team4.tahelper.model.AssignmentSplit;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by sowmyaparameshwara on 11/7/16.
 */

public class ModuleDatabaseUpdationIntentService extends IntentService {

    private DatabaseReference mDatabase;

    public ModuleDatabaseUpdationIntentService() {
        super("ModuleDatabaseUpdationIntentService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ModuleDatabaseUpdationIntentService(String name) {
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
        ModuleDatabaseUpdationIntentService.DatabaseListener dblistener = new ModuleDatabaseUpdationIntentService.DatabaseListener(intent);
        dblistener.setInvokedByService(true);
        mDatabase.child("students").addValueEventListener(dblistener);
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
                if(mode.equals("Add")) {
                    String moduleName = intent.getStringExtra(IntentConstants.MODULE_NAME);
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        mDatabase.child("students").child(postSnapshot.getKey()).child("CS442").child(moduleName).setValue("");
                    }
                }else if(mode.equals("Edit")){
                    String moduleOldName = intent.getStringExtra(IntentConstants.MODULE_OLD_NAME);
                    String moduleNewName = intent.getStringExtra(IntentConstants.MODULE_NEW_NAME);
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        FirebaseDatabase.getInstance().getReference("students/"+postSnapshot.getKey()+"/CS442/"+moduleOldName).removeValue();
                        mDatabase.child("students").child(postSnapshot.getKey()).child("CS442").child(moduleNewName).setValue("");

                        ArrayList<AssignmentEntity> assignmentEntityArrayList = intent.getParcelableArrayListExtra(IntentConstants.ASSIGNMENT_list);
                        for(int i =0 ; i < assignmentEntityArrayList.size();i++){
                            AssignmentEntity assignmentEntity = assignmentEntityArrayList.get(i);
                            mDatabase.child("students").child(postSnapshot.getKey()).child("CS442").child(moduleNewName).child(assignmentEntity.getAssignmentName()).child("Total").setValue(assignmentEntity.getTotalScore());
                            for (int j = 0; j < assignmentEntity.getAssignmentSplits().size(); j++) {
                                AssignmentSplit split = assignmentEntity.getAssignmentSplits().get(j);
                                Log.i("AssignmentsUpdation", "j : " + j + "split  " + split.getSplitName());
                                mDatabase.child("students").child(postSnapshot.getKey()).child("CS442").child(moduleNewName).child(assignmentEntity.getAssignmentName()).child("Splits").child(split.getSplitName()).setValue(String.valueOf(split.getSplitScore()));
                            }
                        }

                    }

                }else if(mode.equals("Delete")){
                    String moduleName = intent.getStringExtra(IntentConstants.MODULE_NAME);
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        FirebaseDatabase.getInstance().getReference("students/"+postSnapshot.getKey()+"/CS442/"+moduleName).removeValue();
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