package com.wdysolutions.notes.Notes_Pig.Swine_Delivery_Report;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.evrencoskun.tableview.TableView;
import com.wdysolutions.notes.AppController;
import com.wdysolutions.notes.Constants;
import com.wdysolutions.notes.DatePicker.DatePickerCustom;
import com.wdysolutions.notes.DatePicker.DatePickerSelectionInterfaceCustom;
import com.wdysolutions.notes.MainActivity;
import com.wdysolutions.notes.Notes_Pig.Swine_Delivery_Report.tableview.TableViewAdapter;
import com.wdysolutions.notes.Notes_Pig.Swine_Delivery_Report.tableview.model.Cell;
import com.wdysolutions.notes.Notes_Pig.Swine_Delivery_Report.tableview.model.ColumnHeader_swineDelivery;
import com.wdysolutions.notes.Notes_Pig.Swine_Delivery_Report.tableview.model.RowHeader_swineDelivery;
import com.wdysolutions.notes.Notes_Pig.Swine_Population.Dialog_Table.dialogTable_adapter;
import com.wdysolutions.notes.R;
import com.wdysolutions.notes.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class daily_swine_delivery_main extends Fragment implements DatePickerSelectionInterfaceCustom {

    RecyclerView recyclerView;
    TextView total_heads, total_weight, ave_weight, total_adg, total_gross, ave_gross, total_sales, txt_start, txt_end,
            total_swinecost, ave_swinecost;
    String company_id, company_code, selected_branch_id;
    LinearLayout layout_main, layout_;
    ProgressBar loading_main, loading_;
    TableView tableview;
    boolean isStart = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.swine_delivery_report_daily_main, container, false);
        SharedPref sharedPref = new SharedPref(getActivity());
        company_id = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYID);
        company_code = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYCODE);
        selected_branch_id = Constants.branch_id;

        ave_swinecost = view.findViewById(R.id.ave_swinecost);
        total_swinecost = view.findViewById(R.id.total_swinecost);
        recyclerView = view.findViewById(R.id.recyclerView);
        total_sales = view.findViewById(R.id.total_sales);
        total_heads = view.findViewById(R.id.total_heads);
        total_weight = view.findViewById(R.id.total_weight);
        ave_weight = view.findViewById(R.id.ave_weight);
        total_adg = view.findViewById(R.id.total_adg);
        total_gross = view.findViewById(R.id.total_gross);
        ave_gross = view.findViewById(R.id.ave_gross);
        layout_main = view.findViewById(R.id.layout_main);
        loading_main = view.findViewById(R.id.progressBar);
        txt_start = view.findViewById(R.id.txt_start);
        txt_end = view.findViewById(R.id.txt_end);
        loading_ = view.findViewById(R.id.loading_);
        layout_ = view.findViewById(R.id.layout_);
        tableview = view.findViewById(R.id.tableview);

        txt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStart = true;
                openDatePicker();
            }
        });

        txt_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStart = false;
                openDatePicker();
            }
        });

        //initializeTableView();
        getDateLimit();
        initSpinner(view);
        return view;
    }

    String restrictedDate, currentDate, start_date, end_date;
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
                    start_date = jsonObject_.getString("current_start_date");
                    end_date = jsonObject_.getString("current_end_date");
                    txt_start.setText(start_date);
                    txt_end.setText(end_date);
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

    String selected = "", type;
    private void initSpinner(View view){
        final List<String> classi = new ArrayList<>();
        classi.add("Generate Report");
        classi.add("All");
        classi.add("Declared");
        classi.add("Undeclared");
        Spinner spinner_generate = view.findViewById(R.id.spinner_generate);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_blue, classi);
        spinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_drop);
        spinner_generate.setAdapter(spinnerAdapter);
        spinner_generate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selected = classi.get(position);
                if (selected.equals("Generate Report")){
                    selected = "";
                } else {
                    if (selected.equals("All")){
                        type = "-1";
                        get_details(type);
                    } else if (selected.equals("Declared")){
                        type = "0";
                        get_details(type);
                    } else if (selected.equals("Undeclared")){
                        type = "1";
                        get_details(type);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    ArrayList<daily_swine_delivery_model> daily_swine_delivery_models;
    public void get_details(final String type) {
        loading_.setVisibility(View.VISIBLE);
        layout_.setVisibility(View.GONE);
        String URL = getString(R.string.URL_online) + "daily_swine_delivery/report_details.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    loading_.setVisibility(View.GONE);
                    layout_.setVisibility(View.VISIBLE);

                    daily_swine_delivery_models = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data_array");

                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject1 = (JSONObject)jsonArray.get(i);

                        daily_swine_delivery_models.add(new daily_swine_delivery_model(jsonObject1.getString("date"),
                                jsonObject1.getString("customer"),
                                jsonObject1.getString("reference_num"),
                                jsonObject1.getString("heads"),
                                jsonObject1.getString("total_weight"),
                                jsonObject1.getString("total_cost"),
                                jsonObject1.getString("ave_cost"),
                                jsonObject1.getString("swine_type"),
                                jsonObject1.getString("ave_age"),
                                jsonObject1.getString("price_per_kg"),
                                jsonObject1.getString("adg"),
                                jsonObject1.getString("gross_total"),
                                jsonObject1.getString("gross_ave"),
                                jsonObject1.getString("total_sales"),
                                jsonObject1.getString("ave_weight"),
                                jsonObject1.getString("delivery_number")));
                    }

                    JSONArray total_array = jsonObject.getJSONArray("total_array");
                    JSONObject jsonObject_total = (JSONObject)total_array.get(0);
                    total_heads.setText(jsonObject_total.getString("total_heads"));
                    total_weight.setText(jsonObject_total.getString("total_weight"));
                    ave_weight.setText(jsonObject_total.getString("total_ave_weight"));
                    total_adg.setText(jsonObject_total.getString("total_adg"));
                    total_gross.setText("₱ "+jsonObject_total.getString("total_gross_total"));
                    ave_gross.setText("₱ "+jsonObject_total.getString("total_gross_ave"));
                    total_sales.setText("₱ "+jsonObject_total.getString("total_sales"));
                    total_swinecost.setText(jsonObject_total.getString("total_swine_cost"));
                    ave_swinecost.setText(jsonObject_total.getString("total_ave_swine_cost"));

                    daily_swine_adapter rs_adapter = new daily_swine_adapter(getContext(), daily_swine_delivery_models);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(rs_adapter);
                    recyclerView.setNestedScrollingEnabled(false);
                }
                catch (JSONException e){}
                catch (Exception e){}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading_.setVisibility(View.GONE);
                layout_.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), getResources().getString(R.string.volley_error_msg), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("company_id", company_id);
                hashMap.put("company_code", company_code);
                hashMap.put("branch_id", selected_branch_id);
                hashMap.put("report_type", type);
                hashMap.put("start_date", start_date);
                hashMap.put("end_date", end_date);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


