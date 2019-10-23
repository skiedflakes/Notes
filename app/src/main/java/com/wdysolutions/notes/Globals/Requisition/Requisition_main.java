package com.wdysolutions.notes.Globals.Requisition;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.wdysolutions.notes.AppController;
import com.wdysolutions.notes.Authenticate_DialogFragment;
import com.wdysolutions.notes.Constants;
import com.wdysolutions.notes.DatePicker.DatePickerCustom;
import com.wdysolutions.notes.DatePicker.DatePickerSelectionInterfaceCustom;
import com.wdysolutions.notes.Globals.Requisition.Requisition_Add.Requisition_add_main;
import com.wdysolutions.notes.Navigation_Panel.frag_Nav_main;
import com.wdysolutions.notes.R;
import com.wdysolutions.notes.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class Requisition_main extends Fragment implements Authenticate_DialogFragment.uploadDialogInterface,DatePickerSelectionInterfaceCustom,Requisition_main_adapter.EventListener{
    RecyclerView rec_requsition;
    SharedPref sharedPref;
    String start_date="",end_date="";
    Requisition_main_adapter rs_adapter;
    ArrayList<Requisition_main_model> rs_main_list = new ArrayList<>();
    TextView btn_end_date, btn_start_date;
    Boolean isStart;

    ProgressDialog loadingScan;

    LinearLayout btn_generate_report;

    //checked array
    ArrayList<String> checked_ids=new ArrayList<>();
    Button btn_add, btn_delete;

    int counter_delete = 0,success_delete=0;

    CheckBox cb_all;

    //user session
    String user_id, company_id,company_code,category_id,selected_branch_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.requisition_main, container, false);

        //user
        sharedPref = new SharedPref(getActivity());
        company_id = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYID);
        company_code = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYCODE);
        category_id = sharedPref.getUserInfo().get(sharedPref.KEY_CATEGORYID);
        selected_branch_id = Constants.branch_id;
        user_id = sharedPref.getUserInfo().get(sharedPref.KEY_USERID);


        rec_requsition = view.findViewById(R.id.rec_requsition);
        cb_all = view.findViewById(R.id.cb_all);
        btn_generate_report = view.findViewById(R.id.btn_generate_report);
        btn_add = view.findViewById(R.id.btn_add);
        btn_delete = view.findViewById(R.id.btn_delete);
        btn_start_date = view.findViewById(R.id.btn_start_date);
        btn_end_date = view.findViewById(R.id.btn_end_date);

        cb_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cb_all.isChecked()){
                    check_all();
                }else{
                    uncheck_all();
                }
            }
        });

        btn_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker(false);
                isStart = true;

            }
        });

        btn_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker(false);
                isStart = false;

            }
        });


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("add_edit","add");
                FragmentManager fragmentManager = getFragmentManager();
                Requisition_add_main fragment = new Requisition_add_main();
                fragment.setArguments(bundle);
                fragment.setTargetFragment(Requisition_main.this, 202); //set request code for backpress
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.container_home, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              int checked =  checked_ids.size();
              if(checked>0){
                  check_user_account();
              }else{
                  Toast.makeText(getContext(), "Please select to delete", Toast.LENGTH_SHORT).show();
              }


            }
        });

        btn_generate_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rs_main_list.clear();
                rs_adapter.notifyDataSetChanged();
                start_date = btn_start_date.getText().toString();
                end_date = btn_end_date.getText().toString();
                get_details("1");
            }
        });
        loadingScan = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);

        //current month
        get_details("0");
        return view;
    }


    public void openDatePicker(boolean isMinusDays21) {
        DatePickerCustom datePickerFragment = new DatePickerCustom();

       // String[] maxDate = currentDate.split(" ");

        Bundle bundle = new Bundle();
        bundle.putString("maxDate", "");
        bundle.putString("maxDate_minus", "");
        bundle.putBoolean("isMinusDays", isMinusDays21);
        datePickerFragment.setArguments(bundle);

        datePickerFragment.delegate = this;
        datePickerFragment.setCancelable(false);
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }


    @Override
    public void onDateSelected(String date) {
       if(isStart){
           btn_end_date.setText(date);

       }else{
           btn_start_date.setText(date);
       }
    }

    @Override
    public void onChecked(String rs_header_id) {
        checked_ids.add(rs_header_id);
    }

    @Override
    public void removeChecked(String rs_header_id) {
        checked_ids.remove(rs_header_id);
    }

    @Override
    public void onbtn_view(String rs_id, String rs_number, String date_raw, String requested_by,
                           String status, String remarks, int check_status, String asset) {

        Bundle bundle = new Bundle();
        bundle.putString("add_edit","edit");
        bundle.putString("rs_id", String.valueOf(rs_id));
        bundle.putString("remarks", String.valueOf(remarks));
        bundle.putString("rs_number", String.valueOf(rs_number));
        bundle.putString("date_added", String.valueOf(date_raw));
        bundle.putString("requested_by", String.valueOf(requested_by));
        bundle.putString("status", String.valueOf(status));
        bundle.putString("check_status", String.valueOf(check_status));
        bundle.putString("asset", String.valueOf(asset));

        FragmentManager fragmentManager = getFragmentManager();
        Requisition_add_main fragment = new Requisition_add_main();
        fragment.setArguments(bundle);
        fragment.setTargetFragment(Requisition_main.this, 202); //set request code for backpress
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_home, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void remove_selected_rs() {
        counter_delete = checked_ids.size();
        success_delete = 0;
        if(checked_ids.size()>0){
            showLoading(loadingScan, "Deleting...").show();

            for(int i =0; i<checked_ids.size();i++){

                delete_rs(checked_ids.get(i));
            }

        }else{
            showLoading(loadingScan, null).dismiss();
            Toast.makeText(getActivity(), "Please select swine sales to delete", Toast.LENGTH_SHORT).show();
        }
    }

    private ProgressDialog showLoading(ProgressDialog loading, String msg){
        loading.setMessage(msg);
        loading.setCancelable(false);
        return loading;
    }


    public void check_all(){
        checked_ids.clear();

        String rs_id,remarks,rs_number,date_added,requested_by,status,count,date_raw,asset;
        try {
            JSONArray jsonArray = new JSONArray(new Gson().toJson(rs_main_list));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject r = jsonArray.getJSONObject(i);
                rs_id = r.getString("rs_id");
                remarks = r.getString("remarks");
                rs_number = r.getString("rs_number");
                date_added = r.getString("date_added");
                requested_by = r.getString("requested_by");
                status = r.getString("status");
                count = r.getString("count");
                date_raw = r.getString("date_raw");
                asset = r.getString("asset");
                rs_main_list.set(i,new Requisition_main_model(rs_id,remarks,rs_number
                        ,date_added,requested_by,status,count,1,date_raw,asset));
                checked_ids.add(rs_id);

            }
            rs_adapter.notifyDataSetChanged();
        }catch (Exception e){}
    }


    public void uncheck_all(){
        checked_ids.clear();

        String rs_id,remarks,rs_number,date_added,requested_by,status,count,date_raw, asset;
        try {
            JSONArray jsonArray = new JSONArray(new Gson().toJson(rs_main_list));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject r = jsonArray.getJSONObject(i);
                rs_id = r.getString("rs_id");
                remarks = r.getString("remarks");
                rs_number = r.getString("rs_number");
                date_added = r.getString("date_added");
                requested_by = r.getString("requested_by");
                status = r.getString("status");
                count = r.getString("count");
                date_raw = r.getString("date_raw");
                asset = r.getString("asset");
                rs_main_list.set(i,new Requisition_main_model(rs_id,remarks,rs_number
                        ,date_added,requested_by,status,count,0,date_raw,asset));
                checked_ids.remove(rs_id);

            }
            rs_adapter.notifyDataSetChanged();
        }catch (Exception e){}
    }

    public void check_user_account(){
        String URL = getString(R.string.URL_online)+"checkSession_user.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("1")){
                    remove_selected_rs();
                } else { //authenticate

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                    if (prev != null) {
                        ft.remove(prev);
                    }
                    ft.addToBackStack(null);

                    Authenticate_DialogFragment fragment = new Authenticate_DialogFragment();
                    fragment.setTargetFragment(Requisition_main.this, 0);
                    FragmentManager manager = getFragmentManager();
                    fragment.show(ft, "UploadDialogFragment");
                    fragment.setCancelable(true);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showLoading(loadingScan, null).dismiss();
                Toast.makeText(getActivity(), "some dr sales failed to delete", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();
                //user
                hashMap.put("company_id", company_id);
                hashMap.put("id", user_id);
                hashMap.put("company_code", company_code);
                hashMap.put("module", "requisition_approval");

                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }



    public void delete_rs(final String rs_id){
        String URL = getString(R.string.URL_online)+"requisition_header_delete.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("1")){
                    String dr_id;
                    try {
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(rs_main_list));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject r = jsonArray.getJSONObject(i);
                            dr_id = r.getString("rs_id");
                            if(dr_id.equals(rs_id)){
                                success_delete++;
                                checked_ids.remove(rs_id);
                                rs_main_list.remove(i);
                                rs_adapter.notifyDataSetChanged();
                            }
                        }

                        if(counter_delete==success_delete){
                          //  btn_remove.setEnabled(true);
                            showLoading(loadingScan, null).dismiss();
                            Toast.makeText(getActivity(), "delete success", Toast.LENGTH_SHORT).show();
                            cb_all.setChecked(false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    showLoading(loadingScan, null).dismiss();
//                    btn_remove.setEnabled(true);
                    Toast.makeText(getActivity(), "some dr sales failed to delete", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showLoading(loadingScan, null).dismiss();
                Toast.makeText(getActivity(), "some dr sales failed to delete", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();
                //user
                hashMap.put("company_id", company_id);
                hashMap.put("category_id", category_id);
                hashMap.put("user_id", user_id);
                hashMap.put("company_code", company_code);
                hashMap.put("branch_id", selected_branch_id);

                hashMap.put("rs_id", rs_id);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void get_details(final String update){
       showLoading(loadingScan, "Loading...").show();
        try {
            String URL = getString(R.string.URL_online)+"requisition_header_details.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                showLoading(loadingScan, null).dismiss();
                    try {
                        if(update.equals("0")){
                            JSONObject Object = new JSONObject(response);

                            //get current date
                            JSONArray date_diag = Object.getJSONArray("date");
                            JSONObject date_Obj = (JSONObject) date_diag.get(0);
                            String start_date = date_Obj.getString("start_date");
                            String end_date = date_Obj.getString("end_date");

                            btn_start_date.setText(start_date);
                            btn_end_date.setText(end_date);

                            //details
                            JSONArray diag = Object.getJSONArray("data");
                            for (int i = 0; i < diag.length(); i++) {
                                JSONObject Obj = (JSONObject) diag.get(i);
                                String rs_id = Obj.getString("id");
                                String remarks = Obj.getString("remarks");
                                String rs_number = Obj.getString("requisition_number");
                                String date_added = Obj.getString("date_added");
                                String requested_by = Obj.getString("requested_by");
                                String status = Obj.getString("status");
                                String count = Obj.getString("count");
                                String date_raw = Obj.getString("date_raw");
                                String asset = Obj.getString("asset");

                                rs_main_list.add(new Requisition_main_model(rs_id,remarks,rs_number
                                        ,date_added,requested_by,status,count,0,date_raw, asset));
                            }

                            rs_adapter = new Requisition_main_adapter(getContext(), rs_main_list,  Requisition_main.this);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                            rec_requsition.setLayoutManager(layoutManager);
                            rec_requsition.setAdapter(rs_adapter);
                            rec_requsition.setNestedScrollingEnabled(false);
                        }else{
                            JSONObject Object = new JSONObject(response);
                            //details
                            JSONArray diag = Object.getJSONArray("data");
                            for (int i = 0; i < diag.length(); i++) {

                                JSONObject Obj = (JSONObject) diag.get(i);
                                String rs_id = Obj.getString("id");
                                String remarks = Obj.getString("remarks");
                                String rs_number = Obj.getString("requisition_number");
                                String date_added = Obj.getString("date_added");
                                String requested_by = Obj.getString("requested_by");
                                String status = Obj.getString("status");
                                String count = Obj.getString("count");
                                String date_raw = Obj.getString("date_raw");
                                String asset = Obj.getString("asset");

                                rs_main_list.add(new Requisition_main_model(rs_id,remarks,rs_number
                                        ,date_added,requested_by,status,count,0,date_raw, asset));

                            }

                            rs_adapter = new Requisition_main_adapter(getContext(), rs_main_list,Requisition_main.this);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                            rec_requsition.setLayoutManager(layoutManager);
                            rec_requsition.setAdapter(rs_adapter);
                            rec_requsition.setNestedScrollingEnabled(false);
                        }
                    } catch (JSONException e) {}


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    showLoading(loadingScan, null).dismiss();
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> hashMap = new HashMap<>();
                    //user
                    hashMap.put("company_id", company_id);
                    hashMap.put("category_id", category_id);
                    hashMap.put("user_id", user_id);
                    hashMap.put("company_code", company_code);
                    hashMap.put("branch_id", selected_branch_id);


                    hashMap.put("end_date", end_date);
                    hashMap.put("start_date", start_date);
                    hashMap.put("update", update);
                    return hashMap;
                }
            };
            AppController.getInstance().setVolleyDuration(stringRequest);
            AppController.getInstance().addToRequestQueue(stringRequest);
        }
        catch (Exception e){}
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode==202){
                rs_main_list.clear();
                rs_adapter.notifyDataSetChanged();
               get_details("0");
            }
        }
    }

    @Override
    public void senddata(String check_dialog) {


        if(check_dialog.equals("okay")){
            remove_selected_rs();
        }else{
            Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
        }
    }
}
