package com.example.travelbud.ui.my_trips;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbud.R;
import com.example.travelbud.TravelBudUser;
import com.example.travelbud.adapter.ChecklistItemsAdapter;
import com.example.travelbud.adapter.TransitCardsAdapter;

public class ChecklistActivity extends AppCompatActivity {
    private TravelBudUser current_user;
    private int trip_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Item Checklist");

        Bundle bundle = getIntent().getExtras();
        trip_index = bundle.getInt("selected_trip");
        Log.i("checklist", "current: " + trip_index);

        MyTripsViewModel myTripsViewModel = new ViewModelProvider(this).get(MyTripsViewModel.class);

//        username to be changed
        myTripsViewModel.getUser("John").observe(this, user -> {
            current_user = user;
            RecyclerView rv = (RecyclerView) findViewById(R.id.checklistItems_rv);
            rv.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
            rv.setLayoutManager(llm);
            ChecklistItemsAdapter adapter =
                    new ChecklistItemsAdapter(user.getTrips().get(trip_index).getCheckList());
            rv.setAdapter(adapter);
        });


    }
}
