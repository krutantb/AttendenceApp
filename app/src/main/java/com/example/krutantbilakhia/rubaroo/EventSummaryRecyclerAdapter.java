package com.example.krutantbilakhia.rubaroo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

import java.util.List;

public class EventSummaryRecyclerAdapter extends RecyclerView.Adapter<EventSummaryRecyclerAdapter.MyHoder> {

    public ClubDetailsClass mylist;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, databaseReference2;
    List<ClubDetailsClass> list;
    Context context;

    public EventSummaryRecyclerAdapter(List<ClubDetailsClass> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @Override
    public MyHoder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_summary_adapter, parent, false);
        MyHoder myHoder = new MyHoder(view);

        return myHoder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHoder myHoder, int i) {

        mylist = list.get(i);
        databaseReference = FirebaseDatabase.getInstance().getReference();


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
        TextView clubName, clubTotal;
        int clubTotalAsInt;

        public MyHoder(View itemView) {
            super(itemView);
            clubName = (TextView) itemView.findViewById(R.id.clubName);
            clubTotal = (TextView) itemView.findViewById(R.id.nTotal);


        }
    }

}
