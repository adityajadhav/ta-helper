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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.activity.ManageAssignmentsActivity;
import com.cs442.team4.tahelper.activity.ModuleListActivity;
import com.cs442.team4.tahelper.contants.IntentConstants;
import com.cs442.team4.tahelper.listItem.ManageAssignmentsListItemAdapter;
import com.cs442.team4.tahelper.listItem.ModuleListItemAdapter;
import com.cs442.team4.tahelper.model.AssignmentEntity;
import com.cs442.team4.tahelper.model.ModuleEntity;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.SimpleShowcaseEventListener;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sowmyaparameshwara on 10/31/16.
 */

public class ManageAssignmentsFragment extends Fragment {

    private ListView manageAssignmentsList;
    private DatabaseReference mDatabase;
    private ArrayList<String> assignmentsList;
    private ManageAssignmentsListItemAdapter manageAssignmentsAdapter;
    private String moduleName;
    private String courseCode;
   // private TextView assignmentName;
    private Button addAssignmentButton;
    private ManageAssignmentFragmentListener manageAssignmentFragmentListener;
    //private Button backButton;
    private RelativeLayout loadingLayout;

    public interface ManageAssignmentFragmentListener{
        public void notifyAddAssignmentEvent(ArrayList<String> assignmentsList);
        //public void notifyBackButton();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout= (LinearLayout) inflater.inflate(R.layout.manage_assignments_fragment,container,false);
        manageAssignmentsList = (ListView) layout.findViewById(R.id.manageAssignmentsFragmentList);
       // assignmentName = (TextView) layout.findViewById(R.id.manageAssignmentsFragmentTextView);
        addAssignmentButton = (Button) layout.findViewById(R.id.manageAssignmentsFragmentAddAssignmentButton);
        //backButton = (Button) layout.findViewById(R.id.manageAssignmentsFragmentBackButton);
        loadingLayout = (RelativeLayout) layout.findViewById(R.id.loadingPanelManageAssignments);

        mDatabase = FirebaseDatabase.getInstance().getReference();


       /* backButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                manageAssignmentFragmentListener.notifyBackButton();
            }
        });*/

        addAssignmentButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                manageAssignmentFragmentListener.notifyAddAssignmentEvent(assignmentsList);
            }
        });

        moduleName = getArguments().getString(IntentConstants.MODULE_NAME);
        courseCode = getArguments().getString(IntentConstants.COURSE_ID);
        addAssignmentButton.setText(" Add "+moduleName+" Sub Module ");
        loadExistingAssignmentFromDatabase();


        return layout;
    }

    private void loadExistingAssignmentFromDatabase() {
        assignmentsList = new ArrayList<String>();
        loadFromDatabase();
        manageAssignmentsAdapter = new ManageAssignmentsListItemAdapter(getActivity(),R.layout.manage_assignments_item_layout,assignmentsList);
        manageAssignmentsList.setAdapter(manageAssignmentsAdapter);
    }

    private void loadFromDatabase() {
        mDatabase.child("modules").child(courseCode).child(moduleName).push();
        mDatabase.child("modules").child(courseCode).child(moduleName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loadingLayout.setVisibility(View.GONE);
                assignmentsList.removeAll(assignmentsList);
                Log.i("","Snaphot "+dataSnapshot+"  "+dataSnapshot.getChildren()+"  "+dataSnapshot.getValue());
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    if(!postSnapshot.getKey().equals("weightage") && !postSnapshot.getKey().equals("isGraded") && !assignmentsList.contains(postSnapshot.getKey())) {
                        assignmentsList.add((String)postSnapshot.getKey());
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
            manageAssignmentFragmentListener = (ManageAssignmentFragmentListener) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement OnNewItemAddedListener");
        }
    }

  /*  @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SharedPreferences prefs = getActivity().getSharedPreferences("com.cs442.team4.tahelper.ManageAssignmentsFragment", MODE_PRIVATE);
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
                .setTarget(new ViewTarget(addAssignmentButton))
                .hideOnTouchOutside()
                .setContentTitle("Click the button to add a new sub module.")
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
                .setTarget(new ViewTarget(manageAssignmentsList.getChildAt(0).findViewById(R.id.manageAsignmentsManageButton)))
                .hideOnTouchOutside()
                .setContentTitle("Click to edit the sub module details.")
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
                .setTarget(new ViewTarget(manageAssignmentsList.getChildAt(0).findViewById(R.id.manageAssignmentsScoreButton)))
                .hideOnTouchOutside()
                .setContentTitle("Click to grade the students for the sub module.")
                .setShowcaseEventListener(new SimpleShowcaseEventListener() {

                    @Override
                    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                       // showFifthShowCase();
                    }

                })
                .build();
    }

/*    private void showFifthShowCase() {
        new ShowcaseView.Builder(getActivity())
                .withMaterialShowcase()
                .setStyle(R.style.CustomShowcaseTheme2)
                .setTarget(new ViewTarget(((ManageAssignmentsActivity)getActivity()).mDrawerLayout))
                .hideOnTouchOutside()
                .setContentTitle("Swipe from left to launch drawer with navigation options.")
                .build();




    }*/

}
