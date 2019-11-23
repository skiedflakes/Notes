package com.wdysolutions.notes.Notes_Pig.Swine_Delivery_Report.Swine_Sales_Report;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wdysolutions.notes.MainActivity;
import com.wdysolutions.notes.Notes_Pig.Swine_Card.RFscanner_main;
import com.wdysolutions.notes.R;

import java.util.ArrayList;

public class swine_delivery_sales_adapter extends RecyclerView.Adapter<swine_delivery_sales_adapter.MyHolder>{

    ArrayList<Swine_delivery_sales_model> mdata;
    private Context context;
    private LayoutInflater inflater;
    EventListener listener;

    public swine_delivery_sales_adapter(Context context, ArrayList<Swine_delivery_sales_model> data, EventListener listener){
        this.context = context;
        this.mdata = data;
        this.listener=listener;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.swine_delivery_sales_row,viewGroup,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, int position) {
        final String getSwine_age = mdata.get(position).getSwine_age();
        final String getDr_row = mdata.get(position).getDr_row();
        final String getSwine_type = mdata.get(position).getSwine_type();
        final String getAdg = mdata.get(position).getAdg();
        final String getDr_total_weight = mdata.get(position).getDr_total_weight();
        final String getDr_swine = mdata.get(position).getDr_swine();
        final String getGross_profit = mdata.get(position).getGross_profit();
        final String getSwine_weight = mdata.get(position).getSwine_weight();
        final String getTotal_amount_sold = mdata.get(position).getTotal_amount_sold();
        final String getWeight = mdata.get(position).getWeight();
        final String swine_id = mdata.get(position).getSwine_id();

        myHolder.tv_counter.setText(String.valueOf(position+1));
        myHolder.tv_adg.setText(getAdg);
        myHolder.tv_age.setText(getSwine_age);
        myHolder.tv_eartag.setText(getDr_swine);
        myHolder.tv_final_wt.setText(getSwine_weight);
        myHolder.tv_gross_profit.setText(getGross_profit);
        myHolder.tv_live_weight.setText(getDr_total_weight);
        myHolder.tv_swine_type.setText(getSwine_type);
        myHolder.tv_sold_amount.setText(getDr_row);
        myHolder.tv_cost.setText(getTotal_amount_sold);
        myHolder.tv_birth.setText(getWeight);

        myHolder.tv_eartag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.close_dismiss();
                Bundle bundle = new Bundle();
                bundle.putString("swine_id", String.valueOf(swine_id));
                FragmentTransaction fragmentTransaction = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
                RFscanner_main frag = new RFscanner_main();
                frag.setArguments(bundle);
                fragmentTransaction.add(R.id.container_home, frag, "Main_menu");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        TextView tv_counter, tv_eartag, tv_swine_type, tv_birth, tv_age, tv_gross_profit, tv_final_wt,
                tv_live_weight, tv_adg, tv_cost, tv_sold_amount;
        public MyHolder(View itemView) {
            super(itemView);
            tv_counter = itemView.findViewById(R.id.tv_counter);
            tv_eartag = itemView.findViewById(R.id.tv_eartag);
            tv_swine_type = itemView.findViewById(R.id.tv_swine_type);
            tv_birth = itemView.findViewById(R.id.tv_birth);
            tv_age = itemView.findViewById(R.id.tv_age);
            tv_final_wt = itemView.findViewById(R.id.tv_final_wt);
            tv_live_weight = itemView.findViewById(R.id.tv_live_weight);
            tv_adg = itemView.findViewById(R.id.tv_adg);
            tv_cost = itemView.findViewById(R.id.tv_cost);
            tv_sold_amount = itemView.findViewById(R.id.tv_sold_amount);
            tv_gross_profit = itemView.findViewById(R.id.tv_gross_profit);
        }
    }

    public interface EventListener {
        void close_dismiss();


    }

}
