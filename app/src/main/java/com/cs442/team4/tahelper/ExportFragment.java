package com.cs442.team4.tahelper;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by ullas on 11/26/2016.
 */

public class ExportFragment extends Fragment {
    String courseId;
    String exportType = null;
    final ArrayList<String > studentNames = new ArrayList<>();
    final ArrayList<String > studentMarks = new ArrayList<>();
    public interface ExportFragmentInterface {
        public void closeExportFragmentInterface();
    }

    ExportFragmentInterface efi;

    public void setExportFragmentInterface(ExportFragmentInterface obj) {
        this.efi = obj;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            courseId = getArguments().getString("course_code");
            Log.i("Code in export: ", courseId);
        }

        return inflater.inflate(R.layout.export_data, container, false);

    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Spinner export_list_spinner = (Spinner) getView().findViewById(R.id.export_list_spinner_layout);
        final ArrayList<String> modules = new ArrayList<String>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_item, modules);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        export_list_spinner.setAdapter(adapter);


        // final ArrayList<String> ta_existing_members = new ArrayList<String>();


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("modules");


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int found_flag = 0;

                if (dataSnapshot.hasChild(courseId)) {
                    found_flag = 1;
                    Log.i("Course found!", "Found");
                }

                if (found_flag == 1) {
                    DataSnapshot course = dataSnapshot.child(courseId);
                    for (DataSnapshot items : course.getChildren()) {


                        try {


                                for (DataSnapshot eachItem : items.getChildren()) {
                                    //To ignore weightage field in Inclass Assignments
                                    if (eachItem.getChildrenCount() > 0) {
                                        modules.add(eachItem.getKey());
                                    }

                                    try {


                                    } catch (Exception e) {
                                        Log.i("Exception", e.toString());
                                    }
                                }
                                adapter.notifyDataSetChanged();



                        } catch (Exception e) {
                            Log.i("Exception", e.toString());
                        }
                    }

                }


            }


            @Override
            public void onCancelled(DatabaseError e) {

            }
        });


        export_list_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("item selected", export_list_spinner.getItemAtPosition(position).toString());
                exportType = export_list_spinner.getItemAtPosition(position).toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button export_btn = (Button) getView().findViewById(R.id.export_btn_layout);
        export_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ExportFunctionRunner ef = new ExportFunctionRunner();
                 ef.execute();
            }
        });


        Button done_btn = (Button) getView().findViewById(R.id.done_btn_layout);
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                efi.closeExportFragmentInterface();
            }
        });
    }


    public class ExportFunctionRunner extends AsyncTask<String, String, String> {

        final ProgressDialog dialog = ProgressDialog.show(getContext(), "",
                "Exporting. Please wait...", true);
        @Override
        protected String doInBackground(String... params) {
           // publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                // Do your long operations here and return the result

                Log.i("->","I'm executing in background");
                final Export exportValues = new Export();






                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference myRef = database.getReference("students");


             //   myRef.addValueEventListener(new ValueEventListener() {
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int found_flag = 0;

                        if (dataSnapshot.hasChild(courseId)) {
                            found_flag = 1;
                            Log.i("Course found!", "Exporting...");
                        }

                        if (found_flag == 1) {
                            DataSnapshot course = dataSnapshot.child(courseId);
                            for (DataSnapshot items : course.getChildren()) {


                                try {

                                        for (DataSnapshot eachItem : items.getChildren()) {

                                            if (eachItem.hasChild(exportType)) {

                                                if(eachItem.child(exportType).child("Total").getValue().toString() != null) {
                                                    studentMarks.add(eachItem.child(exportType).child("Total").getValue().toString());
                                                    studentNames.add(items.getKey());
                                                    Log.i("Name", studentNames.toString());
                                                    Log.i("Marks", studentMarks.toString());
                                                }





                                            }

                                            try {


                                            } catch (Exception e) {
                                                Log.i("Exception", e.toString());
                                            }
                                        }





                                } catch (Exception e) {
                                    Log.i("Exception", e.toString());
                                }
                            }
                           String path = exportValues.exportInClassAssignmentMarks(getActivity(),studentNames,studentMarks,exportType);
                            if(path != null)
                            {
                                dialog.dismiss();
                                Toast.makeText(getContext(),"File " + exportType + " saved to " + path,Toast.LENGTH_LONG).show();
                            }

                        }


                    }


                    @Override
                    public void onCancelled(DatabaseError e) {

                    }
                });






                // Sleeping for given time period


            }  catch (Exception e) {
                e.printStackTrace();

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }
}
