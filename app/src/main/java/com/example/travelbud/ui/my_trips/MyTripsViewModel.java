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
        MutableLiveData<TravelBudUser> user = new MutableLiveData<>();

        mDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    for (DataSnapshot s : snapshot.getChildren()) {
                        TravelBudUser user = s.getValue(TravelBudUser.class);
                        user.setKey(s.getKey());
                        if (user.getKey().equals(user_token)) {
                            fetched_user.postValue(user);
                            return;
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}