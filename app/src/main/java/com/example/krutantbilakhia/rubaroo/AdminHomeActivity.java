package com.example.krutantbilakhia.rubaroo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

//import static com.example.krutantbilakhia.rubaroo.ScannedBarcodeActivity.adminEventForScanner;

public class AdminHomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button signOutButton, updateButton, summaryButton;
    private FirebaseAuth auth;
    public FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mFirebaseUser;

    final Calendar myCalendar = Calendar.getInstance();
    private Button pickTime;
    private TextView timeText;
    private String stime;

    //Change Every Time or Add to Database and Fetch in ScannedBarcodeActivity

    String adminEvent = "Null for Now";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        auth = FirebaseAuth.getInstance();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("users");
        mFirebaseUser = auth.getCurrentUser();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(AdminHomeActivity.this, AdminLoginActivity.class));
                    finish();
                }
            }
        };

        signOutButton = findViewById(R.id.admin_btn_sign_out);

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        //Spinner Data Set

        Spinner spinner = (Spinner) findViewById(R.id.events_spinner);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.events_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        pickTime = (Button) findViewById(R.id.pick_time_button);
        timeText = (TextView) findViewById(R.id.time_text_view);

        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                updateTimeLabel();
            }
        };

        pickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(AdminHomeActivity.this, time, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), false);
                //Date currentTime = Calendar.getInstance().getTime();
                timePickerDialog.show();
            }
        });

        updateButton = findViewById(R.id.admin_btn_update);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference databaseReference = mFirebaseDatabase.getReference();
                String key = databaseReference.push().getKey();
//                EventClass eventClass = new EventClass(adminEvent);
//                databaseReference.child("Event Details").child("Current Event").setValue(eventClass);

                if (TextUtils.isEmpty(stime)) {
                    Toast.makeText(AdminHomeActivity.this, "Please Select Time of Event", Toast.LENGTH_SHORT).show();
                    return;
                }

                else {

                    EventClass eventClass = new EventClass(adminEvent, stime);
                    databaseReference.child("Event Details").child("Current Event").setValue(eventClass);
                    Toast.makeText(AdminHomeActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();

                }

            }
        });

        summaryButton = findViewById(R.id.summary_button);

        summaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHomeActivity.this, EventSummaryActivity.class));
            }
        });

    }

    private void updateTimeLabel() {
        String myFormat = "h:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        stime = sdf.format(myCalendar.getTime());
        timeText.setText(stime);
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }


    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            auth.removeAuthStateListener(authStateListener);
        }
    }

    private void signOut() {
        auth.signOut();
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        adminEvent = (String) parent.getItemAtPosition(pos);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
        adminEvent = "Temporary Null";
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}
