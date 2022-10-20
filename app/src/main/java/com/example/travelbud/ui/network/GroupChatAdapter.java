package com.example.travelbud.ui.network;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbud.GroupChat;
import com.example.travelbud.databinding.ItemGroupChatContainerBinding;
import com.example.travelbud.ui.my_trips.GroupChatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GroupChatAdapter extends RecyclerView.Adapter<GroupChatAdapter.GroupChatViewHolder> {

    private final List<GroupChat> groupChatList;

    public GroupChatAdapter(List<GroupChat> groupChatList) {
        this.groupChatList = groupChatList;
    }

    @NonNull
    @Override
    public GroupChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGroupChatContainerBinding itemGroupChatContainerBinding = ItemGroupChatContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new GroupChatViewHolder(itemGroupChatContainerBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupChatViewHolder holder, int position) {
        holder.setGroupChatData(groupChatList.get(position));
    }

    @Override
    public int getItemCount() {
        return groupChatList.size();
    }

    class GroupChatViewHolder extends RecyclerView.ViewHolder {

        ItemGroupChatContainerBinding binding;

        public GroupChatViewHolder(ItemGroupChatContainerBinding itemGroupChatContainerBinding) {
            super(itemGroupChatContainerBinding.getRoot());
            binding = itemGroupChatContainerBinding;

            itemGroupChatContainerBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int itemPosition = getLayoutPosition();

                    if (itemPosition >= 0 && itemPosition < groupChatList.size()) {
                        Intent intent = new Intent(view.getContext(), GroupChatActivity.class);
                        intent.putExtra("index", String.valueOf(itemPosition));
                        intent.putExtra("groupChatKey", groupChatList.get(itemPosition).getKey());
                        intent.putExtra("groupChatName", groupChatList.get(itemPosition).getName());
                        view.getContext().startActivity(intent);
                    }
                }
            });
        }

        public void setGroupChatData(GroupChat groupChat) {
            SimpleDateFormat df = new SimpleDateFormat("hh:mm dd-MM-yyyy", Locale.US);

            binding.grpChatImage.setAvatarInitials(groupChat.getName());
            binding.grpChatName.setText(groupChat.getName());
            binding.latestMsg.setText(groupChat.getLatestMessage());
            binding.latestMsgTime.setText(df.format(new Date(groupChat.getTime())));
        }
    }
}
