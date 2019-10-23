package com.wdysolutions.notes.Notes_Pig.Farm_Statistics.Dialog_Table_Details;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wdysolutions.notes.MainActivity;
import com.wdysolutions.notes.Notes_Pig.Swine_Card.RFscanner_main;
import com.wdysolutions.notes.R;

import java.util.ArrayList;

public class dialogTable_adapter extends RecyclerView.Adapter<dialogTable_adapter.MyHolder>{

    ArrayList<dialogTable_model> mdata;
    private Context context;
    private LayoutInflater inflater;
    String tv_4_isHide, tv_5_isHide, tv_6_isHide;
    EventListener listener;


    public dialogTable_adapter(Context context, ArrayList<dialogTable_model> data, String tv_4, String tv_5, String tv_6, EventListener listener){
        this.context = context;
        this.mdata = data;
        this.tv_4_isHide = tv_4;
        this.tv_5_isHide = tv_5;
        this.tv_6_isHide = tv_6;
        this.listener = listener;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.farm_stats_dialogtable_details_row,viewGroup,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, final int position) {
        final String getTx_2 = mdata.get(position).getTx_2();
        final String getTx_3 = mdata.get(position).getTx_3();
        final String getTx_4 = mdata.get(position).getTx_4();
        final String getTx_5 = mdata.get(position).getTx_5();
        final String getTx_6 = mdata.get(position).getTx_6();
        final String getSwine_id = mdata.get(position).getSwine_id();

        myHolder.tv_1.setText(String.valueOf(position+1));
        myHolder.tv_2.setText(getTx_2);
        myHolder.tv_3.setText(getTx_3);
        if (!tv_4_isHide.equals("")) {
            if(getTx_4.equals("null")){
                myHolder.tv_4.setText("");
            } else {
                myHolder.tv_4.setText(getTx_4);
            }
            myHolder.tv_4.setVisibility(View.VISIBLE);
        }
        if (!tv_5_isHide.equals("")) { myHolder.tv_5.setText(getTx_5); myHolder.tv_5.setVisibility(View.VISIBLE); }
        if (!tv_6_isHide.equals("")) { myHolder.tv_6.setText(getTx_6); myHolder.tv_6.setVisibility(View.VISIBLE); }

        myHolder.tv_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!getSwine_id.equals("")){
                    listener.close_dismiss();
                    Bundle bundle = new Bundle();
                    bundle.putString("swine_id", getSwine_id);
                    FragmentTransaction fragmentTransaction = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
                    RFscanner_main frag = new RFscanner_main();
                    frag.setArguments(bundle);
                    //fragmentTransaction.setCustomAnimations(android.R.anim.fade_out, android.R.anim.slide_out_right);
                    fragmentTransaction.add(R.id.container_home, frag, "Main_menu");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        TextView tv_1, tv_2, tv_3, tv_4, tv_5, tv_6;
        public MyHolder(View itemView) {
            super(itemView);
            tv_1 = itemView.findViewById(R.id.tv_1);
            tv_2 = itemView.findViewById(R.id.tv_2);
            tv_3 = itemView.findViewById(R.id.tv_3);
            tv_4 = itemView.findViewById(R.id.tv_4);
            tv_5 = itemView.findViewById(R.id.tv_5);
            tv_6 = itemView.findViewById(R.id.tv_6);
        }
    }

    public interface EventListener {
        void close_dismiss();
    }


}
