package com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Region;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
import com.wdysolutions.notes.AppController;
import com.wdysolutions.notes.Constants;
import com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Month.Month_model;
import com.wdysolutions.notes.R;
import com.wdysolutions.notes.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class byregion_main extends Fragment {

    String company_id, category_id, company_code, selected_branch_id, user_id;
    View view;
    TextView txt_error, txt_result_title, txt_generate_error;
    ProgressBar loading_, progressBar;
    LinearLayout layout_main, layout_selection, layout_table;
    Button btn_generate;
    RecyclerView recyclerview;
    ImageView img_map;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.farm_stats_byregion_main, container, false);
        SharedPref sharedPref = new SharedPref(getActivity());
        company_id = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYID);
        company_code = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYCODE);
        category_id = sharedPref.getUserInfo().get(sharedPref.KEY_CATEGORYID);
        user_id = sharedPref.getUserInfo().get(sharedPref.KEY_USERID);
        selected_branch_id = Constants.branch_id;

        progressBar = view.findViewById(R.id.progressBar);
        txt_error = view.findViewById(R.id.txt_error);
        loading_ = view.findViewById(R.id.loading_);
        layout_main = view.findViewById(R.id.layout_main);
        btn_generate = view.findViewById(R.id.btn_generate);
        recyclerview = view.findViewById(R.id.recyclerview);
        txt_result_title = view.findViewById(R.id.txt_result_title);
        layout_table = view.findViewById(R.id.layout_table);
        layout_selection = view.findViewById(R.id.layout_selection);
        txt_generate_error = view.findViewById(R.id.txt_generate_error);
        img_map = view.findViewById(R.id.img_map);

        txt_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentYearMonth();
            }
        });

        btn_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedStats.equals("")){
                    Toast.makeText(getActivity(), "Please select Farm Statistics", Toast.LENGTH_SHORT).show();
                } else {
                    generate();
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

        getCurrentYearMonth();
        return view;
    }

    ArrayList<String> array_statistics;
    int current_year, current_month;
    public void getCurrentYearMonth() {
        layout_main.setVisibility(View.GONE);
        loading_.setVisibility(View.VISIBLE);
        txt_error.setVisibility(View.GONE);
        String URL = getString(R.string.URL_online) + "farmstatistics/farm_stats_byregion_spinner.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    layout_main.setVisibility(View.VISIBLE);
                    loading_.setVisibility(View.GONE);
                    txt_error.setVisibility(View.GONE);

                    JSONObject Object = new JSONObject(response);
                    
                    JSONArray jsonArray = Object.getJSONArray("data_date");
                    JSONObject jsonObject = (JSONObject)jsonArray.get(0);
                    current_year = Integer.valueOf(jsonObject.getString("current_year"));
                    current_month = Integer.valueOf(jsonObject.getString("current_month"));
                    Constants.current_year_online = current_year;

                    array_statistics = new ArrayList<>();
                    array_statistics.add("Select Farm Statistics");
                    JSONArray jsonArray_ = Object.getJSONArray("data_statistic");
                    for (int i=0; i<jsonArray_.length(); i++){
                        JSONObject jsonObject_ = (JSONObject)jsonArray_.get(i);
                        array_statistics.add(jsonObject_.getString("farm_statistics"));
                    }

                    initSpinner();

                }
                catch (JSONException e) {}
                catch (Exception e) {}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                layout_main.setVisibility(View.GONE);
                loading_.setVisibility(View.GONE);
                txt_error.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "Connection Error, please try again.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("company_id", company_id);
                hashMap.put("category_id", category_id);
                hashMap.put("user_id", user_id);
                hashMap.put("company_code", company_code);
                hashMap.put("branch_id", selected_branch_id);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    String selectedYear="", selectedMonth="", selectedStats="";
    private void initSpinner(){
        Spinner spinner_year = view.findViewById(R.id.spinner_year);
        Spinner spinner_month = view.findViewById(R.id.spinner_month);
        Spinner spinner_stats = view.findViewById(R.id.spinner_stats);

        ArrayAdapter<String> spinnerAdapter_ = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_drop, populateMonth());
        spinnerAdapter_.setDropDownViewResource(R.layout.custom_spinner_drop);
        spinner_month.setAdapter(spinnerAdapter_);
        spinner_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Month_model click = month_models.get(position);
                selectedMonth = click.getDate_month();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ArrayAdapter<String> spinnerAdapter_year = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_drop, populateYear());
        spinnerAdapter_year.setDropDownViewResource(R.layout.custom_spinner_drop);
        spinner_year.setAdapter(spinnerAdapter_year);
        spinner_year.setSelection(array_year.size()-1);
        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedYear = array_year.get(position);
                Constants.selectedYear = selectedYear;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ArrayAdapter<String> spinnerAdapter_stats = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_drop, array_statistics);
        spinnerAdapter_stats.setDropDownViewResource(R.layout.custom_spinner_drop);
        spinner_stats.setAdapter(spinnerAdapter_stats);
        spinner_stats.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (array_statistics.get(position).equals("Select Farm Statistics")){
                    selectedStats = "";
                } else {
                    selectedStats = array_statistics.get(position);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    List<String> array_year;
    private List<String> populateYear(){
        array_year = new ArrayList<>();
        for (int i=2014; i<=current_year; i++){
            array_year.add(String.valueOf(i));
        }
        return array_year;
    }

    List<Month_model> month_models;
    private List<String> populateMonth(){
        month_models = new ArrayList<>();
        month_models.add(new Month_model("January","1"));
        month_models.add(new Month_model("February","2"));
        month_models.add(new Month_model("March","3"));
        month_models.add(new Month_model("April","4"));
        month_models.add(new Month_model("May","5"));
        month_models.add(new Month_model("June","6"));
        month_models.add(new Month_model("July","7"));
        month_models.add(new Month_model("August","8"));
        month_models.add(new Month_model("September","9"));
        month_models.add(new Month_model("October","10"));
        month_models.add(new Month_model("November","11"));
        month_models.add(new Month_model("December","12"));

        List<String> lables = new ArrayList<>();
        for (int i = 0; i < month_models.size(); i++) {
            lables.add(month_models.get(i).getMonth());
        }

        return lables;
    }

    ArrayList<byRegion_model> byRegion_models;
    public void generate() {
        btn_generate.setEnabled(false);
        layout_table.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        txt_generate_error.setVisibility(View.GONE);
        String URL = getString(R.string.URL_online) + "farmstatistics/farm_stats_byregion_generate.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    btn_generate.setEnabled(true);
                    layout_table.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                    txt_result_title.setText("Farm Statistics Per Region - "+selectedStats);
                    byRegion_models = new ArrayList<>();
                    JSONObject Object = new JSONObject(response);
                    JSONArray jsonArray = Object.getJSONArray("data_details");

                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = (JSONObject)jsonArray.get(i);

                        byRegion_models.add(new byRegion_model(jsonObject.getString("max"),
                                jsonObject.getString("max_indic"),
                                jsonObject.getString("min"),
                                jsonObject.getString("min_indic"),
                                jsonObject.getString("region"),
                                jsonObject.getString("region_colors")));
                    }

                    byRegion_adapter byRegion_adapter = new byRegion_adapter(getContext(), byRegion_models, selectedStats);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    recyclerview.setLayoutManager(layoutManager);
                    recyclerview.setAdapter(byRegion_adapter);
                    recyclerview.setNestedScrollingEnabled(false);

                }
                catch (JSONException e) {}
                catch (Exception e) {}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                btn_generate.setEnabled(true);
                layout_table.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                txt_generate_error.setVisibility(View.VISIBLE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("company_id", company_id);
                hashMap.put("company_code", company_code);
                hashMap.put("farm_statistics", selectedStats);
                hashMap.put("stat_year", selectedYear);
                hashMap.put("stat_month", selectedMonth);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


}
