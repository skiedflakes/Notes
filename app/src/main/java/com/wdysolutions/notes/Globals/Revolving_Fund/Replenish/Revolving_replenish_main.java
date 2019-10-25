package com.wdysolutions.notes.Globals.Revolving_Fund.Replenish;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.wdysolutions.notes.DatePicker.DatePickerCustom;
import com.wdysolutions.notes.DatePicker.DatePickerSelectionInterfaceCustom;
import com.wdysolutions.notes.Dialog_Action;
import com.wdysolutions.notes.Globals.Petty_Cash.Replenish.PettyCash_replenish_adapter;
import com.wdysolutions.notes.Globals.Petty_Cash.Replenish.PettyCash_replenish_model;
import com.wdysolutions.notes.Globals.Revolving_Fund.Liquidation.Revolving_liquidation_adapter;
import com.wdysolutions.notes.Globals.Revolving_Fund.Liquidation.Revolving_liquidation_model;
import com.wdysolutions.notes.Globals.Revolving_Fund.Replenish.modal_view.Revolving_replenish_modal_main;
import com.wdysolutions.notes.Globals.Revolving_Fund.Request.modal_view.Revolving_request_modal_view;
import com.wdysolutions.notes.MainActivity;
import com.wdysolutions.notes.R;
import com.wdysolutions.notes.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Revolving_replenish_main extends Fragment implements DatePickerSelectionInterfaceCustom,
        Revolving_replenish_adapter.EventListener,Dialog_Action.uploadDialogInterface {

    TextView btn_start_date, btn_end_date;
    LinearLayout btn_generate_report, details_;
    ProgressBar progressBar2;
    RecyclerView rec_cv;
    String company_id, company_code, category_id, selected_branch_id;
    String selectedStartDate = "", selectedEndDate = "", receipt_stat = "";
    boolean isStartDateClick = false;
    ProgressDialog loadingScan;
    //strings
    String selected_id,selected_replenish_num,user_name,selected_date,selected_br_id,user_id;
    Revolving_replenish_adapter revolving_replenish_adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.revolving_replenish_main, container, false);
        SharedPref sharedPref = new SharedPref(getActivity());
        company_id = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYID);
        company_code = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYCODE);
        category_id = sharedPref.getUserInfo().get(sharedPref.KEY_CATEGORYID);
        selected_branch_id = Constants.branch_id;
        user_id = sharedPref.getUserInfo().get(sharedPref.KEY_USERID);
        user_name = sharedPref.getUserInfo().get(sharedPref.KEY_NAME);


        //loading
        loadingScan = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);

        btn_start_date = view.findViewById(R.id.btn_start_date);
        btn_end_date = view.findViewById(R.id.btn_end_date);
        btn_generate_report = view.findViewById(R.id.btn_generate_report);
        progressBar2 = view.findViewById(R.id.progressBar2);
        details_ = view.findViewById(R.id.details_);
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
                    getRevolvingReplenishData();
                }
            }
        });

        getRevolvingReplenishData();
        return view;
    }

    String current_date = "", start_date, end_date;
    ArrayList<Revolving_replenish_model> revolving_replenish_models;
    public void getRevolvingReplenishData() {
        progressBar2.setVisibility(View.VISIBLE);
        details_.setVisibility(View.GONE);
        btn_generate_report.setEnabled(false);
        String URL = getString(R.string.URL_online) + "revolving_fund/revolving_replenish_data.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    progressBar2.setVisibility(View.GONE);
                    details_.setVisibility(View.VISIBLE);
                    btn_generate_report.setEnabled(true);
                    btn_start_date.setEnabled(true);
                    btn_end_date.setEnabled(true);

                    revolving_replenish_models
                            = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject1 = (JSONObject)jsonArray.get(i);

                        revolving_replenish_models.add(new Revolving_replenish_model(jsonObject1.getString("id"),
                                jsonObject1.getString("br_id"),
                                jsonObject1.getString("rplnsh_num"),
                                jsonObject1.getString("date"),
                                jsonObject1.getString("amount"),
                                jsonObject1.getString("remarks"),
                                jsonObject1.getString("status"),
                                jsonObject1.getString("status_color"),
                                jsonObject1.getString("rfr_stats"),
                                jsonObject1.getString("rfr_stat"),
                                jsonObject1.getString("rfr_stat_color"),
                                jsonObject1.getString("dec_status"),
                                jsonObject1.getString("dec_status_color"),
                                jsonObject1.getString("encodedBY"),
                                jsonObject1.getString("approved_by")));
                    }

                    revolving_replenish_adapter  = new Revolving_replenish_adapter(getActivity(), revolving_replenish_models,Revolving_replenish_main.this);
                    rec_cv.setLayoutManager(new LinearLayoutManager(getActivity()));
                    rec_cv.setAdapter(revolving_replenish_adapter);
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


    @Override
    public void view_modal(int position, String tracking_num, String id, String date, String br_id, int view_details, int micro_filming, int approve, int disapprove) {
        selected_id = id;
        selected_replenish_num = tracking_num;
        selected_br_id = br_id;
        selected_date = date;

        Bundle bundle = new Bundle();
        bundle.putInt("view_details", view_details);
        bundle.putInt("micro_filming", micro_filming);
        bundle.putInt("approve", approve);
        bundle.putInt("disapprove", disapprove);
        bundle.putInt("position", position);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }

        ft.addToBackStack(null);
        Dialog_Action fragment = new Dialog_Action();
        fragment.setTargetFragment(Revolving_replenish_main.this, 100);
        FragmentManager manager = getFragmentManager();
        fragment.setArguments(bundle);
        fragment.show(ft, "UploadDialogFragment");
        fragment.setCancelable(true);
    }

    @Override
    public void senddata(String chosen, int position) {
        if(chosen.equals("view_details")){
            openView();
        }else if(chosen.equals("approve")){
            openDialog_approve(user_id,true,position);
        }else if(chosen.equals("disapprove")){

            openDialog_approve("",false,position);
        }else if(chosen.equals("micro_filming")){
            ((MainActivity)getActivity()).openMicro(selected_replenish_num, "", "");
        }
    }

    private void openView(){
        Bundle bundle = new Bundle();
        bundle.putString("getId", selected_id);
        bundle.putString("getRplnsh_num", selected_replenish_num);
        bundle.putString("getUserID", selected_date);
        bundle.putString("getBr_id", selected_br_id);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("details");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        Revolving_replenish_modal_main fragment = new Revolving_replenish_modal_main();
        fragment.setTargetFragment(Revolving_replenish_main.this, 0);
        FragmentManager manager = getFragmentManager();
        fragment.setArguments(bundle);
        fragment.show(getFragmentManager(), "details");
        fragment.setCancelable(true);
    }

    public void openDialog_approve(final String user_id,final boolean type,final int position){
        if(type){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            alertDialog.setTitle("Are you sure you want to approve?");
            alertDialog.setPositiveButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog.setNegativeButton("OK",  new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    approve_disapprove(user_id,position);
                }
            });
            alertDialog.setCancelable(false);
            alertDialog.show();
        }else{
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            alertDialog.setTitle("Are you sure you want to disapprove?");
            alertDialog.setPositiveButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog.setNegativeButton("OK",  new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    approve_disapprove(user_id,position);
                }
            });
            alertDialog.setCancelable(false);
            alertDialog.show();
        }

    }

    private ProgressDialog showLoading(ProgressDialog loading, String msg){
        loading.setMessage(msg);
        loading.setCancelable(false);
        return loading;
    }

    public void approve_disapprove(final String user_id,final int position){
        showLoading(loadingScan, "Loading...").show();
        String URL = getString(R.string.URL_online)+"revolving_fund/approve_replenish.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    showLoading(loadingScan, null).dismiss();
                    if(response.equals("1")){

                        Revolving_replenish_model main_list = revolving_replenish_models.get(position);

                        String id = main_list.getId();
                        String br_id  = main_list.getBr_id();
                        String rplnsh_num = main_list.getRplnsh_num();
                        String date = main_list.getDate();
                        String amount = main_list.getAmount();
                        String remarks = main_list.getRemarks();
                        String status = main_list.getStatus();
                        String status_color = main_list.getStatus_color();
                        String rfr_stats  = main_list.getRfr_stats();
                        String rfr_stat  = main_list.getRfr_stat();
                        String rfr_stat_color = main_list.getRfr_stat_color();
                        String dec_stat = main_list.getDec_stat();
                        String dec_stat_color = main_list.getDec_stat_color();
                        String encodedBY = main_list.getEncodedBY();



                        Revolving_replenish_model newval;
                        if(user_id.equals("")){
                            newval = new Revolving_replenish_model(id,br_id,rplnsh_num,
                                    date,amount,remarks,status,status_color,rfr_stats,
                                    rfr_stat,rfr_stat_color,dec_stat,dec_stat_color,encodedBY,"");

                            Toast.makeText(getContext(), "Disapprove success!", Toast.LENGTH_SHORT).show();
                        }else{
                            newval = new Revolving_replenish_model(id,br_id,rplnsh_num,
                                    date,amount,remarks,status,status_color,rfr_stats,
                                    rfr_stat,rfr_stat_color,dec_stat,dec_stat_color,encodedBY,user_name);

                            Toast.makeText(getContext(), "Approve success!", Toast.LENGTH_SHORT).show();
                        }

                        revolving_replenish_models.set(position,newval);
                        revolving_replenish_adapter.notifyItemChanged(position);

                    }
                }catch (Exception e){

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showLoading(loadingScan, null).dismiss();
                Toast.makeText(getActivity(), "Error Connection", Toast.LENGTH_SHORT).show();

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

                //module
                hashMap.put("rplnsh_num", selected_replenish_num);

                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
