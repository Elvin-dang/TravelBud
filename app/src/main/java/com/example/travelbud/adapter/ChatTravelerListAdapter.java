package com.example.travelbud.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.travelbud.R;
import com.example.travelbud.TravelBudUser;
import com.example.travelbud.ui.my_trips.MessageActivity;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.List;

public class ChatTravelerListAdapter extends RecyclerView.Adapter<ChatTravelerListAdapter.ChatTravelerListViewHolder> {

    private List<HashMap<String,String>> travelerModelList;

    public void ChatTravelerListAdapter(List<HashMap<String,String>> travelerModelList) {
        this.travelerModelList = travelerModelList;
    }

    @NonNull
    @Override
    public ChatTravelerListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_chat_item,
                parent, false);
        return new ChatTravelerListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatTravelerListViewHolder holder, int position) {
        HashMap<String,String> travelerModel= travelerModelList.get(position);
        holder.traveler_name.setText(travelerModel.get("username"));

        //individual chat
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MessageActivity.class);
                intent.putExtra("traveler",travelerModel);
                intent.putExtra("is_group",false);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(travelerModelList == null){
            return 0;
        }else {
            return travelerModelList.size();
        }
    }

    public class ChatTravelerListViewHolder extends RecyclerView.ViewHolder {
        TextView traveler_name,tripName;
        CardView card;
        View item;

        public ChatTravelerListViewHolder(View itemView) {
            super(itemView);
            card = (CardView) itemView.findViewById(R.id.traveler_chat_card);
            traveler_name = (TextView) itemView.findViewById(R.id.traveler_name);
            item = itemView;
        }
    }

}
