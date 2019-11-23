package com.wdysolutions.notes.Globals.Check_Voucher;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wdysolutions.notes.R;
import java.util.ArrayList;

public class Check_Voucher_request_adapter extends RecyclerView.Adapter<Check_Voucher_request_adapter.MyHolder> {
    ArrayList<Check_Voucher_request_model> mdata;
    private Context context;
    private LayoutInflater inflater;
    EventListener listener;

    public Check_Voucher_request_adapter(Context context,ArrayList<Check_Voucher_request_model> data,EventListener listener){
        this.context = context;
        this.mdata = data;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.check_voucher_request_row,viewGroup,false);
        Check_Voucher_request_adapter.MyHolder holder = new Check_Voucher_request_adapter.MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder,final int position) {

        final String id = mdata.get(position).getId();
        final String cv_number = mdata.get(position).getCv_number();
        final String count = mdata.get(position).getCount();

        final String bank = mdata.get(position).getBank();
        final String check_number = mdata.get(position).getCheck_number();
        final String account = mdata.get(position).getAccount();

        final String issued_date = mdata.get(position).getIssued_date();
        final String check_date = mdata.get(position).getCheck_date();
        final String remarks = mdata.get(position).getRemarks();

        final String amount = mdata.get(position).getAmount();
        final String status = mdata.get(position).getStatus();
        final String stat = mdata.get(position).getStat();

        final String encoded_by = mdata.get(position).getEncoded_by();
        final String cv_type = mdata.get(position).getCv_type();
        final String br = mdata.get(position).getBr();
        final String approved_by = mdata.get(position).getApproved_by();

        myHolder.tv_counter.setText(count);
        myHolder.tv_cv_num.setText(cv_number);
        myHolder.tv_account.setText(account);

        myHolder.tv_bank.setText(bank);
        myHolder.tv_check_number.setText(check_number);
        myHolder.tv_issued_date.setText(issued_date);
        myHolder.tv_check_date.setText(check_date);

        myHolder.tv_amount.setText(amount);

        myHolder.tv_status.setText(status);

        if(!encoded_by.equals("null")){
            myHolder.tv_encoded_by.setText(encoded_by);
        }else{
            myHolder.tv_encoded_by.setText("");
        }

        if(approved_by.equals("")){
            myHolder.tv_approved_by.setText("Pending");
        }else{
            myHolder.tv_approved_by.setText(approved_by);
        }

        if(status.equals("Approved")){
            myHolder.tv_status.setTextColor(context.getResources().getColor(R.color.color_btn_green));
        }else{
            myHolder.tv_status.setTextColor(context.getResources().getColor(R.color.color_text_light_black));
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
        TextView tv_counter,tv_cv_num,tv_account,tv_check_number,tv_issued_date,tv_check_date,tv_bank,
                tv_amount,tv_encoded_by,tv_status,btn_edit,tv_approved_by;
        public MyHolder(View itemView) {
            super(itemView);
           // tv_rs_slip = itemView.findViewById(R.id.tv_rs_slip);
            tv_counter = itemView.findViewById(R.id.tv_counter);
            tv_cv_num = itemView.findViewById(R.id.tv_cv_num);
            tv_account = itemView.findViewById(R.id.tv_account);

            tv_bank = itemView.findViewById(R.id.tv_bank);

            tv_check_number = itemView.findViewById(R.id.tv_check_number);
            tv_issued_date = itemView.findViewById(R.id.tv_issued_date);
            tv_check_date = itemView.findViewById(R.id.tv_check_date);

            tv_amount = itemView.findViewById(R.id.tv_amount);
            tv_encoded_by = itemView.findViewById(R.id.tv_encoded_by);
            tv_status = itemView.findViewById(R.id.tv_status);

            btn_edit= itemView.findViewById(R.id.btn_edit);

            tv_approved_by = itemView.findViewById(R.id.tv_approved_by);


        }
    }

    public interface  EventListener{

        void view_modal(final int position,String cv_num,String id,int view_details,int micro_filming,int approve,int disapprove);

    }
}
