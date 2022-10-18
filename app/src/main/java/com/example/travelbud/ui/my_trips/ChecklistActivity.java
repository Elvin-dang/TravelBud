package com.example.travelbud.ui.my_trips;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbud.ChecklistItem;
import com.example.travelbud.Destination;
import com.example.travelbud.FirebaseUtils;
import com.example.travelbud.R;
import com.example.travelbud.TravelBudUser;
import com.example.travelbud.adapter.ChecklistItemsAdapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChecklistActivity extends AppCompatActivity {
    private TravelBudUser current_user;
    private int trip_index;

    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Item Checklist");

        add = findViewById(R.id.checklistItems_add);

        Bundle bundle = getIntent().getExtras();
        trip_index = bundle.getInt("selected_trip");
//        Log.i("checklist", "current: " + trip_index);

        SharedPreferences prefs = this.getSharedPreferences("user_token", Context.MODE_PRIVATE);
        String user_token = prefs.getString("user_token",null);

        MyTripsViewModel myTripsViewModel = new ViewModelProvider(this).get(MyTripsViewModel.class);

        myTripsViewModel.getUser(user_token).observe(this, user -> {
            current_user = user;
            RecyclerView rv = (RecyclerView) findViewById(R.id.checklistItems_rv);
            rv.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
            rv.setLayoutManager(llm);
            if(user.getTrips().size()>0){
                ChecklistItemsAdapter adapter = new ChecklistItemsAdapter(
                        user.getTrips().get(trip_index).getCheckList(), current_user);
                rv.setAdapter(adapter);
            }else{
                ChecklistItemsAdapter adapter = new ChecklistItemsAdapter(
                        new ArrayList<>(), current_user);
                rv.setAdapter(adapter);
            }
        });

        setupListener();
    }

    private void setupListener() {
        ActivityResultLauncher<Intent> mLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        ChecklistItem newItem = new ChecklistItem(
                                result.getData().getStringExtra("name"),
                                Integer.parseInt(result.getData().getStringExtra("amount")),
                                false,
                                result.getData().getStringExtra("category")
                        );

                        // update database
//                        current_user.getTrips().get(trip_index).getCheckList().add(newItem);
//                        FirebaseUtils.update(
//                                FirebaseDatabase.getInstance().getReference(), current_user);
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                        mDatabase.child("trips").child(current_user.getTrips().get(trip_index).getKey()).child("checklist").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                Log.i("hERE","ASDSADHSAUDIHASHDUIDHAUS");

                                List<ChecklistItem> checklist = (List<ChecklistItem>)task.getResult().getValue();
                                List<ChecklistItem> temp = new ArrayList<>();
                                if(checklist != null){
                                    checklist.add(newItem);
                                    mDatabase.child("trips").child(current_user.getTrips().get(trip_index).getKey()).child("checklist").setValue(checklist);

                                }else{
                                    temp.add(newItem);
                                    mDatabase.child("trips").child(current_user.getTrips().get(trip_index).getKey()).child("checklist").setValue(temp);

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
