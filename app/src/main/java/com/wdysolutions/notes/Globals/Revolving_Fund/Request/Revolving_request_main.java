package com.wdysolutions.notes.Globals.Revolving_Fund.Request;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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


public class Revolving_request_main extends Fragment implements DatePickerSelectionInterfaceCustom, Revolving_request_adapter.EventListener,Dialog_Action.uploadDialogInterface {

    TextView btn_start_date, btn_end_date;
    LinearLayout btn_generate_report, details_;
    ProgressBar progressBar2;
    RecyclerView rec_cv;
    SharedPref sharedPref;
    String company_id, company_code, category_id, selected_branch_id;
    String selectedStartDate = "", selectedEndDate = "", receipt_stat = "";
    boolean isStartDateClick = false;


    String s_getId,s_getDate_requested,s_getUserID,s_getBr_id, s_getRcv,s_getRemarks,user_name,user_id;
    ProgressDialog loadingScan;
    Revolving_request_adapter revolving_request_adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.revolving_request_main, container, false);
        sharedPref = new SharedPref(getActivity());
        company_id = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYID);
        company_code = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYCODE);
        category_id = sharedPref.getUserInfo().get(sharedPref.KEY_CATEGORYID);
        selected_branch_id = Constants.branch_id;
        user_id = sharedPref.getUserInfo().get(sharedPref.KEY_USERID);
        user_name= sharedPref.getUserInfo().get(sharedPref.KEY_NAME);
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
                    getRevolvingFundData();
                }
            }
        });

        getRevolvingFundData();
        return view;
    }

    String current_date = "", start_date, end_date;
    ArrayList<Revolving_request_model> revolving_request_models;
    public void getRevolvingFundData() {
        progressBar2.setVisibility(View.VISIBLE);
        details_.setVisibility(View.GONE);
        btn_generate_report.setEnabled(false);
        String URL = getString(R.string.URL_online) + "revolving_fund/revolving_details.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    progressBar2.setVisibility(View.GONE);
                    details_.setVisibility(View.VISIBLE);
                    btn_generate_report.setEnabled(true);

                    revolving_request_models = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject1 = (JSONObject)jsonArray.get(i);

                        revolving_request_models.add(new Revolving_request_model(
                                jsonObject1.getString("credit_method_test"),
                                jsonObject1.getString("id"),
                                jsonObject1.getString("count"),
                                jsonObject1.getString("pcv"),
                                jsonObject1.getString("date_requested"),
                                jsonObject1.getString("amount"),
                                jsonObject1.getString("remarks"),
                                jsonObject1.getString("date_encoded"),
                                jsonObject1.getString("br"),
                                jsonObject1.getString("br_id"),
                                jsonObject1.getString("credit_method"),
                                jsonObject1.getString("created_by"),
                                jsonObject1.getString("userID"),
                                jsonObject1.getString("stats"),
                                jsonObject1.getString("stats_color"),
                                jsonObject1.getString("hid"),
                                jsonObject1.getString("liquidate_stats"),
                                jsonObject1.getString("liquidate_stats_color"),
                                jsonObject1.getString("status"),
                                jsonObject1.getString("status_color"),
                                jsonObject1.getString("rfr_status"),
                                jsonObject1.getString("rfr_status_color"),
                                jsonObject1.getString("dnr_stat"),
                                jsonObject1.getString("liquidation_stat"),
                                jsonObject1.getString("rep_stats"),
                                jsonObject1.getString("rep_stat"),
                                jsonObject1.getString("approved_by")));
                    }

                     revolving_request_adapter = new Revolving_request_adapter(getActivity(), revolving_request_models, Revolving_request_main.this);
                    rec_cv.setLayoutManager(new LinearLayoutManager(getActivity()));
                    rec_cv.setAdapter(revolving_request_adapter);
                    rec_cv.setNestedScrollingEnabled(false);

                    // Current Date
                    JSONArray jsonArray1 = jsonObject.getJSONArray("response_date");
                    JSONObject jsonObject1 = (JSONObject)jsonArray1.get(0);
                    current_date = jsonObject1.getString("current_date");
                    start_date = jsonObject1.getString("start_date");
                    end_date = jsonObject1.getString("end_date");

                    if (selectedStartDate.equals("")) { btn_start_date.setText(start_date); selectedStartDate = start_date; }
                    if (selectedEndDate.equals("")) { btn_end_date.setText(end_date); selectedEndDate = end_date; }

                } catch (JSONException e){}
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
    public void view_modal(int position, String getId, String getDate_requested, String getUserID, String getBr_id, String getPcv, String getRemarks, int view_details, int micro_filming, int approve, int disapprove) {

        s_getId = getId;
        s_getDate_requested = getDate_requested;
        s_getUserID = getUserID;
        s_getBr_id = getBr_id;
        s_getRcv = getPcv;
        s_getRemarks = getRemarks;

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
        fragment.setTargetFragment(Revolving_request_main.this, 100);
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
            ((MainActivity)getActivity()).openMicro(s_getRcv, "", "");
        }
    }

    private void openView(){
        Bundle bundle = new Bundle();
        bundle.putString("getId", s_getId);
        bundle.putString("getDate_requested", s_getDate_requested);
        bundle.putString("getUserID", s_getUserID);
        bundle.putString("getBr_id", s_getBr_id);
        bundle.putString("getPcv", s_getRcv);
        bundle.putString("getRemarks", s_getRemarks);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("details");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        Revolving_request_modal_view fragment = new Revolving_request_modal_view();
        fragment.setTargetFragment(Revolving_request_main.this, 0);
        FragmentManager manager = getFragmentManager();
        fragment.setArguments(bundle);
        fragment.show(getFragmentManager(), "details");
        fragment.setCancelable(true);
    }

    private ProgressDialog showLoading(ProgressDialog loading, String msg){
        loading.setMessage(msg);
        loading.setCancelable(false);
        return loading;
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

    public void approve_disapprove(final String user_id,final int position){
        showLoading(loadingScan, "Loading...").show();
        String URL = getString(R.string.URL_online)+"revolving_fund/approve_request.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    showLoading(loadingScan, null).dismiss();
                    if(response.equals("1")){

                        Revolving_request_model main_list = revolving_request_models.get(position);
                        String credit_method_test = main_list.getCredit_method_test();
                        String id = main_list.getId();
                        String count = main_list.getCount();
                        String pcv = main_list.getPcv();
                        String date_requested = main_list.getDate_requested();
                        String amount = main_list.getAmount();
                        String remarks = main_list.getRemarks();
                        String date_encoded = main_list.getDate_encoded();
                        String br = main_list.getBr();
                        String br_id  = main_list.getBr_id();
                        String credit_method = main_list.getCredit_method();
                        String created_by  = main_list.getCreated_by();
                        String userID = main_list.getUserID();
                        String stats = main_list.getStats();
                        String stats_color = main_list.getStats_color();
                        String hid = main_list.getHid();
                        String liquidate_stats = main_list.getLiquidate_stats();
                        String liquidate_stats_color = main_list.getLiquidate_stats_color();
                        String status = main_list.getStatus();
                        String status_color = main_list.getStatus_color();
                        String rfr_status = main_list.getRfr_status();
                        String rfr_status_color = main_list.getRfr_status_color();
                        String dnr_stat = main_list.getDnr_stat();
                        String liquidation_stat = main_list.getLiquidation_stat();
                        String rep_stats = main_list.getRep_stats();
                        String rep_stat = main_list.getRep_stat();
                        String approved_by = main_list.getApproved_by();


                        Revolving_request_model newval;
                        if(user_id.equals("")){
                            newval = new Revolving_request_model(credit_method_test,id,count,pcv,date_requested,amount,remarks
                            ,date_encoded,br,br_id,credit_method,created_by,userID,stats,stats_color,hid,liquidate_stats,liquidate_stats_color
                            ,status,status_color,rfr_status,rfr_status_color,dnr_stat,liquidation_stat,rep_stats,rep_stat,"");

                            Toast.makeText(getContext(), "Disapprove success!", Toast.LENGTH_SHORT).show();
                        }else{
                            newval = new Revolving_request_model(credit_method_test,id,count,pcv,date_requested,amount,remarks
                                    ,date_encoded,br,br_id,credit_method,created_by,userID,stats,stats_color,hid,liquidate_stats,liquidate_stats_color
                                    ,status,status_color,rfr_status,rfr_status_color,dnr_stat,liquidation_stat,rep_stats,rep_stat,user_name);

                            Toast.makeText(getContext(), "Approve success!", Toast.LENGTH_SHORT).show();
                        }

                        revolving_request_models.set(position,newval);
                        revolving_request_adapter.notifyItemChanged(position);

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
                hashMap.put("revolving_num", s_getRcv);

                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
