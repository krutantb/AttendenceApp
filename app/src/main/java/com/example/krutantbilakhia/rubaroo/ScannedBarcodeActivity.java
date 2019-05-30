package com.example.krutantbilakhia.rubaroo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ScannedBarcodeActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 201;
    SurfaceView surfaceView;
    TextView nameBarcodeValue, clubBarcodeValue;
    Button btnAction;
    String intentData = "";
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    String extractedName, extractedClub;
    public String adminEventForScanner, adminTimeForScanner;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference, databaseReference;
    public String nameCheck = "Name";

    List<AttendeeClass> list;
    List<EventClass> eventClassList;

    AdminHomeActivity adminHomeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_barcode);

        adminHomeActivity = new AdminHomeActivity();
        adminEventForScanner = adminHomeActivity.adminEvent;

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = mFirebaseDatabase.getReference().child("Event Details");
        final DatabaseReference databaseReferenceNew = mFirebaseDatabase.getReference();


        initViews();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    EventClass value = dataSnapshot1.getValue(EventClass.class);
                    adminEventForScanner = value.getEventName();
                    adminTimeForScanner = value.getEventTime();
                }

//                Query mDatabaseReference = mFirebaseDatabase.getReference().child("Current Event");
//
//                mDatabaseReference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        // This method is called once with the initial value and again
//                        // whenever data at this location is updated.
//                        eventClassList = new ArrayList<>();
//                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//
//                            EventClass value = dataSnapshot1.getValue(EventClass.class);
//                            EventClass fire = new EventClass();
//                            String eventName = value.getEventName();
//                            fire.setEventName(eventName);
//                            eventClassList.add(fire);
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError error) {
//                        // Failed to read value
//                        Log.w("Hello", "Failed to read value.", error.toException());
//                    }
//                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }


    private void initViews() {
        nameBarcodeValue = findViewById(R.id.nameBarcodeValue);
        clubBarcodeValue = findViewById(R.id.clubBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
        btnAction = findViewById(R.id.btnAction);

        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (extractedName.length() > 0 && extractedClub.length() > 0)
                    addToDatabase();

            }

        });
    }


    private void initialiseDetectorsAndSources() {

        Toast.makeText(getApplicationContext(), "Barcode Scanner Started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScannedBarcodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ScannedBarcodeActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {

                    nameBarcodeValue.post(new Runnable() {

                        @Override
                        public void run() {

                            intentData = barcodes.valueAt(0).displayValue;
                            extractedName = intentData.substring(0, intentData.indexOf(","));
                            nameBarcodeValue.setText(extractedName);
                            btnAction.setText("ADD ENTRY");

                        }
                    });

                    clubBarcodeValue.post(new Runnable() {
                        @Override
                        public void run() {

                            intentData = barcodes.valueAt(0).displayValue;
                            extractedClub = intentData.substring(intentData.indexOf(",") + 1);
                            clubBarcodeValue.setText(extractedClub);
                            btnAction.setText("ADD ENTRY");

                        }
                    });

                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }

    public void addToDatabase() {
        final DatabaseReference databaseReferenceDatabase = mFirebaseDatabase.getReference();
        final String key = databaseReferenceDatabase.push().getKey();
        String attendeeStatus = "Temporary";


        Calendar calendar1 = Calendar.getInstance();
        String myFormat = "h:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        String currentTime = sdf.format(calendar1.getTime());

        if (currentTime.compareTo(adminTimeForScanner) < 0) {
            attendeeStatus = "Present";
        } else if (currentTime.compareTo(adminTimeForScanner) >= 0) {
            attendeeStatus = "Late Present";
        }

        final AttendeeClass attendeeClass = new AttendeeClass(extractedName, extractedClub, attendeeStatus);

//        Query mDatabaseReference = mFirebaseDatabase.getReference();
//
//                mDatabaseReference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        // This method is called once with the initial value and again
//                        // whenever data at this location is updated.
//                        list = new ArrayList<>();
//                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//
//                            AttendeeClass value = dataSnapshot1.getValue(AttendeeClass.class);
//                            nameCheck = value.getName();
//
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError error) {
//                        // Failed to read value
//                        Log.w("Hello", "Failed to read value.", error.toException());
//                    }
//                });

        if(extractedName.equals(nameCheck))
        {
            Toast.makeText(ScannedBarcodeActivity.this, "Already Added " + extractedName, Toast.LENGTH_SHORT).show();
            //break;
        }

        else
        {
            databaseReferenceDatabase.child("Attendence").child(adminEventForScanner).child(extractedClub).child(key).setValue(attendeeClass);
            Toast.makeText(ScannedBarcodeActivity.this, "Added " + extractedName, Toast.LENGTH_SHORT).show();
           // break;
        }

    }
}
