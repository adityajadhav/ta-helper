package com.cs442.team4.tahelper.student;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.fragment.ModuleListFragment;

import java.util.ArrayList;

/**
 * Created by Mohammed on 10/30/2016.
 */

public class StudentListFragment extends ListFragment implements SearchView.OnQueryTextListener {

    String courseName = "CS442";

    View myFragmentView;
    OnStudentClickListener onStudentClickListener;
    EditText searchStudentEditText;
    Button searchStudentButton;

    public static ArrayList<Student_Entity> studentsArraylist;
    public static StudentListAdapter studentAdapter;

    public interface OnStudentClickListener {
        public void showStudentModules(String courseName, String studentId);
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {

        if (TextUtils.isEmpty(newText)) {
            getListView().clearTextFilter();
        } else {
            getListView().setFilterText(newText);
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myFragmentView = inflater.inflate(R.layout.student_list_fragment, container, false);
        SearchView searchView = (SearchView) myFragmentView.findViewById(R.id.searchBar);
        searchView.setQueryHint("Enter Student Name");
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);

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

        return myFragmentView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        Student_Entity student =  (Student_Entity)getListView().getItemAtPosition(position);

        onStudentClickListener.showStudentModules(courseName, student.getStudentUserName());

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            onStudentClickListener = (StudentListFragment.OnStudentClickListener) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement OnStudentClickListener");
        }
    }

}
