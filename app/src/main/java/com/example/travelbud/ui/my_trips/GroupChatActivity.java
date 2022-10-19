package com.example.travelbud.ui.my_trips;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Intent;
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
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupChatActivity extends AppCompatActivity {

    private static final String TAG = "MESSAGE_ACTIVITY";

    CircleImageView profileImage;
    TextView username;
    ImageButton sendBtn;
    EditText sendText;

    Intent intent;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    GroupChatAdapter chatAdapter;
    List<Chat> chats;
    RecyclerView recyclerView;

    int trip_index = -1;
    private String tripKey;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        String uid = FirebaseAuth.getInstance().getUid();
        MyTripsViewModel myTripsViewModel = new ViewModelProvider(this).get(MyTripsViewModel.class);

        profileImage = findViewById(R.id.profile_image);
        username = findViewById(R.id.header_username);
        sendBtn = findViewById(R.id.btn_send);
        sendText = findViewById(R.id.text_send);

        recyclerView = findViewById(R.id.chat_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        Bundle bundle = getIntent().getExtras();
        intent = getIntent();

        //group chats
        if (intent.getBooleanExtra("is_group_chat",false)){
            trip_index = Integer.parseInt(intent.getStringExtra("selected_trip"));
            connectToChatGroup(trip_index);
            sendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String msg = sendText.getText().toString();
                    if (!msg.equals("")) {
                        sendGroupMessage(firebaseUser.getUid(), tripKey, msg);
                    } else {
                        Toast.makeText(GroupChatActivity.this, "No message to send", Toast.LENGTH_LONG).show();
                    }
                    sendText.setText("");
                }
            });
        }else{
            //individual chat
            userid = intent.getStringExtra("userid");
            connectToChatFriend(userid);
            sendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String msg = sendText.getText().toString();
                    if (!msg.equals("")) {
                        sendMessage(firebaseUser.getUid(), userid, msg);
                    } else {
                        Toast.makeText(GroupChatActivity.this, "No message to send", Toast.LENGTH_LONG).show();
                    }
                    sendText.setText("");
                }
            });
        }





    }

    private void connectToChatGroup(int trip_index) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid()).orderByChild("trips").equalTo(trip_index);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String tripKey = snapshot.getKey();
                readMessageHistory(tripKey);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readMessageHistory(String tripKey) {
        this.tripKey = tripKey;
        chats = new ArrayList<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("gchats").child(tripKey);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
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
        reference = FirebaseDatabase.getInstance().getReference("trips").child(tripKey).child("name");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username.setText(snapshot.getValue(String.class));
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








    private void sendMessage(String sender, String receiver, String message) {
        reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        reference.child("Chats").push().setValue(hashMap);
    }


    private void sendGroupMessage(String sender, String tripKey, String message) {
        reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("message", message);

        reference.child("gchats/"+tripKey).push().setValue(hashMap);

    }

    private void readMessage() {
        chats = new ArrayList<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid()+"/trip/"+trip_index);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String trip = snapshot.getValue(String.class);
                readChats(trip);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readChats(String key) {
        chats = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("chat").child(key);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
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

    //todo think before nest
    private void connectToChatFriend(String userid) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("users").orderByKey().equalTo(firebaseUser.getUid());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TravelBudUser user = snapshot.getValue(TravelBudUser.class);
                username.setText(user.getUsername());
//                if (user.getImageURL().equals("default")) {
//                    profileImage.setImageResource(R.mipmap.ic_launcher);
//                } else {
                Glide.with(GroupChatActivity.this).load("default").into(profileImage);

                //readMessage(firebaseUser.getUid(), userid, "default");
                readMessage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.v(TAG, "Friend connection fail : " + error.getMessage());
            }
        });

    }
}