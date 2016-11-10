package com.cs442.team4.tahelper.listItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.activity.ManageAssignmentsActivity;
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
        final String assignmentContent = getItem(position);
        TextView assignmentName = (TextView) layout.findViewById(R.id.manageAssignmentNameItem);
        ImageButton manageButton = (ImageButton)layout.findViewById(R.id.manageAsignmentsManageButton);
        ImageButton scoreButton = (ImageButton)layout.findViewById(R.id.manageAssignmentsScoreButton);

        manageButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                ((ManageAssignmentsActivity)context).onAssignmentItemClickEditDelete(assignmentContent);
            }
        });
        scoreButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               ((ManageAssignmentsActivity)context).onAssignmentItemClickScore(assignmentContent);
            }
        });

        assignmentName.setText(assignmentContent);

        return layout;
    }


}
