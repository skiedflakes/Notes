package com.wdysolutions.notes.Notes_Pig.Swine_Card.History.Remarks.Add;

import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wdysolutions.notes.AppController;
import com.wdysolutions.notes.DatePicker.DatePickerCustom;
import com.wdysolutions.notes.DatePicker.DatePickerSelectionInterfaceCustom;
import com.wdysolutions.notes.Notes_Pig.Swine_Card.History.Remarks.Remarks_main;
import com.wdysolutions.notes.R;
import com.wdysolutions.notes.SharedPref;

import java.util.HashMap;
import java.util.Map;


public class Remarks_add extends DialogFragment implements DatePickerSelectionInterfaceCustom {

    EditText edittext_remarks;
    TextView btn_date;
    Button btn_save;
    ProgressBar loading_save;
    String company_code, company_id, user_id, swine_scanned_id, selectedDate = "", currentDate ="", category_id;


    public void openDatePicker(boolean isMinusDays21) {
        DatePickerCustom datePickerFragment = new DatePickerCustom();

        String[] maxDate = currentDate.split(" ");

        Bundle bundle = new Bundle();
        bundle.putString("maxDate", maxDate[0]);
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
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.remarks_add, container, false);
        SharedPref sessionPreferences = new SharedPref(getActivity());
        company_code = sessionPreferences.getUserInfo().get(sessionPreferences.KEY_COMPANYCODE);
        company_id = sessionPreferences.getUserInfo().get(sessionPreferences.KEY_COMPANYID);
        user_id = sessionPreferences.getUserInfo().get(sessionPreferences.KEY_USERID);
        category_id = sessionPreferences.getUserInfo().get(sessionPreferences.KEY_CATEGORYID);

        swine_scanned_id = getArguments().getString("swine_scanned_id");
        currentDate = getArguments().getString("currentDate");

        btn_date = view.findViewById(R.id.tv_date);
        edittext_remarks = view.findViewById(R.id.edittext_remarks);
        loading_save = view.findViewById(R.id.loading_save);
        btn_save = view.findViewById(R.id.btn_save);

        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker(false);
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedDate.equals("")){
                    Toast.makeText(getActivity(), "Please select date", Toast.LENGTH_SHORT).show();
                } else if (edittext_remarks.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Please enter remarks", Toast.LENGTH_SHORT).show();
                } else {
                    saveRemarks();
                }
            }
        });

        return view;
    }

    public void saveRemarks() {
        loading_save.setVisibility(View.VISIBLE);
        btn_save.setVisibility(View.GONE);
        String URL = getString(R.string.URL_online)+"scan_eartag/history/remarks_add.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    loading_save.setVisibility(View.GONE);
                    btn_save.setVisibility(View.VISIBLE);
                    if (response.equals("1")){
                        dismiss();
                        FragmentManager fm = getFragmentManager();
                        Remarks_main fragment = (Remarks_main)fm.findFragmentByTag("fragmentRF");
                        fragment.getRemarksDetails(company_code, company_id, swine_scanned_id);
                        Toast.makeText(getActivity(), "Successfully save.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try{
                    loading_save.setVisibility(View.GONE);
                    btn_save.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Connection Error, please try again.", Toast.LENGTH_SHORT).show();
                } catch (Exception e){}
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("company_code", company_code);
                hashMap.put("company_id", company_id);
                hashMap.put("user_id", user_id);
                hashMap.put("swine_id", swine_scanned_id);
                hashMap.put("date_added", selectedDate);
                hashMap.put("category_id", category_id);
                hashMap.put("remarks", edittext_remarks.getText().toString());
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

}
