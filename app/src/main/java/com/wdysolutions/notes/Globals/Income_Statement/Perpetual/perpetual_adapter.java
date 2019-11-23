package com.wdysolutions.notes.Globals.Income_Statement.Perpetual;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wdysolutions.notes.R;

import java.util.ArrayList;

public class perpetual_adapter extends RecyclerView.Adapter<perpetual_adapter.MyHolder> {

    ArrayList<perpetual_model> mdata;
    private Context context;
    private LayoutInflater inflater;
    public perpetual_adapter(Context context,ArrayList<perpetual_model> data){
        this.context = context;
        this.mdata = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.income_statement_perpetual_row, viewGroup,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, final int position) {
        final String getSub_total = mdata.get(position).getSub_total();
        final String getName = mdata.get(position).getName();

        myHolder.txt_name.setText(getName);
        myHolder.txt_subtotal.setText(getSub_total);
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        TextView txt_name, txt_subtotal;
        public MyHolder(View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_subtotal = itemView.findViewById(R.id.txt_subtotal);
        }
    }

}
