package com.example.travelbud.ui.home;

import static android.content.ContentValues.TAG;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.travelbud.Destination;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<List<Destination>> destinations;
    FirebaseFirestore db;

    public LiveData<List<Destination>> getDestinations() {
        if (destinations == null) {
            destinations = new MutableLiveData<List<Destination>>();
            loadDestinations();
        }
        return destinations;
    }

    public void loadDestinations() {
        db = FirebaseFirestore.getInstance();

        // do async operation to fetch articles
        db.collection("destinations")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<Destination> fetched_destinations = new ArrayList<>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Map data = document.getData();
                                ArrayList<String> a = new ArrayList<>();
                                a.add("a");
                                a.add("b");
                                Location targetLocation = new Location("");//provider name is
                                // unnecessary

                                Destination des = new Destination(data.get("address").toString(),
                                        targetLocation, data.get("name").toString(), data.get(
                                        "subtitle").toString(), data.get(
                                        "detailed_address").toString(), a);

                                fetched_destinations.add(des);

                                Log.d(TAG, document.getId() + " => " + document.getData().get(
                                        "address"));
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                        destinations.setValue(fetched_destinations);
                    }
                });
    }


    public HomeViewModel() {

    }


}