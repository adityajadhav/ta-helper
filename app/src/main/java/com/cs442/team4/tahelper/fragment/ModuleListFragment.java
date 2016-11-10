package com.cs442.team4.tahelper.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.activity.ModuleListActivity;
import com.cs442.team4.tahelper.listItem.ModuleListItemAdapter;
import com.cs442.team4.tahelper.model.AssignmentEntity;
import com.cs442.team4.tahelper.model.AssignmentSplit;
import com.cs442.team4.tahelper.model.ModuleEntity;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.SimpleShowcaseEventListener;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.cs442.team4.tahelper.showcase.StandardShowcaseDrawer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sowmyaparameshwara on 10/30/16.
 */

public class ModuleListFragment extends Fragment{

    private ListView moduleListView;
    private ArrayList<String> moduleItemList;
    private ModuleListItemAdapter moduleListItemAdapter;
    private Button addModuleButton;
    private Button backButton;
    private RelativeLayout loadingLayout;

    private ModuleListFragmentListener moduleListFragmentListener;
    private DatabaseReference mDatabase;

    float historicX = Float.NaN, historicY = Float.NaN;
    static final int DELTA = 50;
    enum Direction {LEFT, RIGHT;}



    public interface ModuleListFragmentListener{
        public void addNewModuleEvent(View view);
        public void notifyBackButtonEvent(View view);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.module_list_fragment,container,false);
        moduleListView = (ListView) view.findViewById(R.id.moduleListFragmentView);
        addModuleButton = (Button)view.findViewById(R.id.moduleListFragmentButtonView);
        backButton = (Button) view.findViewById(R.id.moduleListFragmentBackButton);
        loadingLayout = (RelativeLayout) view.findViewById(R.id.loadingPanel);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                moduleListFragmentListener.notifyBackButtonEvent(backButton);
            }
        });

        addModuleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moduleListFragmentListener.addNewModuleEvent(addModuleButton);
            }
        });
       // registerListChangeListener();

        swipeDeletionListView();
        loadPredefinedModules();
        return view;
    }

    private void swipeDeletionListView() {
      /*  moduleListView.setOnDragListener(new View.OnDragListener(){

            @Override
            public boolean onDrag(View v, DragEvent event) {
                return false;
            }
        });*/
   /*     moduleListView.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        historicX = event.getX();
                        historicY = event.getY();
                        break;

                    case MotionEvent.ACTION_UP:
                        if (event.getX() - historicX < -DELTA) {
                           // FunctionDeleteRowWhenSlidingLeft();
                            return true;
                        }
                        else if (event.getX() - historicX > DELTA) {
                            //FunctionDeleteRowWhenSlidingRight();
                            return true;
                        }
                        break;

                    default:
                        return false;
                }
                return false;
            }
        });*/
    }

    private void registerListChangeListener() {
        mDatabase.child("modules").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String moduleName = (String) postSnapshot.getValue();
                    if(!moduleItemList.contains(moduleName)){
                        moduleItemList.add(moduleName);
                    }
                }
                moduleListItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Map<String,String> keyValue = (Map<String, String>) postSnapshot.getValue();
                    Iterator it = keyValue.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry)it.next();
                        moduleItemList.remove(previousChildName);
                        moduleItemList.add((String) pair.getValue());
                    }
                }
                moduleListItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Map<String,String> keyValue = (Map<String, String>) postSnapshot.getValue();
                    Iterator it = keyValue.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry)it.next();
                        moduleItemList.remove((String) pair.getValue());
                    }
                }
                moduleListItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadPredefinedModules() {
        loadFromDatabase();
        moduleItemList = new ArrayList<String>();
        moduleListItemAdapter = new ModuleListItemAdapter(getActivity(),R.layout.module_item_layout,moduleItemList);
        moduleListView.setAdapter(moduleListItemAdapter);
    }

    private void loadFromDatabase() {
        mDatabase.child("modules").push();

        mDatabase.child("modules").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loadingLayout.setVisibility(View.GONE);
                moduleItemList.removeAll(moduleItemList);

                Log.i("","Snaphot "+dataSnapshot+"  "+dataSnapshot.getChildren()+"  "+dataSnapshot.getValue());
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    if(!moduleItemList.contains(postSnapshot.getKey())) {
                        moduleItemList.add((String)postSnapshot.getKey());
                        ModuleEntity.addModule((String)postSnapshot.getKey());
                        Log.i("ModuleList","value "+postSnapshot.getValue());
                        if(postSnapshot.getValue()!=null && postSnapshot.getValue()!="" && !postSnapshot.getValue().getClass().equals(String.class)) {
                            HashMap assignmentsMap = (HashMap) postSnapshot.getValue();
                            Iterator entries = assignmentsMap.entrySet().iterator();
                            while (entries.hasNext()) {
                                AssignmentEntity assignmentEntity = new AssignmentEntity();
                                ArrayList<AssignmentSplit> splitsList = new ArrayList<AssignmentSplit>();
                                assignmentEntity.setAssignmentSplits(splitsList);
                                Map.Entry thisEntry = (Map.Entry) entries.next();
                                String key = (String) thisEntry.getKey();
                                assignmentEntity.setAssignmentName(key);
                                HashMap value = (HashMap) thisEntry.getValue();
                                Iterator entries1 = value.entrySet().iterator();
                                while (entries1.hasNext()) {
                                    Map.Entry thisEntry1 = (Map.Entry) entries1.next();
                                    String key1 = (String) thisEntry1.getKey();
                                    if (key1.equals("Total")) {
                                        assignmentEntity.setTotalScore((String) thisEntry1.getValue());
                                    } else if (key1.equals("Splits")) {
                                        Map<String, String> splitList = (Map<String, String>) thisEntry1.getValue();
                                        Iterator entries2 = splitList.entrySet().iterator();
                                        while (entries2.hasNext()) {
                                            Map.Entry thisEntry2 = (Map.Entry) entries2.next();
                                            splitsList.add(new AssignmentSplit((String) thisEntry2.getKey(), Integer.parseInt((String) thisEntry2.getValue())));
                                        }

                                    }
                                }
                                ModuleEntity.addAssignments((String) postSnapshot.getKey(), assignmentEntity);

                            }
                        }

                    }
                }
                moduleListItemAdapter.notifyDataSetChanged();
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
            moduleListFragmentListener = (ModuleListFragmentListener) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement OnNewItemAddedListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SharedPreferences  prefs = getActivity().getSharedPreferences("com.cs442.team4.tahelper", MODE_PRIVATE);
        boolean isFirstRun = prefs.getBoolean("firstrun", true);
        if (isFirstRun)
        {
            prefs.edit().putBoolean("firstrun", false).commit();
            showFirstShowCase();
        }


    }

    private void showFirstShowCase(){

        new ShowcaseView.Builder(getActivity())
                .withMaterialShowcase()
                .setStyle(R.style.CustomShowcaseTheme2)
                .setTarget(new ViewTarget(addModuleButton))
                .hideOnTouchOutside()
                .setContentTitle("Click the button to add a new module.")
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
                .setTarget(new ViewTarget(backButton))
                .hideOnTouchOutside()
                .setContentTitle("Click the button to go back to course list.")
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
                .setTarget(new ViewTarget(moduleListView.getChildAt(0).findViewById(R.id.editModuleButtonView)))
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
                .setTarget(new ViewTarget(moduleListView.getChildAt(0).findViewById(R.id.manageModuleButtonView)))
                .hideOnTouchOutside()
                .setContentTitle("Click to see the sub modules of this module.")
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
                .setTarget(new ViewTarget(((ModuleListActivity)getActivity()).mDrawerLayout))
                .hideOnTouchOutside()
                .setContentTitle("Swipe from left to launch drawer with navigation options.")
                .setShowcaseEventListener(new SimpleShowcaseEventListener() {

                    @Override
                    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                        //showFifthShowCase();
                    }

                })
                .build();




    }














}
