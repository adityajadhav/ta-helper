package com.cs442.team4.tahelper.listItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.*;

import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.activity.ModuleListActivity;

import java.util.ArrayList;

/**
 * Created by sowmyaparameshwara on 10/30/16.
 */

public class ModuleListItemAdapter extends ArrayAdapter<String>{

    private int resource;
    private Context context;
    ArrayList<String> moduleItemList;

    public ModuleListItemAdapter(Context context, int resource, ArrayList<String> moduleItemList) {
        super(context, resource,moduleItemList);
        this.resource = resource;
        this.context = context;
        this.moduleItemList = moduleItemList;
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
        TextView moduleName = (TextView) layout.findViewById(R.id.moduleNameView);
        ImageButton editModuleButton = (ImageButton)layout.findViewById(R.id.editModuleButtonView);
        ImageButton manageModuleButton = (ImageButton)layout.findViewById(R.id.manageModuleButtonView);

        editModuleButton.setOnClickListener(new View.OnClickListener(){

           @Override
           public void onClick(View v) {
              ((ModuleListActivity)context).onModuleItemClickEditDelete(moduleContent,moduleItemList);
           }
        });
        manageModuleButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ((ModuleListActivity)context).onModuleItemClickedManage(moduleContent);
            }
        });

        moduleName.setText(moduleContent);
        return layout;
    }


}
