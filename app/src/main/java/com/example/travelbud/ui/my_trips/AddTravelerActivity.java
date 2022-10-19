package com.example.travelbud.ui.my_trips;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.travelbud.R;
import com.example.travelbud.TravelBudUser;
import com.example.travelbud.adapter.UserCardsAdapter;
import com.example.travelbud.ui.my_trips.MyTripsViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AddTravelerActivity extends AppCompatActivity {

    int trip_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_traveler);


        Bundle bundle = getIntent().getExtras();
        trip_index = Integer.parseInt(getIntent().getStringExtra("selected_trip"));

//        SharedPreferences prefs = this.getSharedPreferences("user_token", Context.MODE_PRIVATE);
//        String user_token = prefs.getString("user_token",null);

        String uid = FirebaseAuth.getInstance().getUid();

        MyTripsViewModel myTripsViewModel = new ViewModelProvider(this).get(MyTripsViewModel.class);

        getUsers().observe(this, users -> {

//            TravelBudUser curr_user = null;

            myTripsViewModel.getUser(uid).observe(this, currentUser -> {
                if (currentUser.getTrips().size() > 0 && trip_index < currentUser.getTrips().size()) {
                    UserCardsAdapter adapter =
                            new UserCardsAdapter(currentUser.getTrips().get(trip_index).getTravelers(), false,
                                    currentUser, trip_index);

                    RecyclerView rv = (RecyclerView) findViewById(R.id.travelers_rv);
                    rv.setHasFixedSize(true);
                    LinearLayoutManager llm = new LinearLayoutManager(this);
                    rv.setLayoutManager(llm);
                    rv.setAdapter(adapter);

                    List<TravelBudUser> filtered_users;
                    List<TravelBudUser> travelers = currentUser.getTrips().get(trip_index).getTravelers();

                    Set<String> childIds = travelers.stream()
                            .map(TravelBudUser::getUsername)
                            .collect(Collectors.toSet());

                    TravelBudUser finalCurr_user = currentUser;
                    filtered_users = users.stream().filter(person ->
                            !childIds.contains(person.getUsername()) && !person.getUsername().equals(finalCurr_user.getUsername())
                    ).collect(Collectors.toList());


                    UserCardsAdapter adapter1 = new UserCardsAdapter(filtered_users, true, currentUser,
                            trip_index);

                    RecyclerView rv1 = (RecyclerView) findViewById(R.id.users_rv);
                    rv1.setHasFixedSize(true);
                    LinearLayoutManager llm1 = new LinearLayoutManager(this);
                    rv1.setLayoutManager(llm1);
                    rv1.setAdapter(adapter1);
                }
            });

//            for (int i = 0; i < users.size(); i++) {
//                if (users.get(i).getKey().equals(user_token)) {
//                    curr_user = users.get(i);
//                    break;
//                }
//            }

//            UserCardsAdapter adapter =
//                    new UserCardsAdapter(curr_user.getTrips().get(trip_index).getTravelers(), false,
//                            curr_user, trip_index);
//
//            RecyclerView rv = (RecyclerView) findViewById(R.id.travelers_rv);
//            rv.setHasFixedSize(true);
//            LinearLayoutManager llm = new LinearLayoutManager(this);
//            rv.setLayoutManager(llm);
//            rv.setAdapter(adapter);
//
//            List<TravelBudUser> filtered_users;
//            List<TravelBudUser> travelers = curr_user.getTrips().get(trip_index).getTravelers();
//
//            Set<String> childIds = travelers.stream()
//                    .map(TravelBudUser::getUsername)
//                    .collect(Collectors.toSet());
//
////            for (int i = 0; i<travelers.size(); i++) Log.v("ABC", travelers.get(i).getUsername());
//
//            TravelBudUser finalCurr_user = curr_user;
//            filtered_users = users.stream().filter(person ->
//                    !childIds.contains(person.getUsername()) && !person.getUsername().equals(finalCurr_user.getUsername())
//            ).collect(Collectors.toList());
//
//
//            UserCardsAdapter adapter1 = new UserCardsAdapter(filtered_users, true, curr_user,
//                    trip_index);
//
//            RecyclerView rv1 = (RecyclerView) findViewById(R.id.users_rv);
//            rv1.setHasFixedSize(true);
//            LinearLayoutManager llm1 = new LinearLayoutManager(this);
//            rv1.setLayoutManager(llm1);
//            rv1.setAdapter(adapter1);
        });
    }

    public MutableLiveData<List<TravelBudUser>> getUsers() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        MutableLiveData<List<TravelBudUser>> users = new MutableLiveData<>();

        mDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    ArrayList<TravelBudUser> temp = new ArrayList<>();
                    for (DataSnapshot s : snapshot.getChildren()) {
                        TravelBudUser user = s.getValue(TravelBudUser.class);
                        user.setKey(s.getKey());
                        temp.add(user);

                    }
                    users.postValue(temp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return users;
    }
}