package com.example.krutantbilakhia.rubaroo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import static com.example.krutantbilakhia.rubaroo.ScannedBarcodeActivity.adminEventForScanner;

public class EventSummaryActivity extends AppCompatActivity {

    TextView total, present;
    private FirebaseDatabase mFirebaseDatabase;
    int clubCount;
    List<AttendeeClass> listNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_summary);

        total = findViewById(R.id.nTotal);
        present = findViewById(R.id.nPresent);

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        Query mDatabaseReference = mFirebaseDatabase.getReference().child("Attendence").child("3rd Council Meet").child("Panvel");

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                listNew = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    AttendeeClass value = dataSnapshot1.getValue(AttendeeClass.class);
                    AttendeeClass fire = new AttendeeClass();
                    String name = value.getName();
                    String club = value.getClub();
                    String status = value.getStatus();

                    fire.setName(name);
                    fire.setClub(club);
                    fire.setStatus(status);

                    listNew.add(fire);
                }

                clubCount = listNew.size();
                total.setText(String.valueOf(clubCount));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Hello", "Failed to read value.", error.toException());
            }
        });

    }
}
