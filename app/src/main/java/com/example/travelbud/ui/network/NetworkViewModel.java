package com.example.travelbud.ui.network;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.travelbud.GroupChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NetworkViewModel extends ViewModel {

    private MutableLiveData<List<GroupChat>> groupChat;
    DatabaseReference mDatabase;

    public NetworkViewModel() {
        groupChat = new MutableLiveData<>();
    }

    public LiveData<List<GroupChat>> getGroupChat() {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("users").child(FirebaseAuth.getInstance().getUid()).child("trips").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ArrayList<String> tripIdList = (ArrayList<String>) snapshot.getValue();
                    List<GroupChat> groupChats = new ArrayList<>();
                    for (String tripId : tripIdList) {
                        mDatabase.child("groupchats").child(tripId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot groupChatSnapshot) {
                                if (groupChatSnapshot.exists()) {
                                    GroupChat fetchedGroupChat = groupChatSnapshot.getValue(GroupChat.class);
                                    groupChats.add(fetchedGroupChat);
                                    groupChat.postValue(groupChats);
                                } else {
                                    Log.v("Network", "Fail to get group chat");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                } else {
                    Log.v("Network", "Fail to get user trips");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return groupChat;
    }
}