package com.cs442.team4.tahelper;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by ullas on 11/10/2016.
 */

public class Add_ta_fragment extends Fragment {
String selected_item = null;

    public interface addTAToFirebaseInterface
    {
        public void sendTAdata(ArrayList<String> ta_final_list);
        public void closeAddTAFragment();
    }
    addTAToFirebaseInterface ati;

    public void setAddTAFragmentInterface(addTAToFirebaseInterface obj)
    {
        this.ati = obj;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.add_ta_fragment, container, false);

    }
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        final Spinner ta_drop_down = (Spinner) view.findViewById(R.id.ta_drop_down_layout);

        final ArrayList<String> ta_members = new ArrayList<String>();
        final ArrayList<String> ta_existing_members = new ArrayList<String>();
        final TA_list ta_adapter = new TA_list(ta_existing_members,getContext());



        ListView ta_list_view = (ListView) view.findViewById(R.id.ta_list_view_layout);


        ta_list_view.setAdapter(ta_adapter);






        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getBaseContext(),android.R.layout.simple_spinner_item,ta_members);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ta_drop_down.setAdapter(adapter);



        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("users");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot items : dataSnapshot.getChildren()) {


                    try {
                        String key = items.getKey().toString();
                        Log.i("key:", items.child("username").getValue().toString());
                        ta_members.add(items.child("username").getValue().toString());
                        ta_adapter.notifyDataSetChanged();
                        adapter.notifyDataSetChanged();


                    } catch (Exception e) {
                        Log.i("Exception", e.toString());
                    }
                }
            }


            @Override
            public void onCancelled(DatabaseError e) {

            }
        });


     //   ta_drop_down.setId(0);

        ta_drop_down.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("item selected",ta_drop_down.getItemAtPosition(position).toString());
                selected_item = ta_drop_down.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        ta_adapter.setTAListInterface(new TA_list.TaListInterface(){

            public void removeItem(String name)
            {
                if(ta_existing_members.contains(name))
                {
                    ta_existing_members.remove(name);
                    ta_adapter.notifyDataSetChanged();

                }
                else
                {
                    Toast.makeText(getContext(),name + " not found",Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button add_to_list_btn = (Button) view.findViewById(R.id.add_to_list_btn_layout);
        add_to_list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selected_item.length() <=0 )
                {
                    Toast.makeText(getContext(),"Select a name from dropdown",Toast.LENGTH_SHORT).show();
                }
                if(ta_existing_members.contains(selected_item))
                {
                    Toast.makeText(getContext(),"Already in the list",Toast.LENGTH_SHORT).show();
                }
                else {
                    ta_existing_members.add(selected_item);
                    ta_adapter.notifyDataSetChanged();

                }
            }
        });



//        final DatabaseReference existing_members = database.getReference("courses");
//
//        existing_members.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//
//                for (DataSnapshot items : dataSnapshot.getChildren()) {
//
//
//                    try {
//                        String key = items.getKey().toString();
//                        Log.i("key:", items.child("username").getValue().toString());
//                        ta_members.add(items.child("username").getValue().toString());
//                        ta_adapter.notifyDataSetChanged();
//                        adapter.notifyDataSetChanged();
//
//
//                    } catch (Exception e) {
//                        Log.i("Exception", e.toString());
//                    }
//                }
//            }
//
//
//            @Override
//            public void onCancelled(DatabaseError e) {
//
//            }
//        });



            Button add_ta_final_btn = (Button)view.findViewById(R.id.add_ta_final_btn_layout);
            add_ta_final_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(ta_existing_members.size() <=0 )
                    {
                        Toast.makeText(getContext(),"No TAs selcted. Select a TA from dropdown",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                      ati.sendTAdata(ta_existing_members);
                        ati.closeAddTAFragment();
                    }


                }
            });









    }




    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       // ati = (addTAToFirebaseInterface)getActivity();
    }

}
