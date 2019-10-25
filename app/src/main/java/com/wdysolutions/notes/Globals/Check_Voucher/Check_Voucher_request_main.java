package com.wdysolutions.notes.Globals.Check_Voucher;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
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
import com.wdysolutions.notes.Globals.Cash_Voucher.Cash_Voucher_dialog.Cash_Voucher_request_dialog;
import com.wdysolutions.notes.Globals.Cash_Voucher.Cash_Voucher_request_main;
import com.wdysolutions.notes.Globals.Purchase_Order.Purchase_Order;
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


public class Check_Voucher_request_main extends Fragment implements DatePickerSelectionInterfaceCustom, Check_Voucher_request_adapter.EventListener,Dialog_Action.uploadDialogInterface{
    String user_id, company_id,company_code,category_id,selected_branch_id,user_name;
    String start_date="",end_date="";
    SharedPref sharedPref;
    RecyclerView rec_cv;
    Boolean isStart;
    TextView btn_end_date, btn_start_date,tv_total_amount;
    LinearLayout btn_generate_report;
    double t_amount=0.00;

    ArrayList<Check_Voucher_request_model> cv_main_list = new ArrayList<>();
    Check_Voucher_request_adapter cv_adapter;
    ProgressDialog loadingScan;

    String selected_cv_num,selected_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.check_voucher_request_main, container, false);
        //user
        sharedPref = new SharedPref(getActivity());
        company_id = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYID);
        company_code = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYCODE);
        category_id = sharedPref.getUserInfo().get(sharedPref.KEY_CATEGORYID);
        user_name= sharedPref.getUserInfo().get(sharedPref.KEY_NAME);
        selected_branch_id = Constants.branch_id;
        user_id = sharedPref.getUserInfo().get(sharedPref.KEY_USERID);

        rec_cv = view.findViewById(R.id.rec_cv);

        btn_generate_report= view.findViewById(R.id.btn_generate_report);

        loadingScan = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);

        tv_total_amount = view.findViewById(R.id.tv_total_amount);
        btn_start_date = view.findViewById(R.id.btn_start_date);
        btn_end_date = view.findViewById(R.id.btn_end_date);

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
                cv_main_list.clear();
                cv_adapter.notifyDataSetChanged();
                start_date = btn_start_date.getText().toString();
                end_date = btn_end_date.getText().toString();
                get_check_voucher("1");
            }
        });


        //current month
        get_check_voucher("0");

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
    public void get_check_voucher(final String update){
        cv_main_list = new ArrayList<Check_Voucher_request_model>();
        showLoading(loadingScan, "Loading...").show();

        String URL = getString(R.string.URL_online)+"check_voucher/get_header_details.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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

                    //get current date
                    JSONArray diag = Object.getJSONArray("data");
                    for (int i = 0; i < diag.length(); i++) {
                        JSONObject Obj = (JSONObject) diag.get(i);
                        String id = Obj.getString("id");
                        String count = Obj.getString("count");
                        String cv_number = Obj.getString("cv_number");
                        String bank = Obj.getString("bank");
                        String check_number = Obj.getString("check_number");
                        String account = Obj.getString("account");
                        String issued_date = Obj.getString("issued_date");
                        String check_date = Obj.getString("check_date");
                        String remarks = Obj.getString("remarks");
                        String amount = Obj.getString("amount");
                        String status = Obj.getString("status");
                        String stat = Obj.getString("stat");
                        String encoded_byID = Obj.getString("encoded_byID");
                        String cv_type = Obj.getString("cv_type");
                        String br = Obj.getString("br");
                        String approved_by = Obj.getString("approved_by");

                        cv_main_list.add(new Check_Voucher_request_model(
                                id,cv_number,count,bank,
                                check_number,account,issued_date,check_date,
                                remarks,amount,status,stat,
                                encoded_byID,cv_type,br,approved_by));

                        //total amount
                        String sub_amount=amount.replaceAll(",", "");
                        t_amount += Double.valueOf(sub_amount);

                    }

                    NumberFormat formatter = new DecimalFormat("###,###,###.00");
                    String s_total_amount = formatter.format(t_amount);
                    tv_total_amount.setText("P "+s_total_amount);

                    cv_adapter = new Check_Voucher_request_adapter(getContext(), cv_main_list,  Check_Voucher_request_main.this);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    rec_cv.setLayoutManager(layoutManager);
                    rec_cv.setAdapter(cv_adapter);
                    rec_cv.setNestedScrollingEnabled(false);

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
//                hashMap.put("company_id", company_id);
                //user
                hashMap.put("company_id", company_id);
                hashMap.put("category_id", category_id);
                hashMap.put("user_id", user_id);
                hashMap.put("company_code", company_code);
                hashMap.put("branch_id", selected_branch_id);


                hashMap.put("select_val", "");
                hashMap.put("end_date", end_date);
                hashMap.put("start_date", start_date);
                hashMap.put("update", update);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
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


    public void  view_details() {

        Bundle bundle = new Bundle();
        bundle.putString("id", selected_id);
        bundle.putString("cv_num", selected_cv_num);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("details");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        Cash_Voucher_request_dialog fragment = new Cash_Voucher_request_dialog();
        fragment.setTargetFragment(Check_Voucher_request_main.this, 0);
        FragmentManager manager = getFragmentManager();
        fragment.setArguments(bundle);
        fragment.show(getFragmentManager(), "details");
        fragment.setCancelable(true);

    }

    @Override
    public void view_modal(int position, String cv_num, String id, int view_details, int micro_filming, int approve, int disapprove) {

        selected_id = id;
        selected_cv_num = cv_num;

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
        fragment.setTargetFragment(Check_Voucher_request_main.this, 100);
        FragmentManager manager = getFragmentManager();
        fragment.setArguments(bundle);
        fragment.show(ft, "UploadDialogFragment");
        fragment.setCancelable(true);

    }

    @Override
    public void senddata(String chosen, int position) {

        if(chosen.equals("view_details")){
            view_details();
        }else if(chosen.equals("approve")){
         openDialog_approve(user_id,true,position);
        }else if(chosen.equals("disapprove")){
            //
          openDialog_approve("",false,position);
        }else if(chosen.equals("micro_filming")){
            //
            ((MainActivity)getActivity()).openMicro(selected_cv_num, "", "");
        }
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

    public void approve_po(final String user_id,final int position){

        showLoading(loadingScan, "Loading...").show();
        String URL = getString(R.string.URL_online)+"check_voucher/approve_check_voucher.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    showLoading(loadingScan, null).dismiss();
                    if(response.equals("1")){

                        Check_Voucher_request_model main_list = cv_main_list.get(position);
                        String id = main_list.getId();
                        String count = main_list.getCount();
                        String cv_number= main_list.getCv_number();
                        String bank = main_list.getBank();
                        String check_number = main_list.getCheck_number();
                        String account = main_list.getAccount();
                        String issued_date = main_list.getIssued_date();
                        String check_date =main_list.getCheck_date();
                        String remarks = main_list.getRemarks();
                        String amount = main_list.getAmount();
                        String status = main_list.getStatus();
                        String stat = main_list.getStat();
                        String encoded_by =main_list.getEncoded_by();
                        //colors
                        String cv_type = main_list.getCv_type();
                        String br = main_list.getBr();
                        String approved_by = main_list.getApproved_by();

                        Check_Voucher_request_model newval;

                        if(user_id.equals("")){
                            newval = new Check_Voucher_request_model(
                                    id,cv_number,count,bank,
                                    check_number,account,issued_date,check_date,
                                    remarks,amount,status,stat,
                                    encoded_by,cv_type,br,"");
                            Toast.makeText(getContext(), "Disapprove success!", Toast.LENGTH_SHORT).show();
                        }else{
                            newval = new Check_Voucher_request_model(
                                    id,cv_number,count,bank,
                                    check_number,account,issued_date,check_date,
                                    remarks,amount,status,stat,
                                    encoded_by,cv_type,br,user_name);
                            Toast.makeText(getContext(), "Approve success!", Toast.LENGTH_SHORT).show();
                        }

                        cv_main_list.set(position,newval);
                        cv_adapter.notifyItemChanged(position);

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
                hashMap.put("cv_number", selected_cv_num);

                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
