package com.example.travelbud.ui.my_trips;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.travelbud.Destination;
import com.example.travelbud.R;
import com.example.travelbud.TravelBudUser;
import com.example.travelbud.Trip;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DestinationDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DestinationDialogFragment extends DialogFragment implements OnMapReadyCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String detail = "-";
    LatLng latLng;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private GoogleMap mMap;
    private TravelBudUser current_user;


    public DestinationDialogFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TripDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DestinationDialogFragment newInstance(String param1, String param2) {
        DestinationDialogFragment fragment = new DestinationDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        SharedPreferences prefs = getActivity().getSharedPreferences("user_token",
                Context.MODE_PRIVATE);
        String user_token = prefs.getString("user_token", null);

        MyTripsViewModel myTripsViewModel = new ViewModelProvider(this).get(MyTripsViewModel.class);

        myTripsViewModel.getUser(user_token).observe(this, user -> {
            current_user = user;

        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_destination_dialog, container, false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_destination_dialog, null);

        SearchView searchView = view.findViewById(R.id.idSearchView);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getParentFragmentManager().findFragmentById(R.id.map1);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    searchView.clearFocus();
                    String location = searchView.getQuery().toString();
                    List<Address> addressList = null;
                    if (location != null || location.equals("")) {
                        Geocoder geocoder = new Geocoder(getActivity());
                        try {
                            addressList = geocoder.getFromLocationName(location, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (addressList.size() > 0) {
                            Address address = addressList.get(0);
                            latLng = new LatLng(address.getLatitude(), address.getLongitude());

                            detail = address.getAddressLine(0);

                            mMap.clear();
                            mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                        } else {
                            Toast.makeText(getActivity(), "No result...", Toast.LENGTH_SHORT).show();

                        }


                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Search error...", Toast.LENGTH_SHORT).show();

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });
        mapFragment.getMapAsync(this);

        builder.setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        try {
                            EditText editText =
                                    (EditText) getDialog().findViewById(R.id.input_des_name);

                            if (!"".equals(editText.getText().toString().trim())) {
                                Log.i("SEE HERE", current_user.getTrips().size() + "");
                                Destination destination = new Destination();
                                destination.setAddress(editText.getText().toString());
                                destination.setSubtitle(detail);
                                destination.setLat(latLng.latitude);
                                destination.setLng(latLng.longitude);

                                Trip curr_trip =
                                        current_user.getTrips().get(Integer.parseInt(getActivity().getIntent().getExtras().getString("selected_trip")));
                                DatabaseReference mDatabase =
                                        FirebaseDatabase.getInstance().getReference();
                                mDatabase.child("trips").child(curr_trip.getKey()).child(
                                        "destinations").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        List<Destination> destinations =
                                                (List<Destination>) task.getResult().getValue();
                                        List<Destination> temp = new ArrayList<>();
                                        if (destinations != null) {
                                            destinations.add(destination);
                                            mDatabase.child("trips").child(curr_trip.getKey()).child("destinations").setValue(destinations);

                                        } else {
                                            temp.add(destination);
                                            mDatabase.child("trips").child(curr_trip.getKey()).child("destinations").setValue(temp);

                                        }

                                    }
                                });


                            } else {
                                Toast.makeText(getActivity(), "Invalid trip name!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e("EX", e.toString());
                            Toast.makeText(getActivity(), "Please search!",
                                    Toast.LENGTH_SHORT).show();
                        }


                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("ASDSADASD", "ASDASDAS");
                        DestinationDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("REMOVE", "R");

        Fragment fragment = (getFragmentManager().findFragmentById(R.id.map1));
        if (fragment != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();
        }


    }
}