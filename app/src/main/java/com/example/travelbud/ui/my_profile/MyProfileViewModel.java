package com.example.travelbud.ui.my_profile;

import android.widget.Button;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

public class MyProfileViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MyProfileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is my profile fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}