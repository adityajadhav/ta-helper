package com.cs442.team4.tahelper.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.student.StudentListActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import static com.cs442.team4.tahelper.R.id.generateGroupsBtn;
import static com.cs442.team4.tahelper.R.id.sendBcastBtn;


public class ManageCourseFragment extends Fragment implements View.OnClickListener {
    String courseId = null;

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
            courseId = getArguments().getString("course_id");
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
            File file = new File(Environment.getExternalStorageDirectory()
                    + "/Download/Students.xlsx");
            FileInputStream myInput = new FileInputStream(file);

            // Create a POIFSFileSystem object
            //POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);


            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(myInput);

            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);

            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            String name = null;

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                //For each row, iterate through all the columns
                Iterator<Cell> cellIterator = row.cellIterator();
                Cell cell = cellIterator.next();
                name = cell.getStringCellValue();
                myRef.child(courseId).child(name).setValue("");
                cell = cellIterator.next();
                myRef.child(courseId).child(name).child("email").setValue(cell.getStringCellValue());


//                while (cellIterator.hasNext())
//                {
//                    Cell cell = cellIterator.next();
//                    //Check the cell type and format accordingly
//                    switch (cell.getCellType())
//                    {
//                        case Cell.CELL_TYPE_NUMERIC:
//                            System.out.print(cell.getNumericCellValue() + "\t");
//                            Log.d("Sheet : "," Cell Value: "+cell.getNumericCellValue());
//                            myRef.child("CS442").child("u").setValue("");
//
//
//                            break;
//
//                        case Cell.CELL_TYPE_STRING:
//                            System.out.print(cell.getStringCellValue() + "\t");
//                            Log.d("Sheet : "," Cell Value: "+cell.getStringCellValue());
//                            myRef.child("CS442").child("u").setValue("");
//                            break;
//
//                    }
//                }
                System.out.println("");
            }
            //file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView student_list_tv = (TextView) view.findViewById(R.id.student_list_tv_layout);
        TextView generateGroupsBtn = (TextView) view.findViewById(R.id.generateGroupsBtn);

        FloatingActionButton sendBcastBtn = (FloatingActionButton) view.findViewById(R.id.sendBcastBtn);
        student_list_tv.setOnClickListener(this);
        //sendBcastBtn.setOnClickListener(this);
        //generateGroupsBtn.setOnClickListener(this);
        TextView import_student_tv = (TextView) view.findViewById(R.id.import_student_tv_layout);

        /*
        import_student_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importStudentData();
            }
        });
        */

    }

}

