package com.wdysolutions.notes.Globals.Price_Watch;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.LineChart;
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

public class Price_watch_main extends Fragment {
    String company_id, company_code, selected_branch_id;
    int current_year;
    LineChart lineChart;
    TextView txt_date, txt_result_title;
    Spinner spinner_category;
    Button btn_generate;
    ProgressBar progressBar, loading_main;
    LinearLayout layout_selection, layout_main,layout_table;
    RelativeLayout layout_access;
    RecyclerView recyclerview;

    //category
    final List<String> category = new ArrayList<>();

    final List<String> category_id = new ArrayList<>();
    String selectedCategory = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.price_watch_main, container, false);
        SharedPref sharedPref = new SharedPref(getActivity());
        company_id = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYID);
        company_code = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYCODE);
        selected_branch_id = Constants.branch_id;

        lineChart = view.findViewById(R.id.lineChart);
        txt_date = view.findViewById(R.id.txt_date);
        spinner_category = view.findViewById(R.id.spinner_category);
        btn_generate = view.findViewById(R.id.btn_generate);
        progressBar = view.findViewById(R.id.progressBar);
        txt_result_title = view.findViewById(R.id.txt_result_title);
        layout_selection = view.findViewById(R.id.layout_selection);
        loading_main = view.findViewById(R.id.loading_main);
        layout_main = view.findViewById(R.id.layout_main);
        layout_access = view.findViewById(R.id.layout_access);
        recyclerview = view.findViewById(R.id.recyclerview);
        layout_table  = view.findViewById(R.id.layout_table);


        btn_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(selectedCategory.equals("Please select category")){
                        Toast.makeText(getContext(), "Please select category", Toast.LENGTH_SHORT).show();
                    }else{
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

        check_access(view);

        return view;
    }


    public void check_access(final View view) {

        layout_main.setVisibility(View.GONE);
        loading_main.setVisibility(View.VISIBLE);
        layout_access.setVisibility(View.GONE);
        String URL = getString(R.string.URL_online) + "price_watch/check_access2.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading_main.setVisibility(View.GONE);
                //((MainActivity)getActivity()).openDialog(response);
                    try{
                        JSONObject Object = new JSONObject(response);
                        JSONArray jsonArray = Object.getJSONArray("data_access");
                        JSONObject jsonObject = (JSONObject)jsonArray.get(0);
                        String access =   jsonObject.getString("status");

                        if(access.equals("1")){
                            layout_access.setVisibility(View.GONE);
                            layout_main.setVisibility(View.VISIBLE);

                            if(Constants.program_code.equals("P")){
                                initSpinner(view);
                            }else{

                                category.add("Please select category");
                                category_id.add("Please select category");
                                JSONArray jsonArray2 = Object.getJSONArray("data_category");
                                price_watch_models = new ArrayList<>();
                                for (int i=0; i<jsonArray2.length(); i++) {
                                    JSONObject jsonObject2 = (JSONObject)jsonArray2.get(i);
                                    category.add(jsonObject2.getString("pwc_desc"));
                                    category_id.add(jsonObject2.getString("pwc_id"));

                                }

                                spinner_category = view.findViewById(R.id.spinner_category);
                                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_drop, category);
                                spinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_drop);
                                spinner_category.setAdapter(spinnerAdapter);
                                spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                        if (category.get(position).equals("Please select region")){
                                            selectedCategory = "";
                                        } else {
                                            selectedCategory = category_id.get(position);
                                        }

                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                    }
                                });
                            }



                        }else{
                            layout_access.setVisibility(View.VISIBLE);

                        }

                    }catch (Exception e){}

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), getResources().getString(R.string.volley_error_msg), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("company_id", company_id);
                hashMap.put("company_code", company_code);
                hashMap.put("selected_program", Constants.program_code);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


    private void initSpinner(View view){

        category.add("Please select category");
        category.add("Piglet");
        category.add("Weaner");
        category.add("Grower");
        category.add("Finisher");
        category.add("Junior-boar");
        category.add("Senior-boar");
        category.add("Gilt");
        category.add("Sow");
        spinner_category = view.findViewById(R.id.spinner_category);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_drop, category);
        spinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_drop);
        spinner_category.setAdapter(spinnerAdapter);
        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (category.get(position).equals("Please select region")){
                    selectedCategory = "";
                } else {
                    selectedCategory = category.get(position);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

ArrayList<Price_watch_model> price_watch_models;
    public void generateGraph() {
        progressBar.setVisibility(View.VISIBLE);
        layout_table.setVisibility(View.GONE);
        btn_generate.setEnabled(false);
         String URL="";
        if(Constants.program_code.equals("P")){
           URL  = getString(R.string.URL_online) + "price_watch/pig_get_details.php";
        }else{
           URL  = getString(R.string.URL_online) + "price_watch/get_details.php";
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                btn_generate.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                layout_table.setVisibility(View.VISIBLE);
                try {
                 //   ((MainActivity)getActivity()).openDialog(response);
                    JSONObject Object = new JSONObject(response);

                    JSONArray jsonArray2 = Object.getJSONArray("data_date");
                    JSONObject jsonObject2 = (JSONObject)jsonArray2.get(0);
                    current_year = Integer.valueOf(jsonObject2.getString("current_year"));
                    Constants.current_year_online = current_year;

                    JSONArray jsonArray = Object.getJSONArray("data_details");
                    price_watch_models = new ArrayList<>();
                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = (JSONObject)jsonArray.get(i);

                        price_watch_models.add(new Price_watch_model(jsonObject.getString("color"),jsonObject.getString("region"),
                                jsonObject.getString("lowest_price"),
                                jsonObject.getString("lowest_price_date"),
                                jsonObject.getString("highest_price"),
                                jsonObject.getString("highest_price_date"),
                                jsonObject.getString("lowest_arrow"),
                                jsonObject.getString("highest_arrow")));
                    }

                    Price_watch_adapter Price_watch_adapter = new Price_watch_adapter(getContext(), price_watch_models,selectedCategory);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    recyclerview.setLayoutManager(layoutManager);
                    recyclerview.setAdapter(Price_watch_adapter);
                    recyclerview.setNestedScrollingEnabled(false);


                }
                catch (JSONException e){}
                catch (Exception e){}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                layout_table.setVisibility(View.VISIBLE);
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
                hashMap.put("selectedCategory", selectedCategory);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }



}
