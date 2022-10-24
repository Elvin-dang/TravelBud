package com.example.travelbud.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbud.ChecklistItem;
import com.example.travelbud.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ChecklistItemsAdapter extends RecyclerView.Adapter<ChecklistItemsAdapter.ChecklistItemsViewHolder> {
    List<ChecklistItem> checklistItems;
    String key;

    public ChecklistItemsAdapter(List<ChecklistItem> checklistItems, String key) {
        this.checklistItems = checklistItems;
        this.key = key;
    }

    @NonNull
    @Override
    public ChecklistItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.fragment_item_card, parent, false);

        ChecklistItemsViewHolder pvh = new ChecklistItemsViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ChecklistItemsViewHolder holder, int i) {
        holder.name.setText(checklistItems.get(i).getName());
        holder.category.setText(checklistItems.get(i).getCategory());
        holder.amount.setText(String.valueOf(checklistItems.get(i).getAmount()));
        holder.checked.setChecked(checklistItems.get(i).getChecked());
        holder.checked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checked.isChecked()) {
                    holder.checked.setChecked(true);
                    checklistItems.get(holder.getAdapterPosition()).setChecked(true);
                } else {
                    holder.checked.setChecked(false);
                    checklistItems.get(holder.getAdapterPosition()).setChecked(false);
                }

                // update database
                DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                mDatabaseRef.child("trips").child(key).child("checkList").setValue(checklistItems);
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return checklistItems.size();
    }

    public static class ChecklistItemsViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView name;
        TextView category;
        TextView amount;
        CheckBox checked;

        ChecklistItemsViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.item_card);
            name = (TextView) itemView.findViewById(R.id.name);
            category = (TextView) itemView.findViewById(R.id.category);
            amount = (TextView) itemView.findViewById(R.id.amount);
            checked = (CheckBox) itemView.findViewById(R.id.checked);
        }
    }
}


