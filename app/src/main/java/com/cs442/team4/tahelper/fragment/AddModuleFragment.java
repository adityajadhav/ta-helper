package com.cs442.team4.tahelper.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.Toast;

import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.activity.AddModuleActivity;
import com.cs442.team4.tahelper.activity.ModuleListActivity;
import com.cs442.team4.tahelper.contants.IntentConstants;
import com.cs442.team4.tahelper.model.ModuleEntity;
import com.cs442.team4.tahelper.services.ModuleDatabaseUpdationIntentService;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.SimpleShowcaseEventListener;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sowmyaparameshwara on 10/30/16.
 */

public class AddModuleFragment extends Fragment{

    private EditText enterModuleNameFragmentView;
    private Button addModuleFragmentView;
    private Button backButton;
    private DatabaseReference mDatabase;
    private AddModuleFragmentListener addModuleFragmentListener;
    private String courseName;


    public interface AddModuleFragmentListener{
        public void addModuleEvent();
        public void backToModuleList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout= (LinearLayout) inflater.inflate(R.layout.add_module_fragment,container,false);
        enterModuleNameFragmentView = (EditText) layout.findViewById(R.id.enterModuleNameFragmentView);
        addModuleFragmentView = (Button)layout.findViewById(R.id.addModuleButtonFragmentView);
        backButton = (Button) layout.findViewById(R.id.addModuleButtonFragmentViewBackButton);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                addModuleFragmentListener.backToModuleList();
            }
        });

        addModuleFragmentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String moduleName = enterModuleNameFragmentView.getText().toString();
                if(moduleName!=null && moduleName.length()>0){
                    String key; /* = mDatabase.push().getKey();
                    mDatabase.child("modules").child(key).child("name").setValue(moduleName);
                    ModuleEntity.addKeyValue("InClass Assignments",key);
                    addModuleFragmentListener.addModuleEvent();;

                     key  = mDatabase.push().getKey();
                    mDatabase.child("modules").child(key).child("name").setValue("InClass Assignments");
                    ModuleEntity.addKeyValue(moduleName,key);
                    addModuleFragmentListener.addModuleEvent();;

                     key  = mDatabase.push().getKey();
                    mDatabase.child("modules").child(key).child("name").setValue("HW Assignments");
                    ModuleEntity.addKeyValue("HW Assignments",key);
                    addModuleFragmentListener.addModuleEvent();;

                     key  = mDatabase.push().getKey();
                    mDatabase.child("modules").child(key).child("name").setValue("Project");
                    ModuleEntity.addKeyValue("Project",key);
                    addModuleFragmentListener.addModuleEvent();;

                     key  = mDatabase.push().getKey();
                    mDatabase.child("modules").child(key).child("name").setValue("Exam");
                    ModuleEntity.addKeyValue("Exam",key);
                    addModuleFragmentListener.addModuleEvent();;

                     key  = mDatabase.push().getKey();
                    mDatabase.child("modules").child(key).child("name").setValue("Final Score");
                    ModuleEntity.addKeyValue("Final Score",key);
                    addModuleFragmentListener.addModuleEvent();;
*/
             /*    mDatabase.child("modules").child("InClass Assignments").setValue("");
                    mDatabase.child("modules").child("HW Assignments").setValue("");
                    mDatabase.child("modules").child("Project").setValue("");
                    mDatabase.child("modules").child("Exam").setValue("");
                    mDatabase.child("modules").child("Final Score").setValue("");*/
                    // key  = mDatabase.push().getKey();
                  //  mDatabase.child("modules").child(key).child("name").setValue(moduleName);
                    mDatabase.child("modules").child(courseName).child(moduleName).setValue("");
                    ModuleEntity.addModule(moduleName);


                    Intent serviceIntent = new Intent(getActivity(), ModuleDatabaseUpdationIntentService.class);
                    serviceIntent.putExtra(IntentConstants.MODULE_NAME,moduleName);
                    serviceIntent.putExtra(IntentConstants.MODE,"Add");
                    serviceIntent.putExtra(IntentConstants.COURSE_ID,courseName);

                    getActivity().startService(serviceIntent);

                    //ModuleEntity.addKeyValue(moduleName,key);
                    addModuleFragmentListener.addModuleEvent();;

                }else{
                    Toast.makeText(getActivity()," Please enter module name and try again.",Toast.LENGTH_LONG).show();
                }

            }
        });
        return layout;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            addModuleFragmentListener = (AddModuleFragmentListener) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement OnNewItemAddedListener");
        }
    }


/*    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SharedPreferences prefs = getActivity().getSharedPreferences("com.cs442.team4.tahelper.AddModuleFragment", MODE_PRIVATE);
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
                .setTarget(new ViewTarget(enterModuleNameFragmentView))
                .hideOnTouchOutside()
                .setContentTitle("Enter name for the new module to be added.")
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
                .setTarget(new ViewTarget(addModuleFragmentView))
                .hideOnTouchOutside()
                .setContentTitle("Click the button to add the new module.")
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
                .setTarget(new ViewTarget(backButton))
                .hideOnTouchOutside()
                .setContentTitle("Click to go back to module list without adding.")
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
                .setTarget(new ViewTarget(((AddModuleActivity)getActivity()).mDrawerLayout))
                .hideOnTouchOutside()
                .setContentTitle("Swipe from left to launch drawer with navigation options.")
                .build();
    }

    public void initialise(Intent intent) {
        if(intent!=null && intent.getStringExtra(IntentConstants.COURSE_ID)!=null){
            courseName = intent.getStringExtra(IntentConstants.COURSE_ID);
        }
    }





}
