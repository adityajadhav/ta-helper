package com.cs442.team4.tahelper.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.listItem.CustomListAdapter;
import com.cs442.team4.tahelper.student.Student_Entity;
import com.cs442.team4.tahelper.student.groups.LexicographicStudentGroupsImpl;
import com.cs442.team4.tahelper.utils.ObjectUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by aditya on 06-11-2016.
 */

public class GenerateStudentGroupsFragment extends Fragment {

    private DatabaseReference mDatabase;
    private int numStudent;
    private int groupSize = 4;
    private List<Student_Entity> studentList;

    private static String tag = "team4";
    public List<ArrayList<Student_Entity>> groups;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.generate_students_groups_fragment, container, false);

        final EditText groupSizeTxtEdt = (EditText) view.findViewById(R.id.groupSizeTxtEdt);
        final ListView studentLstView = (ListView) view.findViewById(R.id.studentLstView);
        final Button generateGroupsBtn = (Button) view.findViewById(R.id.generateGroupsBtn);


        studentList = new ArrayList<>();
        Student_Entity s1 = new Student_Entity("ajadhav", "aditya", "jadhav", "abc@abc.com", "a123");
        Student_Entity s2 = new Student_Entity("sowmya", "sowmya", "parameshwara", "abc@abc.com", "a123");
        Student_Entity s3 = new Student_Entity("ullas", "ullas", "aithal", "abc@abc.com", "a123");
        Student_Entity s4 = new Student_Entity("mohammed", "mohammed", "shethwala", "abc@abc.com", "a123");
        studentList.addAll(Arrays.asList(s1, s2, s3, s4));

        final ArrayAdapter<Student_Entity> itemsAdapter = new CustomListAdapter<Student_Entity>(getContext(), R.layout.custom_list, studentList);

        //ArrayAdapter<Student_Entity> itemsAdapter = new ArrayAdapter<Student_Entity>(getContext(), android.R.layout.simple_list_item_1, studentList);
        studentLstView.setAdapter(itemsAdapter);
        itemsAdapter.notifyDataSetChanged();

        generateGroupsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groups = new ArrayList<>();

                if (ObjectUtils.isEmpty(groupSizeTxtEdt.getText().toString())) {
                    Snackbar.make(v, "Please provide group size", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    Log.e(tag, "group size is not defined");
                } else {

                    groupSize = Integer.parseInt(groupSizeTxtEdt.getText().toString());

                    if (ObjectUtils.isNotEmpty(studentList)) {

                        numStudent = studentList.size();

                        if (numStudent < 0) {
                            //If the number of items is less than three then it doesn't comply with the
                            //requirements (teams should be more or equal than three.
                            Log.d(tag, "Not Comply with the requirements");
                        } else {


                            if (groupSize > 0) {
                                List<List<Student_Entity>> groups = new LexicographicStudentGroupsImpl().generateGroups(studentList, groupSize);
                                studentList.removeAll(studentList);

                                if (ObjectUtils.isNotEmpty(groups)) {
                                    for (int j = 0; j < groups.size(); j++) {
                                        for (int i = 0; i < groups.get(j).size(); i++) {
                                            Student_Entity student = groups.get(j).get(i);
                                            student.setBelongToGroup(j + 1);
                                            studentList.add(student);
                                            System.out.println(groups.get(j).get(i).getStudentFirstName() + "\t" + j);
                                        }
                                    }
                                }
                                itemsAdapter.notifyDataSetChanged();
                            } else {
                                Snackbar.make(v, groupSize + " is not a valid input", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                            }
                        }
                    }
                }
            }
        });


        return view;

    }

    private void fetchStudents(String courseId) {
        // TODO filter by  courseId
        DatabaseReference ref = mDatabase.child("students");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count ", "" + snapshot.getChildrenCount());
                if (ObjectUtils.isNotEmpty(snapshot.getChildren())) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Student_Entity studentEntity = postSnapshot.getValue(Student_Entity.class);
                        studentList.add(studentEntity);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: ", firebaseError.getMessage());
            }
        });


    }

}