//    TableViewAdapter_br mTableViewAdapter;
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    private void initializeTableView() {
//        mTableViewAdapter = new TableViewAdapter_br(getContext());
//        tableview.setAdapter(mTableViewAdapter);
//        tableview.setRowHeaderWidth(700);
//    }
//
//    List<List<Cell_br>> cellList;
//    List<Cell_br> cell;
//    ArrayList<RowHeader_swineDelivery> rowHeader_swineDeliveries;
//    public void get_details(final String type) {
//        loading_.setVisibility(View.VISIBLE);
//        layout_.setVisibility(View.GONE);
//        String URL = getString(R.string.URL_online) + "daily_swine_delivery/report_details.php";
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                try {
//                    loading_.setVisibility(View.GONE);
//                    layout_.setVisibility(View.VISIBLE);
//
//                    cell = new ArrayList<>();
//                    cellList = new ArrayList<>();
//                    rowHeader_swineDeliveries = new ArrayList<>();
//                    JSONObject jsonObject = new JSONObject(response);
//                    JSONArray jsonArray = jsonObject.getJSONArray("data_array");
//
//                    for (int i=0; i<jsonArray.length(); i++){
//                        JSONObject jsonObject1 = (JSONObject)jsonArray.get(i);
//
//                        rowHeader_swineDeliveries.add(new RowHeader_swineDelivery(jsonObject1.getString("date"),
//                                jsonObject1.getString("customer"),
//                                jsonObject1.getString("heads")));
//
//                        cell.add(new Cell_br(jsonObject1.getString("reference_num"),
//                                jsonObject1.getString("total_weight"),
//                                jsonObject1.getString("total_cost"),
//                                jsonObject1.getString("ave_cost"),
//                                jsonObject1.getString("swine_type"),
//                                jsonObject1.getString("ave_age"),
//                                jsonObject1.getString("price_per_kg"),
//                                jsonObject1.getString("adg"),
//                                jsonObject1.getString("gross_total"),
//                                jsonObject1.getString("gross_ave"),
//                                jsonObject1.getString("total_sales"),
//                                jsonObject1.getString("ave_weight"),
//                                jsonObject1.getString("delivery_number")));
//                    }
//                    cellList.add(cell);
//
//
//                    ///////////////////////////////// Column
//                    List<ColumnHeader_swineDelivery> columnHeaders = new ArrayList<>();
//                    columnHeaders.add(new ColumnHeader_swineDelivery("", "", "", "", ""));
//
//                    mTableViewAdapter.setAllItems(columnHeaders,
//                            rowHeader_swineDeliveries,
//                            cellList);
//
//                    JSONArray total_array = jsonObject.getJSONArray("total_array");
//                    JSONObject jsonObject_total = (JSONObject)total_array.get(0);
//                    total_heads.setText(jsonObject_total.getString("total_heads"));
//                    total_weight.setText(jsonObject_total.getString("total_weight"));
//                    ave_weight.setText(jsonObject_total.getString("total_ave_weight"));
//                    total_adg.setText(jsonObject_total.getString("total_adg"));
//                    total_gross.setText(jsonObject_total.getString("total_gross_total"));
//                    ave_gross.setText(jsonObject_total.getString("total_gross_ave"));
//                    total_sales.setText(jsonObject_total.getString("total_sales"));
//
//
//                }
//                catch (JSONException e){}
//                catch (Exception e){}
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                loading_.setVisibility(View.GONE);
//                layout_.setVisibility(View.VISIBLE);
//                Toast.makeText(getActivity(), getResources().getString(R.string.volley_error_msg), Toast.LENGTH_SHORT).show();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String, String> hashMap = new HashMap<>();
//                hashMap.put("company_id", company_id);
//                hashMap.put("company_code", company_code);
//                hashMap.put("branch_id", selected_branch_id);
//                hashMap.put("report_type", type);
//                hashMap.put("start_date", start_date);
//                hashMap.put("end_date", end_date);
//                return hashMap;
//            }
//        };
//        AppController.getInstance().setVolleyDuration(stringRequest);
//        AppController.getInstance().addToRequestQueue(stringRequest);
//    }

    public void openDatePicker() {
        DatePickerCustom datePickerFragment = new DatePickerCustom();

        Bundle bundle = new Bundle();
        bundle.putString("maxDate", currentDate);
        bundle.putString("minDate", restrictedDate);
        bundle.putBoolean("isSetMinDate", false);
        bundle.putBoolean("isFutureDateTrue", true);
        datePickerFragment.setArguments(bundle);

        datePickerFragment.delegate = this;
        datePickerFragment.setCancelable(false);
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSelected(String date) {
        if (isStart){
            start_date = date;
            txt_start.setText(start_date);
        } else {
            end_date = date;
            txt_end.setText(end_date);
        }
    }


}
