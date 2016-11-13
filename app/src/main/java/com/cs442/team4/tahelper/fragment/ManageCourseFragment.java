package com.cs442.team4.tahelper.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.cs442.team4.tahelper.CourseActivity;
import com.cs442.team4.tahelper.MainActivity;
import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.student.StudentListActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import static android.app.Activity.RESULT_OK;
import static android.support.v4.content.ContextCompat.checkSelfPermission;
import static com.cs442.team4.tahelper.R.id.generateGroupsBtn;
import static com.cs442.team4.tahelper.R.id.sendBcastBtn;


public class ManageCourseFragment extends Fragment implements View.OnClickListener {
    String courseId = null;
    File importfile;
    String filepath;



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.student_list_tv_layout:
                //manageCourseInterface.openModule("STUDENT_LIST");
                Intent intent = new Intent(getContext(), StudentListActivity.class);
                intent.putExtra("course_id", courseId);
                startActivity(intent);
                break;
            case sendBcastBtn:
                openBcastNotificationFragment();
                //manageCourseInterface.openModule(BcastNotificationFragment.MODULE_NAME);
//                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                break;
            case generateGroupsBtn:
                openGenerateGroupFragment();
                break;
        }
    }

    private void openGenerateGroupFragment() {

        GenerateStudentGroupsFragment generateStudentGroupsFragment = new GenerateStudentGroupsFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.course_activity_frame_layout, generateStudentGroupsFragment, "generateStudentGroupsFragment");
        ft.addToBackStack("course_list_fragment");
        ft.commit();
    }

    private void openBcastNotificationFragment() {
        BcastNotificationFragment bcastNotificationFragment = new BcastNotificationFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.course_activity_frame_layout, bcastNotificationFragment, "bcastNotificationFragment");
        ft.addToBackStack("course_list_fragment");
        ft.commit();
    }

    public void openFileEx() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, 15);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                Uri uri = data.getData();
                String mimeType = getContext().getContentResolver().getType(uri);


                importfile = new File(uri.getPath());
                filepath = importfile.getAbsolutePath().split(":")[1];
                Log.i("String:", filepath);
                importStudentData();



            } catch (Exception e) {
                Log.i("File operation error", e.toString());
            }
        }
    }


   /* public interface ManageCourseInterface {
        void openModule(String moduleName);
    }*/

    //private ManageCourseInterface manageCourseInterface;

   /* public void setStudentListInterface(ManageCourseInterface manageCourseInterface) {
        this.manageCourseInterface = manageCourseInterface;
    }*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle args = getArguments();
        if (args != null) {
            courseId = getArguments().getString("COURSE_ID");
            Log.i("Code in fgmt : ", courseId);
        }

        return inflater.inflate(R.layout.main_menu_activity, container, false);

    }

    private void importStudentData() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("students");
        try {
            //FileInputStream file = new FileInputStream(new File("C:\\Users\\Mohammed\\Desktop\\Students.xlsx"));

            // Creating Input Stream
//            File file = new File(Environment.getExternalStorageDirectory()
//                    + "/Download/Students.xlsx");


            String [] format = filepath.split("\\.");
            if(format[format.length-1].equals("xls")  ||  format[format.length-1].equals("xlsx")) {




                File file = new File(Environment.getExternalStorageDirectory() + "/" + filepath);
                FileInputStream myInput = new FileInputStream(file);
                //FileInputStream myInput = new FileInputStream(importfile);

                // Create a POIFSFileSystem object
                //POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);


                //Create Workbook instance holding reference to .xlsx file
                XSSFWorkbook workbook = new XSSFWorkbook(myInput);

                //Get first/desired sheet from the workbook
                XSSFSheet sheet = workbook.getSheetAt(0);

                //Iterate through each rows one by one
                Iterator<Row> rowIterator = sheet.iterator();

                int noOfColumns = sheet.getRow(0).getLastCellNum();
                Log.i("Number of col",Integer.toString(noOfColumns));
                String name = null;

                if(noOfColumns != 2)
                {
                    Toast.makeText(getContext(),"Number of columns should be 2. Import failed!",Toast.LENGTH_SHORT);
                    return;
                }

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    //For each row, iterate through all the columns

                    Iterator<Cell> cellIterator = row.cellIterator();
                    Cell cell = cellIterator.next();
                    name = cell.getStringCellValue();
                    myRef.child(courseId).child(name).setValue("");
                    cell = cellIterator.next();
                    myRef.child(courseId).child(name).child("email").setValue(cell.getStringCellValue());


                    System.out.println("");
                }
                //file.close();
                DatabaseReference myRef1 = database.getReference("courses");
                myRef1.child(courseId).child("imported").setValue(true);
                Toast.makeText(getContext(), "Import Completed", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getContext(), "only xls or xlsx file format supported", Toast.LENGTH_SHORT).show();
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {


            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(),"Permission Granted",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(),"Please provide access to your storage to import student list",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView student_list_tv = (TextView) view.findViewById(R.id.student_list_tv_layout);
        TextView generateGroupsBtn = (TextView) view.findViewById(R.id.generateGroupsBtn);

        FloatingActionButton sendBcastBtn = (FloatingActionButton) view.findViewById(R.id.sendBcastBtn);
        student_list_tv.setOnClickListener(this);
        //sendBcastBtn.setOnClickListener(this);
        //generateGroupsBtn.setOnClickListener(this);
    //    final TextView import_student_tv = (TextView) view.findViewById(R.id.import_student_tv_layout);
        final Button import_student_btn = (Button) view.findViewById(R.id.import_student_btn_layout);


        import_student_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //importStudentData();


                if (Build.VERSION.SDK_INT >= 23)
                {
                    if (checkPermission())
                    {
                        // Code for above or equal 23 API Oriented Device
                        // Create a common Method for both
                    } else {
                        requestPermission();
                    }
                }
                else
                {

                   Toast.makeText(getContext(),"This feature works only in marshmallow!",Toast.LENGTH_SHORT);
                    return;
                }








                openFileEx();

            }
        });


        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference reference = db.getReference("courses");


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                DataSnapshot items = dataSnapshot.child(courseId);
                TextView student_list_tv1 = (TextView) view.findViewById(R.id.student_list_tv_layout);
                // TextView broadcast_email_tv = (TextView) view.findViewById(R.id.broadcast_email_tv_layout);
                TextView export_records_tv = (TextView) view.findViewById(R.id.export_records_tv_layout);
                TextView generate_groups_tv = (TextView) view.findViewById(R.id.generate_groups_tv_layout);

                if(items.child("imported").getValue().equals(true))
                {
                    //TextView import_student_tv1 = (TextView) view.findViewById(R.id.import_student_tv_layout);
                    import_student_btn.setVisibility(View.INVISIBLE);
                    student_list_tv1.setVisibility(View.VISIBLE);
                    // broadcast_email_tv.setVisibility(View.INVISIBLE);
                    export_records_tv.setVisibility(View.VISIBLE);
                    generate_groups_tv.setVisibility(View.VISIBLE);


                }
                else
                {
                    import_student_btn.setVisibility(View.VISIBLE);
                    student_list_tv1.setVisibility(View.INVISIBLE);
                   // broadcast_email_tv.setVisibility(View.INVISIBLE);
                    export_records_tv.setVisibility(View.INVISIBLE);
                    generate_groups_tv.setVisibility(View.INVISIBLE);

                }

            }


            @Override
            public void onCancelled(DatabaseError e) {

            }
        });




    }

}

