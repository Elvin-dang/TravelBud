package com.example.travelbud.ui.my_trips;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Toast;

import com.example.travelbud.Destination;
import com.example.travelbud.FirebaseUtils;
import com.example.travelbud.LoginActivity;
import com.example.travelbud.R;
import com.example.travelbud.TravelBudUser;
import com.example.travelbud.Trip;
import com.example.travelbud.adapter.ChecklistItemsAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GoogleMap mMap;

    String detail = "-";
    LatLng latLng;


    private TravelBudUser current_user;


    public DestinationDialogFragment() {
        // Required empty public constructor
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

        SharedPreferences prefs = getActivity().getSharedPreferences("user_token", Context.MODE_PRIVATE);
        String user_token = prefs.getString("user_token",null);

        MyTripsViewModel myTripsViewModel = new ViewModelProvider(this).get(MyTripsViewModel.class);

        myTripsViewModel.getUser(user_token).observe(this, user -> {
            current_user = user;

        });



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_destination_dialog, container, false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_destination_dialog, null);


        // initializing our search view.
        SearchView searchView = view.findViewById(R.id.idSearchView);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getParentFragmentManager().findFragmentById(R.id.map1);


        // adding on query listener for our search view.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    searchView.clearFocus();
                    // on below line we are getting the
                    // location name from search view.
                    String location = searchView.getQuery().toString();

                    // below line is to create a list of address
                    // where we will store the list of all address.
                    List<Address> addressList = null;

                    // checking if the entered location is null or not.
                    if (location != null || location.equals("")) {
                        // on below line we are creating and initializing a geo coder.
                        Geocoder geocoder = new Geocoder(getActivity());
                        try {
                            // on below line we are getting location from the
                            // location name and adding that location to address list.
                            addressList = geocoder.getFromLocationName(location, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // on below line we are getting the location
                        // from our list a first position.

                        if(addressList.size()>0){
                            Address address = addressList.get(0);
                            // on below line we are creating a variable for our location
                            // where we will add our locations latitude and longitude.
                            latLng = new LatLng(address.getLatitude(), address.getLongitude());

                            detail = address.getAddressLine(0);

                            // on below line we are adding marker to that position.
                            mMap.clear();
                            mMap.addMarker(new MarkerOptions().position(latLng).title(location));

                            // below line is to animate camera to that position.
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                        }else {
                            Toast.makeText(getActivity(), "No result...", Toast.LENGTH_SHORT).show();

                        }



                    }
                }catch (Exception e){
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


        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        try{
                            EditText editText =
                                    (EditText) getDialog().findViewById(R.id.input_des_name);

                            if (!"".equals(editText.getText().toString().trim())) {
                                Log.i("SEE HERE",current_user.getTrips().size()+"");
                                Destination destination = new Destination();
                                destination.setAddress(editText.getText().toString());
                                destination.setSubtitle(detail);
                                destination.setLat(latLng.latitude);
                                destination.setLng(latLng.longitude);

                                current_user.getTrips().get(Integer.parseInt(getActivity().getIntent().getExtras().getString("selected_trip"))).getDestinations().add(destination);

                                FirebaseUtils.update(FirebaseDatabase.getInstance().getReference(),current_user);
                            }else {
                                Toast.makeText(getActivity(), "Invalid trip name!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
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