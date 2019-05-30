package com.example.krutantbilakhia.rubaroo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class EventSummaryRecyclerAdapter extends RecyclerView.Adapter<EventSummaryRecyclerAdapter.MyHoder>{

    public ClubDetailsClass mylist;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, databaseReference2;
    private FirebaseDatabase mFirebaseDatabase;
    List<ClubDetailsClass> list;
    List<AttendeeClass> listNew, listNew2;
    Context context;
    EventSummaryActivity eventSummaryActivity;
    String eventNameforPath;

    public EventSummaryRecyclerAdapter(List<ClubDetailsClass> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @Override
    public MyHoder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_summary_adapter, parent, false);
        MyHoder myHoder = new MyHoder(view);

//        adminHomeActivity = new AdminHomeActivity();
//        eventNameforPath = adminHomeActivity.adminEvent;
//
//        mFirebaseDatabase = FirebaseDatabase.getInstance();
//        final DatabaseReference databaseReference = mFirebaseDatabase.getReference().child("Event Details");
//
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//
//                    EventClass value = dataSnapshot1.getValue(EventClass.class);
//                    eventNameforPath = value.getEventName();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//
//        });

        eventSummaryActivity = new EventSummaryActivity();
        eventNameforPath = eventSummaryActivity.eventForSummary;

        return myHoder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHoder myHoder, int i) {

        mylist = list.get(i);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Club Total");

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        String clubNameforPath = mylist.getClubName();

        Query mDatabaseReference = mFirebaseDatabase.getReference().child("Attendence").child(eventNameforPath).child(clubNameforPath);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                listNew = new ArrayList<>();
                listNew2 = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    AttendeeClass value = dataSnapshot1.getValue(AttendeeClass.class);
                    AttendeeClass fire = new AttendeeClass();
                    AttendeeClass fire2 = new AttendeeClass();
                    String name = value.getName();
                    String club = value.getClub();
                    String status = value.getStatus();

                    if(status.equals("Present"))
                    {
                        fire.setName(name);
                        fire.setClub(club);
                        fire.setStatus(status);
                        listNew.add(fire);
                    }

                    if(status.equals("Late Present"))
                    {
                        fire2.setName(name);
                        fire2.setClub(club);
                        fire2.setStatus(status);
                        listNew2.add(fire2);
                    }

                }

                myHoder.presentCount = listNew.size();
                myHoder.presentMembers.setText(String.valueOf(myHoder.presentCount));

                myHoder.latePresentCount = listNew2.size();
                myHoder.latePresentMembers.setText(String.valueOf(myHoder.latePresentCount));

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Hello", "Failed to read value.", error.toException());
            }
        });

        myHoder.clubName.setText(mylist.getClubName());
        myHoder.clubCount.setText(String.valueOf(mylist.getClubCount()));

    }

    @Override
    public int getItemCount() {
        int arr = 0;
        try {
            if (list.size() == 0) {
                arr = 0;

            } else {
                arr = list.size();
            }
        } catch (Exception e) {
        }
        return arr;
    }

    class MyHoder extends RecyclerView.ViewHolder {
        TextView clubName, clubCount, presentMembers, latePresentMembers;
        int clubTotalAsInt, presentCount, latePresentCount;

        public MyHoder(View itemView) {
            super(itemView);
            clubName = (TextView) itemView.findViewById(R.id.clubName);
            clubCount = (TextView) itemView.findViewById(R.id.nTotal);
            presentMembers = itemView.findViewById(R.id.nPresent);
            latePresentMembers = itemView.findViewById(R.id.nLatePresent);
        }
    }

}
