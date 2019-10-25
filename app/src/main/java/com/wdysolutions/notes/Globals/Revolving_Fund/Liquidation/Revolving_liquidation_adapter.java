package com.wdysolutions.notes.Globals.Revolving_Fund.Liquidation;

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

import com.wdysolutions.notes.Globals.Revolving_Fund.Request.Revolving_request_adapter;
import com.wdysolutions.notes.Globals.Revolving_Fund.Request.Revolving_request_model;
import com.wdysolutions.notes.Globals.Revolving_Fund.Request.modal_view.Revolving_request_modal_view;
import com.wdysolutions.notes.MainActivity;
import com.wdysolutions.notes.R;

import java.util.ArrayList;

public class Revolving_liquidation_adapter extends RecyclerView.Adapter<Revolving_liquidation_adapter.MyHolder> {

    ArrayList<Revolving_liquidation_model> mdata;
    private Context context;
    private LayoutInflater inflater;
    EventListener listener;

    public Revolving_liquidation_adapter(Context context, ArrayList<Revolving_liquidation_model> data,EventListener listener){
        this.context = context;
        this.mdata = data;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.revolving_liquidation_row,viewGroup,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, final int position) {
        final String getId = mdata.get(position).getId();
        final String getAmount = mdata.get(position).getAmount();
        final String getBr_id = mdata.get(position).getBr_id();
        final String getStatus_color = mdata.get(position).getStatus_color();
        final String getStatus = mdata.get(position).getStatus();
        final String getRfr_stat = mdata.get(position).getRfr_stat();
        final String getRfr_stat_color = mdata.get(position).getRfr_stat_color();
        final String getApproved_by = mdata.get(position).getApproved_by();
        final String getTracking_num = mdata.get(position).getTracking_num();
        final String getLiquidate_by = mdata.get(position).getLiquidate_by();
        final String getDate_liquidated = mdata.get(position).getDate_liquidated();
        final String getDate_covered = mdata.get(position).getDate_covered();
        final String getStats_color = mdata.get(position).getStats_color();
        final String getStats = mdata.get(position).getStats();

        myHolder.txt_count.setText(String.valueOf(position+1));
        myHolder.txt_tracking_num.setText(getTracking_num);
        myHolder.txt_date_covered.setText(getDate_covered);
        myHolder.txt_amount.setText(getAmount);
        myHolder.txt_date_liquidate.setText(getDate_liquidated);
        myHolder.txt_liquidated_by.setText(getLiquidate_by);
        myHolder.txt_approved_by.setText(getApproved_by);

        myHolder.txt_liquidation_status.setText(getStats);
        if (getStats_color.equals("green")){
            myHolder.txt_liquidation_status.setTextColor(context.getResources().getColor(R.color.color_green));
        } else { // orange
            myHolder.txt_liquidation_status.setTextColor(context.getResources().getColor(R.color.color_orange));
        }

        myHolder.txt_replenish.setText(getRfr_stat);
        if (getRfr_stat_color.equals("green")){
            myHolder.txt_replenish.setTextColor(context.getResources().getColor(R.color.color_green));
        } else { // orange
            myHolder.txt_replenish.setTextColor(context.getResources().getColor(R.color.color_orange));
        }

        myHolder.txt_status.setText(getStatus);
        if (getStatus_color.equals("green")){
            myHolder.txt_status.setTextColor(context.getResources().getColor(R.color.color_green));
        } else { // orange
            myHolder.txt_status.setTextColor(context.getResources().getColor(R.color.color_orange));
        }

        if(!getApproved_by.equals("")){
            myHolder.btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.view_modal(position,getTracking_num,0,1,0,1);
                }
            });
        }else{
            myHolder.btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.view_modal(position,getTracking_num,0,1,1,0);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        TextView btn_edit,txt_count, txt_tracking_num,txt_date_covered,txt_amount,txt_date_liquidate,txt_liquidation_status,txt_liquidated_by,
        txt_replenish,txt_status,txt_approved_by;
        public MyHolder(View itemView) {
            super(itemView);
            txt_count = itemView.findViewById(R.id.txt_count);
            txt_tracking_num = itemView.findViewById(R.id.txt_tracking_num);
            txt_date_covered = itemView.findViewById(R.id.txt_date_covered);
            txt_amount = itemView.findViewById(R.id.txt_amount);
            txt_date_liquidate = itemView.findViewById(R.id.txt_date_liquidate);
            txt_liquidation_status = itemView.findViewById(R.id.txt_liquidation_status);
            txt_liquidated_by = itemView.findViewById(R.id.txt_liquidated_by);
            txt_replenish = itemView.findViewById(R.id.txt_replenish);
            txt_status = itemView.findViewById(R.id.txt_status);
            txt_approved_by = itemView.findViewById(R.id.txt_approved_by);
            btn_edit= itemView.findViewById(R.id.btn_edit);
        }
    }

    public interface  EventListener{
        void view_modal(final int position,String Tracking_num,int view_details,int micro_filming,int approve,int disapprove);

    }
}
