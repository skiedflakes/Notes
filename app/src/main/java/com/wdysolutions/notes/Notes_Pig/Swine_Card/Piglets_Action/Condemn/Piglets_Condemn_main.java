package com.wdysolutions.notes.Notes_Pig.Swine_Card.Piglets_Action.Condemn;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.wdysolutions.notes.DatePicker.DatePickerCustom;
import com.wdysolutions.notes.DatePicker.DatePickerSelectionInterfaceCustom;
import com.wdysolutions.notes.Notes_Pig.Swine_Card.Action.Mortality.Disease_model;
import com.wdysolutions.notes.Notes_Pig.Swine_Card.RFscanner_main;
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
import java.util.List;
import java.util.Map;


public class Piglets_Condemn_main  extends DialogFragment implements DatePickerSelectionInterfaceCustom {

    Button btn_save;
    ProgressBar loading_save, progressBar;
    EditText edittext_remarks;
    TextView btn_date;
    Spinner spinner_cause;
    LinearLayout layout_;
    ArrayList<Disease_model> disease_models = new ArrayList<>();
    String selectedDate = "", selectedCause = "", current_date, company_code, company_id, user_id, swine_scanned_id,
            checkedCounter, array_piglets, pen_code, date_max_plus7;

    SharedPref sharedPref;
    String  category_id, selected_branch_id;

    public void openDatePicker(boolean isMinusDays21) {
        DatePickerCustom datePickerFragment = new DatePickerCustom();

        Bundle bundle = new Bundle();
        bundle.putString("maxDate", date_max_plus7);
        bundle.putString("maxDate_minus", "");
        bundle.putBoolean("isMinusDays", isMinusDays21);
        datePickerFragment.setArguments(bundle);

        datePickerFragment.delegate = this;
        datePickerFragment.setCancelable(false);
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSelected(String date) {
        selectedDate = date;
        btn_date.setText(selectedDate);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.piglets_condemn_main, container, false);
        sharedPref = new SharedPref(getActivity());
        company_id = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYID);
        company_code = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYCODE);
        category_id = sharedPref.getUserInfo().get(sharedPref.KEY_CATEGORYID);
        selected_branch_id = Constants.branch_id;
        user_id = sharedPref.getUserInfo().get(sharedPref.KEY_USERID);

        swine_scanned_id = getArguments().getString("swine_scanned_id");
        checkedCounter = getArguments().getString("checkedCounter");
        array_piglets = getArguments().getString("array_piglets");
        pen_code = getArguments().getString("pen_code");

        layout_ = view.findViewById(R.id.layout_);
        progressBar = view.findViewById(R.id.progressBar);
        btn_date = view.findViewById(R.id.tv_date);
        spinner_cause = view.findViewById(R.id.spinner_cause);
        edittext_remarks = view.findViewById(R.id.edittext_remarks);
        loading_save = view.findViewById(R.id.loading_save);
        btn_save = view.findViewById(R.id.btn_save);

        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkDate_plus7days();
            }
        });
        
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedDate.equals("")){
                    Toast.makeText(getActivity(), "Please select date", Toast.LENGTH_SHORT).show();
                } else if (selectedCause.equals("")){
                    Toast.makeText(getActivity(), "Please select cause", Toast.LENGTH_SHORT).show();
                } else {
                    saveCondemn();
                }
            }
        });

        getSpinnerCause();
        return view;
    }

    private void checkDate_plus7days(){
        try {
            final Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String[] date = current_date.split(" ");
            c.setTime(sdf.parse(date[0]));
            c.add(Calendar.DATE, 7);
            date_max_plus7 = sdf.format(c.getTime());

            openDatePicker(false);
        } catch (ParseException e) {}
    }

    public void getSpinnerCause(){
        String URL = getString(R.string.URL_online)+"scan_eartag/pig_piglets_spinner.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    layout_.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    JSONObject Object = new JSONObject(response);

                    // Current date ----------------------------------------------------------------
                    final JSONArray response_date = Object.getJSONArray("response_date");
                    JSONObject json_date = (JSONObject) response_date.get(0);
                    current_date = json_date.getString("current_date");


                    // get Spinner -----------------------------------------------------------------
                    disease_models.add(new Disease_model("","Please Select"));
                    final JSONArray diag = Object.getJSONArray("data");
                    for (int i = 0; i < diag.length(); i++) {
                        JSONObject cusObj = (JSONObject) diag.get(i);

                        disease_models.add(new Disease_model(cusObj.getString("disease_id"),
                                cusObj.getString("cause")));
                    }

                    // Populate Spinner
                    List<String> lables = new ArrayList<>();
                    for (int i = 0; i < disease_models.size(); i++) {
                        lables.add(disease_models.get(i).getCause());
                    }
                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_drop, lables);
                    spinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_drop);
                    spinner_cause.setAdapter(spinnerAdapter);
                    spinner_cause.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            Disease_model click = disease_models.get(position);
                            if (!click.getCause().equals("Please Select")){
                                selectedCause = String.valueOf(click.getDisease_id());
                            } else {
                                selectedCause = "";
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {}
                    });

                }
                catch (JSONException e) {}
                catch (Exception e){}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try{
                    dismiss();
                    Toast.makeText(getActivity(), "Connection error, please try again.", Toast.LENGTH_SHORT).show();
                } catch (Exception e){}
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("company_id", company_id);
                hashMap.put("company_code", company_code);
                hashMap.put("swine_id", swine_scanned_id);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void saveCondemn(){
        loading_save.setVisibility(View.VISIBLE);
        btn_save.setVisibility(View.GONE);
        String URL = getString(R.string.URL_online)+"scan_eartag/pig_piglets_condemn.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    loading_save.setVisibility(View.GONE);
                    btn_save.setVisibility(View.VISIBLE);

                    String success="";
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray details = jsonObject.getJSONArray("data");
                    for (int i=0; i<details.length();i++){
                        JSONObject r = details.getJSONObject(i);
                        success += r.getString("status")+"\n";
                    }

                    JSONArray jsonArray = jsonObject.getJSONArray("data_status");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    if (jsonObject1.getString("status").equals("complete")){
                        dialogBox(success);
                    }

                } catch (Exception e){}

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try{
                    loading_save.setVisibility(View.GONE);
                    btn_save.setVisibility(View.VISIBLE);
                    reloadPigletView();
                    Toast.makeText(getActivity(), "Connection Error, please try again.", Toast.LENGTH_SHORT).show();
                } catch (Exception e){}
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("company_id", company_id);
                hashMap.put("category_id", category_id);
                hashMap.put("company_code", company_code);
                hashMap.put("user_id", user_id);
                hashMap.put("cause", selectedCause);
                hashMap.put("mortality_date", selectedDate);
                hashMap.put("remarks", edittext_remarks.getText().toString());
                hashMap.put("array_piglets", array_piglets);
                hashMap.put("sow_id", swine_scanned_id);
                hashMap.put("pen_id", pen_code);
                hashMap.put("checkedCounter", checkedCounter);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void reloadPigletView(){
        dismiss();
        FragmentManager fm = getFragmentManager();
        RFscanner_main fragment = (RFscanner_main)fm.findFragmentByTag("Main_menu");
        fragment.get_details(company_code, company_id, swine_scanned_id);
    }

    void dialogBox(String name){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setMessage(name);
        alertDialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        reloadPigletView();
                        Toast.makeText(getActivity(), "Successfully save.", Toast.LENGTH_SHORT).show();
                    }
                });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }


}
