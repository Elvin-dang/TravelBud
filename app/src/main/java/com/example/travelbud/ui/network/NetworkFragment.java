package com.example.travelbud.ui.network;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.travelbud.databinding.FragmentNetworkBinding;

public class NetworkFragment extends Fragment {

    private FragmentNetworkBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NetworkViewModel networkViewModel =
                new ViewModelProvider(this).get(NetworkViewModel.class);

        binding = FragmentNetworkBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        getGroupChat(networkViewModel);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getGroupChat(NetworkViewModel networkViewModel) {
        networkViewModel.getGroupChat().observe(getViewLifecycleOwner(), groupChats -> {
            GroupChatAdapter groupChatAdapter = new GroupChatAdapter(groupChats);
            binding.chatRv.setAdapter(groupChatAdapter);
        });
    }
}