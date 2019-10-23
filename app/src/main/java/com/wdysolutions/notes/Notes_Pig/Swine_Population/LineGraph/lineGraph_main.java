package com.wdysolutions.notes.Notes_Pig.Swine_Population.LineGraph;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.wdysolutions.notes.DatePicker.DatePickerCustom;
import com.wdysolutions.notes.DatePicker.DatePickerSelectionInterfaceCustom;
import com.wdysolutions.notes.MainActivity;
import com.wdysolutions.notes.Notes_Pig.Farm_Statistics.Graph.CustomMarkerView;
import com.wdysolutions.notes.Notes_Pig.Farm_Statistics.Graph.MyValueFormatter;
import com.wdysolutions.notes.R;
import com.wdysolutions.notes.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;


public class lineGraph_main extends Fragment implements DatePickerSelectionInterfaceCustom {

    String company_id, company_code, selected_branch_id;
    LineChart lineChart;
    TextView txt_date, txt_result_title;
    Spinner spinner_region;
    Button btn_generate;
    ProgressBar progressBar, loading_main;
    LinearLayout layout_selection, layout_main;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.swine_population_line_graph_main, container, false);
        SharedPref sharedPref = new SharedPref(getActivity());
        company_id = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYID);
        company_code = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYCODE);
        selected_branch_id = Constants.branch_id;

        lineChart = view.findViewById(R.id.lineChart);
        txt_date = view.findViewById(R.id.txt_date);
        spinner_region = view.findViewById(R.id.spinner_region);
        btn_generate = view.findViewById(R.id.btn_generate);
        progressBar = view.findViewById(R.id.progressBar);
        txt_result_title = view.findViewById(R.id.txt_result_title);
        layout_selection = view.findViewById(R.id.layout_selection);
        loading_main = view.findViewById(R.id.loading_main);
        layout_main = view.findViewById(R.id.layout_main);

        txt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });

        btn_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedRegion.equals("")){
                    Toast.makeText(getActivity(), "Please select region", Toast.LENGTH_SHORT).show();
                } else if (selectedDate.equals("")){
                    Toast.makeText(getActivity(), "Please select date", Toast.LENGTH_SHORT).show();
                } else {
                    generateGraph();
                }
            }
        });

        txt_result_title.setOnClickListener(new View.OnClickListener() {
            boolean isToggle = false;
            @Override
            public void onClick(View v) {
                if (isToggle){
                    isToggle = false;
                    Drawable img = getContext().getResources().getDrawable( R.drawable.ic_arrow_drop_down_24dp );
                    img.setBounds(0, 0, 48, 48);
                    txt_result_title.setCompoundDrawables(null, null, img, null);
                    layout_selection.setVisibility(View.VISIBLE);
                } else {
                    isToggle = true;
                    Drawable img = getContext().getResources().getDrawable( R.drawable.ic_arrow_drop_up_24dp );
                    img.setBounds(0, 0, 48, 48);
                    txt_result_title.setCompoundDrawables(null, null, img, null);
                    layout_selection.setVisibility(View.GONE);
                }
            }
        });

        getDateLimit();
        initSpinner(view);
        return view;
    }

    String restrictedDate, currentDate;
    public void getDateLimit() {
        layout_main.setVisibility(View.GONE);
        loading_main.setVisibility(View.VISIBLE);
        String URL = getString(R.string.URL_online) + "dateLimits.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    layout_main.setVisibility(View.VISIBLE);
                    loading_main.setVisibility(View.GONE);

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("dateLimit");
                    JSONObject jsonObject_ = (JSONObject)jsonArray.get(0);
                    restrictedDate = jsonObject_.getString("restrictedD");
                    currentDate = jsonObject_.getString("current_date");
                }
                catch (JSONException e){}
                catch (Exception e){}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                layout_main.setVisibility(View.VISIBLE);
                loading_main.setVisibility(View.GONE);
                Toast.makeText(getActivity(), getResources().getString(R.string.volley_error_msg), Toast.LENGTH_SHORT).show();
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

    String selectedRegion = "";
    private void initSpinner(View view){
        final List<String> region = new ArrayList<>();
        region.add("Please select region");
        region.add("NCR - National Capital Region");
        region.add("CAR - Cordillera Administrative Region");
        region.add("REGION I (Ilocos Region)");
        region.add("REGION II (Cagayan Valley)");
        region.add("REGION III (Central Luzon)");
        region.add("REGION IV-A (CALABARZON)");
        region.add("REGION IV-B(MIMAROPA)");
        region.add("REGION V (Bicol Region)");
        region.add("REGION VI (Western Visayas)");
        region.add("REGION VII (Central Visayas)");
        region.add("REGION VIII (Eastern Visayas)");
        region.add("REGION IX (Zamboanga Peninsula)");
        region.add("REGION X (Northern Mindanao)");
        region.add("REGION XI (Davao Region)");
        region.add("REGION XII (Soccsksargen)");
        region.add("REGION XIII (Caraga)");
        region.add("ARMM - Autonomous Region in Muslim Mindanao");
        spinner_region = view.findViewById(R.id.spinner_region);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_drop, region);
        spinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_drop);
        spinner_region.setAdapter(spinnerAdapter);
        spinner_region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (region.get(position).equals("Please select region")){
                    selectedRegion = "";
                } else {
                    selectedRegion = region.get(position);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    ArrayList<Entry> array_data;
    public void generateGraph() {
        progressBar.setVisibility(View.VISIBLE);
        lineChart.setVisibility(View.GONE);
        btn_generate.setEnabled(false);
        String URL = getString(R.string.URL_online) + "swine_population/line_graph/graph_generate.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    //((MainActivity)getActivity()).openDialog(response);
                    progressBar.setVisibility(View.GONE);
                    lineChart.setVisibility(View.VISIBLE);
                    btn_generate.setEnabled(true);

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

                    // date
                    JSONArray jsonArray_date = jsonObject.getJSONArray("graph_date");
                    JSONObject jsonObject_date = jsonArray_date.getJSONObject(0);


                    CustomMarkerView mv = new CustomMarkerView (getActivity(), R.layout.farm_stats_graph_markerview, getChartLabel());
                    lineChart.setMarkerView(mv);
                    lineChart.setTouchEnabled(true);
                    lineChart.setDescription("Swine Population Graph as of "+jsonObject_date.getString("date"));
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
                progressBar.setVisibility(View.GONE);
                lineChart.setVisibility(View.VISIBLE);
                btn_generate.setEnabled(true);
                Toast.makeText(getActivity(), getResources().getString(R.string.volley_error_msg), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("company_id", company_id);
                hashMap.put("company_code", company_code);
                hashMap.put("branch_id", selected_branch_id);

                hashMap.put("graph_date", selectedDate);
                hashMap.put("region", selectedRegion);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private ArrayList<String> getChartLabel(){
        ArrayList<String> xAxes_label = new ArrayList<>();
        xAxes_label.add("Jan");
        xAxes_label.add("Feb");
        xAxes_label.add("Mar");
        xAxes_label.add("Apr");
        xAxes_label.add("May");
        xAxes_label.add("Jun");
        xAxes_label.add("Jul");
        xAxes_label.add("Aug");
        xAxes_label.add("Sep");
        xAxes_label.add("Oct");
        xAxes_label.add("Nov");
        xAxes_label.add("Dec");
        return xAxes_label;
    }

    String selectedDate = "";
    public void openDatePicker() {
        DatePickerCustom datePickerFragment = new DatePickerCustom();

        Bundle bundle = new Bundle();
        bundle.putString("maxDate", currentDate);
        bundle.putString("minDate", restrictedDate);
        bundle.putBoolean("isSetMinDate", true);
        datePickerFragment.setArguments(bundle);

        datePickerFragment.delegate = this;
        datePickerFragment.setCancelable(false);
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSelected(String date) {
        selectedDate = date;
        txt_date.setText(selectedDate);
    }
}
