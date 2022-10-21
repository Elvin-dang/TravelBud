package com.example.travelbud.ui.my_trips;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.travelbud.R;

import com.example.travelbud.model.BillModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BillAddingActivity extends AppCompatActivity {

    TextView description, amount;
    Button saveBtn;

    private DatabaseReference reference;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_adding);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("New Bill");

        description = findViewById(R.id.bill_description_txt);
        amount = findViewById(R.id.bill_amount_txt);
        saveBtn = findViewById(R.id.bill_add_btn);

        Intent intent = getIntent();
        String tripKey = intent.getExtras().getString("trip_key");

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBill(tripKey);
                finish();
            }
        });

    }



    private void addBill(String tripKey) {

        reference = FirebaseDatabase.getInstance().getReference("bills").child(tripKey).push();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        BillModel bill = new BillModel(description.getText().toString(),Double.parseDouble(amount.getText().toString()),
                firebaseUser.getUid());
        reference.setValue(bill).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //TODO
                }
            }
        });
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