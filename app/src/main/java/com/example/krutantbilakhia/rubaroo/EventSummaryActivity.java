package com.example.krutantbilakhia.rubaroo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class EventSummaryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView total, present;
    List<ClubDetailsClass> list;
    EventSummaryRecyclerAdapter eventSummaryRecyclerAdapter;
    FirebaseDatabase database;
    DatabaseReference myRef;
    RecyclerView recycle;
    public String eventForSummary = "Null For Now";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_summary);

        //eventSummaryRecyclerAdapter = new EventSummaryRecyclerAdapter(list, EventSummaryActivity.this);

        total = findViewById(R.id.nTotal);
        present = findViewById(R.id.nPresent);

        recycle = findViewById(R.id.recycle);

        Spinner spinner = (Spinner) findViewById(R.id.events_spinner);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.events_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

//        updateButton = findViewById(R.id.admin_btn_update);
//
//        updateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                    Toast.makeText(EventSummaryActivity.this, eventForSummary, Toast.LENGTH_SHORT).show();
//
//
//            }
//        });

        myRef = FirebaseDatabase.getInstance().getReference().child("Club Total");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    ClubDetailsClass value = dataSnapshot1.getValue(ClubDetailsClass.class);
                    ClubDetailsClass fire = new ClubDetailsClass();
                    String clubName = value.getClubName();
                    int clubCount = value.getClubCount();

                    fire.setClubName(clubName);
                    fire.setClubCount(clubCount);

                    list.add(fire);
                }

                onActivityOpen();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Hello", "Failed to read value.", error.toException());
            }
        });


    }

    public void onActivityOpen() {
        EventSummaryRecyclerAdapter recyclerAdapter = new EventSummaryRecyclerAdapter(list, EventSummaryActivity.this);
        RecyclerView.LayoutManager recycleVariable = new LinearLayoutManager(EventSummaryActivity.this);
        recycle.setLayoutManager(recycleVariable);
        recycle.setItemAnimator(new DefaultItemAnimator());
        recycle.setAdapter(recyclerAdapter);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        eventForSummary = (String) parent.getItemAtPosition(pos);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
        eventForSummary = "Temporary Null";
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
