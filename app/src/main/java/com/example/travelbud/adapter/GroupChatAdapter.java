package com.example.travelbud.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbud.Chat;
import com.example.travelbud.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import io.getstream.avatarview.AvatarView;

public class GroupChatAdapter extends RecyclerView.Adapter<GroupChatAdapter.ViewHolder> {
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private final Context context;
    private final List<Chat> chats;
    private final String imageUrl;
    private int itemViewType;

    public GroupChatAdapter(Context context, List<Chat> chats, String imageUrl) {
        this.context = context;
        this.chats = chats;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public GroupChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        itemViewType = viewType;
        if (viewType == MSG_TYPE_LEFT) {
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
        }
        return new GroupChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupChatAdapter.ViewHolder holder, int position) {
        Chat chat = chats.get(position);

        if (itemViewType == MSG_TYPE_LEFT) {
            holder.senderName.setText(chat.getName());
        }
        holder.profileImage.setAvatarInitials(chat.getName());
        holder.showMessage.setText(chat.getMessage());
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    @Override
    public int getItemViewType(int position) {
        String uid = FirebaseAuth.getInstance().getUid();
        if (chats.get(position).getSender().equals(uid)) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView showMessage;
        public AvatarView profileImage;
        public TextView senderName;

        public ViewHolder(View itemView) {
            super(itemView);

            senderName = itemView.findViewById(R.id.sender_name);
            showMessage = itemView.findViewById(R.id.show_message);
            profileImage = itemView.findViewById(R.id.profile_image);
        }
    }
}
