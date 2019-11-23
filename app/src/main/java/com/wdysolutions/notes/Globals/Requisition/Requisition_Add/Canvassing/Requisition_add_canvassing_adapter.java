package com.wdysolutions.notes.Globals.Requisition.Requisition_Add.Canvassing;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wdysolutions.notes.R;

import java.util.ArrayList;

public class Requisition_add_canvassing_adapter extends RecyclerView.Adapter<Requisition_add_canvassing_adapter.MyHolder> {

    ArrayList<CanvasTable_model> mdata;
    private Context context;
    private LayoutInflater inflater;
    EventListener listener;


    public Requisition_add_canvassing_adapter(Context context, ArrayList<CanvasTable_model> data, EventListener listener) {
        this.context = context;
        this.mdata = data;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.requisition_add_canvassing_row, viewGroup, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, final int position) {
        final String getSupplier_id = mdata.get(position).getSupplier_id();
        final String getDate_added = mdata.get(position).getDate_added();
        final String getConsumed = mdata.get(position).getConsumed();
        final String getStatus = mdata.get(position).getStatus();
        final String getRemarks = mdata.get(position).getRemarks();
        final String getInventory = mdata.get(position).getInventory();
        final String getPending = mdata.get(position).getPending();
        final String getPrice = mdata.get(position).getPrice();
        final String getId = mdata.get(position).getId();
        final String getSupplier = mdata.get(position).getSupplier();

        myHolder.tv_price.setText(getPrice);
        myHolder.tv_supplier.setText(getSupplier);
        myHolder.tv_remarks.setText(getRemarks);
        myHolder.tv_status.setText(getStatus);

        myHolder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.approve(getId);
            }
        });
        myHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.delete(getId);
            }
        });

        if(getStatus.equals("Approved")){
            myHolder.approve.setEnabled(false);
            myHolder.approve.setBackgroundColor(context.getResources().getColor(R.color.color_btn_green2));
        }else{
            myHolder.approve.setEnabled(true);
            myHolder.approve.setBackgroundColor(context.getResources().getColor(R.color.color_btn_green));
        }
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView tv_supplier, tv_price, tv_remarks, tv_status;
        ImageView approve, delete;

        public MyHolder(View itemView) {
            super(itemView);
            tv_status = itemView.findViewById(R.id.tv_status);
            delete = itemView.findViewById(R.id.delete);
            approve = itemView.findViewById(R.id.approve);
            tv_supplier = itemView.findViewById(R.id.tv_supplier);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_remarks = itemView.findViewById(R.id.tv_remarks);
        }
    }

    public interface EventListener{
        void delete(String id);
        void approve(String id);
    }
}
