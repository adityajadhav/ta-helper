package com.cs442.team4.tahelper.listItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.activity.ModuleListActivity;

import java.util.ArrayList;

/**
 * Created by sowmyaparameshwara on 10/31/16.
 */

public class ManageAssignmentsListItemAdapter extends ArrayAdapter<String>{

    private int resource;
    private Context context;


    public ManageAssignmentsListItemAdapter(Context context, int resource, ArrayList<String> manageAssignmentsList) {
        super(context, resource,manageAssignmentsList);
        this.resource = resource;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LinearLayout layout= null;

        if (convertView == null) {
            layout = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li;
            li = (LayoutInflater)getContext().getSystemService(inflater);
            li.inflate(resource, layout, true);
        } else {
            layout = (LinearLayout) convertView;
        }
        final String moduleContent = getItem(position);
        TextView moduleName = (TextView) layout.findViewById(R.id.manageAssignmentNameItem);
        Button manageButton = (Button)layout.findViewById(R.id.manageAsignmentsManageButton);
        Button scoreButton = (Button)layout.findViewById(R.id.manageAssignmentsScoreButton);

        manageButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
               // ((ModuleListActivity)context).onModuleItemClickEditDelete(moduleContent);
            }
        });
        scoreButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               // ((ModuleListActivity)context).onModuleItemClickedManage(moduleContent);
            }
        });

        moduleName.setText(moduleContent);

        return layout;
    }


}
