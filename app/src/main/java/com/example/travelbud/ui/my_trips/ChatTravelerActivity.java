package com.example.travelbud.ui.my_trips;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbud.R;
import com.example.travelbud.adapter.ChatTravelerListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatTravelerActivity extends AppCompatActivity {

    TextView tripNameText;
    private String tripkey, tripName;
    private DatabaseReference reference;
    private FirebaseUser firebaseUser;
    private RecyclerView recyclerView;
    private ChatTravelerListAdapter travelerAdapter;
    private final List<HashMap<String, String>> listOfUsers = new ArrayList<>();
    private ValueEventListener eventListenerReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_traveler);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tripNameText = findViewById(R.id.trip_name);

        //group chat
        RelativeLayout chatStartBtn = findViewById(R.id.group_chat_start);

        Intent intent = getIntent();
        tripkey = intent.getExtras().getString("trip_key");
        tripName = intent.getExtras().getString("trip_name");

        tripNameText.setText("Group Chat : " + tripName);

        //individual chat
        recyclerView = findViewById(R.id.traveller_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        travelerAdapter = new ChatTravelerListAdapter();
        recyclerView.clearOnChildAttachStateChangeListeners();
        recyclerView.setAdapter(travelerAdapter);


        //message starts with group
        chatStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), GroupChatActivity.class);
                intent.putExtra("groupChatName", tripName);
                intent.putExtra("groupChatKey", tripkey);
                intent.putExtra("index", "-1");
                startActivity(intent);
            }
        });

        getTravelers();
    }

    private void getTravelers() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("trips").child(tripkey +
                "/travelers");
        listOfUsers.clear();
        eventListenerReference = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reference.removeEventListener(eventListenerReference);
                List<HashMap<String, String>> travelers = new ArrayList<>();
                travelers.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    HashMap<String, String> traveler = (HashMap<String, String>) data.getValue();
                    if (!firebaseUser.getUid().equals(traveler.get("altKey"))) {
                        traveler.put("tripname", tripName);
                        setUserItem(traveler);
                    }
                }
                travelerAdapter.ChatTravelerListAdapter(listOfUsers);
                travelerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void setUserItem(HashMap<String, String> traveler) {
        listOfUsers.add(traveler);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}