package com.wdysolutions.notes.Globals.Purchase_Order;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wdysolutions.notes.R;

import java.util.ArrayList;
import java.util.EventListener;

public class Purchase_Order_adapter extends RecyclerView.Adapter<Purchase_Order_adapter.MyHolder> {
    ArrayList<Purchase_Order_model> mdata;
    private Context context;
    private LayoutInflater inflater;
    EventListener listener;

    public Purchase_Order_adapter(Context context,ArrayList<Purchase_Order_model> data,EventListener listener){
        this.context = context;
        this.mdata = data;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.purchase_order_row,viewGroup,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder,final int position) {
    final String id =  mdata.get(position).getId();
    String br_id =  mdata.get(position).getBr_id();
    String count =  mdata.get(position).getCount();
    final String purchase_num =  mdata.get(position).getPurchase_num();
    String date =  mdata.get(position).getDate();
    String supplier =  mdata.get(position).getSupplier();
    String remarks =  mdata.get(position).getRemarks();
    String category =  mdata.get(position).getCategory();
    String po_status =  mdata.get(position).getPo_status();
    String rr_status =  mdata.get(position).getRr_status();
    String unrecieved_total =  mdata.get(position).getUnrecieved_total();
    String encoded_by =  mdata.get(position).getEncoded_by();
    String declared_status =  mdata.get(position).getDeclared_status();
    String checked_by =  mdata.get(position).getChecked_by();
    String approved_by =  mdata.get(position).getApproved_by();

    //color
    String po_stat_color =  mdata.get(position).getPo_status_color();
    String rr_status_color =  mdata.get(position).getRr_status_color();
    String dec_status_color =  mdata.get(position).getDec_status_color();

    //set text
        myHolder.tv_counter.setText(count);
        myHolder.tv_purchase_num.setText(purchase_num);
        myHolder.tv_date.setText(date);
        myHolder.tv_supplier.setText(supplier);
        myHolder.tv_remarks.setText(remarks);
        myHolder.tv_category.setText(category);
        myHolder.po_status.setText(po_status);
        myHolder.tv_rr_status.setText(rr_status);
        myHolder.tv_unreceived_total.setText(unrecieved_total);
        myHolder.tv_declared_status.setText(declared_status);
        myHolder.tv_encoded_by.setText(encoded_by);
        myHolder.tv_checked_by.setText(checked_by);
        myHolder.tv_approved_by.setText(approved_by);

    //set color if approved


    //set status color
        if(po_stat_color.equals("red")){
            myHolder.po_status.setTextColor(context.getResources().getColor(R.color.color_btn_red));
        }else if(po_stat_color.equals("green")){
            myHolder.po_status.setTextColor(context.getResources().getColor(R.color.color_green));
        }else{
            myHolder.po_status.setTextColor(context.getResources().getColor(R.color.color_text_light_black));
        }

        if(rr_status_color.equals("red")){
            myHolder.tv_rr_status.setTextColor(context.getResources().getColor(R.color.color_btn_red));
        }else if(rr_status_color.equals("green")){
            myHolder.tv_rr_status.setTextColor(context.getResources().getColor(R.color.color_green));
        }else if(rr_status_color.equals("orange")){
            myHolder.tv_rr_status.setTextColor(context.getResources().getColor(R.color.color_orange));
        }else{
            myHolder.tv_rr_status.setTextColor(context.getResources().getColor(R.color.color_text_light_black));
        }

        if(dec_status_color.equals("red")){
            myHolder.tv_declared_status.setTextColor(context.getResources().getColor(R.color.color_btn_red));
        }else if(dec_status_color.equals("green")){
            myHolder.tv_declared_status.setTextColor(context.getResources().getColor(R.color.color_green));
        }else{
            myHolder.tv_declared_status.setTextColor(context.getResources().getColor(R.color.color_text_light_black));
        }


        String checkedby = myHolder.tv_checked_by.getText().toString();
        String approveby =  myHolder.tv_approved_by.getText().toString();
        if(!checkedby.equals("")){


            if(!approveby.equals("")){
                myHolder.btn_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.view_modal(position,purchase_num,id,1,1, 0,1);
                    }
                });
            }else{

                myHolder.btn_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.view_modal(position,purchase_num,id,1,1,1,0);
                    }
                });
            }

        }else{
            //set on click
            myHolder.btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.view_modal(position,purchase_num,id,1,1,0,0);
                }
            });
        }




    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        //TextView tv_rs_slip,tv_date,tv_remarks,tv_requested_by,tv_status,tv_counter,btn_edit;
        TextView tv_purchase_num,tv_date,tv_supplier,tv_remarks,po_status,tv_category,
                tv_rr_status,tv_unreceived_total,tv_declared_status,tv_encoded_by, btn_edit,tv_counter,tv_approved_by,tv_checked_by;
        public MyHolder(View itemView) {
            super(itemView);
            tv_purchase_num = itemView.findViewById(R.id.tv_purchase_num);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_supplier = itemView.findViewById(R.id.tv_supplier);
            tv_remarks = itemView.findViewById(R.id.tv_remarks);
            tv_category = itemView.findViewById(R.id.tv_category);
            po_status = itemView.findViewById(R.id.po_status);
            tv_rr_status = itemView.findViewById(R.id.tv_rr_status);
            tv_unreceived_total = itemView.findViewById(R.id.tv_unreceived_total);
            tv_declared_status = itemView.findViewById(R.id.tv_declared_status);
            tv_encoded_by = itemView.findViewById(R.id.tv_encoded_by);
            btn_edit = itemView.findViewById(R.id.btn_edit);
            tv_counter = itemView.findViewById(R.id.tv_counter);
            tv_approved_by = itemView.findViewById(R.id.tv_approved_by);
            tv_checked_by = itemView.findViewById(R.id.tv_checked_by);
//
        }
    }

    public interface  EventListener{
        void view_modal(final int position,String po_number,String id,int view_details,int micro_filming,int approve,int disapprove);
    }
}
