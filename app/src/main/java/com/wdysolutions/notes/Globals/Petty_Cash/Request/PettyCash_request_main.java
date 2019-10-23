package com.wdysolutions.notes.Globals.Petty_Cash.Request;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wdysolutions.notes.AppController;
import com.wdysolutions.notes.Constants;
import com.wdysolutions.notes.DatePicker.DatePickerCustom;
import com.wdysolutions.notes.DatePicker.DatePickerSelectionInterfaceCustom;
import com.wdysolutions.notes.R;
import com.wdysolutions.notes.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PettyCash_request_main extends Fragment implements DatePickerSelectionInterfaceCustom {

    TextView btn_start_date, btn_end_date;
    LinearLayout btn_generate_report;
    ScrollView sv_swine_sales;
    RecyclerView rec_cv;
    SharedPref sharedPref;
    String company_id, company_code, category_id, selected_branch_id;
    boolean isStartDateClick = false;
    String selectedStartDate = "", selectedEndDate = "", receipt_stat = "";
    LinearLayout details_;
    ProgressBar progressBar2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pettycash_request_main, container, false);
        sharedPref = new SharedPref(getActivity());
        company_id = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYID);
        company_code = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYCODE);
        category_id = sharedPref.getUserInfo().get(sharedPref.KEY_CATEGORYID);
        selected_branch_id = Constants.branch_id;

        progressBar2 = view.findViewById(R.id.progressBar2);
        details_ = view.findViewById(R.id.details_);
        btn_start_date = view.findViewById(R.id.btn_start_date);
        btn_end_date = view.findViewById(R.id.btn_end_date);
        btn_generate_report = view.findViewById(R.id.btn_generate_report);
        sv_swine_sales = view.findViewById(R.id.sv_swine_sales);
        rec_cv = view.findViewById(R.id.rec_cv);

        btn_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartDateClick = true;
                openDatePicker();
            }
        });

        btn_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartDateClick = false;
                openDatePicker();
            }
        });

        btn_generate_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedStartDate.equals("")){
                    Toast.makeText(getActivity(), "Please select start date", Toast.LENGTH_SHORT).show();
                } else if (selectedEndDate.equals("")){
                    Toast.makeText(getActivity(), "Please select end date", Toast.LENGTH_SHORT).show();
                } else {
                    getPettyCashData();
                }
            }
        });

        getPettyCashData();
        return view;
    }

    String current_date = "", start_date, end_date;
    ArrayList<PettyCash_request_model> petty_cash_request_models;
    public void getPettyCashData() {
        progressBar2.setVisibility(View.VISIBLE);
        details_.setVisibility(View.GONE);
        btn_generate_report.setEnabled(false);
        btn_start_date.setEnabled(false);
        btn_end_date.setEnabled(false);
        String URL = getString(R.string.URL_online) + "petty_cash/get_pettycash.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    progressBar2.setVisibility(View.GONE);
                    details_.setVisibility(View.VISIBLE);
                    btn_generate_report.setEnabled(true);
                    btn_start_date.setEnabled(true);
                    btn_end_date.setEnabled(true);

                    petty_cash_request_models = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i=0; i < jsonArray.length(); i++){
                        JSONObject jsonObject1 = (JSONObject)jsonArray.get(i);
                        petty_cash_request_models.add(new PettyCash_request_model(jsonObject1.getString("id"),
                                jsonObject1.getString("credit_method_test"),
                                jsonObject1.getString("count"),
                                jsonObject1.getString("pcv"),
                                jsonObject1.getString("date_requested"),
                                jsonObject1.getString("amount"),
                                jsonObject1.getString("remarks"),
                                jsonObject1.getString("date_encoded"),
                                jsonObject1.getString("created_by"),
                                jsonObject1.getString("br_id"),
                                jsonObject1.getString("receipt_status"),
                                jsonObject1.getString("receipt_status_color"),
                                jsonObject1.getString("userID"),
                                jsonObject1.getString("stats"),
                                jsonObject1.getString("stats_color"),
                                jsonObject1.getString("hid"),
                                jsonObject1.getString("liquidate_stats"),
                                jsonObject1.getString("liquidate_color"),
                                jsonObject1.getString("declared_status"),
                                jsonObject1.getString("declared_status_color"),
                                jsonObject1.getString("rfr_status"),
                                jsonObject1.getString("rfr_status_color"),
                                jsonObject1.getString("dnr_stat"),
                                jsonObject1.getString("liquidation_stat"),
                                jsonObject1.getString("rep_stats"),
                                jsonObject1.getString("rep_stat"),
                                jsonObject1.getString("approved_by")));
                    }

                    PettyCash_request_adapter adapter = new PettyCash_request_adapter(getActivity(), petty_cash_request_models);
                    rec_cv.setLayoutManager(new LinearLayoutManager(getActivity()));
                    rec_cv.setAdapter(adapter);
                    rec_cv.setNestedScrollingEnabled(false);


                    // Current Date
                    JSONArray jsonArray1 = jsonObject.getJSONArray("response_date");
                    JSONObject jsonObject1 = (JSONObject)jsonArray1.get(0);
                    current_date = jsonObject1.getString("current_date");
                    start_date = jsonObject1.getString("start_date");
                    end_date = jsonObject1.getString("end_date");

                    if (selectedStartDate.equals("")) { btn_start_date.setText(start_date); selectedStartDate = start_date; }
                    if (selectedEndDate.equals("")) { btn_end_date.setText(end_date); selectedEndDate = end_date; }

                }
                catch (JSONException e){}
                catch (Exception e){}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar2.setVisibility(View.GONE);
                details_.setVisibility(View.VISIBLE);
                btn_generate_report.setEnabled(true);
                btn_start_date.setEnabled(true);
                btn_end_date.setEnabled(true);
                Toast.makeText(getActivity(), getResources().getString(R.string.volley_error_msg), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("receipt_stat", receipt_stat);
                hashMap.put("start_date", selectedStartDate);
                hashMap.put("end_date", selectedEndDate);
                hashMap.put("company_id", company_id);
                hashMap.put("company_code", company_code);
                hashMap.put("branch_id", selected_branch_id);
                hashMap.put("category_id", category_id);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void openDatePicker() {
        DatePickerCustom datePickerFragment = new DatePickerCustom();

        Bundle bundle = new Bundle();
        bundle.putString("maxDate", current_date);
        bundle.putString("minDate", "");
        bundle.putBoolean("isSetMinDate",false);
        bundle.putBoolean("isFutureDateTrue", true);
        datePickerFragment.setArguments(bundle);

        datePickerFragment.delegate = this;
        datePickerFragment.setCancelable(false);
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSelected(String date) {
        if (isStartDateClick){
            selectedStartDate = date;
            btn_start_date.setText(selectedStartDate);
        } else {
            selectedEndDate = date;
            btn_end_date.setText(selectedEndDate);
        }
    }
}
