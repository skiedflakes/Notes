package com.wdysolutions.notes.Globals.Petty_Cash.Replenish;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.wdysolutions.notes.Globals.Petty_Cash.Replenish.modal_view.PettyCash_replenish_modal_main;
import com.wdysolutions.notes.MainActivity;
import com.wdysolutions.notes.R;

import java.util.ArrayList;

public class PettyCash_replenish_adapter extends RecyclerView.Adapter<PettyCash_replenish_adapter.MyHolder> {

    ArrayList<PettyCash_replenish_model> mdata;
    private Context context;
    private LayoutInflater inflater;

    public PettyCash_replenish_adapter(Context context, ArrayList<PettyCash_replenish_model> data){
        this.context = context;
        this.mdata = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.pettycash_replenish_row,viewGroup,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, final int position) {
        final String getRfr_stat_color = mdata.get(position).getRfr_stat_color();
        final String getRfr_stat = mdata.get(position).getRfr_stat();
        final String getApproved_by = mdata.get(position).getApproved_by();
        final String getStatus = mdata.get(position).getStatus();
        final String getStatus_color = mdata.get(position).getStatus_color();
        final String getBr_id = mdata.get(position).getBr_id();
        final String getId = mdata.get(position).getId();
        final String getAmount = mdata.get(position).getAmount();
        final String getRemarks = mdata.get(position).getRemarks();
        final String getDate = mdata.get(position).getDate();
        final String getDec_stat = mdata.get(position).getDec_stat();
        final String getDec_stat_color = mdata.get(position).getDec_stat_color();
        final String getEncodedBY = mdata.get(position).getEncodedBY();
        final String getRfr_stats = mdata.get(position).getRfr_stats();
        final String getRplnsh_num = mdata.get(position).getRplnsh_num();

        myHolder.txt_count.setText(String.valueOf(position+1));
        myHolder.txt_amount.setText(getAmount);
        //myHolder.txt_approve.setText(getApproved_by);
        myHolder.txt_encoded_by.setText(getEncodedBY);
        myHolder.txt_remarks.setText(getRemarks);
        myHolder.txt_date.setText(getDate);
        myHolder.txt_tracking_num.setText(getRplnsh_num);

        myHolder.txt_declared_status.setText(getDec_stat);
        if (getDec_stat_color.equals("green")){
            myHolder.txt_declared_status.setTextColor(context.getResources().getColor(R.color.color_green));
        } else { // orange
            myHolder.txt_declared_status.setTextColor(context.getResources().getColor(R.color.color_orange));
        }

        myHolder.txt_replenish_status.setText(getRfr_stat);
        if (getRfr_stat_color.equals("green")){
            myHolder.txt_replenish_status.setTextColor(context.getResources().getColor(R.color.color_green));
        } else if (getRfr_stat_color.equals("red")){
            myHolder.txt_replenish_status.setTextColor(context.getResources().getColor(R.color.color_btn_red));
        } else { // orange
            myHolder.txt_replenish_status.setTextColor(context.getResources().getColor(R.color.color_orange));
        }

        myHolder.txt_status.setText(getStatus);
        if (getStatus_color.equals("green")){
            myHolder.txt_status.setTextColor(context.getResources().getColor(R.color.color_green));
        } else if (getStatus_color.equals("blue")){
            myHolder.txt_status.setTextColor(context.getResources().getColor(R.color.dark_blue));
        } else { // no color
            myHolder.txt_status.setTextColor(context.getResources().getColor(R.color.color_black));
        }

        myHolder.actions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPicker(getId, getRplnsh_num, getDate, getBr_id);
            }
        });
    }

    private String selected;
    private void dialogPicker(final String getId, final String getRplnsh_num, final String getDate, final String getBr_id){
        final String[] name = {context.getResources().getString(R.string.str_view),
                context.getResources().getString(R.string.str_microfilming)};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(name, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                selected = name[item];

                if (selected.equals(context.getResources().getString(R.string.str_view))){
                    openView(getId, getRplnsh_num, getDate, getBr_id);
                } else {
                    Toast.makeText(context, "microfilming", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void openView(String getId, String getRplnsh_num, String getDate, String getBr_id){
        Bundle bundle = new Bundle();
        bundle.putString("getId", getId);
        bundle.putString("getRplnsh_num", getRplnsh_num);
        bundle.putString("getUserID", getDate);
        bundle.putString("getBr_id", getBr_id);
        DialogFragment dialogFragment = new PettyCash_replenish_modal_main();
        FragmentTransaction ft = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
        Fragment prev = ((MainActivity) context).getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {ft.remove(prev);}
        ft.addToBackStack(null);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(ft, "dialog");
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        TextView txt_count,txt_amount,txt_status, txt_tracking_num,txt_replenish_status,txt_date,txt_remarks,txt_encoded_by,
        txt_declared_status,txt_approve;
        ImageView actions;
        public MyHolder(View itemView) {
            super(itemView);
            actions = itemView.findViewById(R.id.actions);
            txt_count = itemView.findViewById(R.id.txt_count);
            txt_tracking_num = itemView.findViewById(R.id.txt_tracking_num);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_amount = itemView.findViewById(R.id.txt_amount);
            txt_remarks = itemView.findViewById(R.id.txt_remarks);
            txt_status = itemView.findViewById(R.id.txt_status);
            txt_replenish_status = itemView.findViewById(R.id.txt_replenish_status);
            txt_encoded_by = itemView.findViewById(R.id.txt_encoded_by);
            txt_declared_status = itemView.findViewById(R.id.txt_declared_status);
            txt_approve = itemView.findViewById(R.id.txt_approve);
        }
    }
}
