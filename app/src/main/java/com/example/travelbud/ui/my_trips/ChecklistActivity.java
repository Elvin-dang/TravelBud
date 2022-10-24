package com.example.travelbud.ui.my_trips;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbud.ChecklistItem;
import com.example.travelbud.FirebaseUtils;
import com.example.travelbud.R;
import com.example.travelbud.TravelBudUser;
import com.example.travelbud.Trip;
import com.example.travelbud.adapter.ChecklistItemsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChecklistActivity extends AppCompatActivity {
    Button add;
    private TravelBudUser current_user;
    private String tripKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Item Checklist");

        add = findViewById(R.id.checklistItems_add);

        Bundle bundle = getIntent().getExtras();
        tripKey = bundle.getString("tripKey");

        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mDatabaseRef.child("trips").child(tripKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Trip trip = dataSnapshot.getValue(Trip.class);
                trip.setKey(tripKey);
                if (trip.getCheckList() == null)
                    trip.setCheckList(new ArrayList<ChecklistItem>());

                RecyclerView rv = (RecyclerView) findViewById(R.id.checklistItems_rv);
                rv.setHasFixedSize(true);
                LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                rv.setLayoutManager(llm);
                ChecklistItemsAdapter adapter = new ChecklistItemsAdapter(
                        trip.getCheckList(), tripKey);
                rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        setupListener();
    }

    private void setupListener() {
        ActivityResultLauncher<Intent> mLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        ChecklistItem newItem = new ChecklistItem(
                                result.getData().getStringExtra("name"),
                                Integer.parseInt(result.getData().getStringExtra("amount")),
                                false,
                                result.getData().getStringExtra("category")
                        );

                        DatabaseReference mDatabaseRef =
                                FirebaseDatabase.getInstance().getReference();
                        mDatabaseRef.child("trips").child(tripKey).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (!task.isSuccessful()) {
                                    Log.e("firebase", "Error getting data", task.getException());
                                } else {
                                    Trip trip = task.getResult().getValue(Trip.class);
                                    trip.setKey(tripKey);
                                    List<ChecklistItem> checkList = trip.getCheckList();
                                    checkList.add(newItem);
                                    FirebaseUtils.updateTrip(trip);
                                }
                            }
                        });
                    }
                }
        );

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        ChecklistActivity.this, ChecklistAddActivity.class);
                mLauncher.launch(intent);
            }
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
