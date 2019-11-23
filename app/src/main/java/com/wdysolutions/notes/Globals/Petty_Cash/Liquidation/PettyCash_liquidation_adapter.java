package com.wdysolutions.notes.Globals.Petty_Cash.Liquidation;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wdysolutions.notes.R;

import java.util.ArrayList;

public class PettyCash_liquidation_adapter extends RecyclerView.Adapter<PettyCash_liquidation_adapter.MyHolder> {

    ArrayList<PettyCash_liquidation_model> mdata;
    private Context context;
    private LayoutInflater inflater;
    EventListener listener;

    public PettyCash_liquidation_adapter(Context context, ArrayList<PettyCash_liquidation_model> data,  EventListener listener){
        this.context = context;
        this.mdata = data;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.pettycash_liquidation_row,viewGroup,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, final int position) {
        final String getStatus_color = mdata.get(position).getStatus_color();
        final String getStatus = mdata.get(position).getStatus();
        final String getRfr_status = mdata.get(position).getRfr_status();
        final String getStats_color = mdata.get(position).getStats_color();
        final String getAmount = mdata.get(position).getAmount();
        final String getId = mdata.get(position).getId();
        final String getApproved_by = mdata.get(position).getApproved_by();
        final String getBr_id = mdata.get(position).getBr_id();
        final String getDate_covered = mdata.get(position).getDate_covered();
        final String getDate_liquidated = mdata.get(position).getDate_liquidated();
        final String getLiquidate_by = mdata.get(position).getLiquidate_by();
        final String getRfr_stat = mdata.get(position).getRfr_stat();
        final String getRfr_stat_color = mdata.get(position).getRfr_stat_color();
        final String getTracking_num = mdata.get(position).getTracking_num();
        final String getStats = mdata.get(position).getStats();
        final String getBranch = mdata.get(position).getBranch();

        myHolder.txt_count.setText(String.valueOf(position+1));
        myHolder.txt_amount.setText(getAmount);

        if(getApproved_by.equals("")){
            myHolder.txt_approved.setText("Pending");
        }else{
            myHolder.txt_approved.setText(getApproved_by);
        }

        myHolder.txt_branch.setText(getBranch);
        myHolder.txt_date_covered.setText(getDate_covered);
        myHolder.txt_date_liquidation.setText(getDate_liquidated);
        myHolder.txt_liquidation_by.setText(getLiquidate_by);
        myHolder.txt_tracking_num.setText(getTracking_num);

        myHolder.txt_replenish_status.setText(getRfr_stat);
        if (getRfr_stat_color.equals("green")){
            myHolder.txt_replenish_status.setTextColor(context.getResources().getColor(R.color.color_green));
        } else { // orange
            myHolder.txt_replenish_status.setTextColor(context.getResources().getColor(R.color.color_orange));
        }

        myHolder.txt_liquidation_status.setText(getStats);
        if (getStats_color.equals("green")){
            myHolder.txt_liquidation_status.setTextColor(context.getResources().getColor(R.color.color_green));
        } else { // orange
            myHolder.txt_liquidation_status.setTextColor(context.getResources().getColor(R.color.color_orange));
        }

        myHolder.txt_status.setText(getStatus);
        if (getStatus_color.equals("green")){
            myHolder.txt_status.setTextColor(context.getResources().getColor(R.color.color_green));
        } else { // orange
            myHolder.txt_status.setTextColor(context.getResources().getColor(R.color.color_orange));
        }

        if(!getApproved_by.equals("")){
            myHolder.btn_edit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.approved, 0, 0, 0);
            myHolder.btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.view_modal(position,getTracking_num,getId,1,1,0,0);
                }
            });
        }else{
            myHolder.btn_edit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_settings, 0, 0, 0);
            myHolder.btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.view_modal(position,getTracking_num,getId,1,1,1,0);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        TextView btn_edit,txt_count,txt_amount,txt_status,txt_approved, txt_tracking_num,txt_date_covered,txt_branch,txt_date_liquidation,
        txt_liquidation_status,txt_liquidation_by,txt_replenish_status;
        public MyHolder(View itemView) {
            super(itemView);
            txt_count = itemView.findViewById(R.id.txt_count);
            txt_tracking_num = itemView.findViewById(R.id.txt_tracking_num);
            txt_date_covered = itemView.findViewById(R.id.txt_date_covered);
            txt_branch = itemView.findViewById(R.id.txt_branch);
            txt_amount = itemView.findViewById(R.id.txt_amount);
            txt_date_liquidation = itemView.findViewById(R.id.txt_date_liquidation);
            txt_liquidation_status = itemView.findViewById(R.id.txt_liquidation_status);
            txt_liquidation_by = itemView.findViewById(R.id.txt_liquidation_by);
            txt_replenish_status = itemView.findViewById(R.id.txt_replenish_status);
            txt_status = itemView.findViewById(R.id.txt_status);
            txt_approved = itemView.findViewById(R.id.txt_approved);
            btn_edit= itemView.findViewById(R.id.btn_edit);
        }
    }
    public interface  EventListener{
        void view_modal(final int position,String tracking_num,String id,int view_details,int micro_filming,int approve,int disapprove);

    }

}
