package com.example.travelbud.ui.my_trips;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.travelbud.R;
import com.example.travelbud.TravelBudUser;
import com.example.travelbud.adapter.BillListAdapter;
import com.example.travelbud.model.BillModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class BudgetActivity extends AppCompatActivity {

    private Button budgetAdding;
    private DatabaseReference reference;
    private FirebaseUser firebaseUser;
    private RecyclerView recyclerView;
    private BillListAdapter billListAdapter;
    private String gettingUsers;
    private long gettingUsersCount;
    TextView text_view_total,perPerson_view_txt,your_pay_text;

    private static final DecimalFormat df = new DecimalFormat("0.00");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        budgetAdding = findViewById(R.id.add_budget_btn);
        recyclerView = findViewById(R.id.budget_recycler);


        Intent intent = getIntent();
        String tripKey = intent.getStringExtra("trip_key");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.clearOnChildAttachStateChangeListeners();

        text_view_total = findViewById(R.id.text_view_total);
        perPerson_view_txt =  findViewById(R.id.perPerson_view_txt);
        your_pay_text = findViewById(R.id.your_pay_text);

        getTripMemberCount(tripKey);
        readBills(tripKey);

        budgetAdding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), BillAddingActivity.class);
                intent.putExtra("trip_key",tripKey);
                v.getContext().startActivity(intent);
            }
        });


    }

    private void readBills(String tripKey) {
        reference = FirebaseDatabase.getInstance().getReference("bills").child(tripKey);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<BillModel> bills = new ArrayList<>();
                Double total = 0.0;
                Double perPerson =0.0;
                Double currentUserLiability = 0.0;
                for(DataSnapshot data: snapshot.getChildren()){
                    BillModel bill = data.getValue(BillModel.class);

                    //curent user cal
                    if(firebaseUser.getUid().equals(bill.getPayer())){
                        currentUserLiability -= bill.getAmount();
                    }
                    //trip total
                    total+=bill.getAmount();

                    //user name set
                    getUserName(bill.getPayer());

                    bill.setPayer(gettingUsers);

                    bills.add(bill);
                }
                //pe-person
                perPerson = total/gettingUsersCount;
                currentUserLiability+=perPerson;

                billListAdapter = new BillListAdapter(bills,total,perPerson,currentUserLiability);
                billListAdapter.notifyDataSetChanged();
                recyclerView.clearOnChildAttachStateChangeListeners();
                recyclerView.setAdapter(billListAdapter);


                text_view_total.setText("Total : $"+df.format(total)+"");
                if(currentUserLiability < 0) {
                    your_pay_text.setText("Your Pay : -$" + df.format(Math.abs(currentUserLiability))+"");
                }else {
                    your_pay_text.setText("Your Pay : $" +df.format(currentUserLiability)+"");
                }
                perPerson_view_txt.setText("Per person : $"+df.format(perPerson)+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getTripMemberCount(String tripKey) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("trips").child(tripKey+"/travelers");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                setCount(snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private int getUserName(String payer) {
        Query query = FirebaseDatabase.getInstance().getReference("users").orderByKey().equalTo(payer);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TravelBudUser user = snapshot.getValue(TravelBudUser.class);
                setUser(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return 6;
    }

    private void setUser(String user){
        this.gettingUsers = user;
    }

    private void setCount(long number){
        this.gettingUsersCount = number;
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