package com.wdysolutions.notes.Globals.Micro_Filming;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wdysolutions.notes.R;

import java.util.ArrayList;

public class title_adapter extends RecyclerView.Adapter<title_adapter.MyHolder> {

    ArrayList<title_model> mdata;
    private Context context;
    private LayoutInflater inflater;
    clickRecycler clickRecycler;

    public title_adapter(Context context, ArrayList<title_model> data, clickRecycler clickRecycler){
        this.context = context;
        this.mdata = data;
        this.clickRecycler = clickRecycler;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.microfilming_title_row,viewGroup,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, final int position) {
        final String getRef_num = mdata.get(position).getRef_num();
        final String getType = mdata.get(position).getType();
        final String getAmount = mdata.get(position).getAmount();

        myHolder.txt_title.setText(getRef_num);
        myHolder.btn_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRecycler.clickInterface(getRef_num, getAmount);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        TextView txt_title;
        LinearLayout btn_;
        public MyHolder(View itemView) {
            super(itemView);
            btn_ = itemView.findViewById(R.id.btn_);
            txt_title = itemView.findViewById(R.id.txt_title);
        }
    }
}
