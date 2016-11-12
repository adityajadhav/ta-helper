package com.cs442.team4.tahelper;

/**
 * Created by ullas on 11/11/2016.
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class TA_list extends ArrayAdapter<String> {

    public ArrayList<String> ta_array_list;
    public Context c;

    public interface TaListInterface
    {
        void removeItem(String name);
    }
    TaListInterface tal;

    public void setTAListInterface(TaListInterface obj)
    {
        this.tal = obj;
    }


    public TA_list(ArrayList<String> arraylist, Context c) {
        super(c, R.layout.ta_list_view_layout, arraylist);
        this.ta_array_list = arraylist;
        this.c = c;

    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View view = convertView;


        if (view == null)
        {
            view = View.inflate(c, R.layout.ta_list_view_layout, null);
        } else

        {
            view = convertView;
        }

        TextView ta_name_tv = (TextView) view.findViewById(R.id.ta_name_tv_layout);
        ta_name_tv.setText(ta_array_list.get(position));
        Button select_ta_btn = (Button) view.findViewById(R.id.select_ta_btn_layout);
        select_ta_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tal.removeItem(ta_array_list.get(position));
            }
        });



        return view;
    }
}

