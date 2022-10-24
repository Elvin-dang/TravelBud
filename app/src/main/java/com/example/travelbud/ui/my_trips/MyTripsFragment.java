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
import com.example.travelbud.TravelBudUser;
import com.example.travelbud.adapter.TripCardsAdapter;
import com.example.travelbud.databinding.FragmentMyTripsBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MyTripsFragment extends Fragment {

    public TravelBudUser current_user;
    private FragmentMyTripsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        MyTripsViewModel myTripsViewModel =
                new ViewModelProvider(this).get(MyTripsViewModel.class);

        binding = FragmentMyTripsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        String uid = FirebaseAuth.getInstance().getUid();

        myTripsViewModel.getUser(uid).observe(getViewLifecycleOwner(), user -> {
            current_user = user;

            TripCardsAdapter adapter = new TripCardsAdapter(user.getTrips());
            RecyclerView rv = (RecyclerView) root.findViewById(R.id.trips_rv);
            rv.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(root.getContext());
            rv.setLayoutManager(llm);
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