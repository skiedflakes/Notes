package com.wdysolutions.notes.Notes_Pig.Parity_Report;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.wdysolutions.notes.AppController;
import com.wdysolutions.notes.Constants;
import com.wdysolutions.notes.MainActivity;
import com.wdysolutions.notes.Notes_Pig.Farm_Statistics.Graph.CustomMarkerView;
import com.wdysolutions.notes.Notes_Pig.Farm_Statistics.Graph.MyValueFormatter;
import com.wdysolutions.notes.R;
import com.wdysolutions.notes.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class Parity_report_main extends Fragment {

    String company_id, company_code, branch_id;
    BarChart barChart;
    RecyclerView recyclerView;
    Button btn_age_by_month, btn_parities;
    ProgressBar loading_;
    LinearLayout layout_main;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parity_report_main, container, false);
        SharedPref sharedPref = new SharedPref(getActivity());
        company_id = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYID);
        company_code = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYCODE);
        branch_id = Constants.branch_id;

        layout_main = view.findViewById(R.id.layout_main);
        loading_ = view.findViewById(R.id.loading_);
        barChart = view.findViewById(R.id.barChart);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        btn_age_by_month = view.findViewById(R.id.btn_age_by_month);
        btn_parities = view.findViewById(R.id.btn_parities);

        btn_parities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_age_by_month.setBackgroundResource(R.drawable.btn_ripple_light_blue);
                btn_parities.setBackgroundResource(R.drawable.btn_ripple_blue);
                displayGraph("parities", "Parity");
            }
        });

        btn_age_by_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_age_by_month.setBackgroundResource(R.drawable.btn_ripple_blue);
                btn_parities.setBackgroundResource(R.drawable.btn_ripple_light_blue);
                displayGraph("age_by_month", "Age by Month");
            }
        });

        getGraph();
        return view;
    }

    private void displayGraph(String jsonArray_name, String graph_label){
        try {
            parity_report_models = new ArrayList<>();
            JSONArray jsonArray = jsonObject.getJSONArray(jsonArray_name);

            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                JSONArray data_array = json.getJSONArray("data");

                parity_report_models.add(new Parity_report_model(
                        json.getString("name"),
                        json.getString("company_name"),
                        data_array));
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(new Parity_adapter(getActivity(), parity_report_models, graph_label));
            recyclerView.setNestedScrollingEnabled(false);

        }
        catch (JSONException e){}
        catch (Exception e){}
    }

    JSONObject jsonObject;
    ArrayList<Parity_report_model> parity_report_models;
    public void getGraph() {
        layout_main.setVisibility(View.GONE);
        loading_.setVisibility(View.VISIBLE);
        String URL = getString(R.string.URL_online)+"parity_report/parity_report_branch.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    //((MainActivity)getActivity()).openDialog(response);
                    layout_main.setVisibility(View.VISIBLE);
                    loading_.setVisibility(View.GONE);
                    jsonObject = new JSONObject(response);

                    // default
                    displayGraph("parities", "Parity");
                }
                catch (JSONException e){}
                catch (Exception e){}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    getGraph();
                    //Toast.makeText(getActivity(), getResources().getString(R.string.volley_error_msg), Toast.LENGTH_SHORT).show();
                } catch (Exception e){}
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("company_id", company_id);
                hashMap.put("company_code", company_code);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }



}
