package com.example.travelbud.ui.my_trips;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.travelbud.R;
import com.example.travelbud.TravelBudUser;
import com.example.travelbud.adapter.TransitCardsAdapter;
import com.example.travelbud.databinding.ActivityDestinationsBinding;
import com.example.travelbud.databinding.ActivityMainBinding;

public class DestinationsActivity extends AppCompatActivity {
    private ActivityDestinationsBinding binding;
    private TravelBudUser current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destinations);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        MyTripsViewModel myTripsViewModel = new ViewModelProvider(this).get(MyTripsViewModel.class);


        myTripsViewModel.getUser("John").observe(this, user -> {
            current_user = user;
            RecyclerView rv = (RecyclerView) findViewById(R.id.destinations_rv);
            rv.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
            rv.setLayoutManager(llm);
            TransitCardsAdapter adapter =
                    new TransitCardsAdapter(user.getTrips().get(0).getDestinations());
            rv.setAdapter(adapter);
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}