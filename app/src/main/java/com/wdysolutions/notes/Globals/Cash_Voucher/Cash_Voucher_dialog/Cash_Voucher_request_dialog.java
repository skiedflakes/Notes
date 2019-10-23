package com.wdysolutions.notes.Globals.Cash_Voucher.Cash_Voucher_dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Cash_Voucher_request_dialog extends DialogFragment implements Cash_Voucher_request_dialog_adapter.EventListener,Cash_Voucher_request_dialog_adapter_supplier.EventListener_supplier {
    //details
    String cv_id,cv_num,remarks,encoded_by,doc_type,doc_num,cv_date,type,status,cv_status,status_color;
    TextView tv_remarks,tv_encoded_by,tv_doc_type,tv_doc_num,tv_cv_date,tv_cv,tv_type,tv_status;


    //user
    SharedPref sharedPref;
    String user_id, company_id,company_code,category_id,selected_branch_id;


    // -- DATA TABLE --

     //other accounts
    double total_debit=0.00;
    double total_credit=0.00;

    ArrayList<Cash_Voucher_request_dialog_model> cav_main_list = new ArrayList<>();
    RecyclerView rec_cv;
    Cash_Voucher_request_dialog_adapter cav_adapter;
    TextView tv_total_debit,tv_total_credit;

     //supplier
    ArrayList<Cash_Voucher_request_dialog_model_supplier> s_cav_main_list = new ArrayList<>();
    RecyclerView s_rec_cv;
    Cash_Voucher_request_dialog_adapter_supplier s_cav_adapter;
    TextView tv_total_amount;


    // -- LAYOUTS --

    //supplier layout
    double total_amount=0.00;
    LinearLayout s_doc_type,s_doc_num;
    RelativeLayout o_relative,s_relative;

    //details layout
    LinearLayout layout_2;
    ProgressBar progressBar;


    //button
    Button btn_close;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.cash_voucher_request_dialog, container, false);

        //user
        sharedPref = new SharedPref(getActivity());
        company_id = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYID);
        company_code = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYCODE);
        category_id = sharedPref.getUserInfo().get(sharedPref.KEY_CATEGORYID);
        selected_branch_id = Constants.branch_id;
        user_id = sharedPref.getUserInfo().get(sharedPref.KEY_USERID);

        //textview
        tv_remarks = view.findViewById(R.id.tv_remarks);
        tv_encoded_by = view.findViewById(R.id.tv_encoded_by);
        tv_doc_type = view.findViewById(R.id.tv_doc_type);
        tv_doc_num = view.findViewById(R.id.tv_doc_num);
        tv_cv_date = view.findViewById(R.id.tv_cv_date);
        tv_cv = view.findViewById(R.id.tv_cv);
        tv_type = view.findViewById(R.id.tv_type);
        tv_status = view.findViewById(R.id.tv_status);
        tv_total_amount= view.findViewById(R.id.tv_total_amount);
        tv_total_debit= view.findViewById(R.id.tv_total_debit);
        tv_total_credit= view.findViewById(R.id.tv_total_credit);

        //layouts
        s_doc_type = view.findViewById(R.id.s_doc_type);
        s_doc_num = view.findViewById(R.id.s_doc_num);
        o_relative = view.findViewById(R.id.o_relative);
        s_relative = view.findViewById(R.id.s_relative);
        layout_2 = view.findViewById(R.id.layout_2);
        progressBar = view.findViewById(R.id.progressBar);

        //recycler
        rec_cv = view.findViewById(R.id.recyclerView_);
        s_rec_cv = view.findViewById(R.id.s_recyclerView_);

        //button
        btn_close = view.findViewById(R.id.btn_close);
        getBundle();

        //onclick
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        return  view;
    }


    private void getBundle(){
        Bundle bundle = getArguments();
        if(bundle != null) {
            cv_id = bundle.getString("id");
            cv_num = bundle.getString("cv_num");
            get_cash_voucher_dialog();


        }
    }

    public void get_cash_voucher_dialog(){
        layout_2.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        String URL = getString(R.string.URL_online)+"cash_voucher/get_header_dialog.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                layout_2.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

                try{
                    JSONObject Object = new JSONObject(response);
                    JSONArray diag = Object.getJSONArray("data");
                    JSONObject Obj = (JSONObject) diag.get(0);
                    doc_num = Obj.getString("cv_doc_num");
                    doc_type = Obj.getString("cv_doc_type");
                    encoded_by = Obj.getString("cv_encoded");
                    remarks = Obj.getString("cv_remarks");
                    cv_date = Obj.getString("cv_date");
                    status = Obj.getString("status");
                    cv_status = Obj.getString("cv_status");
                    status_color = Obj.getString("status_color");
                    if(status_color.equals("APPROVED")){
                        tv_status.setTextColor(getActivity().getResources().getColor(R.color.color_btn_green));
                    }else{
                        tv_status.setTextColor(getActivity().getResources().getColor(R.color.color_text_light_black));
                    }

                    if(cv_status.equals("O")){
                        tv_type.setText("Other Accounts");

                        tv_doc_type.setText(doc_type);
                        tv_doc_num.setText(doc_num);

                        //hide show
                        o_relative.setVisibility(View.VISIBLE);
                        s_relative.setVisibility(View.GONE);
                        get_cash_voucher_table_other_accounts();

                    }else{
                        tv_type.setText("Supplier");


                        //hide show
                        s_doc_type.setVisibility(View.GONE);
                        s_doc_num.setVisibility(View.GONE);

                        o_relative.setVisibility(View.GONE);
                        s_relative.setVisibility(View.VISIBLE);
                        get_cash_voucher_table_supplier();

                    }
                    tv_remarks.setText(remarks);
                    tv_encoded_by.setText(encoded_by);

                    tv_cv_date.setText(cv_date);
                    tv_cv.setText(cv_num);

                    tv_status.setText(status);

                }catch (Exception e){}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error internet conneciton", Toast.LENGTH_SHORT).show();
                layout_2.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();
                //user
//                hashMap.put("company_id", company_id);
                //user
                hashMap.put("company_id", company_id);
                hashMap.put("category_id", category_id);
                hashMap.put("user_id", user_id);
                hashMap.put("company_code", company_code);
                hashMap.put("branch_id", selected_branch_id);

                hashMap.put("cv_number", cv_num);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void get_cash_voucher_table_other_accounts(){
        cav_main_list = new ArrayList<Cash_Voucher_request_dialog_model>();

        String URL = getString(R.string.URL_online)+"cash_voucher/get_header_dialog_table.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject Object = new JSONObject(response);
                    //get current date
                    JSONArray diag = Object.getJSONArray("data");

                    for (int i = 0; i < diag.length(); i++) {
                        JSONObject Obj = (JSONObject) diag.get(i);
                        String id = Obj.getString("id");
                        String count = Obj.getString("count");
                        String expense_id = Obj.getString("expense_id");
                        String remarks = Obj.getString("remarks");
                        String debit = Obj.getString("debit");
                        String credit = Obj.getString("credit");
                        cav_main_list.add(new Cash_Voucher_request_dialog_model(id,count,expense_id,remarks,debit,credit));

                        String sub_debit=debit.replaceAll(",", "");
                        total_debit += Double.valueOf(sub_debit);

                        String sub_credit=credit.replaceAll(",", "");
                        total_credit += Double.valueOf(sub_credit);

                    }
                }catch (Exception e){}

                    double pre_credit = total_debit - total_credit;
                    NumberFormat formatter = new DecimalFormat("###,###,###.00");
                    String pre_cred = formatter.format(pre_credit);
                    String formattedNumber_credit = formatter.format(total_credit+pre_credit);
                    String formattedNumber_debit = formatter.format(total_debit);
                    tv_total_debit.setText("P "+formattedNumber_debit);
                    tv_total_credit.setText("P "+formattedNumber_credit);

                    cav_main_list.add(new Cash_Voucher_request_dialog_model("","","","","0.00",pre_cred));


                    cav_adapter = new Cash_Voucher_request_dialog_adapter(getContext(), cav_main_list,  Cash_Voucher_request_dialog.this);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    rec_cv.setLayoutManager(layoutManager);
                    rec_cv.setAdapter(cav_adapter);
                    rec_cv.setNestedScrollingEnabled(false);




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error internet conneciton", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();
                //user
//                hashMap.put("company_id", company_id);
                //user
                hashMap.put("company_id", company_id);
                hashMap.put("category_id", category_id);
                hashMap.put("user_id", user_id);
                hashMap.put("company_code", company_code);
                hashMap.put("branch_id", selected_branch_id);

                hashMap.put("cv_number", cv_num);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void get_cash_voucher_table_supplier(){
        s_cav_main_list = new ArrayList<Cash_Voucher_request_dialog_model_supplier>();

        String URL = getString(R.string.URL_online)+"cash_voucher/get_header_dialog_table_supplier.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject Object = new JSONObject(response);
                    //get current date
                    JSONArray diag = Object.getJSONArray("data");

                    for (int i = 0; i < diag.length(); i++) {
                        JSONObject Obj = (JSONObject) diag.get(i);
                        String id = Obj.getString("id");
                        String count = Obj.getString("count");
                        String supplier = Obj.getString("supplier");
                        String amount = Obj.getString("amount");
                        String ref_num = Obj.getString("rr_number");
                        String remarks = Obj.getString("remarks");

                        s_cav_main_list.add(new Cash_Voucher_request_dialog_model_supplier(id,count,supplier,ref_num,remarks,amount));
                        String sub_amount=amount.replaceAll(",", "");

                        total_amount += Double.valueOf(sub_amount);
                    }
                    NumberFormat formatter = new DecimalFormat("###,###,###.00");
                    String total_amt = formatter.format(total_amount);
                    tv_total_amount.setText("P "+total_amt);
                    s_cav_adapter = new Cash_Voucher_request_dialog_adapter_supplier(getContext(), s_cav_main_list,  Cash_Voucher_request_dialog.this);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    s_rec_cv.setLayoutManager(layoutManager);
                    s_rec_cv.setAdapter(s_cav_adapter);
                    s_rec_cv.setNestedScrollingEnabled(false);


                }catch (Exception e){}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), "Error internet conneciton", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();
                //user
//                hashMap.put("company_id", company_id);
                //user
                hashMap.put("company_id", company_id);
                hashMap.put("category_id", category_id);
                hashMap.put("user_id", user_id);
                hashMap.put("company_code", company_code);
                hashMap.put("branch_id", selected_branch_id);

                hashMap.put("cv_number", cv_num);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


    @Override
    public void test(String id) {

    }

    @Override
    public void test_supplier(String id) {

    }
}
