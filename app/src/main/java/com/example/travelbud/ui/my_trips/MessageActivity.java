package com.example.travelbud.ui.my_trips;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbud.R;
import com.example.travelbud.TravelBudUser;
import com.example.travelbud.adapter.MessageAdapter;
import com.example.travelbud.model.ChatModel;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    private static final String TAG = "MESSAGE_ACTIVITY";
    CircleImageView profileImage;
    TextView username;
    ImageButton sendBtn;
    EditText sendText;
    Intent intent;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    MessageAdapter messageAdapter;
    MessageAdapter groupChatAdapter;
    List<ChatModel> chats;
    List<ChatModel> gChats;
    RecyclerView recyclerView;
    private ValueEventListener eventListenerReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sendBtn = findViewById(R.id.btn_send);
        sendText = findViewById(R.id.text_send);

        recyclerView = findViewById(R.id.chat_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        intent = getIntent();
        boolean isGroupChat = intent.getBooleanExtra("is_group", false);
        HashMap<String, String> traveler = (HashMap<String, String>) intent.getSerializableExtra(
                "traveler");
        String tripKey = intent.getStringExtra("trip_key");
        if (isGroupChat) {
            //connectToChatGroup(tripKey);
        } else {
            connectToChatFriend(traveler);
            getSupportActionBar().setTitle(traveler.get("username"));
        }

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = sendText.getText().toString();
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (!msg.equals("")) {
                    if (isGroupChat) {
                        //sendGroupMessage(tripKey,firebaseUser.getUid(),msg);
                    } else {
                        sendMessage(firebaseUser.getUid(), traveler, msg);
                    }
                } else {
                    Toast.makeText(MessageActivity.this, "No message to send", Toast.LENGTH_LONG).show();
                }
                sendText.setText("");
            }
        });


    }


    private void sendMessage(String sender, HashMap<String, String> receiver, String message) {
        reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver.get("altKey"));
        hashMap.put("message", message);
        reference.child("chats").push().setValue(hashMap);
    }

    private void readMessage(String reader, String sender, String imageUrl) {
        chats = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chats.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    ChatModel chat = data.getValue(ChatModel.class);
                    if (chat.getReceiver().equals(reader) && chat.getSender().equals(sender) ||
                            chat.getReceiver().equals(sender) && chat.getSender().equals(reader)) {
                        chats.add(chat);
                    }
                    messageAdapter = new MessageAdapter(MessageActivity.this, chats, imageUrl,
                            false);
                    messageAdapter.notifyDataSetChanged();
                    recyclerView.clearOnChildAttachStateChangeListeners();
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void connectToChatFriend(HashMap<String, String> traveler) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users").child(traveler.get(
                "altKey"));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TravelBudUser user = snapshot.getValue(TravelBudUser.class);
                //username.setText(user.getUsername());
                readMessage(firebaseUser.getUid(), traveler.get("altKey"), "default");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.v(TAG, "Friend connection fail : " + error.getMessage());
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