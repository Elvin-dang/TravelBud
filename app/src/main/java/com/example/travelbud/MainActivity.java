package com.example.travelbud;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.travelbud.ui.my_trips.DestinationsActivity;
import com.example.travelbud.ui.my_trips.MyTripsFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.travelbud.databinding.ActivityMainBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_my_trips, R.id.navigation_home, R.id.navigation_network,
                R.id.navigation_my_profile)
                .build();
        NavController navController = Navigation.findNavController(this,
                R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        db = FirebaseFirestore.getInstance();


        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController,
                                             @NonNull NavDestination navDestination,
                                             @Nullable Bundle bundle) {
                Log.i("asd", navDestination.getDisplayName());
            }
        });

        Fragment t = new MyTripsFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.navigation_my_trips, t);
//        fragmentTransaction.add(R.id.fragmnt_container, fb);
//        fragmentTransaction.add(R.id.fragmnt_container, fc);
        fragmentTransaction.hide(t);
        fragmentTransaction.commit();


    }


    public View initSampleData(View view) {


        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        //add a new user
        TravelBudUser user1 = new TravelBudUser("John", new ArrayList<Trip>(),
                new ArrayList<TravelBudUser>());
        TravelBudUser user2 = new TravelBudUser("Doe", new ArrayList<Trip>(),
                new ArrayList<TravelBudUser>());
        TravelBudUser user3 = new TravelBudUser("Bib", new ArrayList<Trip>(),
                new ArrayList<TravelBudUser>());


        List<Trip> trips = new ArrayList<>();


        // Create a new user with a first, middle, and last name
        ArrayList<TravelBudUser> a = new ArrayList<>();
        a.add(user1);
        a.add(user2);
        a.add(user3);
        double loc1Lat = new Double(-33.914714);
        double loc1Lng = new Double(151.201957);
        double loc2Lat = new Double(-33.599152);
        double loc2Lng = new Double(151.325675);

        Destination d1 = new Destination("1", loc1Lat, loc1Lng, "123", "11", "1111",
                new ArrayList<>());
        Destination d2 = new Destination("2", loc2Lat, loc2Lng, "2", "2", "2", new ArrayList<>());
        List<Destination> destinations = new ArrayList<>();
        destinations.add(d1);
        destinations.add(d2);

        ArrayList<ChecklistItem> checklist = new ArrayList<>();

        ChecklistItem item1 = new ChecklistItem("soap", 1, false, "essential");
        ChecklistItem item2 = new ChecklistItem("flashlight", 2, false, "survival");
        ChecklistItem item3 = new ChecklistItem("tent", 3, true, "survival");
        checklist.add(item1);
        checklist.add(item2);
        checklist.add(item3);


        Trip trip1 = new Trip("Hawaii Trip", new ArrayList<>(), "Sydney", checklist, destinations);
        Trip trip2 = new Trip("Michigan Trip", new ArrayList<>(), "MIMI", checklist, destinations);

        trips.add(trip1);
        trips.add(trip2);

        List<TravelBudUser> users = new ArrayList<>();
        List<TravelBudUser> friends = new ArrayList<>();
        friends.add(user3);
        friends.add(user2);
        trip1.setTravelers(friends);
        user1.setTrips(trips);
        user1.setFriends(friends);
        users.add(user1);
        trip2.setTravelers(friends);

        FirebaseUtils.insert(mDatabase, user1);


        return view;
    }

}