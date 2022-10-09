package com.example.travelbud.ui.my_trips;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbud.R;
import com.example.travelbud.TransitCardsAdapter;
import com.example.travelbud.databinding.FragmentDestinationsBinding;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyTripsFragment extends Fragment {

    private FragmentDestinationsBinding binding;
    private View transit_card;
    private DatabaseReference mDatabase;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        MyTripsViewModel homeViewModel =
                new ViewModelProvider(this).get(MyTripsViewModel.class);

        binding = FragmentDestinationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        homeViewModel.getDestinations().observe(getViewLifecycleOwner(), destinations -> {
            RecyclerView rv = (RecyclerView) root.findViewById(R.id.destinations_rv);
            rv.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(root.getContext());
            rv.setLayoutManager(llm);
            TransitCardsAdapter adapter = new TransitCardsAdapter(destinations);
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