package com.wdysolutions.notes.Notes_Pig.Swine_Card.History.Med_Vac_Schedule;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.wdysolutions.notes.AppController;
import com.wdysolutions.notes.Constants;
import com.wdysolutions.notes.DatePicker.DatePickerCustom;
import com.wdysolutions.notes.DatePicker.DatePickerSelectionInterfaceCustom;
import com.wdysolutions.notes.Notes_Pig.Swine_Card.History.Medication.Medication_main;
import com.wdysolutions.notes.Notes_Pig.Swine_Card.History.Vaccination.Vaccination_main;
import com.wdysolutions.notes.R;
import com.wdysolutions.notes.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class Med_Vac_schedule_main extends DialogFragment implements DatePickerSelectionInterfaceCustom {

    RecyclerView recyclerView;
    TextView txt_date, txt_title;
    CheckBox chck_box;
    String company_id, company_code, category_id, user_id, selected_branch_id, swine_id, value;
    String current_date;
    Button btn_apply;
    LinearLayout layout_main;
    ProgressBar loading_main, loading_save;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.med_vac_schedule_main, container, false);
        SharedPref sharedPref = new SharedPref(getActivity());
        company_id = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYID);
        company_code = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYCODE);
        category_id = sharedPref.getUserInfo().get(sharedPref.KEY_CATEGORYID);
        user_id = sharedPref.getUserInfo().get(sharedPref.KEY_USERID);
        selected_branch_id = Constants.branch_id;

        swine_id = getArguments().getString("swine_scanned_id");
        value = getArguments().getString("value");

        recyclerView = view.findViewById(R.id.recyclerView);
        txt_date = view.findViewById(R.id.txt_date);
        chck_box = view.findViewById(R.id.chck_box);
        txt_title = view.findViewById(R.id.txt_title);
        btn_apply = view.findViewById(R.id.btn_apply);
        layout_main = view.findViewById(R.id.layout_main);
        loading_main = view.findViewById(R.id.loading_main);
        loading_save = view.findViewById(R.id.loading_save);

        if (value.equals("M")){
            txt_title.setText("Add Medication From Schedule");
        } else {
            txt_title.setText("Add Vaccination From Schedule");
        }

        txt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });

        chck_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chck_box.isChecked()){
                    CheckAll("1");
                } else {
                    CheckAll("0");
                }
            }
        });

        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedDate.equals("")){
                    Toast.makeText(getActivity(), "Please select date", Toast.LENGTH_SHORT).show();
                } else if (!checkSelected()){
                    Toast.makeText(getActivity(), "Please select product", Toast.LENGTH_SHORT).show();
                } else {
                    apply();
                }
            }
        });

        getSched();
        return view;
    }

    public void openDialog(String msg){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton("Close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void refreshLayout(){
        if (count_success > 0){

            getSched();

            // refresh vaccination / medication
            if (value.equals("M")){
                Medication_main fragment = (Medication_main)getFragmentManager().findFragmentByTag("fragmentRF");
                fragment.getMedicationDetails(company_code, company_id, swine_id, "3");
            } else {
                Vaccination_main fragment = (Vaccination_main)getFragmentManager().findFragmentByTag("fragmentRF");
                fragment.getVaccineDetails(company_code, company_id, swine_id, "3");
            }
        }
    }

    int count_success;
    String result_ = "";
    public void apply(){
        rewriteDosage();
        loading_save.setVisibility(View.VISIBLE);
        btn_apply.setVisibility(View.GONE);
        String URL = getString(R.string.URL_online)+"scan_eartag/history/med_vac_applied.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    count_success = 0;
                    loading_save.setVisibility(View.GONE);
                    btn_apply.setVisibility(View.VISIBLE);

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject_ = (JSONObject)jsonArray.get(i);

                        String res = jsonObject_.getString("result");
                        String status = jsonObject_.getString("status");

                        if (status.equals("success")){
                            count_success++;
                        }

                        result_ += res+"\n\n";
                    }

                    refreshLayout();

                    openDialog(result_);

                }
                catch (JSONException e){}
                catch (Exception e){}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading_save.setVisibility(View.GONE);
                btn_apply.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), getResources().getString(R.string.volley_error_msg), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("company_code", company_code);
                hashMap.put("company_id", company_id);
                hashMap.put("all_data", new Gson().toJson(medVac_sched_models));
                hashMap.put("action", value);
                hashMap.put("date_applied", selectedDate);
                hashMap.put("user_id", user_id);
                hashMap.put("category_id", category_id);
                hashMap.put("branch_id", selected_branch_id);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    Med_Vac_schedule_adapter adapter;
    ArrayList<Med_Vac_sched_model> medVac_sched_models;
    ArrayList<Med_Vac_sched_model> medVac_sched_models_dosage;
    public void getSched(){
        loading_main.setVisibility(View.VISIBLE);
        layout_main.setVisibility(View.GONE);
        String URL = getString(R.string.URL_online)+"scan_eartag/history/med_vac_sched.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    loading_main.setVisibility(View.GONE);
                    layout_main.setVisibility(View.VISIBLE);

                    JSONObject jsonObject1 = new JSONObject(response);

                    medVac_sched_models = new ArrayList<>();
                    medVac_sched_models_dosage = new ArrayList<>();
                    JSONArray jsonArray = jsonObject1.getJSONArray("sched_array");
                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject_ = (JSONObject)jsonArray.get(i);

                        medVac_sched_models.add(new Med_Vac_sched_model(jsonObject_.getString("med_vacc_sched_id"),
                                jsonObject_.getString("prod_name"),
                                jsonObject_.getString("dosage"),
                                jsonObject_.getString("disease"),
                                jsonObject_.getString("date"),
                                "0"));

                        // for edit text dosage
                        medVac_sched_models_dosage.add(new Med_Vac_sched_model("",
                                "",
                                jsonObject_.getString("dosage"),
                                "",
                                "",
                                ""));
                    }

                    adapter = new Med_Vac_schedule_adapter(getContext(), medVac_sched_models, medVac_sched_models_dosage);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setNestedScrollingEnabled(false);


                    JSONArray date = jsonObject1.getJSONArray("date");
                    JSONObject jsonObject_date = (JSONObject)date.get(0);
                    current_date = jsonObject_date.getString("current_date");

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
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("company_code", company_code);
                hashMap.put("company_id", company_id);
                hashMap.put("swine_id", swine_id);
                hashMap.put("value", value);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    
    int counterSelected;
    private boolean checkSelected(){
        counterSelected = 0;
        for (int i=0; i<medVac_sched_models.size(); i++){
            Med_Vac_sched_model med_vac_sched_model = medVac_sched_models.get(i);
            if (med_vac_sched_model.getCheck_status().equals("1")){
                counterSelected++;
            }
        }
        
        if (counterSelected > 0){
            return true;   
        }
        return false;
    }

    private void CheckAll(String checkStatus){
        rewriteDosage();
        for (int i = 0; i< medVac_sched_models.size(); i++){
            Med_Vac_sched_model vacc = medVac_sched_models.get(i);
            medVac_sched_models.set(i, new Med_Vac_sched_model(vacc.getMed_vacc_sched_id(),
                    vacc.getProd_name(),
                    vacc.getDosage(),
                    vacc.getDisease(),
                    vacc.getDate(),
                    checkStatus));
        }
        adapter.notifyDataSetChanged();
    }

    private void rewriteDosage(){
        for (int i=0; i<medVac_sched_models.size(); i++){
            Med_Vac_sched_model med_vac_sched_model = medVac_sched_models.get(i);
            Med_Vac_sched_model med_vac_sched_model_dosage = medVac_sched_models_dosage.get(i);

            medVac_sched_models.set(i, new Med_Vac_sched_model(med_vac_sched_model.getMed_vacc_sched_id(),
                    med_vac_sched_model.getProd_name(),
                    med_vac_sched_model_dosage.getDosage(),
                    med_vac_sched_model.getDisease(),
                    med_vac_sched_model.getDate(),
                    med_vac_sched_model.getCheck_status()));
        }
    }

    String selectedDate = "";
    private void openDatePicker() {
        DatePickerCustom datePickerFragment = new DatePickerCustom();

        Bundle bundle = new Bundle();
        bundle.putString("maxDate", plus7days());
        bundle.putString("minDate", "");
        bundle.putBoolean("isSetMinDate",false);
        datePickerFragment.setArguments(bundle);

        datePickerFragment.delegate = this;
        datePickerFragment.setCancelable(false);
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }

    private String plus7days(){
        try {
            final Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            c.setTime(sdf.parse(current_date));
            c.add(Calendar.DATE, 7);
            String date_min_minus1 = sdf.format(c.getTime());
            return date_min_minus1;
        } catch (ParseException e){}
        return null;
    }

    @Override
    public void onDateSelected(String date) {
        selectedDate = date;
        txt_date.setText(selectedDate);
    }
}
