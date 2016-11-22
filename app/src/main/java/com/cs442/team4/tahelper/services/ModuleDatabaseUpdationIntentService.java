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
    private String courseCode;

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
        if(intent.getStringExtra(IntentConstants.COURSE_ID)!=null) {
            courseCode = intent.getStringExtra(IntentConstants.COURSE_ID);
            mDatabase.child("students").child(courseCode).push();
            ModuleDatabaseUpdationIntentService.DatabaseListener dblistener = new ModuleDatabaseUpdationIntentService.DatabaseListener(intent);
            dblistener.setInvokedByService(true);
            mDatabase.child("students").child(courseCode).addValueEventListener(dblistener);
        }
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
                    String courseName = intent.getStringExtra(IntentConstants.COURSE_ID);
                    String moduleWeightage = intent.getStringExtra(IntentConstants.MODULE_WEIGHTAGE);
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        mDatabase.child("students").child(courseName).child(postSnapshot.getKey()).child(moduleName).child("weightage").setValue(moduleWeightage);
                    }
                }else if(mode.equals("Edit")){
                    String moduleOldName = intent.getStringExtra(IntentConstants.MODULE_OLD_NAME);
                    String moduleNewName = intent.getStringExtra(IntentConstants.MODULE_NEW_NAME);
                    String courseName = intent.getStringExtra(IntentConstants.COURSE_ID);
                    String moduleWeightage = intent.getStringExtra(IntentConstants.MODULE_WEIGHTAGE);

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        FirebaseDatabase.getInstance().getReference("students/"+courseName+"/"+postSnapshot.getKey()+"/"+moduleOldName).removeValue();
                        mDatabase.child("students").child(courseName).child(postSnapshot.getKey()).child(moduleNewName).child("weightage").setValue(moduleWeightage);

                        ArrayList<AssignmentEntity> assignmentEntityArrayList = intent.getParcelableArrayListExtra(IntentConstants.ASSIGNMENT_list);
                        for(int i =0 ; i < assignmentEntityArrayList.size();i++){
                            AssignmentEntity assignmentEntity = assignmentEntityArrayList.get(i);
                            mDatabase.child("students").child(courseName).child(postSnapshot.getKey()).child(moduleNewName).child(assignmentEntity.getAssignmentName()).child("Total").setValue(assignmentEntity.getTotalScore());
                            for (int j = 0; j < assignmentEntity.getAssignmentSplits().size(); j++) {
                                AssignmentSplit split = assignmentEntity.getAssignmentSplits().get(j);
                                Log.i("AssignmentsUpdation", "j : " + j + "split  " + split.getSplitName());
                                mDatabase.child("students").child(courseName).child(postSnapshot.getKey()).child(moduleNewName).child(assignmentEntity.getAssignmentName()).child("Splits").child(split.getSplitName()).setValue(String.valueOf(split.getSplitScore()));
                            }
                        }

                    }

                }else if(mode.equals("Delete")){
                    String moduleName = intent.getStringExtra(IntentConstants.MODULE_NAME);
                    String courseName = intent.getStringExtra(IntentConstants.COURSE_ID);
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        FirebaseDatabase.getInstance().getReference("students/"+courseName+"/"+postSnapshot.getKey()+"/"+moduleName).removeValue();
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