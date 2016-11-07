package com.cs442.team4.tahelper.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cs442.team4.tahelper.R;

/**
 * Created by ullas on 11/6/2016.
 */

public class ManageCourseFragment extends Fragment {

    public interface CallStudentListInterface{
        void callStudentListActivity();
    }

    CallStudentListInterface obj;

    public void setStudentListInterface(CallStudentListInterface obj){
        this.obj = obj;
    }
    @Nullable
    @Override


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle args = getArguments();
        if(args  != null) {
            String courseId = getArguments().getString("course_code");
            Log.i("Code in fgmt : ", courseId);
        }

        return inflater.inflate(R.layout.main_menu_activity,container,false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView student_list_tv = (TextView) view.findViewById(R.id.student_list_tv_layout);
        student_list_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obj.callStudentListActivity();
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}
