package com.example.travelbud;

import static android.content.ContentValues.TAG;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.travelbud.databinding.ActivityMainBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
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

    }


    public View populateDestination(View view) {
        // Create a new user with a first, middle, and last name
        ArrayList<String> a = new ArrayList<>();
        a.add("a");
        a.add("b");
        Location targetLocation = new Location("");//provider name is unnecessary

        Destination d1 = new Destination("1", targetLocation, "123", "11", "1111", a);
        Destination d2 = new Destination("2", targetLocation, "2", "2", "2", a);
        List<Destination> test = new ArrayList<>();
        test.add(d1);
        test.add(d1);
        test.add(d1);
        test.add(d2);

        test.forEach(des -> {
            Map<String, Object> destination = new HashMap<>();

            destination.put("address", des.getAddress());
            destination.put("name", des.getName());
            destination.put("subtitle", des.getSubtitle());
            destination.put("detailed_address", des.getDetailed_address());
            // Add a new document with a generated ID
            db.collection("destinations")
                    .add(destination)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG,
                                    "DocumentSnapshot added with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
        });


        return view;
    }

}