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

public class Brooding_Report_allbatch_sideheader_adapter extends RecyclerView.Adapter<Brooding_Report_allbatch_sideheader_adapter.MyHolder>{

    ArrayList<all_batch_header_model> mdata;
    private Context context;
    private LayoutInflater inflater;

    public Brooding_Report_allbatch_sideheader_adapter(Context context, ArrayList<all_batch_header_model> data){
        this.context = context;
        this.mdata = data;
        inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.egg_brooding_report_header_row, viewGroup,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, final int position) {
        final String get_name = mdata.get(position).getName();
        myHolder.txt_name.setText(get_name);
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        TextView txt_name;
        public MyHolder(View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_name);
        }
    }
}
