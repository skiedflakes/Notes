package com.wdysolutions.notes.Globals.Revolving_Fund.Replenish.modal_view;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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


public class Revolving_replenish_modal_main extends DialogFragment {

    RecyclerView rec_cv;
    TextView txt_tracking_num, txt_date, txt_remarks,txt_credit_method,txt_check_num,txt_total, txt_status;
    CheckBox chck_undeclared;
    String company_id, company_code, category_id, user_id, selected_branch_id, rplnsh_num, getId;
    ScrollView scroll_view;
    ProgressBar progressBar4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.revolving_replenish_modal_main, container, false);
        SharedPref sharedPref = new SharedPref(getActivity());
        company_id = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYID);
        company_code = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYCODE);
        category_id = sharedPref.getUserInfo().get(sharedPref.KEY_CATEGORYID);
        user_id = sharedPref.getUserInfo().get(sharedPref.KEY_USERID);
        selected_branch_id = Constants.branch_id;

        getId = getArguments().getString("getId");
        rplnsh_num = getArguments().getString("getRplnsh_num");

        txt_status = view.findViewById(R.id.txt_status);
        progressBar4 = view.findViewById(R.id.progressBar4);
        scroll_view = view.findViewById(R.id.scroll_view);
        txt_total = view.findViewById(R.id.txt_total);
        rec_cv = view.findViewById(R.id.rec_cv);
        txt_date = view.findViewById(R.id.txt_date);
        txt_remarks = view.findViewById(R.id.txt_remarks);
        txt_credit_method = view.findViewById(R.id.txt_credit_method);
        txt_check_num = view.findViewById(R.id.txt_check_num);
        txt_tracking_num = view.findViewById(R.id.txt_tracking_num);
        chck_undeclared = view.findViewById(R.id.chck_undeclared);

        getReplenishDataView();
        return view;
    }

    ArrayList<Revolving_replenish_modal_model> revolving_replenish_modal_models;
    public void getReplenishDataView() {
        progressBar4.setVisibility(View.VISIBLE);
        scroll_view.setVisibility(View.GONE);
        String URL = getString(R.string.URL_online) + "revolving_fund/revolving_replenish_view.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    progressBar4.setVisibility(View.GONE);
                    scroll_view.setVisibility(View.VISIBLE);

                    revolving_replenish_modal_models = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject1 = (JSONObject)jsonArray.get(i);

                        revolving_replenish_modal_models.add(new Revolving_replenish_modal_model(jsonObject1.getString("id"),
                                jsonObject1.getString("branch"),
                                jsonObject1.getString("liquidation"),
                                jsonObject1.getString("amount"),
                                jsonObject1.getString("hid")));

                        txt_total.setText(jsonObject1.getString("total"));
                    }

                    Revolving_replenish_adapter pettyCashreplenish_modal_adapter = new Revolving_replenish_adapter(getActivity(), revolving_replenish_modal_models);
                    rec_cv.setLayoutManager(new LinearLayoutManager(getActivity()));
                    rec_cv.setAdapter(pettyCashreplenish_modal_adapter);
                    rec_cv.setNestedScrollingEnabled(false);

                    // details
                    JSONArray jsonArray1 = jsonObject.getJSONArray("data_rpl");
                    JSONObject jsonObject1 = (JSONObject)jsonArray1.get(0);
                    txt_tracking_num.setText(jsonObject1.getString("rplnsh_num"));
                    txt_date.setText(jsonObject1.getString("date"));
                    txt_check_num.setText(jsonObject1.getString("check_number"));
                    txt_remarks.setText(jsonObject1.getString("remarks"));
                    txt_credit_method.setText(jsonObject1.getString("credit_method"));

                    String status = jsonObject1.getString("status");
                    if (status.equals("0")){
                        txt_status.setText("PENDING");
                        txt_status.setBackgroundColor(getResources().getColor(R.color.color_orange));
                    } else {
                        txt_status.setText("APPROVED");
                        txt_status.setBackgroundColor(getResources().getColor(R.color.color_green));
                    }

                    String declared_status = jsonObject1.getString("declared_status");
                    if (declared_status.equals("checked")){
                        chck_undeclared.setChecked(true);
                    } else {
                        chck_undeclared.setChecked(false);
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
                hashMap.put("pm_number", rplnsh_num);
                hashMap.put("company_id", company_id);
                hashMap.put("company_code", company_code);
                hashMap.put("branch_id", selected_branch_id);
                hashMap.put("replenish_id", getId);
                hashMap.put("user_id", user_id);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


}
