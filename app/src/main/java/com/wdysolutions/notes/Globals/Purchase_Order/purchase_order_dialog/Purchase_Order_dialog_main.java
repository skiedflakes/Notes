package com.wdysolutions.notes.Globals.Purchase_Order.purchase_order_dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

public class Purchase_Order_dialog_main extends DialogFragment implements Purchase_Order_dialog_adapter.EventListener {
    //user
    SharedPref sharedPref;
    String user_id, company_id,company_code,category_id,selected_branch_id,date;

    //module
    String id,supplier_id;
    TextView tv_status,tv_po,tv_rs,tv_supplier,ty_payment_types,tv_terms,tv_encoded_by,tv_remarks,tv_date;
    String status,po,rs,supplier,payment_type,terms,encoded_by,remarks,status_color,dec;
    CheckBox cb_undeclared;
    Button btn_close;

    //datatable
    ArrayList<Purchase_Order_dialog_model> pod_list = new ArrayList<>();
    RecyclerView po_recyclerView_;
    Purchase_Order_dialog_adapter pod_adapter;

    //total
    double t_amount=0.00;
    TextView tv_total;

    //layouts progress
    RelativeLayout rl_progress_details;
    LinearLayout l_details;


    ProgressBar s_loading_table;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.purchase_order_dialog_main, container, false);

        //user
        sharedPref = new SharedPref(getActivity());
        company_id = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYID);
        company_code = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYCODE);
        category_id = sharedPref.getUserInfo().get(sharedPref.KEY_CATEGORYID);
        selected_branch_id = Constants.branch_id;
        user_id = sharedPref.getUserInfo().get(sharedPref.KEY_USERID);

        //module init

        //textview
        tv_status=view.findViewById(R.id.tv_status);
        tv_po=view.findViewById(R.id.tv_po);
        tv_rs=view.findViewById(R.id.tv_rs);
        tv_supplier=view.findViewById(R.id.tv_supplier);
        ty_payment_types=view.findViewById(R.id.ty_payment_types);
        tv_terms=view.findViewById(R.id.tv_terms);
        tv_encoded_by=view.findViewById(R.id.tv_encoded_by);
        tv_remarks=view.findViewById(R.id.tv_remarks);
        cb_undeclared=view.findViewById(R.id.cb_undeclared);
        tv_total = view.findViewById(R.id.tv_total);
        tv_date = view.findViewById(R.id.tv_date);

        //layout
        rl_progress_details = view.findViewById(R.id.rl_progress_details);
        l_details= view.findViewById(R.id.l_details);
        s_loading_table= view.findViewById(R.id.s_loading_table);

        //button
        btn_close = view.findViewById(R.id.btn_close);

        //recycler
        po_recyclerView_ = view.findViewById(R.id.po_recyclerView_);

        //onclicks
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });




        getBundle();
        get_po_dialog_details();
        return view;
    }

    private void getBundle(){
        Bundle bundle = getArguments();
        if(bundle != null) {
            id = bundle.getString("id");
        }
    }

    public void get_po_dialog_details(){

        String URL = getString(R.string.URL_online)+"purchase_order/get_po_dialog_details.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                l_details.setVisibility(View.VISIBLE);
                rl_progress_details.setVisibility(View.GONE);
                try{

                    JSONObject Object = new JSONObject(response);
                    JSONArray diag = Object.getJSONArray("data");
                    JSONObject Obj = (JSONObject) diag.get(0);

                    //get string
                    status = Obj.getString("status");
                    status_color = Obj.getString("status_color");
                    po = Obj.getString("po_number");
                    rs = Obj.getString("rs_number");
                    supplier = Obj.getString("supplier");
                    payment_type= Obj.getString("pay_type");
                    terms = Obj.getString("terms");
                    encoded_by = Obj.getString("encoded");
                    remarks =Obj.getString("remarks");
                    dec=Obj.getString("dec_stat");
                    supplier_id = Obj.getString("supplier_id");
                    date= Obj.getString("date");

                    //set text
                    tv_date.setText(date);
                    tv_status.setText(status);
                    tv_po.setText(po);
                    tv_rs.setText(rs);
                    tv_supplier.setText(supplier);
                    ty_payment_types.setText(payment_type);
                    tv_terms.setText(terms);
                    tv_encoded_by.setText(encoded_by);
                    tv_remarks.setText(remarks);
                    if(dec.equals("checked")){
                        cb_undeclared.setChecked(true);
                    }



                    //set color
                    if(status_color.equals("red")){
                        tv_status.setTextColor(getActivity().getResources().getColor(R.color.color_btn_red));
                    }else{
                        tv_status.setTextColor(getActivity().getResources().getColor(R.color.color_green));
                    }

                  get_po_dialog_table();
                }
                catch (Exception e){}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
                hashMap.put("id", id);

                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void get_po_dialog_table(){
        s_loading_table.setVisibility(View.VISIBLE);

        String URL = getString(R.string.URL_online)+"purchase_order/get_po_dialog_table.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                s_loading_table.setVisibility(View.GONE);

               // ((MainActivity)getActivity()).openDialog(response);
                try{
                    JSONObject Object = new JSONObject(response);

                    JSONArray diag = Object.getJSONArray("data");
                    for (int i = 0; i < diag.length(); i++) {

                        JSONObject Obj = (JSONObject) diag.get(i);
                        String id = Obj.getString("id");
                        String count = Obj.getString("count");
                        String product_id = Obj.getString("product_id");
                        String btn_update = Obj.getString("btn_update");
                        String quantity = Obj.getString("quantity");
                        String date_needed = Obj.getString("date_needed");
                        String needed_by = Obj.getString("needed_by");
                        String remaining_qty = Obj.getString("remaining_qty");
                        String remarks = Obj.getString("remarks");
                        String purpose = Obj.getString("purpose");
                        String description = Obj.getString("description");
                        String supplier_id = Obj.getString("supplier_id");
                        String supplier_price = Obj.getString("supplier_price");
                        String packaging_id = Obj.getString("packaging_id");
                        String subtotal = Obj.getString("subtotal");


                        String sub_amount=subtotal.replaceAll(",", "");
                        t_amount += Double.valueOf(sub_amount);

                        pod_list.add(new Purchase_Order_dialog_model(count,id,product_id,"",quantity,date_needed,
                                needed_by,remaining_qty,remarks,purpose,description,supplier_id,supplier_price,packaging_id
                        ,subtotal));
                    }

                    NumberFormat formatter = new DecimalFormat("###,###,###.00");
                    String total_amount = formatter.format(t_amount);
                    if(t_amount==0){ tv_total.setText("P 0.00");}else{tv_total.setText("P "+total_amount);}



                    pod_adapter = new Purchase_Order_dialog_adapter(getContext(), pod_list,  Purchase_Order_dialog_main.this);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    po_recyclerView_.setLayoutManager(layoutManager);
                    po_recyclerView_.setAdapter(pod_adapter);
                    po_recyclerView_.setNestedScrollingEnabled(false);


                }catch (Exception e){}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
                hashMap.put("supplier_id", supplier_id);
                hashMap.put("po_number", po);

                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    @Override
    public void test(String id) {

    }
}
