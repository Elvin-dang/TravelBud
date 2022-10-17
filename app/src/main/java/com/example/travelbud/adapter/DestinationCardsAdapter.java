package com.example.travelbud.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbud.Destination;
import com.example.travelbud.R;

import java.util.List;

public class DestinationCardsAdapter extends RecyclerView.Adapter<DestinationCardsAdapter.DestinationViewHolder> {

    List<Destination> destinations;

    public DestinationCardsAdapter(List<Destination> destinations) {
        this.destinations = destinations;
    }

    public DestinationCardsAdapter() {

    }

    public List<Destination> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<Destination> destinations) {
        this.destinations = destinations;
    }

    @NonNull
    @Override
    public DestinationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_destination_card, parent, false);
        DestinationViewHolder pvh = new DestinationViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(DestinationViewHolder destinationViewHolder, int i) {
        destinationViewHolder.address.setText(destinations.get(i).getAddress());
        destinationViewHolder.subtitle.setText(destinations.get(i).getSubtitle());

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public int getItemCount() {
        return destinations.size();
    }

    public static class DestinationViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView address;
        TextView subtitle;
        TextView detailed_address;

        DestinationViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.destination_card);
            address = (TextView) itemView.findViewById(R.id.address);
            subtitle = (TextView) itemView.findViewById(R.id.subtitle);
        }
    }

}