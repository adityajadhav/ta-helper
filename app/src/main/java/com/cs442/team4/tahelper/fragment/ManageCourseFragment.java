package com.cs442.team4.tahelper.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cs442.team4.tahelper.CourseActivity;
import com.cs442.team4.tahelper.Export;
import com.cs442.team4.tahelper.R;
import com.cs442.team4.tahelper.contants.IntentConstants;
import com.cs442.team4.tahelper.student.StudentListFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import static android.app.Activity.RESULT_OK;
import static com.cs442.team4.tahelper.R.id.generate_groups_tv_layout;
import static com.cs442.team4.tahelper.R.id.sendBcastBtn;


public class ManageCourseFragment extends Fragment implements View.OnClickListener {
    String courseId = null;
    File importfile;
    String filepath;

    public interface ManageCourseFragmentInterface {

        public void callExportFragment(String courseId);
    }
    ManageCourseFragmentInterface mcfi;

    public void setManageCourseFragmentInterface(ManageCourseFragmentInterface obj)
    {
        this.mcfi = obj;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.student_list_tv_layout:
                //manageCourseInterface.openModule("STUDENT_LIST");
                openStudentList();
                break;
            case sendBcastBtn:
                openBcastNotificationFragment();
                //manageCourseInterface.openModule(BcastNotificationFragment.MODULE_NAME);
//                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                break;
            case generate_groups_tv_layout:
                openGenerateGroupFragment();
                break;
        }
    }

    private void openStudentList() {
        StudentListFragment studentListFragment = new StudentListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(IntentConstants.COURSE_ID, courseId);
        studentListFragment.setArguments(bundle);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.course_activity_frame_layout, studentListFragment, "studentListFragment");
        ft.addToBackStack("course_list_fragment");
        ft.commit();
    }


    private void openGenerateGroupFragment() {

        GenerateStudentGroupsFragment generateStudentGroupsFragment = new GenerateStudentGroupsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CourseActivity.COURCE_ID_KEY, courseId);
        generateStudentGroupsFragment.setArguments(bundle);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.course_activity_frame_layout, generateStudentGroupsFragment, "generateStudentGroupsFragment");
        ft.addToBackStack("course_list_fragment");
        ft.commit();
    }

    private void openBcastNotificationFragment() {
        BcastNotificationFragment bcastNotificationFragment = new BcastNotificationFragment();

        Bundle bundle = new Bundle();
        bundle.putString(CourseActivity.COURCE_ID_KEY, courseId);
        bcastNotificationFragment.setArguments(bundle);
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













    public class ImportFunctionRunner extends AsyncTask<String, String, String> {

        final ProgressDialog dialog = ProgressDialog.show(getContext(), "",
                "Importing. Please wait...", true);
        @Override
        protected String doInBackground(String... params) {
            // publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                // Do your long operations here and return the result

                importStudentData();

                dialog.dismiss();

                // Sleeping for given time period


            }  catch (Exception e) {
                e.printStackTrace();
                dialog.dismiss();


            }
            return "done";
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation

        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onProgressUpdate(Progress[])
         */
        @Override
        protected void onProgressUpdate(String... text) {

            // Things to be done while execution of long running operation is in
            // progress. For example updating ProgessDialog
        }
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
                ImportFunctionRunner ifr = new ImportFunctionRunner();
                ifr.execute();

              //  importStudentData();

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
            courseId = getArguments().getString(CourseActivity.COURCE_ID_KEY);
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


            String[] format = filepath.split("\\.");
            if (format[format.length - 1].equals("xls") || format[format.length - 1].equals("xlsx")) {


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
                Log.i("Number of col", Integer.toString(noOfColumns));
                String name = null;

                if (noOfColumns != 4) {
                    Toast.makeText(getContext(), "Number of columns should be 2. Import failed!", Toast.LENGTH_SHORT).show();
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
                    cell = cellIterator.next();
                    myRef.child(courseId).child(name).child("lastName").setValue(cell.getStringCellValue());
                    cell = cellIterator.next();
                    myRef.child(courseId).child(name).child("firstName").setValue(cell.getStringCellValue());



                }
                //file.close();
                DatabaseReference myRef1 = database.getReference("courses");
                myRef1.child(courseId).child("imported").setValue(true);
                Toast.makeText(getContext(), "Import Completed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "only xls or xlsx file format supported", Toast.LENGTH_SHORT).show();
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

    }











    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Please provide access to your storage to import student list", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView student_list_tv = (TextView) view.findViewById(R.id.student_list_tv_layout);
        TextView generateGroupsBtn = (TextView) view.findViewById(generate_groups_tv_layout);

        FloatingActionButton sendBcastBtn = (FloatingActionButton) view.findViewById(R.id.sendBcastBtn);
        student_list_tv.setOnClickListener(this);
        sendBcastBtn.setOnClickListener(this);
        generateGroupsBtn.setOnClickListener(this);
        //  TextView import_student_tv = (TextView) view.findViewById(R.id.import_student_tv_layout);
        final Button import_student_btn = (Button) view.findViewById(R.id.import_student_btn_layout);


        import_student_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //importStudentData();
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermission()) {
                        // Code for above or equal 23 API Oriented Device
                        // Create a common Method for both
                    } else {
                        requestPermission();
                    }
                } else {
                    Toast.makeText(getContext(), "This feature works only in marshmallow!", Toast.LENGTH_SHORT).show();
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
                TextView generate_groups_tv = (TextView) view.findViewById(generate_groups_tv_layout);
                FloatingActionButton sendBcastBtn = (FloatingActionButton) view.findViewById(R.id.sendBcastBtn);
                TextView import_tool_tip_tv = (TextView) view.findViewById(R.id.import_tool_tip_tv_layout);
                try {
                    if (items.child("imported").getValue().equals(true)) {
                        //TextView import_student_tv1 = (TextView) view.findViewById(R.id.import_student_tv_layout);
                        import_student_btn.setVisibility(View.INVISIBLE);
                        import_tool_tip_tv.setVisibility(View.INVISIBLE);

                        student_list_tv1.setVisibility(View.VISIBLE);
                        // broadcast_email_tv.setVisibility(View.INVISIBLE);
                        export_records_tv.setVisibility(View.VISIBLE);
                        generate_groups_tv.setVisibility(View.VISIBLE);
                        sendBcastBtn.setVisibility(View.VISIBLE);


                    } else {
                        import_student_btn.setVisibility(View.VISIBLE);
                        import_tool_tip_tv.setVisibility(View.VISIBLE);
                        student_list_tv1.setVisibility(View.INVISIBLE);
                        // broadcast_email_tv.setVisibility(View.INVISIBLE);
                        export_records_tv.setVisibility(View.INVISIBLE);
                        generate_groups_tv.setVisibility(View.INVISIBLE);
                        sendBcastBtn.setVisibility(View.INVISIBLE);

                    }
                }
                catch (Exception e)
                {
                    Log.i("Exception",e.toString());
                }

            }


            @Override
            public void onCancelled(DatabaseError e) {

            }
        });

        TextView export_records_tv = (TextView) view.findViewById(R.id.export_records_tv_layout);

        export_records_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mcfi.callExportFragment(courseId);
            }
        });
        Button export = (Button) view.findViewById(R.id.export_btn_layout);
        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermission()) {
                        // Code for above or equal 23 API Oriented Device
                        // Create a common Method for both
                    } else {
                        requestPermission();
                    }
                } else {
                    Toast.makeText(getContext(), "This feature works only in marshmallow!", Toast.LENGTH_SHORT);
                    return;
                }
                //exportStudentData();
                Export e = new Export();
                e.exportMarks(getActivity());
            }
        });


    }

}

