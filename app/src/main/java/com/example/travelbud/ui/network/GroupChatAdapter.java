package com.example.travelbud.ui.network;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbud.GroupChat;
import com.example.travelbud.databinding.ItemGroupChatContainerBinding;
import com.example.travelbud.ui.my_trips.GroupChatActivity;

import java.text.SimpleDateFormat;
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
                    Log.v("TESTTT", String.valueOf(itemPosition));
                    // fake chat intent added by ZJ
                    Intent intent = new Intent(view.getContext(), GroupChatActivity.class);
                    intent.putExtra("is_group_chat", true);
                    intent.putExtra("selected_trip", String.valueOf(itemPosition));
                    view.getContext().startActivity(intent);

                }
            });
        }

        public void setGroupChatData(GroupChat groupChat) {
            SimpleDateFormat df = new SimpleDateFormat("hh:mm dd-MM-yyyy", Locale.US);

            binding.grpChatImage.setAvatarInitials(groupChat.name);
            binding.grpChatName.setText(groupChat.name);
            binding.latestMsg.setText(groupChat.latestMessage);
            binding.latestMsgTime.setText(df.format(groupChat.time));
        }
    }
}
