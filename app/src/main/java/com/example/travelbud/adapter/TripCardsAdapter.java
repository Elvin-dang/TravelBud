package com.example.travelbud.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbud.ui.my_trips.AddTravelerActivity;
import com.example.travelbud.Trip;
import com.example.travelbud.R;
import com.example.travelbud.Trip;
import com.example.travelbud.ui.my_trips.ChecklistActivity;
import com.example.travelbud.ui.my_trips.DestinationsActivity;
import com.example.travelbud.ui.my_trips.GroupChatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.example.travelbud.ui.my_trips.GroupChatActivity;

import java.util.ArrayList;
import java.util.List;

public class TripCardsAdapter extends RecyclerView.Adapter<TripCardsAdapter.TripViewHolder> {

    List<Trip> trips;

    public TripCardsAdapter(List<Trip> trips) {
        this.trips = trips;
    }

    public TripCardsAdapter() {

    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_trip_card,
                parent, false);
        TripViewHolder pvh = new TripViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(TripViewHolder tripViewHolder, int pos) {
        int position = tripViewHolder.getAdapterPosition();
        Trip selected_trip = trips.get(position);
        tripViewHolder.trip_name.setText(" " + selected_trip.getName());

        String travelers_name = "";


        for (int i = 0; i < selected_trip.getTravelers().size(); i++) {

            travelers_name += (i == 0 ? "" : ", ") + selected_trip.getTravelers().get(i).getUsername();
            ;
        }


        tripViewHolder.trip_members.setText(travelers_name);


        Button destinations = tripViewHolder.view.findViewById(R.id.show_destinations);

        Button checklist = tripViewHolder.view.findViewById(R.id.show_checklist);
        Button expense = tripViewHolder.view.findViewById(R.id.show_expense);

        destinations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DestinationsActivity.class);
                intent.putExtra("selected_trip", String.valueOf(position));

                view.getContext().startActivity(intent);
                Log.i("W4K", "Click-" + position);

            }
        });
        checklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ChecklistActivity.class);
                intent.putExtra("selected_trip", position);

                view.getContext().startActivity(intent);
            }
        });
        expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "TO BE IMPLEMENTED", Toast.LENGTH_SHORT).show();
            }
        });



        add_travelers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), GroupChatActivity.class);
                intent.putExtra("is_group_chat", true);
                intent.putExtra("selected_trip", String.valueOf(position));
                v.getContext().startActivity(intent);
            }
        });


//        tripViewHolder.cv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), GroupChatActivity.class);
//                intent.putExtra("is_group_chat", true);
//                intent.putExtra("selected_trip", String.valueOf(position));
//                v.getContext().startActivity(intent);
//            }
//        });

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public int getItemCount() {
        return trips.size();
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView trip_name;
        TextView trip_members;
        ImageButton add_friend;
        View view;               // <----- here
        Button destinations;


        public TripViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.trip_card);
            trip_name = (TextView) itemView.findViewById(R.id.trip_name);
            trip_members = (TextView) itemView.findViewById(R.id.trip_members);
            //add_friend = (ImageButton) itemView.findViewById(R.id.add_friend);
            this.view = itemView;            // <----- here

        }


    }

}

