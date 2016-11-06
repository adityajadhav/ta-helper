package com.cs442.team4.tahelper.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.cs442.team4.tahelper.R;

/**
 * Created by neo on 05-11-2016.
 */

public class BcastNotificationFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (LinearLayout) inflater.inflate(R.layout.broadcast_email, container, false);

        final EditText toEdtTxt = (EditText) view.findViewById(R.id.toEdtTxt);
        final EditText ccEdtTxt = (EditText) view.findViewById(R.id.ccEdtTxt);
        final EditText subjectEdtTxt = (EditText) view.findViewById(R.id.subjectEdtTxt);
        final EditText bodyEdtTxt = (EditText) view.findViewById(R.id.bodyEdtTxt);

        Button sendBtn = (Button) view.findViewById(R.id.sendBtn);

        sendBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setType("text/plain");
                // TODO Get all students email ids 
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"ajadhav4@hawk.iit.edu"});
                intent.putExtra(Intent.EXTRA_SUBJECT, subjectEdtTxt.getText());
                intent.putExtra(Intent.EXTRA_TEXT, bodyEdtTxt.getText());
                //intent.setData(Uri.parse("mailto:ajadhav4@hawk.iit.edu")); // or just "mailto:" for blank
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                startActivity(intent);

            }
        });
        return view;

    }

}
