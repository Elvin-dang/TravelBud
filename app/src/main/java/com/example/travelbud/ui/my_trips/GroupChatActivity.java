package com.example.travelbud.ui.my_trips;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.travelbud.Chat;
import com.example.travelbud.R;
import com.example.travelbud.TravelBudUser;
import com.example.travelbud.Trip;
import com.example.travelbud.adapter.GroupChatAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupChatActivity extends AppCompatActivity {

    ImageButton sendBtn;
    EditText sendText;

    Intent intent;
    DatabaseReference reference;

    GroupChatAdapter chatAdapter;
    List<Chat> chats;
    RecyclerView recyclerView;

    int trip_index = -1;
    private String tripKey;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        SharedPreferences settings = getSharedPreferences("user_token", 0);

        userName = settings.getString("user_name", "");

        intent = getIntent();

        tripKey = intent.getStringExtra("groupChatKey");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(intent.getStringExtra("groupChatName"));

        String uid = FirebaseAuth.getInstance().getUid();

        sendBtn = findViewById(R.id.btn_send);
        sendText = findViewById(R.id.text_send);

        recyclerView = findViewById(R.id.chat_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        trip_index = Integer.parseInt(intent.getStringExtra("index"));
        readMessageHistory(tripKey);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = sendText.getText().toString();

                if (!msg.equals("")) {
                    sendGroupMessage(uid, tripKey, msg);
                } else {
                    Toast.makeText(GroupChatActivity.this, "No message to send", Toast.LENGTH_LONG).show();
                }

                sendText.setText("");
            }
        });
    }

    private void readMessageHistory(String tripKey) {
        chats = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("groupchats").child(tripKey).child("chatList");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chats = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Chat chat = data.getValue(Chat.class);
                    chats.add(chat);
                }
                chatAdapter = new GroupChatAdapter(GroupChatActivity.this, chats, "default");
                recyclerView.setAdapter(chatAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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


    private void sendGroupMessage(String sender, String tripKey, String message) {
        reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("name", userName);
        hashMap.put("message", message);
        hashMap.put("time", (new Date()).getTime());

        Map<String, Object> updates = new HashMap<>();
        updates.put("latestMessage", message);
        updates.put("time", (new Date()).getTime());

        reference.child("groupchats").child(tripKey).updateChildren(updates);
        reference.child("groupchats").child(tripKey).child("chatList").push().setValue(hashMap);
    }
}