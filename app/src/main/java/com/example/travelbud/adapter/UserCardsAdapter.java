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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.List;

import io.getstream.avatarview.AvatarView;

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
        TravelBudUser selected_user = users.get(position);

        userViewHolder.username.setText(selected_user.getUsername());
        try{
            userViewHolder.user_avatar.setAvatarInitials(selected_user.getUsername().substring(0,2));

        }catch (Exception e){
            userViewHolder.user_avatar.setAvatarInitials("-");

        }

        ImageButton add_or_remove_user = userViewHolder.view.findViewById(R.id.add_or_remove_user);
        if ((selected_user.getKey() != null && selected_user.getKey().equals(FirebaseAuth.getInstance().getUid()))
            || (selected_user.getAltKey() != null && selected_user.getAltKey().equals(FirebaseAuth.getInstance().getUid()))) {
            add_or_remove_user.setVisibility(View.INVISIBLE);
        }

        add_or_remove_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("CAO", position + "");
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                if (isAdd) {
                    Trip currentTrip = curr_user.getTrips().get(trip_index);
                    TravelBudUser addedUser = users.get(position);
                    List<TravelBudUser> temp_travelers = currentTrip.getTravelers();
                    temp_travelers.add(addedUser);
                    addedUser.setAltKey(addedUser.getKey());
                    currentTrip.setTravelers(temp_travelers);

                    mDatabase.child("trips").child(currentTrip.getKey()).setValue(currentTrip);

                    mDatabase.child("users").child(addedUser.getKey()).child("trips").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                List<String> tripList = (List<String>) task.getResult().getValue();
                                if (tripList == null) tripList = new ArrayList<>();
                                tripList.add(currentTrip.getKey());
                                mDatabase.child("users").child(addedUser.getKey()).child("trips").setValue(tripList);
                            }
                            else {
                                Log.e("firebase", "Error getting data", task.getException());
                            }
                        }
                    });
                } else {
                    Trip currentTrip = curr_user.getTrips().get(trip_index);
                    TravelBudUser removedUser = users.get(position);

                    List<TravelBudUser> temp_travelers = currentTrip.getTravelers();
                    temp_travelers.remove(position);
                    currentTrip.setTravelers(temp_travelers);

                    mDatabase.child("trips").child(currentTrip.getKey()).setValue(currentTrip);

                    mDatabase.child("users").child(removedUser.getAltKey()).child("trips").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                List<String> tripList = (List<String>) task.getResult().getValue();
                                if (tripList == null) tripList = new ArrayList<>();
                                Log.v("ka", tripList.size() +"");
                                tripList.remove(currentTrip.getKey());
                                Log.v("ka1", tripList.size() +"");
                                mDatabase.child("users").child(removedUser.getAltKey()).child("trips").setValue(tripList);
                            }
                            else {
                                Log.e("firebase", "Error getting data", task.getException());
                            }
                        }
                    });
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
        AvatarView user_avatar;

        public UserViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.user_card);
            username = (TextView) itemView.findViewById(R.id.user_name);
            addOrRemoveButton = (ImageButton) itemView.findViewById(R.id.add_or_remove_user);
            user_avatar = itemView.findViewById(R.id.user_avatar);

            this.view = itemView;

        }
    }

}

