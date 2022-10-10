package com.example.travelbud.ui.my_trips;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbud.R;
import com.example.travelbud.TravelBudUser;
import com.example.travelbud.adapter.TransitCardsAdapter;
import com.example.travelbud.adapter.TripCardsAdapter;
import com.example.travelbud.databinding.FragmentMyTripsBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyTripsFragment extends Fragment {

    private FragmentMyTripsBinding binding;
    private View transit_card;
    private DatabaseReference mDatabase;
    public TravelBudUser current_user;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        MyTripsViewModel myTripsViewModel =
                new ViewModelProvider(this).get(MyTripsViewModel.class);

        binding = FragmentMyTripsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        myTripsViewModel.getUser("John").observe(getViewLifecycleOwner(), user -> {
            current_user = user;
            RecyclerView rv = (RecyclerView) root.findViewById(R.id.trips_rv);
            rv.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(root.getContext());
            rv.setLayoutManager(llm);
            TripCardsAdapter adapter = new TripCardsAdapter(user.getTrips());
            rv.setAdapter(adapter);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}