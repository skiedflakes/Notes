package com.wdysolutions.notes.Globals.Price_Watch;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wdysolutions.notes.MainActivity;
import com.wdysolutions.notes.Globals.Price_Watch.pw_Graph.PW_graph;
import com.wdysolutions.notes.R;

import java.util.ArrayList;

public class Price_watch_adapter extends  RecyclerView.Adapter<Price_watch_adapter.MyHolder>{

    ArrayList<Price_watch_model> mdata;
    private Context context;
    private LayoutInflater inflater;
    private String category_type;

    public Price_watch_adapter(Context context, ArrayList<Price_watch_model> data,String category_type){
        this.context = context;
        this.mdata = data;
        this.category_type =category_type;
        inflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.price_watch_row,viewGroup,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, final int position) {
        final String region_color = mdata.get(position).getRegion_color();
        final String region = mdata.get(position).getRegion();
        final String lowest_price = mdata.get(position).getLowest_price();
        final String lowest_price_date = mdata.get(position).getLowest_price_date();
        final String highest_price = mdata.get(position).getHighest_price();
        final String highest_price_date = mdata.get(position).getHighest_price_date();
        final String lowest_arrow = mdata.get(position).getLowest_arrow();
        final String highest_arrow = mdata.get(position).getHighest_arrow();

        myHolder.bg_color.setBackgroundColor(Color.parseColor(region_color));
        myHolder.tv_region.setText(region);
        myHolder.tv_lowest_price.setText(lowest_price);
        myHolder.tv_low_date.setText(lowest_price_date);
        myHolder.tv_highest_price.setText(highest_price);
        myHolder.tv_high_date.setText(highest_price_date);
        Drawable arrow_down = context.getResources().getDrawable( R.drawable.arrow_down_red);
        Drawable arrow_up = context.getResources().getDrawable( R.drawable.arrow_up_green);
        int h = arrow_down.getIntrinsicHeight();
        int w = arrow_down.getIntrinsicWidth();
        arrow_down.setBounds( 0, 0, w, h );

        int x = arrow_up.getIntrinsicHeight();
        int y = arrow_up.getIntrinsicWidth();
        arrow_up.setBounds( 0, 0, y, x );

        if(lowest_arrow.equals("up")){
            myHolder.tv_lowest_price.setCompoundDrawables(arrow_up,null,null,null);
        }else if(lowest_arrow.equals("down")){
            myHolder.tv_lowest_price.setCompoundDrawables(arrow_down,null,null,null);
        }else{
            myHolder.tv_highest_price.setCompoundDrawables(null,null,null,null);
        }


        if(highest_arrow.equals("up")){
            myHolder.tv_highest_price.setCompoundDrawables(arrow_up,null,null,null);
        }else if(highest_arrow.equals("down")){
            myHolder.tv_highest_price.setCompoundDrawables(arrow_down,null,null,null);
        }else{
            myHolder.tv_highest_price.setCompoundDrawables(null,null,null,null);
        }

        myHolder.linear_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(region);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        TextView tv_region,tv_lowest_price,tv_low_date,tv_highest_price,tv_high_date,bg_color;
        LinearLayout linear_item;
        public MyHolder(View itemView) {
            super(itemView);
            tv_region = itemView.findViewById(R.id.tv_region);
            tv_lowest_price = itemView.findViewById(R.id.tv_lowest_price);
            tv_low_date = itemView.findViewById(R.id.tv_low_date);
            tv_highest_price = itemView.findViewById(R.id.tv_highest_price);
            tv_high_date = itemView.findViewById(R.id.tv_high_date);
            linear_item = itemView.findViewById(R.id.linear_item);
            bg_color = itemView.findViewById(R.id.bg_color);
        }
    }

    private void openDialog(String region){
        Bundle bundle = new Bundle();
        bundle.putString("product_type", category_type);
        bundle.putString("region", region);
        DialogFragment dialogFragment = new PW_graph();
        FragmentTransaction ft = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
        Fragment prev = ((MainActivity) context).getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {ft.remove(prev);}
        ft.addToBackStack(null);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(ft, "dialog");
    }

}
