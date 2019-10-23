package com.wdysolutions.notes.Notes_Pig.Swine_Population.Dialog_Table;

import android.content.Context;
import android.net.Uri;
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
import com.wdysolutions.notes.R;
import com.wdysolutions.notes.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class dialogTable_main extends DialogFragment implements dialogTable_adapter.EventListener {

    String company_id, company_code;
    RecyclerView recyclerView;
    TextView txt_module, no_data;
    Button btn_close;
    ProgressBar progressBar;
    LinearLayout layout_main;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.swine_population_dialog_table_main, container, false);
        SharedPref sharedPref = new SharedPref(getActivity());
        company_id = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYID);
        company_code = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYCODE);

        recyclerView = view.findViewById(R.id.recyclerView);
        txt_module = view.findViewById(R.id.txt_module);
        btn_close = view.findViewById(R.id.btn_close);
        progressBar = view.findViewById(R.id.progressBar);
        layout_main = view.findViewById(R.id.layout_main);
        no_data = view.findViewById(R.id.no_data);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        String table_type = getArguments().getString("table_type");
        if(table_type.equals("by_class")){
            String id = getArguments().getString("id");
            String classtype = getArguments().getString("classtype");
            String branch = getArguments().getString("branch");
            String selectedDate = getArguments().getString("selectedDate");
            getTableDetails_byClass(id, classtype, selectedDate, branch);
        }
        else if(table_type.equals("by_age")){
            String selectedDate = getArguments().getString("selectedDate");
            String swine_type = getArguments().getString("swine_type");
            String branch_id = Constants.branch_id;
            String branch = getArguments().getString("branch");
            getTableDetails_byAge(selectedDate,swine_type,branch_id,branch);
        }

        return view;
    }

    ArrayList<dialogTable_model> dialogTable_models;
    private void getTableDetails_byClass(final String id, final String classtype, final String date, final String branch) {
        layout_main.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        String URL = getString(R.string.URL_online) + "/swine_population/by_class/sp_byclass_details.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    dialogTable_models = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data_array");

                    if (jsonArray.length() == 0){
                        progressBar.setVisibility(View.GONE);
                        no_data.setVisibility(View.VISIBLE);

                    } else {
                        layout_main.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);

                        for (int i=0; i<jsonArray.length(); i++){
                            JSONObject jsonObject1 = (JSONObject)jsonArray.get(i);

                            dialogTable_models.add(new dialogTable_model(jsonObject1.getString("sw_id"),
                                    jsonObject1.getString("sw_type"),
                                    jsonObject1.getString("current_pen"),
                                    jsonObject1.getString("age"),
                                    jsonObject1.getString("sw_code")));
                        }

                        dialogTable_adapter rs_adapter = new dialogTable_adapter(getContext(), dialogTable_models,dialogTable_main.this);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(rs_adapter);
                        recyclerView.setNestedScrollingEnabled(false);
                    }

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
                hashMap.put("branch", branch);
                hashMap.put("id", id);
                hashMap.put("class_type", classtype);
                hashMap.put("start_date", date);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void getTableDetails_byAge(final String selectedDate,final String swine_type,final String branch_id,final String branch) {
        layout_main.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        String URL = getString(R.string.URL_online) + "/swine_population/by_age/by_age_modal_details.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    dialogTable_models = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data_array");

                    if (jsonArray.length() == 0){
                        progressBar.setVisibility(View.GONE);
                        no_data.setVisibility(View.VISIBLE);

                    } else {
                        layout_main.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);

                        for (int i=0; i<jsonArray.length(); i++){
                            JSONObject jsonObject1 = (JSONObject)jsonArray.get(i);

                            dialogTable_models.add(new dialogTable_model(jsonObject1.getString("sw_id"),
                                    jsonObject1.getString("sw_type"),
                                    jsonObject1.getString("current_pen"),
                                    jsonObject1.getString("age"),
                                    jsonObject1.getString("sw_code")));
                        }

                        dialogTable_adapter rs_adapter = new dialogTable_adapter(getContext(), dialogTable_models,dialogTable_main.this);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(rs_adapter);
                        recyclerView.setNestedScrollingEnabled(false);
                    }

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
                hashMap.put("swine_type", swine_type);
                hashMap.put("selected_date", selectedDate);
                hashMap.put("branch_id", branch_id);
                hashMap.put("getBranch", branch);
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
