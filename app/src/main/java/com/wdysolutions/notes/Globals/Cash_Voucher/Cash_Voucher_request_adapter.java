package com.wdysolutions.notes.Globals.Cash_Voucher;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wdysolutions.notes.R;

import java.util.ArrayList;

public class Cash_Voucher_request_adapter extends RecyclerView.Adapter<Cash_Voucher_request_adapter.MyHolder> {
    ArrayList<Cash_Voucher_request_model> mdata;
    private Context context;
    private LayoutInflater inflater;
    EventListener listener;

    public Cash_Voucher_request_adapter(Context context,ArrayList<Cash_Voucher_request_model> data,EventListener listener){
        this.context = context;
        this.mdata = data;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.cash_voucher_request_row,viewGroup,false);
        Cash_Voucher_request_adapter.MyHolder holder = new Cash_Voucher_request_adapter.MyHolder(view);
        return holder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder,final int position) {
        final String id = mdata.get(position).getId();
        final String cv_number = mdata.get(position).getCv_number();
        final String count = mdata.get(position).getCount();
        final String acount = mdata.get(position).getAccount();
        final String date = mdata.get(position).getDate();
        final String amount = mdata.get(position).getAmount();
        final String approved_by = mdata.get(position).getApproved_by();
        final String encoded_by = mdata.get(position).getEncoded_by();
        final String status = mdata.get(position).getStatus();

        myHolder.tv_counter.setText(count);
        myHolder.tv_cv_num.setText(cv_number);

        myHolder.tv_date.setText(date);
        myHolder.tv_amount.setText(amount);
        myHolder.tv_encoded_by.setText(encoded_by);
        myHolder.tv_status.setText(status);

        if(!acount.equals("null")){
            myHolder.tv_account.setText(acount);
        }else{
            myHolder.tv_account.setText("");
        }

        if(status.equals("Approved")){
            myHolder.tv_status.setTextColor(context.getResources().getColor(R.color.color_btn_green));
        }else{
            myHolder.tv_status.setTextColor(context.getResources().getColor(R.color.color_text_light_black));
        }
        if(approved_by.equals("")){
            myHolder.tv_approved_by.setText("Pending");
        }else{
            myHolder.tv_approved_by.setText(approved_by);
        }

        if(!approved_by.equals("")){
            myHolder.btn_edit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.approved, 0, 0, 0);
            myHolder.btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.view_modal(position,cv_number,id,1,1,0,0);
                }
            });
        }else{
            myHolder.btn_edit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_settings, 0, 0, 0);
            myHolder.btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.view_modal(position,cv_number,id,1,1,1,0);
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
        TextView tv_cv_num,tv_counter,tv_account,tv_date,tv_encoded_by,tv_amount,tv_status,tv_approved_by;
        TextView btn_edit;
        public MyHolder(View itemView) {
            super(itemView);
            btn_edit = itemView.findViewById(R.id.btn_edit);
            tv_counter = itemView.findViewById(R.id.tv_counter);
            tv_cv_num = itemView.findViewById(R.id.tv_cv_num);
            tv_account = itemView.findViewById(R.id.tv_account);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_encoded_by = itemView.findViewById(R.id.tv_encoded_by);
            tv_approved_by= itemView.findViewById(R.id.tv_approved_by);
        }
    }

    public interface  EventListener{
        void view_modal(final int position,String cv_num,String id,int view_details,int micro_filming,int approve,int disapprove);

    }
}
