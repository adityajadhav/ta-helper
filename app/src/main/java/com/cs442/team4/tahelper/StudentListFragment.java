package com.cs442.team4.tahelper;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Mohammed on 10/30/2016.
 */

public class StudentListFragment extends ListFragment {

    View myFragmentView;
    StudentListFragment.OnStudentTextViewClickListener studentClick;
    EditText searchStudentEditText;
    Button searchStudentButton;

    public static ArrayList<Student_Entity> studentsArraylist;
    public static StudentListAdapter studentAdapter;

    public interface OnStudentTextViewClickListener{
        public void showStudentModules();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myFragmentView = inflater.inflate(R.layout.student_list_fragment, container, false);
        searchStudentEditText = (EditText) myFragmentView.findViewById(R.id.searchStudentEditText);
        searchStudentButton = (Button) myFragmentView.findViewById(R.id.searchStudentButton);

        int resID = R.layout.student_list_textview;

        studentsArraylist = new ArrayList<Student_Entity>();

        //We will fetch the students from an excel sheet or xml file and then populate the arraylist with it for the first time
        //After populating, it will be stored in the firebase database, so after that it will be fetched from that only
        Student_Entity student1 = new Student_Entity("mshethwa", "Mohammed", "Shethwala", "mshethwa@hawk.iit.edu", "A12345678");
        Student_Entity student2 = new Student_Entity("uaithal", "Ullas", "Aithal", "uaithal@hawk.iit.edu", "A12345678");
        Student_Entity student3 = new Student_Entity("ajadhav", "Aditya", "Jadhav", "ajadhav@hawk.iit.edu", "A12345678");

        studentsArraylist.add(student1);
        studentsArraylist.add(student2);
        studentsArraylist.add(student3);


        studentAdapter = new StudentListAdapter(getContext(), resID, studentsArraylist);
        setListAdapter(studentAdapter);



        searchStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        return myFragmentView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        Student_Entity student =  (Student_Entity)getListView().getItemAtPosition(position);

        Toast.makeText(getActivity(), "Student Email Id: "+student.getStudentEmail(), Toast.LENGTH_SHORT).show();

    }

}
