package com.cs442.team4.tahelper.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cs442.team4.tahelper.R;

import static com.cs442.team4.tahelper.R.id.sendBcastBtn;


public class ManageCourseFragment extends Fragment implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.student_list_tv_layout:
                manageCourseInterface.openModule("STUDENT_LIST");
                break;
            case sendBcastBtn:
                manageCourseInterface.openModule(BcastNotificationFragment.MODULE_NAME);
//                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                break;
        }
    }

    public interface ManageCourseInterface {
        void openModule(String moduleName);
    }

    private ManageCourseInterface manageCourseInterface;

    public void setStudentListInterface(ManageCourseInterface manageCourseInterface) {
        this.manageCourseInterface = manageCourseInterface;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle args = getArguments();
        if (args != null) {
            String courseId = getArguments().getString("course_code");
            Log.i("Code in fgmt : ", courseId);
        }

        return inflater.inflate(R.layout.main_menu_activity, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView student_list_tv = (TextView) view.findViewById(R.id.student_list_tv_layout);
        FloatingActionButton sendBcastBtn = (FloatingActionButton) view.findViewById(R.id.sendBcastBtn);
        student_list_tv.setOnClickListener(this);
        sendBcastBtn.setOnClickListener(this);

    }

}
