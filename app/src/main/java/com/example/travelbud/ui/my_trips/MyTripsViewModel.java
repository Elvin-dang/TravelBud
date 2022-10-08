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
    private MutableLiveData<List<Destination>> destinations;
    FirebaseFirestore db;
    DatabaseReference mDatabase;


    public LiveData<List<Destination>> getDestinations() {
        if (destinations == null) {
            destinations = new MutableLiveData<List<Destination>>();
            loadDestinations();
        }
        return destinations;
    }

    public void loadDestinations() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        MutableLiveData<List<Destination>> fetched_destinations = new MutableLiveData<>();

        mDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<TravelBudUser> users = new ArrayList<>();

                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    for (DataSnapshot s : snapshot.getChildren()) {
                        TravelBudUser user = s.getValue(TravelBudUser.class);
                        user.setKey(s.getKey());
                        users.add(user);
                        Log.i("asd1", s.getKey());

                    }
                }

                destinations.postValue(users.get(0).getTrips().get(0).getDestinations());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        destinations = fetched_destinations;
    }


    public MyTripsViewModel() {

    }


}