package com.example.travelbud.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbud.R;
import com.example.travelbud.model.BillModel;

import java.util.List;

public class BillListAdapter extends RecyclerView.Adapter<BillListAdapter.BillListHolderView> {

    private List<BillModel> billModelList;


    public BillListAdapter(List<BillModel> billModelList, double total, double perPerson, double currentUserAmount) {
        this.billModelList = billModelList;
    }

    @NonNull
    @Override
    public BillListHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.budget_item,
                parent, false);
        return new BillListHolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillListHolderView holder, int position) {
        BillModel billModel= billModelList.get(position);



        holder.payer.setText(billModel.getPayer());
        holder.amount.setText(billModel.getAmount()+"" );
        holder.description.setText(billModel.getDescription());

        //.traveler_name.setText(billModel.getName());

//        holder.card.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), MessageActivity.class);
//                intent.putExtra("userid",billModel.getKey());
//                intent.putExtra("is_group",false);
//                v.getContext().startActivity(intent);
//            }
//        });

        //clicking the trip item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, MessageActivity.class);
//                intent.putExtra("userid",user.getId());
//                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(billModelList == null){
            return 0;
        }else {
            return billModelList.size();
        }
    }

    public class BillListHolderView extends RecyclerView.ViewHolder {
        TextView amount, description,payer;



        public BillListHolderView(View itemView) {
            super(itemView);
            //card = (CardView) itemView.findViewById(R.id.traveler_item_card);
            amount = (TextView) itemView.findViewById(R.id.amount_txt);
            description = (TextView) itemView.findViewById(R.id.bill_description);
            payer = (TextView) itemView.findViewById(R.id.payer_name);

        }
    }

}
