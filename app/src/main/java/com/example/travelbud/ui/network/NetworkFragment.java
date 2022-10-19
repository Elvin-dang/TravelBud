package com.example.travelbud.ui.network;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.travelbud.GroupChat;
import com.example.travelbud.databinding.FragmentNetworkBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NetworkFragment extends Fragment {

    private FragmentNetworkBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NetworkViewModel networkViewModel =
                new ViewModelProvider(this).get(NetworkViewModel.class);

        binding = FragmentNetworkBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        getGroupChat();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getGroupChat() {
        List<GroupChat> groupChatList = new ArrayList<>();

        // Sample Data
        groupChatList.add(new GroupChat("Tokyo Trip", "Hi all", new Date()));
        groupChatList.add(new GroupChat("Sydney Tour", "Let's go!!", new Date()));
        groupChatList.add(new GroupChat("ABC", "XYZ", new Date()));

        GroupChatAdapter groupChatAdapter = new GroupChatAdapter(groupChatList);
        binding.chatRv.setAdapter(groupChatAdapter);
    }
}