package com.cs442.team4.tahelper.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.listItem.ModuleListItemAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

/**
 * Created by sowmyaparameshwara on 10/30/16.
 */

public class ModuleListFragment extends Fragment{

    private ListView moduleListView;
    private ArrayList<String> moduleItemList;
    private ModuleListItemAdapter moduleListItemAdapter;
    private Button addModuleButton;
    private ModuleListFragmentListener moduleListFragmentListener;
    private DatabaseReference mDatabase;

    public interface ModuleListFragmentListener{
        public void addNewModuleEvent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.module_list_fragment,container,false);
        moduleListView = (ListView) view.findViewById(R.id.moduleListFragmentView);
        addModuleButton = (Button)view.findViewById(R.id.moduleListFragmentButtonView);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        addModuleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moduleListFragmentListener.addNewModuleEvent();
            }
        });
        loadPredefinedModules();
        return view;
    }

    private void loadPredefinedModules() {
        moduleItemList = new ArrayList<String>();


        Query q = mDatabase.child("modules").orderByKey();

        String[] items = {"InClass Assignments","HW Assignments","Project","Exam","Final Score"};
        for(int i = 0 ; i< items.length; i++){
            moduleItemList.add(items[i]);
        }
        moduleListItemAdapter = new ModuleListItemAdapter(getActivity(),R.layout.module_item_layout,moduleItemList);
        moduleListView.setAdapter(moduleListItemAdapter);
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
}
