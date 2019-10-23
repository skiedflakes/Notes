package com.wdysolutions.notes.Globals.Requisition.Requisition_Add.Canvassing;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
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
import com.wdysolutions.notes.Globals.Requisition.Requisition_Add.Requisition_add_main;
import com.wdysolutions.notes.MainActivity;
import com.wdysolutions.notes.R;
import com.wdysolutions.notes.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class Requisition_add_canvassing_main extends DialogFragment implements Requisition_add_canvassing_adapter.EventListener {

    ArrayList<Supplier_model> supplier_models = new ArrayList<>();
    ArrayList<CanvasTable_model> canvasTable_models = new ArrayList<>();
    String selectedSupplier="";
    String rs_details_id, product,package_name,qty, selectedDate, product_id;
    ProgressBar progressBar, loading_inventory, loading_table;
    LinearLayout layout_2;
    ProgressDialog progressDialog;
    boolean isAddEntry = false;
    Requisition_add_canvassing_adapter rs_adapter;
    TextView txt_name, txt_rm, txt_sales_last_month, txt_feed_consump, txt_inventory, txt_packaging, txt_order_quantity,
            txt_projection, txt_pendingPo, txt_load_table;
    Button btn_add_entry_;
    Spinner spinner_supplier2;
    RecyclerView recyclerView_;
    EditText editText_price1, editText_remarks1;
    ScrollView scroll_table;

    SharedPref sharedPref;
    //user session
    String user_id, company_id,company_code,category_id,selected_branch_id;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.requisition_add_canvassing_main, container, false);

        //user
        sharedPref = new SharedPref(getActivity());
        company_id = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYID);
        company_code = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYCODE);
        category_id = sharedPref.getUserInfo().get(sharedPref.KEY_CATEGORYID);
        selected_branch_id = Constants.branch_id;
        user_id = sharedPref.getUserInfo().get(sharedPref.KEY_USERID);

        progressDialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
        Button btn_close = view.findViewById(R.id.btn_close);
        progressBar = view.findViewById(R.id.progressBar);
        loading_inventory = view.findViewById(R.id.loading_inventory);
        layout_2 = view.findViewById(R.id.layout_2);
        loading_table = view.findViewById(R.id.loading_table);
        scroll_table = view.findViewById(R.id.scroll_table);

        txt_name = view.findViewById(R.id.txt_name);
        txt_rm = view.findViewById(R.id.txt_rm);
        txt_sales_last_month = view.findViewById(R.id.txt_sales_last_month);
        txt_feed_consump = view.findViewById(R.id.txt_feed_consump);
        txt_inventory = view.findViewById(R.id.txt_inventory);
        txt_packaging = view.findViewById(R.id.txt_packaging);
        txt_order_quantity = view.findViewById(R.id.txt_order_quantity);
        txt_projection = view.findViewById(R.id.txt_projection);
        txt_pendingPo = view.findViewById(R.id.txt_pendingPo);
        spinner_supplier2 = view.findViewById(R.id.spinner_supplier2);
        btn_add_entry_ = view.findViewById(R.id.btn_add_entry_);
        recyclerView_ = view.findViewById(R.id.recyclerView_);
        editText_price1 = view.findViewById(R.id.editText_price1);
        editText_remarks1 = view.findViewById(R.id.editText_remarks1);
        txt_load_table = view.findViewById(R.id.txt_load_table);

        btn_add_entry_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedSupplier.equals("")){
                    Toast.makeText(getActivity(), "Please select supplier", Toast.LENGTH_SHORT).show();
                } else if (editText_price1.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Please enter price", Toast.LENGTH_SHORT).show();
                } else {
                    addCanvas();
                }
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });
        getBundle();
        getCanvassDetails();
        get_inventory();
        return view;
    }

    private void getBundle(){
        Bundle bundle = getArguments();
        if(bundle != null) {
            rs_details_id = bundle.getString("id");
            product = bundle.getString("product");
            product_id = bundle.getString("product_id");
            package_name = bundle.getString("package_name");
            selectedDate = bundle.getString("selectedDate");
            qty = bundle.getString("qty");
        }
    }

    public void getCanvassDetails(){
        layout_2.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        String URL =  getString(R.string.URL_online)+"requisition_canvassing_details2.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    //((MainActivity)getActivity()).openDialog(response);
                    layout_2.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data_details");
                    JSONObject jsonObject1 = (JSONObject)jsonArray.get(0);

                    String raw_mats = jsonObject1.getString("raw_mats_used");
                    String sales_lastmonth = jsonObject1.getString("sales_lastmonth");
                    String feed_cons = jsonObject1.getString("feed_cons");

                    String pending_po = jsonObject1.getString("pending_po");
                    String projection_reg = jsonObject1.getString("projection_req");

                    txt_name.setText(product);
                    txt_rm.setText(raw_mats);
                    txt_sales_last_month.setText(sales_lastmonth);
                    txt_feed_consump.setText(feed_cons);
                    txt_packaging.setText(package_name);
                    txt_order_quantity.setText(qty);
                    txt_projection.setText(projection_reg);
                    txt_pendingPo.setText(pending_po);


                    ////////////////////////////////// SPINNER ////////////////////////////////
                    JSONArray jsonArray_1 = jsonObject.getJSONArray("data_spinner");
                    supplier_models.add(new Supplier_model("Please select", "00"));
                    for (int i=0; i<jsonArray_1.length(); i++){
                        JSONObject jsonObject_ = (JSONObject)jsonArray_1.get(i);
                        supplier_models.add(new Supplier_model(jsonObject_.getString("supplier"),
                                jsonObject_.getString("supplier_id")));
                    }

                    // Populate Spinner
                    List<String> lables = new ArrayList<>();
                    for (int i = 0; i < supplier_models.size(); i++) {
                        lables.add(supplier_models.get(i).getSupplier());
                    }
                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_drop, lables);
                    spinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_drop);
                    spinner_supplier2.setAdapter(spinnerAdapter);
                    spinner_supplier2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            Supplier_model click = supplier_models.get(position);
                            if (!click.getSupplier().equals("Please select")){
                                selectedSupplier = click.getSupplier_id();
                            } else {
                                selectedSupplier = "";
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {}
                    });

                    get_table();

                }
                catch (JSONException e){}
                catch (Exception e){}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    dismiss();
                    Toast.makeText(getActivity(), "Connection Error, please try again.", Toast.LENGTH_SHORT).show();
                } catch (Exception e){}
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("product_id", product_id);
                hashMap.put("rs_detail_id", rs_details_id);
                hashMap.put("datepicker", selectedDate);

                //user
                hashMap.put("company_id", company_id);
                hashMap.put("category_id", category_id);
                hashMap.put("user_id", user_id);
                hashMap.put("company_code", company_code);
                hashMap.put("branch_id", selected_branch_id);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void get_table(){
        scroll_table.setVisibility(View.GONE);
        loading_table.setVisibility(View.VISIBLE);
        txt_load_table.setVisibility(View.GONE);
        String URL =  getString(R.string.URL_online)+"requisition_canvassing_get_table.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    scroll_table.setVisibility(View.VISIBLE);
                    loading_table.setVisibility(View.GONE);
                    txt_load_table.setOnClickListener(null);
                    JSONObject jsonObject = new JSONObject(response);

                    ////////////////////////////////// TABLE ////////////////////////////////
                    canvasTable_models = new ArrayList<>();
                    JSONArray jsonArray_ = jsonObject.getJSONArray("data_table");
                    for (int i=0; i<jsonArray_.length(); i++){
                        JSONObject jsonObject2 = (JSONObject)jsonArray_.get(i);
                        canvasTable_models.add(new CanvasTable_model(jsonObject2.getString("id"),
                                jsonObject2.getString("supplier_id"),
                                jsonObject2.getString("price"),
                                jsonObject2.getString("date_added"),
                                jsonObject2.getString("remarks"),
                                "",
                                "",
                                "",
                                jsonObject2.getString("status"),
                                jsonObject2.getString("supplier")));
                    }

                    rs_adapter = new Requisition_add_canvassing_adapter(getContext(), canvasTable_models, Requisition_add_canvassing_main.this);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    recyclerView_.setLayoutManager(layoutManager);
                    recyclerView_.setAdapter(rs_adapter);
                    recyclerView_.setNestedScrollingEnabled(false);

                } catch (JSONException e){}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    scroll_table.setVisibility(View.GONE);
                    loading_table.setVisibility(View.GONE);
                    txt_load_table.setVisibility(View.VISIBLE);
                    txt_load_table.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            get_table();
                        }
                    });
                } catch (Exception e){}
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("rs_detail_id", rs_details_id);

                //user
                hashMap.put("company_id", company_id);
                hashMap.put("category_id", category_id);
                hashMap.put("user_id", user_id);
                hashMap.put("company_code", company_code);
                hashMap.put("branch_id", selected_branch_id);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void get_inventory(){
        loading_inventory.setVisibility(View.VISIBLE);
        txt_inventory.setVisibility(View.GONE);
        String URL =  getString(R.string.URL_online)+"requisition_canvassing_inventory.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    loading_inventory.setVisibility(View.GONE);
                    txt_inventory.setVisibility(View.VISIBLE);
                    txt_inventory.setOnClickListener(null);
                    txt_inventory.setTextColor(getResources().getColor(R.color.color_text_light_black));
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data_details");
                    JSONObject jsonObject1 = (JSONObject)jsonArray.get(0);
                    String inventory = jsonObject1.getString("inventory");
                    txt_inventory.setText(inventory);

                }
                catch (JSONException e){}
                catch (Exception e){}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    loading_inventory.setVisibility(View.GONE);
                    txt_inventory.setVisibility(View.VISIBLE);
                    txt_inventory.setTextColor(getResources().getColor(R.color.color_btn_red));
                    txt_inventory.setText("Failed to load, tap to refresh.");
                    txt_inventory.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            get_inventory();
                        }
                    });
                } catch (Exception e){}
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("product_id", product_id);

                //user
                hashMap.put("company_id", company_id);
                hashMap.put("category_id", category_id);
                hashMap.put("user_id", user_id);
                hashMap.put("company_code", company_code);
                hashMap.put("branch_id", selected_branch_id);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


    public void addCanvas(){
        isAddEntry = true;
        ((MainActivity)getActivity()).showLoading(progressDialog, "Saving...").show();
        String URL = getString(R.string.URL_online)+"requisition_canvassing_add.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    if (response.equals("1")){
                        get_table();
                        Toast.makeText(getActivity(), "Successfully save", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Failed to save", Toast.LENGTH_SHORT).show();
                    }
                    ((MainActivity)getActivity()).showLoading(progressDialog, "").dismiss();
                } catch (Exception e){}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    ((MainActivity)getActivity()).showLoading(progressDialog, "").dismiss();
                    Toast.makeText(getActivity(), "Connection Error, please try again.", Toast.LENGTH_SHORT).show();
                } catch (Exception e){}
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("supplier_id", selectedSupplier);
                hashMap.put("supplier_price", editText_price1.getText().toString());
                hashMap.put("rs_detail_id", rs_details_id);
                hashMap.put("canvassing_remarks", editText_remarks1.getText().toString());

                //user
                hashMap.put("company_id", company_id);
                hashMap.put("category_id", category_id);
                hashMap.put("user_id", user_id);
                hashMap.put("company_code", company_code);
                hashMap.put("branch_id", selected_branch_id);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void approve_canvas(final String id){
        isAddEntry = true;
        ((MainActivity)getActivity()).showLoading(progressDialog, "Saving...").show();
        String URL = getString(R.string.URL_online)+"requisition_canvassing_approve.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    if (response.equals("1")){

                        JSONArray jsonArray = new JSONArray(new Gson().toJson(canvasTable_models));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject Obj = jsonArray.getJSONObject(i);
                            canvasTable_models.set(i,new CanvasTable_model(Obj.getString("id"),
                                    Obj.getString("supplier_id"),
                                    Obj.getString("price"),
                                    Obj.getString("date_added"),
                                    Obj.getString("remarks"),
                                    Obj.getString("inventory"),
                                    Obj.getString("consumed"),
                                    Obj.getString("pending"),
                                    "",
                                    Obj.getString("supplier")));

                            if(Obj.getString("id").equals(id)){
                                canvasTable_models.set(i,new CanvasTable_model(Obj.getString("id"),
                                        Obj.getString("supplier_id"),
                                        Obj.getString("price"),
                                        Obj.getString("date_added"),
                                        Obj.getString("remarks"),
                                        Obj.getString("inventory"),
                                        Obj.getString("consumed"),
                                        Obj.getString("pending"),
                                        "Approved",
                                        Obj.getString("supplier")));
                            }
                        }
                        rs_adapter.notifyDataSetChanged();

                        Toast.makeText(getActivity(), "Successfully save", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Failed to save", Toast.LENGTH_SHORT).show();
                    }
                    ((MainActivity)getActivity()).showLoading(progressDialog, "").dismiss();

                } catch (JSONException e){}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    ((MainActivity)getActivity()).showLoading(progressDialog, "").dismiss();
                    Toast.makeText(getActivity(), "Connection Error, please try again.", Toast.LENGTH_SHORT).show();
                } catch (Exception e){}
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("rs_canvas_id", id);
                hashMap.put("rs_detail_id", rs_details_id);
                //user
                hashMap.put("company_id", company_id);
                hashMap.put("category_id", category_id);
                hashMap.put("user_id", user_id);
                hashMap.put("company_code", company_code);
                hashMap.put("branch_id", selected_branch_id);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void dialogDelete(final String id){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setMessage("Are you sure you wan't to delete?");
        alertDialog.setNegativeButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        delete_canvas(id);
                    }
                });
        alertDialog.setPositiveButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public void delete_canvas(final String id){
        isAddEntry = true;
        ((MainActivity)getActivity()).showLoading(progressDialog, "Saving...").show();
        String URL = getString(R.string.URL_online)+"requisition_canvassing_delete.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //String test = response.replaceAll("\\D+","");
                if (response.equals("1")){
                    try{
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(canvasTable_models));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject Obj = jsonArray.getJSONObject(i);
                            if(Obj.getString("id").equals(id)){
                                Toast.makeText(getActivity(), id, Toast.LENGTH_SHORT).show();
                                canvasTable_models.remove(i);
                                rs_adapter.notifyDataSetChanged();
                            }
                        }

                    }catch (Exception e){}

                    Toast.makeText(getActivity(), "Successfully save", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Failed to save", Toast.LENGTH_SHORT).show();
                }
                ((MainActivity)getActivity()).showLoading(progressDialog, "").dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    ((MainActivity)getActivity()).showLoading(progressDialog, "").dismiss();
                    Toast.makeText(getActivity(), "Connection Error, please try again.", Toast.LENGTH_SHORT).show();
                } catch (Exception e){}
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("rs_canvas_id", id);
                hashMap.put("rs_detail_id", rs_details_id);
                //user
                hashMap.put("company_id", company_id);
                hashMap.put("category_id", category_id);
                hashMap.put("user_id", user_id);
                hashMap.put("company_code", company_code);
                hashMap.put("branch_id", selected_branch_id);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    @Override
    public void delete(String id) {
        dialogDelete(id);
    }

    @Override
    public void approve(String id) {
        approve_canvas(id);
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        intent_frag();
    }

    public void intent_frag() {
        Intent intent = new Intent(getContext(), Requisition_add_canvassing_main.class);
        getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, intent);
        // getFragmentManager().popBackStack();
    }
}
