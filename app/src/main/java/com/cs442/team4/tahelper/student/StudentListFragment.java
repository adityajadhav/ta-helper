package com.cs442.team4.tahelper.student;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
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

import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.contants.IntentConstants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Mohammed on 10/30/2016.
 */

public class StudentListFragment extends Fragment implements SearchView.OnQueryTextListener {

    String courseName = "CS442";

    View myFragmentView;
    SearchView searchView;
    TextView studentListTextView;
    EditText searchStudentEditText;
    Button searchStudentButton;
    ListView studentListView;
    private DatabaseReference mDatabase;

    public static ArrayList<Student_Entity> studentsArraylist;
    public static StudentListAdapter studentAdapter;


    @Override
    public boolean onQueryTextChange(String newText) {

        if (TextUtils.isEmpty(newText)) {
            studentListView.clearTextFilter();
        } else {
            studentListView.setFilterText(newText.toString());
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
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
        searchView.setQueryHint("Search With Student Id");
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);

        studentListView.setTextFilterEnabled(true);

        studentListTextView.setText("Student List | " + courseName);

        int resID = R.layout.student_list_textview;

        studentsArraylist = new ArrayList<Student_Entity>();

        studentAdapter = new StudentListAdapter(getContext(), resID, studentsArraylist);
        studentListView.setAdapter(studentAdapter);

        studentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Student_Entity student = (Student_Entity) studentListView.getItemAtPosition(position);

                /*
                Intent intent = new Intent(getActivity(), StudentModulesActivity.class);
                intent.putExtra(IntentConstants.STUDENT_ID, student.getStudentUserName());
                intent.putExtra(IntentConstants.COURSE_NAME, courseName);
                startActivity(intent);
                */

                //jkanskjkjkajbs

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                StudentModulesFragment studentModulesFragment = new StudentModulesFragment();
                Bundle bundle = new Bundle();
                bundle.putString(IntentConstants.STUDENT_ID, student.getStudentUserName());
                bundle.putString(IntentConstants.COURSE_NAME, courseName);
                studentModulesFragment.setArguments(bundle);

                ft.replace(R.id.course_activity_frame_layout,studentModulesFragment,"student_modules_fragment");
                ft.addToBackStack("student_list_fragment");
                ft.commit();

            }
        });

        loadStudentListFromDatabase();

        return myFragmentView;
    }

    private void loadStudentListFromDatabase() {
        Log.d("Course Name : ", " Name: " + courseName);
        mDatabase.child("students").child(courseName).push();

        mDatabase.child("students").child(courseName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                studentsArraylist.removeAll(studentsArraylist);

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (!studentsArraylist.contains(postSnapshot.getKey())) {

                        String id = (String) postSnapshot.getKey();
                        String email = (String) postSnapshot.child("email").getValue();

                        Log.d("Student : ", " Id: " + id);
                        Log.d("Email : ", " Email: " + email);

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
                Log.d("ModuleListFragment : ", " Read cancelled due to " + databaseError.getMessage());
            }
        });
    }


}