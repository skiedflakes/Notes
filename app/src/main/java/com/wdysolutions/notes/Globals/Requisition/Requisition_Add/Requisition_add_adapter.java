package com.wdysolutions.notes.Globals.Requisition.Requisition_Add;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.wdysolutions.notes.R;

import java.util.ArrayList;

public class Requisition_add_adapter extends RecyclerView.Adapter<Requisition_add_adapter.MyHolder>  {
    ArrayList<Requisition_add_model> mdata;
    private Context context;
    private LayoutInflater inflater;
    EventListener listener;

    public Requisition_add_adapter(Context context, ArrayList<Requisition_add_model> data, Requisition_add_adapter.EventListener listener){
        this.context = context;
        this.mdata = data;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.requisition_add_main_row,viewGroup,false);
        Requisition_add_adapter.MyHolder holder = new Requisition_add_adapter.MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder,final int position) {
        final String id  = mdata.get(position).getId();
        final String count = mdata.get(position).getCount();
        final String item = mdata.get(position).getProduct();
        final String packaging = mdata.get(position).getPackage_name();
        final String qty = mdata.get(position).getQuantity();
        final String req = mdata.get(position).getNeeded_by();
        final String desc = mdata.get(position).getDescription();
        final String supplier = mdata.get(position).getSupplier_id();
        final String supplier_price = mdata.get(position).getSupplier_price();
        final String status = mdata.get(position).getStatus();
        final String type = mdata.get(position).getType();
        final int checked_status = mdata.get(position).getCheck_status();
        final String getCanvas_status = mdata.get(position).getCanvas_status();
        final String getProduct_id = mdata.get(position).getProduct_id();

        myHolder.cb_selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myHolder.cb_selected.isChecked()){
                    listener.onChecked(id);
                }else{
                    listener.removeChecked(id);
                }
            }
        });


        if (status.equals("Approved")){
            myHolder.tv_status.setTextColor(context.getResources().getColor(R.color.color_blue));
            myHolder.btn_edit.setEnabled(true);
            myHolder.btn_edit.setBackgroundColor(context.getResources().getColor(R.color.color_button_bg));
        } else if (status.equals("Finished")){
            myHolder.tv_status.setTextColor(context.getResources().getColor(R.color.color_btn_green));
            myHolder.btn_edit.setBackgroundColor(context.getResources().getColor(R.color.color_btn_green2));
            myHolder.btn_edit.setEnabled(false);
        } else {
            myHolder.btn_edit.setBackgroundColor(context.getResources().getColor(R.color.color_button_bg));
            myHolder.tv_status.setTextColor(context.getResources().getColor(R.color.color_text_light_black));
            myHolder.btn_edit.setEnabled(true);
        }


        if(checked_status==0){
            myHolder.cb_selected.setChecked(false);
        }else{
            myHolder.cb_selected.setChecked(true);
        }


        if(getCanvas_status.equals("1")){
            myHolder.check_green.setVisibility(View.VISIBLE);
        }else{
            myHolder.check_green.setVisibility(View.GONE);
        }


        myHolder.tv_counter.setText(count);
        myHolder.tv_item.setText(item);
        myHolder.tv_packaging.setText(packaging);
        myHolder.tv_qty.setText(qty);
        myHolder.tv_req.setText(req);
        myHolder.tv_desc.setText(desc);

        myHolder.tv_supplier_price.setText(supplier_price);
        myHolder.tv_status.setText(status);
        myHolder.tv_type.setText(type);

        //conditions

        if(supplier!=null){
            myHolder.tv_supplier.setText(supplier);
        }else{
            myHolder.tv_supplier.setText("");
        }

        myHolder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.open_canvassing(id,item,packaging,qty,getProduct_id);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        TextView tv_counter,
                tv_item,tv_packaging,tv_qty,tv_req,tv_desc,tv_supplier,
                tv_supplier_price,tv_status,tv_type;
        ImageView check_green, btn_edit;
        CheckBox cb_selected;
        public MyHolder(View itemView) {
            super(itemView);
            check_green = itemView.findViewById(R.id.check_green);
            tv_counter = itemView.findViewById(R.id.tv_counter);
            tv_item= itemView.findViewById(R.id.tv_item);
            tv_packaging = itemView.findViewById(R.id.tv_packaging);
            tv_qty= itemView.findViewById(R.id.tv_qty);
            tv_req = itemView.findViewById(R.id.tv_req);
            tv_desc= itemView.findViewById(R.id.tv_desc);
            tv_supplier = itemView.findViewById(R.id.tv_supplier);
            tv_supplier_price= itemView.findViewById(R.id.tv_supplier_price);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_type= itemView.findViewById(R.id.tv_type);
            cb_selected= itemView.findViewById(R.id.cb_selected);
            btn_edit =  itemView.findViewById(R.id.btn_edit);

        }
    }

    public interface EventListener{
        void onChecked(String id);
        void removeChecked(String id);
        void open_canvassing(String id,String product,String package_name,String qty, String product_id);
    }

}
