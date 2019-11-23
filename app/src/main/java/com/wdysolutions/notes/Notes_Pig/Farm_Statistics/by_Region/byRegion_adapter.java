package com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Region;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wdysolutions.notes.MainActivity;
import com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Region.byRegion_Graph.byRegion_graph_main;
import com.wdysolutions.notes.R;

import java.util.ArrayList;

public class byRegion_adapter extends RecyclerView.Adapter<byRegion_adapter.MyHolder>{

    ArrayList<byRegion_model> mdata;
    private Context context;
    private LayoutInflater inflater;
    private String farm_stats;


    public byRegion_adapter(Context context, ArrayList<byRegion_model> data, String farm_stats){
        this.context = context;
        this.mdata = data;
        this.farm_stats = farm_stats;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.farm_stats_byregion_row,viewGroup,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, final int position) {
        final String getMax_indic = mdata.get(position).getMax_indic();
        final String getMax = mdata.get(position).getMax();
        final String getMin_indic = mdata.get(position).getMin_indic();
        final String getMin = mdata.get(position).getMin();
        final String getRegion_colors = mdata.get(position).getRegion_colors();
        final String getRegion = mdata.get(position).getRegion();

        myHolder.tv_region.setText(getRegion);
        myHolder.bg_color.setBackgroundColor(Color.parseColor(getRegion_colors));
        myHolder.tv_high.setText(getMax);
        myHolder.tv_low.setText(getMin);

        if (getMax_indic.equals("up_green")){
            myHolder.icon_arrow_up.setVisibility(View.VISIBLE);
            myHolder.icon_arrow_up.setBackgroundResource(R.drawable.ic_arrow_up_24dp);
        } else if (getMax_indic.equals("down_red")) {
            myHolder.icon_arrow_up.setVisibility(View.VISIBLE);
            myHolder.icon_arrow_up.setBackgroundResource(R.drawable.ic_arrow_down_24dp);
        } else {
            myHolder.icon_arrow_up.setVisibility(View.GONE);
        }

        if (getMin_indic.equals("up_green")){
            myHolder.lowest_icon_arrow_up.setVisibility(View.VISIBLE);
            myHolder.lowest_icon_arrow_up.setBackgroundResource(R.drawable.ic_arrow_up_24dp);
        } else if (getMin_indic.equals("down_red")) {
            myHolder.lowest_icon_arrow_up.setVisibility(View.VISIBLE);
            myHolder.lowest_icon_arrow_up.setBackgroundResource(R.drawable.ic_arrow_down_24dp);
        } else {
            myHolder.lowest_icon_arrow_up.setVisibility(View.GONE);
        }

        myHolder.btn_high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(getRegion);
            }
        });

        myHolder.btn_low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(getRegion);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        TextView tv_region, tv_low, tv_high, bg_color;
        ImageView icon_arrow_up, lowest_icon_arrow_up;
        LinearLayout btn_high, btn_low;
        public MyHolder(View itemView) {
            super(itemView);
            btn_low = itemView.findViewById(R.id.btn_low);
            btn_high = itemView.findViewById(R.id.btn_high);
            tv_region = itemView.findViewById(R.id.tv_region);
            tv_low = itemView.findViewById(R.id.tv_low);
            tv_high = itemView.findViewById(R.id.tv_high);
            bg_color = itemView.findViewById(R.id.bg_color);
            icon_arrow_up = itemView.findViewById(R.id.icon_arrow_up);
            lowest_icon_arrow_up = itemView.findViewById(R.id.lowest_icon_arrow_up);
        }
    }

    private void openDialog(String region){
        Bundle bundle = new Bundle();
        bundle.putString("farm_statistics", farm_stats);
        bundle.putString("region", region);
        DialogFragment dialogFragment = new byRegion_graph_main();
        FragmentTransaction ft = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
        Fragment prev = ((MainActivity) context).getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {ft.remove(prev);}
        ft.addToBackStack(null);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(ft, "dialog");
    }

}
