package com.cs442.team4.tahelper.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.student.Student_Entity;
import com.cs442.team4.tahelper.student.groups.LexicographicStudentGroupsImpl;
import com.cs442.team4.tahelper.utils.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aditya on 06-11-2016.
 */

public class GenerateStudentGroupsFragment extends Fragment {

    private int numStudent;
    private int groupSize = 4;

    private static String tag = "team4";
    public List<ArrayList<Student_Entity>> groups;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.generate_students_groups_fragment, container, false);

        final EditText groupSizeTxtEdt = (EditText) view.findViewById(R.id.groupSizeTxtEdt);

        groups = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(groupSizeTxtEdt.getText().toString())) {
            groupSize = Integer.parseInt(groupSizeTxtEdt.getText().toString());
        }

        // TODO fetch the students from a course
        List<Student_Entity> students = new ArrayList<>();

        if (ObjectUtils.isNotEmpty(students)) {

            numStudent = students.size();

            if (numStudent < 3) {
                //If the number of items is less than three then it doesn't comply with the
                //requirements (teams should be more or equal than three.
                Log.d(tag, "Not Comply with the requirements");
                return view;
            }

            List<List<Student_Entity>> groups = new LexicographicStudentGroupsImpl().generateGroups(students, groupSize);


        }

        return view;

    }

}
