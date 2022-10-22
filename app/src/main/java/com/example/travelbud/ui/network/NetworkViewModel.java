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
import java.util.stream.Collectors;

public class NetworkViewModel extends ViewModel {

    private MutableLiveData<List<GroupChat>> groupChat;
    private ValueEventListener eventListener;
    DatabaseReference mDatabase;

    public NetworkViewModel() {
        this.groupChat = new MutableLiveData<>();
    }

    public LiveData<List<GroupChat>> getGroupChat() {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        eventListener = mDatabase.child("users").child(FirebaseAuth.getInstance().getUid()).child("trips").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mDatabase.removeEventListener(eventListener);
                if (snapshot.exists()) {
                    ArrayList<String> tripIdList = (ArrayList<String>) snapshot.getValue();
                    liveChatListUpdate(tripIdList);
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

    private void liveChatListUpdate(ArrayList<String> tripIdList) {
        mDatabase = FirebaseDatabase.getInstance().getReference("groupchats");
        List<GroupChat> groupChats = new ArrayList<>();
        groupChats.clear();
        for (String tripId : tripIdList) {
            eventListener = mDatabase.child(tripId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot groupChatSnapshot) {
                    mDatabase.removeEventListener(eventListener);
                    if (groupChatSnapshot.exists()) {
                        GroupChat fetchedGroupChat = groupChatSnapshot.getValue(GroupChat.class);
                        fetchedGroupChat.setKey(groupChatSnapshot.getKey());

                        if (groupChats.size() >= tripIdList.size()) {
                            for (int i = 0; i < groupChats.size(); i++) {
                                if (groupChats.get(i).getKey().equals(groupChatSnapshot.getKey())) {
                                    groupChats.set(i, fetchedGroupChat);
                                    break;
                                }
                            }
                        } else groupChats.add(fetchedGroupChat);

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

    }
}