package com.wdysolutions.notes.Notes_Egg.Layer_Reports;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wdysolutions.notes.R;

import java.util.ArrayList;

public class Brooding_Report_allbatch_adapter extends RecyclerView.Adapter<Brooding_Report_allbatch_adapter.MyHolder>{
    ArrayList<all_batch_data_model> mdata;
    private Context context;
    private LayoutInflater inflater;

    public Brooding_Report_allbatch_adapter(Context context, ArrayList<all_batch_data_model> data){
        this.context = context;
        this.mdata = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.egg_brooding_report_row, viewGroup,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, final int position) {

        String breed = mdata.get(position).getBreed();
        String running_population = mdata.get(position).getRunning_population();
        String initial_population = mdata.get(position).getInitial_population();

        myHolder.tv_IP.setText(initial_population);
        myHolder.tv_RP.setText(running_population);
        myHolder.tv_variety.setText(breed);
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        TextView tv_variety, tv_IP,tv_RP;
        public MyHolder(View itemView) {
            super(itemView);
            tv_variety = itemView.findViewById(R.id.tv_variety);
            tv_IP = itemView.findViewById(R.id.tv_IP);
            tv_RP = itemView.findViewById(R.id.tv_RP);
        }
    }
}
