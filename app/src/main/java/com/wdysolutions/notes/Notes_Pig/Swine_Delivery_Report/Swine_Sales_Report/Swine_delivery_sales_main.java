package com.wdysolutions.notes.Notes_Pig.Swine_Delivery_Report.Swine_Sales_Report;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wdysolutions.notes.AppController;
import com.wdysolutions.notes.Constants;
import com.wdysolutions.notes.MainActivity;
import com.wdysolutions.notes.Notes_Pig.Swine_Delivery_Report.daily_swine_adapter;
import com.wdysolutions.notes.R;
import com.wdysolutions.notes.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Swine_delivery_sales_main extends DialogFragment implements swine_delivery_sales_adapter.EventListener {

    Button btn_close;
    ProgressBar progressBar;
    TextView no_data, tv_sold_amount, tv_gross_profit, txt_title;
    RecyclerView recyclerView;
    LinearLayout layout_main;
    String company_id, company_code, selected_branch_id, dr_num;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.swine_delivery_sales_main, container, false);
        SharedPref sharedPref = new SharedPref(getActivity());
        company_id = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYID);
        company_code = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYCODE);
        selected_branch_id = Constants.branch_id;
        dr_num = getArguments().getString("dr_num");

        layout_main = view.findViewById(R.id.layout_main);
        btn_close = view.findViewById(R.id.btn_close);
        progressBar = view.findViewById(R.id.progressBar);
        no_data = view.findViewById(R.id.no_data);
        recyclerView = view.findViewById(R.id.recyclerView);
        tv_sold_amount = view.findViewById(R.id.tv_sold_amount);
        tv_gross_profit = view.findViewById(R.id.tv_gross_profit);
        txt_title = view.findViewById(R.id.txt_title);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        get_details();
        return view;
    }

    ArrayList<Swine_delivery_sales_model> swine_delivery_sales_models;
    public void get_details() {
        layout_main.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        String URL = getString(R.string.URL_online) + "daily_swine_delivery/report_dialog_details.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    layout_main.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                    swine_delivery_sales_models = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data_array");

                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject_ = (JSONObject)jsonArray.get(i);

                        swine_delivery_sales_models.add(new Swine_delivery_sales_model(jsonObject_.getString("dr_swine"),
                                jsonObject_.getString("swine_type"),
                                jsonObject_.getString("weight"),
                                jsonObject_.getString("swine_age"),
                                jsonObject_.getString("swine_weight"),
                                jsonObject_.getString("dr_total_weight"),
                                jsonObject_.getString("adg"),
                                jsonObject_.getString("total_amount_sold"),
                                jsonObject_.getString("dr_row"),
                                jsonObject_.getString("gross_profit"), jsonObject_.getString("swine_id")));
                    }

                    txt_title.setText("Delivery #: "+dr_num);

                    JSONArray total_array = jsonObject.getJSONArray("total_array");
                    JSONObject jsonObject_total = (JSONObject)total_array.get(0);
                    tv_gross_profit.setText(jsonObject_total.getString("total_gross_profit"));
                    tv_sold_amount.setText(jsonObject_total.getString("total_amount_sold"));

                    swine_delivery_sales_adapter rs_adapter = new swine_delivery_sales_adapter(getContext(), swine_delivery_sales_models,Swine_delivery_sales_main.this);
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
                dismiss();
                Toast.makeText(getActivity(), getResources().getString(R.string.volley_error_msg), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("company_id", company_id);
                hashMap.put("company_code", company_code);
                hashMap.put("branch_id", selected_branch_id);
                hashMap.put("dr_num", dr_num);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    @Override
    public void close_dismiss() {
        dismiss();
    }
}
