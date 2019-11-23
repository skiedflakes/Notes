package com.wdysolutions.notes.Globals.Requisition;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.wdysolutions.notes.R;

import java.util.ArrayList;

public class Requisition_main_adapter extends RecyclerView.Adapter<Requisition_main_adapter.MyHolder>   {
    ArrayList<Requisition_main_model> mdata;
    private Context context;
    private LayoutInflater inflater;
    EventListener listener;

    public Requisition_main_adapter(Context context,ArrayList<Requisition_main_model> data,EventListener listener){
        this.context = context;
        this.mdata = data;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.requisition_main_row,viewGroup,false);
        Requisition_main_adapter.MyHolder holder = new Requisition_main_adapter.MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder,final int position) {

        final String rs_id = mdata.get(position).getRs_id();
        final String rs_number = mdata.get(position).getRs_number();
        final String date_added = mdata.get(position).getDate_added();
        final String requested_by = mdata.get(position).getRequested_by();
        final String status = mdata.get(position).getStatus();
        final String remarks = mdata.get(position).getRemarks();
        final String count = mdata.get(position).getCount();
        final int checked_status = mdata.get(position).getCheck_status();
        final String date_raw = mdata.get(position).getDate_raw();
        final String getAsset = mdata.get(position).getAsset();

        //set text
        myHolder.tv_rs_slip.setText(rs_number);
        myHolder.tv_date.setText(date_added);
        myHolder.tv_remarks.setText(remarks);
        myHolder.tv_requested_by.setText(requested_by);
        myHolder.tv_status.setText(status);
        myHolder.tv_counter.setText(count);

        myHolder.cb_selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myHolder.cb_selected.isChecked()){
                    listener.onChecked(rs_id);
                }else{
                    listener.removeChecked(rs_id);
                }
            }
        });

        if(checked_status==0){
            myHolder.cb_selected.setChecked(false);
        }else{
            myHolder.cb_selected.setChecked(true);
        }

        myHolder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onbtn_view(rs_id,rs_number,date_raw,requested_by,status,remarks,checked_status,getAsset);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        TextView tv_rs_slip,tv_date,tv_remarks,tv_requested_by,tv_status,tv_counter,btn_edit;
        CheckBox cb_selected;
        public MyHolder(View itemView) {
            super(itemView);
            tv_rs_slip = itemView.findViewById(R.id.tv_rs_slip);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_remarks = itemView.findViewById(R.id.tv_remarks);
            tv_requested_by = itemView.findViewById(R.id.tv_requested_by);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_counter = itemView.findViewById(R.id.tv_counter);
            cb_selected = itemView.findViewById(R.id.cb_selected);
            btn_edit = itemView.findViewById(R.id.btn_edit);
        }
    }

    public interface  EventListener{

        void onChecked(String rs_header_id);
        void removeChecked(String rs_header_id);
        void onbtn_view(String rs_id,String rs_number,String date_added,String requested_by,String status,
                        String remarks, int check_status, String asset);

    }

}
