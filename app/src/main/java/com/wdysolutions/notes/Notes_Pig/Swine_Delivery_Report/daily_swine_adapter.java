package com.wdysolutions.notes.Notes_Pig.Swine_Delivery_Report;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wdysolutions.notes.MainActivity;
import com.wdysolutions.notes.Notes_Pig.Swine_Delivery_Report.Swine_Sales_Report.Swine_delivery_sales_main;
import com.wdysolutions.notes.R;

import java.util.ArrayList;

public class daily_swine_adapter extends RecyclerView.Adapter<daily_swine_adapter.MyHolder>{

    ArrayList<daily_swine_delivery_model> mdata;
    private Context context;
    private LayoutInflater inflater;


    public daily_swine_adapter(Context context, ArrayList<daily_swine_delivery_model> data){
        this.context = context;
        this.mdata = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.swine_delivery_report_daily_row,viewGroup,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, int position) {
        final String getAve_age = mdata.get(position).getAve_age();
        final String getAve_cost = mdata.get(position).getAve_cost();
        final String getSwine_type = mdata.get(position).getSwine_type();
        final String getAdg = mdata.get(position).getAdg();
        final String getGross_ave = mdata.get(position).getGross_ave();
        final String getGross_total = mdata.get(position).getGross_total();
        final String getPrice_per_kg = mdata.get(position).getPrice_per_kg();
        final String getReference_num = mdata.get(position).getReference_num();
        final String getCustomer = mdata.get(position).getCustomer();
        final String getHeads = mdata.get(position).getHeads();
        final String getTotal_cost = mdata.get(position).getTotal_cost();
        final String getTotal_sales = mdata.get(position).getTotal_sales();
        final String getTotal_weight = mdata.get(position).getTotal_weight();
        final String getDate = mdata.get(position).getDate();
        final String getAve_weight = mdata.get(position).getAve_weight();
        final String getDelivery_number = mdata.get(position).getDelivery_number();

        myHolder.counter.setText(String.valueOf(position+1));
        myHolder.tv_adg.setText(getAdg);
        myHolder.tv_ave_gross.setText(getGross_ave);
        myHolder.tv_reference.setText(getReference_num);
        myHolder.tv_heads.setText(getHeads);
        myHolder.tv_customer.setText(getCustomer);
        myHolder.tv_total_gross.setText(getGross_total);
        myHolder.tv_total_sales.setText(getTotal_sales);
        myHolder.tv_total_weight.setText(getTotal_weight);
        myHolder.tv_per_kg.setText(getPrice_per_kg);
        myHolder.tv_ave_age.setText(getAve_age);
        myHolder.tv_swine_type.setText(getSwine_type);
        myHolder.tv_date.setText(getDate);
        myHolder.tv_ave_weight.setText(getAve_weight);

        myHolder.tv_swine_ave.setText(getAve_cost);
        myHolder.tv_swine_total.setText(getTotal_cost);
        
        myHolder.btn_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogTable(getDelivery_number);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        TextView counter, tv_date, tv_customer, tv_reference, tv_heads, tv_total_weight, tv_ave_weight, tv_adg, tv_total_gross, tv_ave_gross,
                tv_total_sales, tv_per_kg, tv_ave_age, tv_swine_type, tv_swine_ave, tv_swine_total;
        LinearLayout btn_;
        public MyHolder(View itemView) {
            super(itemView);
            counter = itemView.findViewById(R.id.counter);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_customer = itemView.findViewById(R.id.tv_customer);
            tv_reference = itemView.findViewById(R.id.tv_reference);
            tv_heads = itemView.findViewById(R.id.tv_heads);
            tv_total_weight = itemView.findViewById(R.id.tv_total_weight);
            tv_ave_weight = itemView.findViewById(R.id.tv_ave_weight);
            tv_adg = itemView.findViewById(R.id.tv_adg);
            tv_total_gross = itemView.findViewById(R.id.tv_total_gross);
            tv_ave_gross = itemView.findViewById(R.id.tv_ave_gross);
            tv_total_sales = itemView.findViewById(R.id.tv_total_sales);
            tv_per_kg = itemView.findViewById(R.id.tv_per_kg);
            tv_ave_age = itemView.findViewById(R.id.tv_ave_age);
            tv_swine_type = itemView.findViewById(R.id.tv_swine_type);
            tv_swine_ave = itemView.findViewById(R.id.tv_swine_ave);
            tv_swine_total = itemView.findViewById(R.id.tv_swine_total);
            btn_ = itemView.findViewById(R.id.btn_);
        }
    }

    private void openDialogTable(String dr_num){
        Bundle bundle = new Bundle();
        bundle.putString("dr_num", dr_num);
        DialogFragment dialogFragment = new Swine_delivery_sales_main();
        FragmentTransaction ft = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
        Fragment prev = ((MainActivity) context).getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {ft.remove(prev);}
        ft.addToBackStack(null);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(ft, "dialog");
    }

}
