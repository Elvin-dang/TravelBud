package com.example.travelbud.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbud.FirebaseUtils;
import com.example.travelbud.TravelBudUser;
import com.example.travelbud.R;
import com.example.travelbud.Trip;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.List;

public class UserCardsAdapter extends RecyclerView.Adapter<UserCardsAdapter.UserViewHolder> {

    List<TravelBudUser> users;
    Boolean isAdd;
    TravelBudUser curr_user;
    int trip_index;


    public List<TravelBudUser> getUsers() {
        return users;
    }

    public void setUsers(List<TravelBudUser> users) {
        this.users = users;
    }

    public UserCardsAdapter(List<TravelBudUser> users, Boolean isAdd, TravelBudUser curr_user,
                            int trip_index) {
        this.users = users;
        this.isAdd = isAdd;
        this.curr_user = curr_user;
        this.trip_index = trip_index;
    }


    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_user_card,
                parent, false);
        UserViewHolder pvh = new UserViewHolder(v);
        if (!isAdd) {
            ImageButton the_button = v.findViewById(R.id.add_or_remove_user);
            the_button.setImageResource(R.drawable.ic_baseline_remove_circle_outline_24);
        }

        return pvh;
    }

    @Override
    public void onBindViewHolder(UserViewHolder userViewHolder, int pos) {

        int position = userViewHolder.getAdapterPosition();
        TravelBudUser selected_trip = users.get(position);

        userViewHolder.username.setText(selected_trip.getUsername());

        ImageButton add_or_remove_user = userViewHolder.view.findViewById(R.id.add_or_remove_user);


        add_or_remove_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("CAO",position+"");
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                if (isAdd) {

                    List<TravelBudUser> temp_travelers =
                            curr_user.getTrips().get(trip_index).getTravelers();
                    temp_travelers.add(users.get(position));

                    curr_user.getTrips().get(trip_index).setTravelers(temp_travelers);
                    FirebaseUtils.update(mDatabase, curr_user);

                } else {
                    List<TravelBudUser> temp_travelers =
                            curr_user.getTrips().get(trip_index).getTravelers();
                    temp_travelers.remove(position);

                    curr_user.getTrips().get(trip_index).setTravelers(temp_travelers);
                    FirebaseUtils.update(mDatabase, curr_user);
                }
            }
        });

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView username;
        View view;
        ImageButton addOrRemoveButton;


        public UserViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.user_card);
            username = (TextView) itemView.findViewById(R.id.user_name);
            addOrRemoveButton = (ImageButton) itemView.findViewById(R.id.add_or_remove_user);
            this.view = itemView;

        }


    }

}

