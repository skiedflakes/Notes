package com.wdysolutions.notes.Globals.Revolving_Fund.Request.modal_view;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
import com.wdysolutions.notes.R;
import com.wdysolutions.notes.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Revolving_request_modal_view extends DialogFragment {

    ProgressBar loading_;
    TextView txt_status, txt_pcv, txt_date, txt_branch, txt_account, txt_remarks, txt_total;
    CheckBox chk_no_receipt, chk_undeclare, chk_do_not_replenish;
    LinearLayout details_;
    RecyclerView rec_cv;
    ScrollView scroll_view;
    String company_id, company_code, user_id, selected_branch_id, id, getDate_requested, getUserID, getBr_id, getPcv, getRemarks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.revolving_request_modal_view, container, false);
        SharedPref sharedPref = new SharedPref(getActivity());
        company_id = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYID);
        company_code = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYCODE);
        user_id = sharedPref.getUserInfo().get(sharedPref.KEY_USERID);
        selected_branch_id = Constants.branch_id;
        id = getArguments().getString("getId");
        getDate_requested = getArguments().getString("getDate_requested");
        getUserID = getArguments().getString("getUserID");
        getBr_id = getArguments().getString("getBr_id");
        getPcv = getArguments().getString("getPcv");
        getRemarks = getArguments().getString("getRemarks");

        scroll_view = view.findViewById(R.id.scroll_view);
        details_ = view.findViewById(R.id.details_);
        loading_ = view.findViewById(R.id.loading_);
        txt_status = view.findViewById(R.id.txt_status);
        txt_pcv = view.findViewById(R.id.txt_pcv);
        txt_date = view.findViewById(R.id.txt_date);
        txt_branch = view.findViewById(R.id.txt_branch);
        txt_account = view.findViewById(R.id.txt_account);
        txt_remarks = view.findViewById(R.id.txt_remarks);
        chk_no_receipt = view.findViewById(R.id.chk_no_receipt);
        chk_undeclare = view.findViewById(R.id.chk_undeclare);
        rec_cv = view.findViewById(R.id.rec_cv);
        txt_total = view.findViewById(R.id.txt_total);
        chk_do_not_replenish = view.findViewById(R.id.chk_do_not_replenish);

        getRevolvingDetails();
        return view;
    }

    ArrayList<Revolving_request_modal_model> revolving_request_modal_models;
    public void getRevolvingDetails() {
        loading_.setVisibility(View.VISIBLE);
        scroll_view.setVisibility(View.GONE);
        String URL = getString(R.string.URL_online) + "revolving_fund/get_revolving_view.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    loading_.setVisibility(View.GONE);
                    scroll_view.setVisibility(View.VISIBLE);

                    revolving_request_modal_models = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("revolving_data");
                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject1 = (JSONObject)jsonArray.get(i);

                        revolving_request_modal_models.add(new Revolving_request_modal_model(jsonObject1.getString("id"),
                                jsonObject1.getString("amount"),
                                jsonObject1.getString("chart"),
                                jsonObject1.getString("desc"),
                                jsonObject1.getString("doc_type"),
                                jsonObject1.getString("doc_num"),
                                jsonObject1.getString("doc_date"),
                                jsonObject1.getString("pcv"),
                                jsonObject1.getString("chart_id"),
                                jsonObject1.getString("br_id")));

                        txt_total.setText(jsonObject1.getString("total"));
                    }

                    Revolving_request_modal_adapter revolving_request_modal_adapter = new Revolving_request_modal_adapter(getActivity(), revolving_request_modal_models);
                    rec_cv.setLayoutManager(new LinearLayoutManager(getActivity()));
                    rec_cv.setAdapter(revolving_request_modal_adapter);
                    rec_cv.setNestedScrollingEnabled(false);


                    JSONArray jsonArray_ = jsonObject.getJSONArray("data");
                    JSONObject jsonObject1 = (JSONObject)jsonArray_.get(0);
                    String status = jsonObject1.getString("status");
                    String status_color = jsonObject1.getString("status_color");
                    String receipt_status = jsonObject1.getString("receipt_status");
                    String declared_status = jsonObject1.getString("declared_status");
                    String dnr_status = jsonObject1.getString("dnr_status");
                    String branch = jsonObject1.getString("branch");

                    txt_status.setText(status);
                    if (status_color.equals("green")){
                        txt_status.setBackgroundColor(getResources().getColor(R.color.color_green));
                    } else if (status_color.equals("red")){
                        txt_status.setBackgroundColor(getResources().getColor(R.color.color_btn_red));
                    } else { // orange
                        txt_status.setBackgroundColor(getResources().getColor(R.color.color_orange));
                    }

                    txt_pcv.setText("PCV: "+getPcv);
                    txt_date.setText(getDate_requested);
                    txt_account.setText(getUserID);
                    txt_branch.setText(branch);
                    txt_remarks.setText(getRemarks);

                    if (receipt_status.equals("checked")){
                        chk_no_receipt.setChecked(true);
                    }
                    if (declared_status.equals("checked")){
                        chk_undeclare.setChecked(true);
                    }
                    if (dnr_status.equals("checked")){
                        chk_do_not_replenish.setChecked(true);
                    }

                    chk_no_receipt.setClickable(false);
                    chk_undeclare.setClickable(false);
                    chk_do_not_replenish.setClickable(false);

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
                hashMap.put("pettyCash_id", id);
                hashMap.put("company_id", company_id);
                hashMap.put("company_code", company_code);
                hashMap.put("user_id", user_id);
                hashMap.put("branch_id", getBr_id);
                hashMap.put("pcv_number", getPcv);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


}
