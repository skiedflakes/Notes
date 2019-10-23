package com.wdysolutions.notes.Notes_Pig.Swine_Population.Dialog_Table;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wdysolutions.notes.MainActivity;
import com.wdysolutions.notes.Notes_Pig.Swine_Card.RFscanner_main;
import com.wdysolutions.notes.R;

import java.util.ArrayList;

public class dialogTable_adapter extends RecyclerView.Adapter<dialogTable_adapter.MyHolder>{

    ArrayList<dialogTable_model> mdata;
    private Context context;
    private LayoutInflater inflater;
    EventListener listener;

    public dialogTable_adapter(Context context, ArrayList<dialogTable_model> data,EventListener listener){
        this.context = context;
        this.mdata = data;
        this.listener=listener;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.swine_population_dialog_table_row,viewGroup,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final dialogTable_adapter.MyHolder myHolder, final int position) {
        final String getCurrent_pen = mdata.get(position).getCurrent_pen();
        final String getSw_id = mdata.get(position).getSw_id();
        final String getSw_type = mdata.get(position).getSw_type();
        final String getAge = mdata.get(position).getAge();
        final String getSw_code = mdata.get(position).getSw_code();

        myHolder.tv_1.setText(String.valueOf(position+1));
        myHolder.tv_2.setText(getSw_code);
        myHolder.tv_3.setText(getSw_type);
        myHolder.tv_4.setText(getCurrent_pen);
        myHolder.tv_5.setText(getAge);

        myHolder.tv_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.close_dismiss();
                Bundle bundle = new Bundle();
                bundle.putString("swine_id", String.valueOf(getSw_id));
                FragmentTransaction fragmentTransaction = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
                RFscanner_main frag = new RFscanner_main();
                frag.setArguments(bundle);
                //fragmentTransaction.setCustomAnimations(android.R.anim.fade_out, android.R.anim.slide_out_right);
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
        TextView tv_1, tv_2, tv_3, tv_4, tv_5;
        public MyHolder(View itemView) {
            super(itemView);
            tv_1 = itemView.findViewById(R.id.tv_1);
            tv_2 = itemView.findViewById(R.id.tv_2);
            tv_3 = itemView.findViewById(R.id.tv_3);
            tv_4 = itemView.findViewById(R.id.tv_4);
            tv_5 = itemView.findViewById(R.id.tv_5);
        }
    }

    public interface EventListener {
        void close_dismiss();
    }

}
