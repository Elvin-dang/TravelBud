package com.example.travelbud.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbud.Trip;
import com.example.travelbud.R;
import com.example.travelbud.Trip;

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
    public void onBindViewHolder(TripViewHolder tripViewHolder, int i) {
        tripViewHolder.trip_name.setText(" "+trips.get(i).getName());
        tripViewHolder.trip_members.setText(trips.get(i).getTravelers().get(0).getUsername());
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

       public TripViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.trip_card);
            trip_name = (TextView) itemView.findViewById(R.id.trip_name);
            trip_members = (TextView) itemView.findViewById(R.id.trip_members);
            add_friend = (ImageButton) itemView.findViewById(R.id.add_friend);
        }
    }

    public interface ClickListener {

        void onPositionClicked(int position);

        void onLongClicked(int position);
    }
}

