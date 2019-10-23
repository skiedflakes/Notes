package com.wdysolutions.notes.Globals.Purchase_Order;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
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
import com.wdysolutions.notes.DatePicker.DatePickerCustom;
import com.wdysolutions.notes.DatePicker.DatePickerSelectionInterfaceCustom;
import com.wdysolutions.notes.Dialog_Action;
import com.wdysolutions.notes.Globals.Micro_Filming.Microfilming_main;
import com.wdysolutions.notes.Globals.Purchase_Order.purchase_order_dialog.Purchase_Order_dialog_main;
import com.wdysolutions.notes.MainActivity;
import com.wdysolutions.notes.R;
import com.wdysolutions.notes.SharedPref;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class Purchase_Order extends Fragment implements DatePickerSelectionInterfaceCustom,Purchase_Order_adapter.EventListener, Dialog_Action.uploadDialogInterface{

    //user
    SharedPref sharedPref;
    String user_id, company_id,company_code,category_id,selected_branch_id,user_name;

    //loading scan
    ProgressDialog loadingScan;

    //module
    LinearLayout btn_generate_report;
    TextView total_amount;
    double t_amount=0.00;

    //dates
    TextView btn_end_date, btn_start_date,tv_total;
    String start_date="",end_date="";
    Boolean isStart;

    //purchase order
    ArrayList<Purchase_Order_model> po_main_list = new ArrayList<>();
    Purchase_Order_adapter po_adapter;
    RecyclerView rec_po;

    //spinner
    Spinner spinner_filter,spinner_rr_status;
    String selectedFilter="",selected_rr_status="-1";


    //selected
    String selected_po_number;
    String selected_dialog_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.purchase_order, container, false);
        //user
        sharedPref = new SharedPref(getActivity());
        company_id = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYID);
        company_code = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYCODE);
        category_id = sharedPref.getUserInfo().get(sharedPref.KEY_CATEGORYID);
        selected_branch_id = Constants.branch_id;
        user_id = sharedPref.getUserInfo().get(sharedPref.KEY_USERID);
        user_name= sharedPref.getUserInfo().get(sharedPref.KEY_NAME);

        //loading scan
        loadingScan = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);

        //module
        btn_generate_report = view.findViewById(R.id.btn_generate_report);
        total_amount = view.findViewById(R.id.total_amount);

        //recycler
        rec_po = view.findViewById(R.id.rec_po);

        //spinner
        spinner_filter = view.findViewById(R.id.spinner_filter);
        spinner_rr_status = view.findViewById(R.id.spinner_rr_status);

        //date
        btn_start_date = view.findViewById(R.id.btn_start_date);
        btn_end_date = view.findViewById(R.id.btn_end_date);

        //buttons on click
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

        btn_generate_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                start_date = btn_start_date.getText().toString();
                end_date = btn_end_date.getText().toString();
                get_purchase_order_details("1");

            }
        });

        //filter
        ArrayAdapter<String> spinnerAdapter_filter = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_drop, populateFilter());
        spinnerAdapter_filter.setDropDownViewResource(R.layout.custom_spinner_drop);
        spinner_filter.setAdapter(spinnerAdapter_filter);
        spinner_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if (list_filter.get(position).equals("All")){
                    selectedFilter = "";
                } else if (list_filter.get(position).equals("Declared")){
                    selectedFilter = "d";
                } else if (list_filter.get(position).equals("Undeclared")){
                    selectedFilter = "ud";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

                    //filter
                    ArrayAdapter<String> spinnerAdapter_rr_status = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_drop, populateRR());
                    spinnerAdapter_filter.setDropDownViewResource(R.layout.custom_spinner_drop);
                    spinner_rr_status.setAdapter(spinnerAdapter_rr_status);
                    spinner_rr_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                            if (list_rr_status.get(position).equals("All")){
                                selected_rr_status = "-1";
                            } else if (list_rr_status.get(position).equals("Pending")){
                                selected_rr_status = "P";
                            } else if (list_rr_status.get(position).equals("Fully Served")){
                                selected_rr_status = "F";
                            }else if (list_rr_status.get(position).equals("Closed")){
                                selected_rr_status = "C";
                            }

                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
        get_purchase_order_details("0");
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

    public void get_purchase_order_details(final String update){
        po_main_list = new ArrayList<>();
        showLoading(loadingScan, "Loading...").show();
        String URL = getString(R.string.URL_online)+"purchase_order/get_header_details.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                t_amount=0.00;
                showLoading(loadingScan, null).dismiss();
                try{
                    JSONObject Object = new JSONObject(response);

                    //get current date
                    JSONArray date_diag = Object.getJSONArray("date");
                    JSONObject date_Obj = (JSONObject) date_diag.get(0);
                    String start_date = date_Obj.getString("start_date");
                    String end_date = date_Obj.getString("end_date");

                    btn_start_date.setText(start_date);
                    btn_end_date.setText(end_date);

                    JSONArray diag = Object.getJSONArray("data");
                    for (int i = 0; i < diag.length(); i++) {
                        JSONObject Obj = (JSONObject) diag.get(i);

                        String id = Obj.getString("id");
                        String count = Obj.getString("count");
                        String branch_id = Obj.getString("br_id");
                        String po_number = Obj.getString("po_number");
                        String date = Obj.getString("date");
                        String supplier = Obj.getString("supplier_id");
                        String remarks = Obj.getString("remarks");
                        String prod_category = Obj.getString("prod_category");
                        String po_stat = Obj.getString("po_stat");
                        String rr_status = Obj.getString("rr_status");
                        String total = Obj.getString("total");
                        String encodedBy = Obj.getString("encodedBy");
                        String dec_status = Obj.getString("dec_status");

                        //total amount
                        String sub_amount=total.replaceAll(",", "");
                        t_amount += Double.valueOf(sub_amount);

                        //colors
                        String po_stat_color = Obj.getString("po_stat_color");
                        String rr_status_color = Obj.getString("rr_status_color");
                        String dec_status_color = Obj.getString("dec_status_color");

                        //checked by or approved by
                        String checked_by = Obj.getString("checked_by");
                        String approved_by = Obj.getString("approved_by");

                        po_main_list.add(new Purchase_Order_model(id,count,branch_id,po_number,date,
                                supplier,remarks,prod_category,po_stat,rr_status,total,
                                encodedBy,dec_status,po_stat_color,rr_status_color,dec_status_color,approved_by,checked_by));

                    }
                    //total amount
                    NumberFormat formatter = new DecimalFormat("###,###,###.00");
                    String s_total_amount = formatter.format(t_amount);
                    total_amount.setText("P "+s_total_amount);


                    po_adapter = new Purchase_Order_adapter(getContext(), po_main_list,  Purchase_Order.this);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    rec_po.setLayoutManager(layoutManager);
                    rec_po.setAdapter(po_adapter);
                    rec_po.setNestedScrollingEnabled(false);
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
                hashMap.put("filter_status", selectedFilter);
                hashMap.put("rr_status", selected_rr_status);
                hashMap.put("end_date", end_date);
                hashMap.put("start_date", start_date);
                hashMap.put("update", update);

                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void approve_po(final String user_id,final int position){
        showLoading(loadingScan, "Loading...").show();
        String URL = getString(R.string.URL_online)+"purchase_order/approve_po.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    showLoading(loadingScan, null).dismiss();
                    if(response.equals("1")){

                            Purchase_Order_model po_model_list = po_main_list.get(position);
                            String id = po_model_list.getId();
                            String count = po_model_list.getCount();
                            String branch_id = po_model_list.getBr_id();
                            String po_number = po_model_list.getPurchase_num();
                            String date = po_model_list.getDate();
                            String supplier = po_model_list.getSupplier();
                            String remarks = po_model_list.getRemarks();
                            String prod_category =po_model_list.getCategory();
                            String po_stat = po_model_list.getPo_status();
                            String rr_status = po_model_list.getRr_status();
                            String total = po_model_list.getUnrecieved_total();
                            String encodedBy = po_model_list.getEncoded_by();
                            String dec_status =po_model_list.getDeclared_status();
                            //colors
                            String po_stat_color = po_model_list.getPo_status_color();
                            String rr_status_color = po_model_list.getRr_status_color();
                            String dec_status_color = po_model_list.getDec_status_color();

                            //checked by or approved by
                            String checked_by = po_model_list.getChecked_by();
                            String approved_by = po_model_list.getApproved_by();
                         //   po_main_list.remove(i);
                        Purchase_Order_model newval;
                        if(user_id.equals("")){
                             newval = new Purchase_Order_model(id,count,branch_id,po_number,date,
                                    supplier,remarks,prod_category,po_stat,rr_status,total,
                                    encodedBy,dec_status,po_stat_color,rr_status_color,dec_status_color,"",checked_by);

                            Toast.makeText(getContext(), "Disapprove success!", Toast.LENGTH_SHORT).show();
                        }else{
                            newval = new Purchase_Order_model(id,count,branch_id,po_number,date,
                                    supplier,remarks,prod_category,po_stat,rr_status,total,
                                    encodedBy,dec_status,po_stat_color,rr_status_color,dec_status_color,user_name,checked_by);

                            Toast.makeText(getContext(), "Approve success!", Toast.LENGTH_SHORT).show();
                        }

                            po_main_list.set(position,newval);
                            po_adapter.notifyItemChanged(position);

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
                hashMap.put("po_number", selected_po_number);

                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


    List<String> list_filter;
    private List<String> populateFilter(){
        list_filter = new ArrayList<>();

        list_filter.add("All");
        list_filter.add("Undeclared");
        list_filter.add("Declared");

        return list_filter;
    }

    List<String> list_rr_status;
    private List<String> populateRR(){
        list_rr_status = new ArrayList<>();
        list_rr_status.add("All");
        list_rr_status.add("Pending");
        list_rr_status.add("Fully Served");
        list_rr_status.add("Closed");
        return list_rr_status;
    }

    @Override
    public void senddata(String chosen,final int position) {

        if(chosen.equals("view_details")){
            open_details();
        }else if(chosen.equals("approve")){
            openDialog_approve(user_id,true,position);
        }else if(chosen.equals("disapprove")){
            //
            openDialog_approve("",false,position);
        }else if(chosen.equals("micro_filming")){

            ((MainActivity)getActivity()).openMicro(selected_po_number);
        }
    }

    public void openDialog_approve(final String user_id,final boolean type,final int position){
        if(type){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            alertDialog.setTitle("Are you sure you want to approve?");
            alertDialog.setPositiveButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog.setNegativeButton("OK",  new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    approve_po(user_id,position);
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
                    approve_po(user_id,position);
                }
            });
            alertDialog.setCancelable(false);
            alertDialog.show();
        }

    }

    public void open_details(){

        Bundle bundle = new Bundle();
        bundle.putString("id", selected_dialog_id);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("details");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        Purchase_Order_dialog_main fragment = new Purchase_Order_dialog_main();
        fragment.setTargetFragment(Purchase_Order.this, 0);
        FragmentManager manager = getFragmentManager();
        fragment.setArguments(bundle);
        fragment.show(getFragmentManager(), "details");
        fragment.setCancelable(true);
    }

    @Override
    public void onDateSelected(String date) {
        if(isStart){
            btn_end_date.setText(date);

        }else{
            btn_start_date.setText(date);
        }
    }

    private ProgressDialog showLoading(ProgressDialog loading, String msg){
        loading.setMessage(msg);
        loading.setCancelable(false);
        return loading;
    }

    @Override
    public void view_modal(final int position,String po_number,String id,int view_details, int micro_filming, int approve,int disapprove) {
        selected_dialog_id=id;
        selected_po_number = po_number;
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
        fragment.setTargetFragment(Purchase_Order.this, 100);
        FragmentManager manager = getFragmentManager();
        fragment.setArguments(bundle);
        fragment.show(ft, "UploadDialogFragment");
        fragment.setCancelable(true);

    }
}
