package com.wdysolutions.notes.Globals.Purchase_Order.purchase_order_dialog;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wdysolutions.notes.R;

import java.util.ArrayList;

public class Purchase_Order_dialog_adapter  extends RecyclerView.Adapter<Purchase_Order_dialog_adapter.MyHolder>{

    ArrayList<Purchase_Order_dialog_model> mdata;
    private Context context;
    private LayoutInflater inflater;
    EventListener listener;

    public Purchase_Order_dialog_adapter(Context context,ArrayList<Purchase_Order_dialog_model> data,EventListener listener){
        this.context = context;
        this.mdata = data;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.purchase_order_dialog_row,viewGroup,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder,final int position) {
        //get data from model
        final String id = mdata.get(position).getId();
        final String count = mdata.get(position).getCount();
        final String product = mdata.get(position).getProduct_id();

        final String quantity = mdata.get(position).getQuantity();
        final String date_needed = mdata.get(position).getDate_needed();
        final String needed_by = mdata.get(position).getNeeded_by();
        final String remaining_qty = mdata.get(position).getRemaining_qty();
        final String remarks = mdata.get(position).getRemarks();
        final String purpose = mdata.get(position).getPurpose();
        final String description = mdata.get(position).getDescription();
        final String supplier_id = mdata.get(position).getSupplier_id();
        final String supplier_price = mdata.get(position).getSupplier_price();
        final String packaging_id = mdata.get(position).getPackaging_id();
        final String subtotal = mdata.get(position).getSubtotal();


        //set text
        myHolder.tv_count.setText(count);
        myHolder.tv_product.setText(product);
        myHolder.tv_packaging.setText(packaging_id);
        myHolder.tv_qty.setText(quantity);
        myHolder.tv_remaining_qty.setText(remaining_qty);
        myHolder.tv_supplier_price.setText(supplier_price);
        myHolder.tv_supplier.setText(supplier_id);
        myHolder.tv_needed_by.setText(needed_by);
        myHolder.tv_description.setText(description);
        myHolder.tv_remarks.setText(remarks);
        myHolder.tv_subtotal.setText(subtotal);

    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        TextView tv_count,tv_product,tv_packaging,tv_qty,tv_remaining_qty
                ,tv_supplier_price,tv_supplier,tv_needed_by,tv_description,tv_remarks,tv_subtotal;

        public MyHolder(View itemView) {
            super(itemView);
            tv_count = itemView.findViewById(R.id.tv_count);
            tv_product = itemView.findViewById(R.id.tv_product);
            tv_packaging = itemView.findViewById(R.id.tv_packaging);
            tv_qty = itemView.findViewById(R.id.tv_qty);
            tv_remaining_qty = itemView.findViewById(R.id.tv_remaining_qty);
            tv_supplier_price = itemView.findViewById(R.id.tv_supplier_price);
            tv_supplier = itemView.findViewById(R.id.tv_supplier);
            tv_needed_by = itemView.findViewById(R.id.tv_needed_by);
            tv_description = itemView.findViewById(R.id.tv_description);
            tv_remarks = itemView.findViewById(R.id.tv_remarks);
            tv_subtotal = itemView.findViewById(R.id.tv_subtotal);
        }
    }


    public interface  EventListener{
        void test(String id);
    }
}
