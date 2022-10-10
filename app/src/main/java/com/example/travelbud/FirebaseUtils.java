package com.example.travelbud;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.travelbud.adapter.TransitCardsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public final class FirebaseUtils {

    public static void insert(final DatabaseReference mDatabaseRef, TravelBudUser user) {
        if (user == null) {
            return;
        } else {
            mDatabaseRef.child("users").push().setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.i("SUCCESS", "SUCCESS");
                    } else {
                        Log.i("FAIL", "FAIL");

                    }
                }
            });
        }

    }


    public static void select(DatabaseReference mDatabaseRef, TransitCardsAdapter adapter) {

        mDatabaseRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<TravelBudUser> users = new ArrayList<>();

                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    for (DataSnapshot s : snapshot.getChildren()) {
                        TravelBudUser user = s.getValue(TravelBudUser.class);
                        user.setKey(s.getKey());
                        users.add(user);
                        Log.i("asd1", s.getKey());

                    }
                    //TODO with users


                    adapter.setDestinations(users.get(0).getTrips().get(0).getDestinations());
                    Log.i("laguziji", users.get(0).getKey());

                    adapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public static void update(DatabaseReference mDatabaseRef, TravelBudUser updatedUser) {
        if (updatedUser == null) {
            return;
        }
        mDatabaseRef.child("users").child(updatedUser.getKey()).setValue(updatedUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.i("SUCCESS", "SUCCESS");

                } else {
                    Log.i("FAIL", "FAIL");

                }
            }
        });
    }


    public static void delete(DatabaseReference mDatabaseRef, TravelBudUser selectedUser) {
        if (selectedUser == null) {
            return;
        }
        final String selectedUserKey = selectedUser.getKey();
        mDatabaseRef.child("users").child(selectedUserKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.i("SUCCESS", "SUCCESS");

                } else {
                    Log.i("FAIL", "FAIL");

                }
            }
        });
    }


}