package com.example.travelbud.ui.my_trips;

import static android.content.ContentValues.TAG;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbud.Destination;
import com.example.travelbud.FirebaseUtils;
import com.example.travelbud.R;
import com.example.travelbud.TravelBudUser;
import com.example.travelbud.Trip;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MyTripsViewModel extends ViewModel {
    private MutableLiveData<TravelBudUser> fetched_user;
    FirebaseFirestore db;
    DatabaseReference mDatabase;

    public MyTripsViewModel() {

    }

    //TODO: use username mimic uuid, will change later
    public LiveData<TravelBudUser> getUser(String user_token) {
        if (fetched_user == null) {
            fetched_user = new MutableLiveData<TravelBudUser>();
            loadUser(user_token);
        }
        return fetched_user;
    }

    //TODO: use username mimic uuid, will change later
    public void loadUser(String user_token) {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("users").child(user_token).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    TravelBudUser user = snapshot.getValue(TravelBudUser.class);
                    user.setKey(snapshot.getKey());
                    fetched_user.postValue(user);

                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                        if (dataSnapshot.getKey().equals("trips")) {
                            List<String> tripList = dataSnapshot.getValue(List.class);

                            for (String s: tripList) {
                                mDatabase.child("trips").child(s).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            Trip trip = snapshot.getValue(Trip.class);
                                            trip.setKey(snapshot.getKey());

                                            boolean check = true;
                                            List<Trip> userTripList = user.getTrips();
                                            for (int i = 0; i<userTripList.size(); i++) {
                                                if (userTripList.get(i).getKey().equals(trip.getKey())) {
                                                    userTripList.set(i, trip);
                                                    user.setTrips(userTripList);
                                                    check = false;
                                                    break;
                                                }
                                            }
                                            if (check) {
                                                userTripList.add(trip);
                                                user.setTrips(userTripList);
                                            }
//                                            fetched_user.postValue(user);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                    }
                    fetched_user.postValue(user);
//                    for (DataSnapshot s : snapshot.getChildren()) {
//                        TravelBudUser user = s.getValue(TravelBudUser.class);
//                        user.setKey(s.getKey());
//                        if (user.getKey().equals(user_token)) {
//                            fetched_user.postValue(user);
//                            return;
//                        }
//                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}