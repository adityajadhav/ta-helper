package com.cs442.team4.tahelper.student;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.contants.IntentConstants;
import com.cs442.team4.tahelper.fragment.ModuleListFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import static java.util.logging.Logger.global;

/**
 * Created by Mohammed on 10/30/2016.
 */

public class StudentListFragment extends Fragment implements SearchView.OnQueryTextListener {

    String courseName = "CS442";

    View myFragmentView;
    SearchView searchView;
    OnStudentClickListener onStudentClickListener;
    TextView studentListTextView;
    EditText searchStudentEditText;
    Button searchStudentButton;
    ListView studentListView;
    private DatabaseReference mDatabase;
    
    public static ArrayList<Student_Entity> studentsArraylist;
    public static StudentListAdapter studentAdapter;

    public interface OnStudentClickListener {
        public void showStudentModules(String courseName, String studentId);
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {

        if (TextUtils.isEmpty(newText)) {
            studentListView.clearTextFilter();
        } else {
            studentListView.setFilterText(newText.toString());
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

        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Later, will have to check if bundle is there or not and then only assign the value...
        final String courseNameFromActivity = getArguments().getString(IntentConstants.COURSE_ID);
        courseName = courseNameFromActivity;

        myFragmentView = inflater.inflate(R.layout.student_list_fragment, container, false);
        studentListTextView = (TextView) myFragmentView.findViewById(R.id.studentListTextView);
        searchView = (SearchView) myFragmentView.findViewById(R.id.searchBar);
        studentListView = (ListView) myFragmentView.findViewById(R.id.studentListView);
        searchView.setQueryHint("Enter Student Name");
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);

        studentListView.setTextFilterEnabled(true);

        studentListTextView.setText("Student List: "+courseName);

        int resID = R.layout.student_list_textview;

        studentsArraylist = new ArrayList<Student_Entity>();

        //We will fetch the students from an excel sheet or xml file and then populate the arraylist with it for the first time
        //After populating, it will be stored in the firebase database, so after that it will be fetched from that only
        //Student_Entity student1 = new Student_Entity("mshethwa", "Mohammed", "Shethwala", "mshethwa@hawk.iit.edu", "A12345678");
        //Student_Entity student2 = new Student_Entity("uaithal", "Ullas", "Aithal", "uaithal@hawk.iit.edu", "A12345678");
        //Student_Entity student3 = new Student_Entity("ajadhav", "Aditya", "Jadhav", "ajadhav@hawk.iit.edu", "A12345678");

        //studentsArraylist.add(student1);
        //studentsArraylist.add(student2);
        //studentsArraylist.add(student3);

        studentAdapter = new StudentListAdapter(getContext(), resID, studentsArraylist);
        studentListView.setAdapter(studentAdapter);

        studentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView< ? > parent, View view,
                                     int position, long id){

                Student_Entity student =  (Student_Entity)studentListView.getItemAtPosition(position);

                onStudentClickListener.showStudentModules(courseName, student.getStudentUserName());

            }
        });

        loadStudentListFromDatabase();

        return myFragmentView;
    }

    private void loadStudentListFromDatabase()
    {
        Log.d("Course Name : "," Name: "+courseName);
        mDatabase.child("students").child(courseName).push();

        mDatabase.child("students").child(courseName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                studentsArraylist.removeAll(studentsArraylist);

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {
                    if(!studentsArraylist.contains(postSnapshot.getKey()))
                    {

                        String id = (String) postSnapshot.getKey();
                        String email = (String) postSnapshot.child("email").getValue();

                        Log.d("Student : "," Id: "+id);
                        Log.d("Email : "," Email: "+email);

                        Student_Entity student = new Student_Entity(id, email);
                        student.setUserName(id);
                        student.setEmail(email);

                        studentsArraylist.add(student);
                    }
                }
                studentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ModuleListFragment : "," Read cancelled due to "+databaseError.getMessage());
            }
        });
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
