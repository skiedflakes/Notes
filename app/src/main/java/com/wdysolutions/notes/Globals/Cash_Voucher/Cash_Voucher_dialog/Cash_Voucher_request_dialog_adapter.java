package com.wdysolutions.notes.Globals.Cash_Voucher.Cash_Voucher_dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wdysolutions.notes.R;

import java.util.ArrayList;

public class Cash_Voucher_request_dialog_adapter extends RecyclerView.Adapter<Cash_Voucher_request_dialog_adapter.MyHolder>{

    ArrayList<Cash_Voucher_request_dialog_model> mdata;
    private Context context;
    private LayoutInflater inflater;
    EventListener listener;

    public Cash_Voucher_request_dialog_adapter(Context context,ArrayList<Cash_Voucher_request_dialog_model> data,EventListener listener){
        this.context = context;
        this.mdata = data;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.cash_voucher_request_dialog_row,viewGroup,false);
        Cash_Voucher_request_dialog_adapter.MyHolder holder = new Cash_Voucher_request_dialog_adapter.MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder,final int position) {
        final String id = mdata.get(position).getId();
        final String count = mdata.get(position).getCount();
        final String expense_id = mdata.get(position).getExpense_id();
        final String desc = mdata.get(position).getRemarks();
        final String debit = mdata.get(position).getDebit();
        final String credit = mdata.get(position).getCredit();

        myHolder.tv_count.setText(count);
        myHolder.tv_payment.setText(expense_id);
        myHolder.tv_desc.setText(desc);
        myHolder.tv_credit.setText(credit);
        myHolder.tv_debit.setText(debit);

        Drawable edit = context.getResources().getDrawable( R.drawable.ic_settings );


        if(count.equals("")){
            myHolder.btn_edit.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
        }else{
            myHolder.btn_edit.setCompoundDrawablesWithIntrinsicBounds(edit,null,null,null);

        }

    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        TextView tv_count,tv_credit,tv_debit,tv_desc,tv_payment,btn_edit;

        public MyHolder(View itemView) {
            super(itemView);
            tv_count = itemView.findViewById(R.id.tv_count);
            tv_credit = itemView.findViewById(R.id.tv_credit);
            tv_debit = itemView.findViewById(R.id.tv_debit);
            tv_desc = itemView.findViewById(R.id.tv_desc);
            tv_payment = itemView.findViewById(R.id.tv_payment);
            btn_edit = itemView.findViewById(R.id.btn_edit);


        }
    }

    public interface  EventListener{
        void test(String id);
    }
}
