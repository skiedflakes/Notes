package com.wdysolutions.notes.Globals.Petty_Cash.Request;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wdysolutions.notes.Globals.Petty_Cash.Request.modal_view.PettyCash_modal_view;
import com.wdysolutions.notes.MainActivity;
import com.wdysolutions.notes.R;

import java.util.ArrayList;

public class PettyCash_request_adapter extends RecyclerView.Adapter<PettyCash_request_adapter.MyHolder> {

    ArrayList<PettyCash_request_model> mdata;
    private Context context;
    private LayoutInflater inflater;
    EventListener listener;

    public PettyCash_request_adapter(Context context, ArrayList<PettyCash_request_model> data,EventListener listener){
        this.context = context;
        this.mdata = data;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.pettycash_request_row,viewGroup,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, final int position) {
        final String getRemarks = mdata.get(position).getRemarks();
        final String getDate_requested = mdata.get(position).getDate_requested();
        final String getCreated_by = mdata.get(position).getCreated_by();
        final String getAmount = mdata.get(position).getAmount();
        final String getId = mdata.get(position).getId();
        final String getApproved_by = mdata.get(position).getApproved_by();
        final String getBr_id = mdata.get(position).getBr_id();
        final String getCount = mdata.get(position).getCount();
        final String getCredit_method_test = mdata.get(position).getCredit_method_test();
        final String getDate_encoded = mdata.get(position).getDate_encoded();
        final String getDeclared_status = mdata.get(position).getDeclared_status();
        final String getHid = mdata.get(position).getHid();
        final String getDeclared_status_color = mdata.get(position).getDeclared_status_color();
        final String getDnr_stat = mdata.get(position).getDnr_stat();
        final String getLiquidate_color = mdata.get(position).getLiquidate_color();
        final String getLiquidate_stats = mdata.get(position).getLiquidate_stats();
        final String getPcv = mdata.get(position).getPcv();
        final String getLiquidation_stat = mdata.get(position).getLiquidation_stat();
        final String getReceipt_status = mdata.get(position).getReceipt_status();
        final String getReceipt_status_color = mdata.get(position).getReceipt_status_color();
        final String getRep_stat = mdata.get(position).getRep_stat();
        final String getRep_stats = mdata.get(position).getRep_stats();
        final String getRfr_status = mdata.get(position).getRfr_status();
        final String getRfr_status_color = mdata.get(position).getRfr_status_color();
        final String getStats = mdata.get(position).getStats();
        final String getStats_color = mdata.get(position).getStats_color();
        final String getUserID = mdata.get(position).getUserID();

        myHolder.txt_count.setText(String.valueOf(position+1));
        myHolder.txt_amount.setText(getAmount);
        myHolder.txt_approved.setText(getApproved_by);
        myHolder.txt_code.setText(getPcv);
        myHolder.txt_created_by.setText(getCreated_by);
        myHolder.txt_date_requested.setText(getDate_requested);
        myHolder.txt_remarks.setText(getRemarks);
        myHolder.txt_requested_by.setText(getUserID);

        myHolder.txt_approval.setText(getStats);
        if (getStats_color.equals("green")){
            myHolder.txt_approval.setTextColor(context.getResources().getColor(R.color.color_green));
        } else { // orange
            myHolder.txt_approval.setTextColor(context.getResources().getColor(R.color.color_orange));
        }

        myHolder.txt_receipt.setText(getReceipt_status);
        if (getReceipt_status_color.equals("green")){
            myHolder.txt_receipt.setTextColor(context.getResources().getColor(R.color.color_green));
        } else if (getReceipt_status_color.equals("orange")){
            myHolder.txt_receipt.setTextColor(context.getResources().getColor(R.color.color_orange));
        } else {
            myHolder.txt_receipt.setTextColor(context.getResources().getColor(R.color.color_txt));
        }

        myHolder.txt_liquidation.setText(getLiquidate_stats);
        if (getLiquidate_color.equals("green")){
            myHolder.txt_liquidation.setTextColor(context.getResources().getColor(R.color.color_green));
        } else if (getLiquidate_color.equals("red")){
            myHolder.txt_liquidation.setTextColor(context.getResources().getColor(R.color.color_btn_red));
        } else { // orange
            myHolder.txt_liquidation.setTextColor(context.getResources().getColor(R.color.color_orange));
        }

        myHolder.txt_replenish.setText(getRfr_status);
        if (getRfr_status_color.equals("green")){
            myHolder.txt_replenish.setTextColor(context.getResources().getColor(R.color.color_green));
        } else if (getRfr_status_color.equals("red")){
            myHolder.txt_replenish.setTextColor(context.getResources().getColor(R.color.color_btn_red));
        } else { // orange
            myHolder.txt_replenish.setTextColor(context.getResources().getColor(R.color.color_orange));
        }

        myHolder.txt_status.setText(getDeclared_status);
        if (getDeclared_status_color.equals("green")){
            myHolder.txt_status.setTextColor(context.getResources().getColor(R.color.color_green));
        } else { // orange
            myHolder.txt_status.setTextColor(context.getResources().getColor(R.color.color_orange));
        }

        if(!getApproved_by.equals("")){
            myHolder.actions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.view_modal(position,getId,getDate_requested,getUserID,getBr_id,getPcv,getRemarks,1,1,0,1);
                }
            });
        }else{
            myHolder.actions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.view_modal(position,getId,getDate_requested,getUserID,getBr_id,getPcv,getRemarks,1,1,1,0);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        TextView txt_count,txt_code,txt_created_by,txt_requested_by,txt_remarks,txt_amount,txt_date_requested,
                txt_receipt,txt_approval,txt_liquidation,txt_replenish,txt_status,txt_approved;
        ImageView actions;
        public MyHolder(View itemView) {
            super(itemView);
            actions = itemView.findViewById(R.id.actions);
            txt_count = itemView.findViewById(R.id.txt_count);
            txt_code = itemView.findViewById(R.id.txt_code);
            txt_created_by = itemView.findViewById(R.id.txt_created_by);
            txt_requested_by = itemView.findViewById(R.id.txt_requested_by);
            txt_remarks = itemView.findViewById(R.id.txt_remarks);
            txt_amount = itemView.findViewById(R.id.txt_amount);
            txt_date_requested = itemView.findViewById(R.id.txt_date_requested);
            txt_receipt = itemView.findViewById(R.id.txt_receipt);
            txt_approval = itemView.findViewById(R.id.txt_approval);
            txt_liquidation = itemView.findViewById(R.id.txt_liquidation);
            txt_replenish = itemView.findViewById(R.id.txt_replenish);
            txt_status = itemView.findViewById(R.id.txt_status);
            txt_approved = itemView.findViewById(R.id.txt_approved);
        }
    }


    public interface  EventListener{
        void view_modal(final int position,String getId,String getDate_requested,String getUserID,
                        String getBr_id,String getPcv,String getRemarks,int view_details,int micro_filming,int approve,int disapprove);

    }


}
