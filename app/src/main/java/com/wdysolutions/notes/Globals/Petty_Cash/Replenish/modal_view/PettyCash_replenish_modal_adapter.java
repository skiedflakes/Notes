package com.wdysolutions.notes.Globals.Petty_Cash.Replenish.modal_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wdysolutions.notes.R;

import java.util.ArrayList;

public class PettyCash_replenish_modal_adapter extends RecyclerView.Adapter<PettyCash_replenish_modal_adapter.MyHolder> {

    ArrayList<PettyCash_replenish_modal_model> mdata;
    private Context context;
    private LayoutInflater inflater;

    public PettyCash_replenish_modal_adapter(Context context, ArrayList<PettyCash_replenish_modal_model> data){
        this.context = context;
        this.mdata = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.pettycash_replenish_modal_row,viewGroup,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, final int position) {
        final String getBranch = mdata.get(position).getBranch();
        final String getAmount = mdata.get(position).getAmount();
        final String getId = mdata.get(position).getId();
        final String getHid = mdata.get(position).getHid();
        final String getLiquidation = mdata.get(position).getLiquidation();

        myHolder.txt_count.setText(String.valueOf(position+1));
        myHolder.txt_liquidation_num.setText(getLiquidation);
        myHolder.txt_amount.setText(getAmount);
        myHolder.txt_branch.setText(getBranch);
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        TextView txt_count,txt_amount,txt_liquidation_num,txt_branch;
        public MyHolder(View itemView) {
            super(itemView);
            txt_count = itemView.findViewById(R.id.txt_count);
            txt_liquidation_num = itemView.findViewById(R.id.txt_liquidation_num);
            txt_branch = itemView.findViewById(R.id.txt_branch);
            txt_amount = itemView.findViewById(R.id.txt_amount);
        }
    }
}
