package com.wdysolutions.notes.Notes_Pig.Farm_Statistics.Graph;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.wdysolutions.notes.AppController;
import com.wdysolutions.notes.Constants;
import com.wdysolutions.notes.MainActivity;
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


public class graph_main extends DialogFragment {

    String company_id, company_code, branch_id,
    start_year, end_year, module, table_is;
    Spinner spinner_start_year, spinner_end_year;
    LineChart lineChart;
    ProgressBar loading_graph;
    View view;
    Button btn_generate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.farm_stats_graph_main, container, false);
        SharedPref sharedPref = new SharedPref(getActivity());
        company_id = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYID);
        company_code = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYCODE);
        branch_id = Constants.branch_id;
        module = getArguments().getString("farm_statistics");
        table_is = getArguments().getString("table_is");
        start_year = String.valueOf(Constants.current_year_online);
        end_year = String.valueOf(Constants.current_year_online);

        btn_generate = view.findViewById(R.id.btn_generate);
        lineChart = view.findViewById(R.id.lineChart);
        loading_graph = view.findViewById(R.id.loading_graph);

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

    private String tableNow(){
        if (table_is.equals("bymonth")){
            return "farm_stats_bymonth_graph.php";
        } else {
            return "farm_stats_byweek_graph.php";
        }
    }

    private void initSpinner(View view){
        spinner_start_year = view.findViewById(R.id.spinner_start_year);
        ArrayAdapter<String> spinnerAdapter_year = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_drop, populateYearName());
        spinnerAdapter_year.setDropDownViewResource(R.layout.custom_spinner_drop);
        spinner_start_year.setAdapter(spinnerAdapter_year);
        spinner_start_year.setSelection(array_year.size()-1);
        spinner_start_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                start_year = array_year.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinner_end_year = view.findViewById(R.id.spinner_end_year);
        ArrayAdapter<String> spinnerAdapter_year_end = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_drop, populateYearName());
        spinnerAdapter_year_end.setDropDownViewResource(R.layout.custom_spinner_drop);
        spinner_end_year.setAdapter(spinnerAdapter_year_end);
        spinner_end_year.setSelection(array_year.size()-1);
        spinner_end_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                end_year = array_year.get(position);
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

    private String moth_year(){
        if (table_is.equals("bymonth")){
            return "Farm";
        } else {
            return "Weekly";
        }
    }

    ArrayList<Entry> array_data;
    public void getGraph() {
        loading_graph.setVisibility(View.VISIBLE);
        lineChart.setVisibility(View.GONE);
        btn_generate.setClickable(false);
        String URL = getString(R.string.URL_online)+"farmstatistics/"+tableNow();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    loading_graph.setVisibility(View.GONE);
                    lineChart.setVisibility(View.VISIBLE);
                    btn_generate.setClickable(true);

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray_graph = jsonObject.getJSONArray("graph_data");

                    LineDataSet line;
                    Random rnd = new Random();
                    ArrayList<ILineDataSet> ilineDataSets = new ArrayList<>();
                    for (int i=0; i<jsonArray_graph.length(); i++){
                        JSONObject jsonObject1 = jsonArray_graph.getJSONObject(i);

                        array_data = new ArrayList<>();
                        JSONArray data_array = jsonObject1.getJSONArray("data");
                        for (int j=0; j<data_array.length(); j++){

                            if (!data_array.getString(j).equals("null") && !data_array.getString(j).equals("")){
                                array_data.add(new Entry(Float.parseFloat(data_array.getString(j)), j));
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
                    lineChart.setDescription(module+" "+moth_year()+" Statistics "+start_year+" to "+end_year);
                    lineChart.setDescriptionColor(Color.BLACK);
                    lineChart.setData(new LineData(getChartLabel(), ilineDataSets));
                    lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                    //lineChart.getXAxis().setLabelRotationAngle(-30);
                    lineChart.setVisibleXRangeMaximum(20f);
                    lineChart.invalidate();
                    lineChart.setOnChartGestureListener(new OnChartGestureListener() {
                        @Override
                        public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

                        }
                        @Override
                        public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

                        }
                        @Override
                        public void onChartLongPressed(MotionEvent me) {

                        }
                        @Override
                        public void onChartDoubleTapped(MotionEvent me) {

                        }
                        @Override
                        public void onChartSingleTapped(MotionEvent me) {

                        }
                        @Override
                        public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

                        }
                        @Override
                        public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

                        }
                        @Override
                        public void onChartTranslate(MotionEvent me, float dX, float dY) {

                        }
                    });

                }
                catch (JSONException e){}
                catch (Exception e){}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    loading_graph.setVisibility(View.GONE);
                    lineChart.setVisibility(View.VISIBLE);
                    btn_generate.setClickable(true);
                    Toast.makeText(getActivity(), getResources().getString(R.string.volley_error_msg), Toast.LENGTH_SHORT).show();
                } catch (Exception e){}
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("company_id", company_id);
                hashMap.put("company_code", company_code);
                hashMap.put("branch_id", branch_id);

                hashMap.put("sy", start_year);
                hashMap.put("ey", end_year);
                hashMap.put("module", module);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private ArrayList<String> getChartLabel(){
        ArrayList<String> xAxes_label = new ArrayList<>();

        if (table_is.equals("bymonth")){
            xAxes_label.add("January");
            xAxes_label.add("February");
            xAxes_label.add("March");
            xAxes_label.add("April");
            xAxes_label.add("May");
            xAxes_label.add("June");
            xAxes_label.add("July");
            xAxes_label.add("August");
            xAxes_label.add("September");
            xAxes_label.add("October");
            xAxes_label.add("November");
            xAxes_label.add("December");
        } else {
            for (int i=1; i<= 52; i++){
                xAxes_label.add("Week "+i);
            }
        }

        return xAxes_label;
    }

    List<Integer> array_color = new ArrayList<>();
    private void getColor(){
        array_color.add(Color.argb(255, 0,0,0)); // black
        array_color.add(Color.argb(255, 128,0,0)); // maroon
        array_color.add(Color.argb(255, 0,128,0)); // green
        array_color.add(Color.argb(255, 128,128,0)); // olive
        array_color.add(Color.argb(255, 0,0,128)); // navy
        array_color.add(Color.argb(255, 128,0,128)); // purple
    }


}
