package com.example.travelbud.ui.my_trips;


import android.content.Context;
import android.content.SharedPreferences;
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

        SharedPreferences prefs = getActivity().getSharedPreferences("user_token", Context.MODE_PRIVATE);
        String user_token = prefs.getString("user_token",null);

        myTripsViewModel.getUser(user_token).observe(getViewLifecycleOwner(), user -> {
            current_user = user;
//            Log.i("???",user.getTrips().get(1).getName()+"");

            TripCardsAdapter adapter = new TripCardsAdapter(user.getTrips());
//
            RecyclerView rv = (RecyclerView) root.findViewById(R.id.trips_rv);
            rv.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(root.getContext());
            rv.setLayoutManager(llm);
            rv.setAdapter(adapter);
//
//            RecyclerView rv = (RecyclerView) root.findViewById(R.id.destinations_rv);
//            rv.setHasFixedSize(true);
//            LinearLayoutManager llm = new LinearLayoutManager(root.getContext());
//            rv.setLayoutManager(llm);
//            TransitCardsAdapter adapter = new TransitCardsAdapter(destinations);
//            rv.setAdapter(adapter);
        });


//        View v = root.findViewById(R.id.fragment_destinations);
//        View     v1 = root.findViewById(R.id.main_page);
//
//        v1.setVisibility(View.GONE);
//        v.setVisibility(View.VISIBLE);
        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}