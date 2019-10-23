package com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Region.byRegion_Graph;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.wdysolutions.notes.AppController;
import com.wdysolutions.notes.Constants;
import com.wdysolutions.notes.MainActivity;
import com.wdysolutions.notes.Notes_Pig.Farm_Statistics.Dialog_Table_Details.dialogTable_adapter;
import com.wdysolutions.notes.Notes_Pig.Farm_Statistics.Dialog_Table_Details.dialogTable_details;
import com.wdysolutions.notes.Notes_Pig.Farm_Statistics.Dialog_Table_Details.dialogTable_model;
import com.wdysolutions.notes.Notes_Pig.Farm_Statistics.Graph.CustomMarkerView;
import com.wdysolutions.notes.Notes_Pig.Farm_Statistics.Graph.MyValueFormatter;
import com.wdysolutions.notes.R;
import com.wdysolutions.notes.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class byRegion_graph_main extends DialogFragment {

    String company_id, company_code, selected_branch_id, farm_statistics, region, selectedYear;
    LineChart lineChart;
    Spinner spinner_year;
    Button btn_generate;
    ProgressBar loading_graph;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.farm_stats_byregion_graph_main, container, false);
        SharedPref sharedPref = new SharedPref(getActivity());
        company_id = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYID);
        company_code = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYCODE);
        selected_branch_id = Constants.branch_id;
        selectedYear = String.valueOf(Constants.current_year_online);

        farm_statistics = getArguments().getString("farm_statistics");
        region = getArguments().getString("region");

        lineChart = view.findViewById(R.id.lineChart);
        loading_graph = view.findViewById(R.id.loading_graph);
        btn_generate = view.findViewById(R.id.btn_generate);

        btn_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGraph();
            }
        });

        initSpinner(view);
        getGraph();
        return view;
    }

    private void initSpinner(View view){
        spinner_year = view.findViewById(R.id.spinner_year);
        ArrayAdapter<String> spinnerAdapter_year = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_drop, populateYearName());
        spinnerAdapter_year.setDropDownViewResource(R.layout.custom_spinner_drop);
        spinner_year.setAdapter(spinnerAdapter_year);
        spinner_year.setSelection(array_year.size()-1);
        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedYear = array_year.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    ArrayList<String> array_year;
    private List<String> populateYearName(){
        array_year = new ArrayList<>();
        for (int i = 2014; i <= Constants.current_year_online; i++) {
            array_year.add(String.valueOf(i));
        }
        return array_year;
    }

    ArrayList<Entry> array_data;
    public void getGraph() {
        loading_graph.setVisibility(View.VISIBLE);
        lineChart.setVisibility(View.GONE);
        btn_generate.setEnabled(false);
        String URL = getString(R.string.URL_online) + "farmstatistics/farm_stats_byregion_graph.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    loading_graph.setVisibility(View.GONE);
                    lineChart.setVisibility(View.VISIBLE);
                    btn_generate.setEnabled(true);

                    //((MainActivity)getActivity()).openDialog(response);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray_graph = jsonObject.getJSONArray("graph_data");

                    LineDataSet line;
                    Random rnd = new Random();
                    ArrayList<ILineDataSet> ilineDataSets = new ArrayList<>();
                    for (int i=0; i<jsonArray_graph.length(); i++){
                        JSONObject jsonObject1 = (JSONObject)jsonArray_graph.get(i);

                        array_data = new ArrayList<>();
                        JSONArray jsonArray1 = jsonObject1.getJSONArray("data");
                        for (int j=0; j<jsonArray1.length(); j++){

                            if (!jsonArray1.getString(j).equals("null") && !jsonArray1.getString(j).equals("")){
                                array_data.add(new Entry(Float.parseFloat(jsonArray1.getString(j)), j));
                            }
                        }

                        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                        line = new LineDataSet(array_data, jsonObject1.getString("name"));
                        line.setValueFormatter(new MyValueFormatter());
                        line.setDrawCircles(true);
                        line.setCircleColorHole(color);
                        line.setCircleColor(color);
                        line.setColor(color);
                        line.setValueTextSize(10);

                        ilineDataSets.add(line);
                    }

                    CustomMarkerView mv = new CustomMarkerView (getActivity(), R.layout.farm_stats_graph_markerview, getChartLabel());
                    lineChart.setMarkerView(mv);
                    lineChart.setTouchEnabled(true);
                    lineChart.setDescription(region+" Farm Statistics - "+farm_statistics);
                    lineChart.setDescriptionColor(Color.BLACK);
                    lineChart.setData(new LineData(getChartLabel(), ilineDataSets));
                    lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                    //lineChart.getXAxis().setLabelRotationAngle(-30);
                    lineChart.setVisibleXRangeMaximum(20f);
                    lineChart.invalidate();

                }
                catch (JSONException e){}
                catch (Exception e){}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading_graph.setVisibility(View.GONE);
                lineChart.setVisibility(View.VISIBLE);
                btn_generate.setEnabled(true);
                Toast.makeText(getActivity(), getResources().getString(R.string.volley_error_msg), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("region", region);
                hashMap.put("year", selectedYear);
                hashMap.put("farm_statistics", farm_statistics);
                hashMap.put("company_id", company_id);
                hashMap.put("company_code", company_code);
                hashMap.put("branch_id", selected_branch_id);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private ArrayList<String> getChartLabel(){
        ArrayList<String> xAxes_label = new ArrayList<>();
        for (int i=1; i<= 52; i++){
            xAxes_label.add("Week "+i);
        }
        return xAxes_label;
    }


}
