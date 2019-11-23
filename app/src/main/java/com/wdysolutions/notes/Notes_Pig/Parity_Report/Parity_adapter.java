package com.wdysolutions.notes.Notes_Pig.Parity_Report;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.wdysolutions.notes.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class Parity_adapter extends RecyclerView.Adapter<Parity_adapter.MyHolder>{

    ArrayList<Parity_report_model> mdata;
    private Context context;
    private LayoutInflater inflater;
    String graph_label;

    public Parity_adapter(Context context, ArrayList<Parity_report_model> data, String graph_label){
        this.context = context;
        this.mdata = data;
        this.graph_label = graph_label;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.parity_report_row,viewGroup,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, int position) {
        final String getBranch = mdata.get(position).getBranch();
        final String getCompany_name = mdata.get(position).getCompany_name();
        final JSONArray getJsonArray = mdata.get(position).getJsonArray();

        if (graph_label.equals("Parity")){
            myHolder.txt_branch.setText(getCompany_name+" No. of Sows Per Parity \n"+getBranch);
        } else {
            myHolder.txt_branch.setText(getCompany_name+" No. of Gilts by Age \n"+getBranch);
        }
        setGraph(getJsonArray, myHolder, graph_label);
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        TextView txt_branch;
        BarChart barChart;
        public MyHolder(View itemView) {
            super(itemView);
            txt_branch = itemView.findViewById(R.id.txt_branch);
            barChart = itemView.findViewById(R.id.barChart);
        }
    }

    ArrayList<BarEntry> array_entry;
    ArrayList<IBarDataSet> iBarDataSets;
    ArrayList<String> xAxes_label;
    private void setGraph(JSONArray array, MyHolder myHolder, String graph_label){
        try {
            BarDataSet bar = null;
            Random rnd = new Random();
            xAxes_label = new ArrayList<>();
            iBarDataSets = new ArrayList<>();
            array_entry = new ArrayList<>();

            for (int j=0; j<array.length(); j++) {
                JSONObject json_data = (JSONObject)array.get(j);
                String y = json_data.getString("y");
                String name = json_data.getString("name");

                array_entry.add(new BarEntry(Integer.valueOf(y), j));
                xAxes_label.add(name);

                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                bar = new BarDataSet(array_entry, graph_label);
                bar.setColor(color);
            }

            bar.setValueFormatter(new myFormatter());
            bar.setValueTextSize(10);
            iBarDataSets.add(bar);

            //CustomMarkerView mv = new CustomMarkerView (context, R.layout.farm_stats_graph_markerview, xAxes_label);
            //myHolder.barChart.setMarkerView(mv);
            myHolder.barChart.setTouchEnabled(true);
            myHolder.barChart.setDescription("");
            myHolder.barChart.setDescriptionColor(Color.BLACK);
            myHolder.barChart.setData(new BarData(xAxes_label, iBarDataSets));
            myHolder.barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            myHolder.barChart.setVisibleXRangeMaximum(20f);
            myHolder.barChart.invalidate();

        }
        catch (JSONException e){}
        catch (Exception e){}
    }
}
