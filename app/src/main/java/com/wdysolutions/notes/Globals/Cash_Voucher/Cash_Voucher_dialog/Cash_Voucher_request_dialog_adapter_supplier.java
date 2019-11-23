package com.wdysolutions.notes.Globals.Cash_Voucher.Cash_Voucher_dialog;


import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wdysolutions.notes.R;

import java.util.ArrayList;

public class Cash_Voucher_request_dialog_adapter_supplier extends RecyclerView.Adapter<Cash_Voucher_request_dialog_adapter_supplier.MyHolder>{

    ArrayList<Cash_Voucher_request_dialog_model_supplier> mdata;
    private Context context;
    private LayoutInflater inflater;
    EventListener_supplier listener;

    public Cash_Voucher_request_dialog_adapter_supplier(Context context,ArrayList<Cash_Voucher_request_dialog_model_supplier> data,EventListener_supplier listener){
        this.context = context;
        this.mdata = data;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.cash_voucher_request_dialog_row_supplier,viewGroup,false);
        Cash_Voucher_request_dialog_adapter_supplier.MyHolder holder = new Cash_Voucher_request_dialog_adapter_supplier.MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder,final int position) {
        final String id = mdata.get(position).getId();
        final String count = mdata.get(position).getCount();
        final String desc = mdata.get(position).getDesc();
        final String ref_num = mdata.get(position).getRef_num();
        final String supplier = mdata.get(position).getSupplier();
        final String amount = mdata.get(position).getAmount();

        myHolder.tv_count.setText(count);
        myHolder.tv_desc.setText(desc);
        myHolder.tv_amount.setText(amount);
        myHolder.tv_ref_num.setText(ref_num);
        myHolder.tv_supplier.setText(supplier);



    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        TextView tv_count,tv_amount,tv_desc,tv_ref_num,tv_supplier;
        public MyHolder(View itemView) {
            super(itemView);
            tv_count = itemView.findViewById(R.id.tv_count);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            tv_ref_num = itemView.findViewById(R.id.tv_ref_num);
            tv_desc = itemView.findViewById(R.id.tv_desc);
            tv_supplier = itemView.findViewById(R.id.tv_supplier);


        }
    }

    public interface  EventListener_supplier{
        void test_supplier(String id);
    }
}