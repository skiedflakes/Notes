package com.wdysolutions.notes.Globals.Petty_Cash.Request.modal_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wdysolutions.notes.R;

import java.util.ArrayList;

public class PettyCash_modal_adapter extends RecyclerView.Adapter<PettyCash_modal_adapter.MyHolder> {

    ArrayList<PettyCash_modal_model> mdata;
    private Context context;
    private LayoutInflater inflater;

    public PettyCash_modal_adapter(Context context, ArrayList<PettyCash_modal_model> data){
        this.context = context;
        this.mdata = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.pettycash_request_modal_row,viewGroup,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, final int position) {
        final String getAmount = mdata.get(position).getAmount();
        final String getId = mdata.get(position).getId();
        final String getBr_id = mdata.get(position).getBr_id();
        final String getPcv = mdata.get(position).getPcv();
        final String getChart_id = mdata.get(position).getChart_id();
        final String getChart = mdata.get(position).getChart();
        final String getDesc = mdata.get(position).getDesc();
        final String getDoc_date = mdata.get(position).getDoc_date();
        final String getDoc_num = mdata.get(position).getDoc_num();
        final String getDoc_type = mdata.get(position).getDoc_type();

        myHolder.txt_count.setText(String.valueOf(position+1));
        myHolder.txt_amount.setText(getAmount);
        myHolder.txt_description.setText(getDesc);
        myHolder.txt_doc_date.setText(getDoc_date);
        myHolder.txt_doc_num.setText(getDoc_num);
        myHolder.txt_doc_type.setText(getDoc_type);

        if (getChart.equals("null")){
            myHolder.txt_chart_account.setText("");
        } else {
            myHolder.txt_chart_account.setText(getChart);
        }

    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
            TextView txt_count, txt_doc_date, txt_doc_type,txt_doc_num,txt_chart_account,txt_description,txt_amount;
        public MyHolder(View itemView) {
            super(itemView);
            txt_count = itemView.findViewById(R.id.txt_count);
            txt_doc_date = itemView.findViewById(R.id.txt_doc_date);
            txt_doc_type = itemView.findViewById(R.id.txt_doc_type);
            txt_doc_num = itemView.findViewById(R.id.txt_doc_num);
            txt_chart_account = itemView.findViewById(R.id.txt_chart_account);
            txt_description = itemView.findViewById(R.id.txt_description);
            txt_amount = itemView.findViewById(R.id.txt_amount);
        }
    }
}
