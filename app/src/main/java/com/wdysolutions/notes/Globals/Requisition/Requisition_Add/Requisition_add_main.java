package com.wdysolutions.notes.Globals.Requisition.Requisition_Add;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.wdysolutions.notes.DatePicker.DatePickerCustom;
import com.wdysolutions.notes.DatePicker.DatePickerSelectionInterfaceCustom;
import com.wdysolutions.notes.Globals.Requisition.Requisition_Add.Canvassing.Requisition_add_canvassing_main;
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


public class Requisition_add_main extends Fragment implements DatePickerSelectionInterfaceCustom,Requisition_add_adapter.EventListener {

    TextView requi_num, txt_date, txt_delivery, txt_encoded, tv_title, txt_error;
    EditText editText_remarks, editText_qty, editText_descript;
    CheckBox chckBox_requisition, chckBox_purchaseflock;
    Button btn_add, btn_add_item, btn_conso, btn_delete;
    Spinner spinner_classi, spinner_type, spinner_gender, spinner_packaging, spinner_requested, spinner_item;
    SharedPref sharedPref;
    LinearLayout layout_spinner_gender, layout_flock, layout_requisition_details, layout_rs_details;

    String date, selectedDate = "", req_stat = "", selectedProduct = "", selectedPackage = "", selectEmployee = "";
    RecyclerView recyclerView;
    CheckBox cb_all;
    boolean isAssetReq = false;
    ProgressDialog progressDialog;
    ArrayList<Product_model> product_models = new ArrayList<>();
    ArrayList<Package_model> package_models = new ArrayList<>();
    ArrayList<Employee_model> employee_models = new ArrayList<>();

    //string bundle
    String add_edit, rs_id, remarks, rs_number, asset, requested_by, status, check_status;

    JSONObject json_spinner;
    ScrollView scroll_details;
    ProgressBar loading_layout;

    //table
    Requisition_add_adapter req_adapter;
    ArrayList<Requisition_add_model> rs_add_list = new ArrayList<>();
    ArrayList<String> checked_ids = new ArrayList<>();

    int counter_delete = 0, success_delete = 0;
    //user session
    String user_id, user_name, company_id, company_code, category_id, selected_branch_id, selected_branch_name;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.requisition_add_main, container, false);

        //user
        sharedPref = new SharedPref(getActivity());
        company_id = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYID);
        company_code = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYCODE);
        category_id = sharedPref.getUserInfo().get(sharedPref.KEY_CATEGORYID);
        selected_branch_id = Constants.branch_id;
        selected_branch_name = Constants.branch;
        user_id = sharedPref.getUserInfo().get(sharedPref.KEY_USERID);
        user_name = sharedPref.getUserInfo().get(sharedPref.KEY_NAME);

        progressDialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
        chckBox_purchaseflock = view.findViewById(R.id.chckBox_purchaseflock);
        requi_num = view.findViewById(R.id.requi_num);
        txt_date = view.findViewById(R.id.txt_date);
        editText_remarks = view.findViewById(R.id.editText_remarks);
        txt_delivery = view.findViewById(R.id.txt_delivery);
        txt_encoded = view.findViewById(R.id.txt_encoded);
        chckBox_requisition = view.findViewById(R.id.chckBox_requisition);
        btn_add = view.findViewById(R.id.btn_add);
        spinner_gender = view.findViewById(R.id.spinner_gender);
        spinner_type = view.findViewById(R.id.spinner_type);
        spinner_classi = view.findViewById(R.id.spinner_classi);
        layout_spinner_gender = view.findViewById(R.id.layout_spinner_gender);
        layout_flock = view.findViewById(R.id.layout_flock);
        layout_requisition_details = view.findViewById(R.id.layout_requisition_details);
        layout_rs_details = view.findViewById(R.id.layout_rs_details);
        spinner_item = view.findViewById(R.id.spinner_item);
        editText_qty = view.findViewById(R.id.editText_qty);
        spinner_packaging = view.findViewById(R.id.spinner_packaging);
        spinner_requested = view.findViewById(R.id.spinner_requested);
        editText_descript = view.findViewById(R.id.editText_descript);
        btn_add_item = view.findViewById(R.id.btn_add_item);
        btn_conso = view.findViewById(R.id.btn_conso);
        btn_delete = view.findViewById(R.id.btn_delete);
        cb_all = view.findViewById(R.id.cb_all);
        recyclerView = view.findViewById(R.id.recyclerView);
        tv_title = view.findViewById(R.id.tv_title);
        scroll_details = view.findViewById(R.id.scroll_details);
        loading_layout = view.findViewById(R.id.loading_layout);
        txt_error = view.findViewById(R.id.txt_error);

        cb_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb_all.isChecked()) {
                    check_all();
                } else {
                    uncheck_all();
                }
            }
        });

        chckBox_purchaseflock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chckBox_purchaseflock.isChecked()) {
                    chckBox_requisition.setChecked(false);
                    layout_flock.setVisibility(View.VISIBLE);
                    req_stat = "flock";
                } else {
                    layout_flock.setVisibility(View.GONE);
                    req_stat = "";
                }
            }
        });

        chckBox_requisition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chckBox_requisition.isChecked()) {
                    chckBox_purchaseflock.setChecked(false);
                    layout_flock.setVisibility(View.GONE);
                    req_stat = "asset";
                } else {
                    req_stat = "";
                }
            }
        });

        txt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker(false);
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedDate.equals("")) {
                    Toast.makeText(getActivity(), "Please select date", Toast.LENGTH_SHORT).show();
                } else {
                    saveRequi();
                }
            }
        });

        btn_add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedProduct.equals("")) {
                    Toast.makeText(getActivity(), "Please select product", Toast.LENGTH_SHORT).show();
                } else if (editText_qty.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter qty", Toast.LENGTH_SHORT).show();
                } else if (selectedPackage.equals("")) {
                    Toast.makeText(getActivity(), "Please select package", Toast.LENGTH_SHORT).show();
                } else if (selectEmployee.equals("")) {
                    Toast.makeText(getActivity(), "Please select employee", Toast.LENGTH_SHORT).show();
                } else {
                    addItem();
                }
            }
        });

        btn_conso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish_requisition();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove_selected_rs();
            }
        });

        //bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            add_edit = bundle.getString("add_edit");
            if (add_edit.equals("add")) {

                init_add_edit(add_edit);
                getDetails("add");
            } else {
                tv_title.setText("Requisition Details");
                rs_id = bundle.getString("rs_id");
                remarks = bundle.getString("remarks");
                rs_number = bundle.getString("rs_number");
                selectedDate = bundle.getString("date_added");

                requested_by = bundle.getString("requested_by");
                status = bundle.getString("status");
                check_status = bundle.getString("check_status");
                asset = bundle.getString("asset");

                init_add_edit(add_edit);
                getDetails("edit");
            }

        } else {
            Toast.makeText(getContext(), "error loading", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private boolean setCheckRequisition(){
        if (asset.equals("Yes")){
            return true;
        } else {
            return false;
        }
    }

    public void init_add_edit(String add_edit) {
        if (add_edit.equals("add")) {

        } else { // if edit
            requi_num.setText(rs_number);
            txt_date.setText(selectedDate);
            editText_remarks.setText(remarks);
            txt_delivery.setText(selected_branch_name);
            txt_encoded.setText(requested_by);
            chckBox_requisition.setChecked(setCheckRequisition());
            disableForm("edit");
            ((MainActivity) getActivity()).showLoading(progressDialog, "Loading...").show();
            get_table();
        }
    }

    public void getDetails(final String add_edit) {
        try {
            loading_layout.setVisibility(View.VISIBLE);
            scroll_details.setVisibility(View.GONE);
            txt_error.setVisibility(View.GONE);
            String URL = getString(R.string.URL_online) + "requisition_details.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        //((MainActivity)getActivity()).openDialog(response);
                        loading_layout.setVisibility(View.GONE);
                        scroll_details.setVisibility(View.VISIBLE);
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("rs_number");
                        JSONObject object = (JSONObject) jsonArray.get(0);

                        if (add_edit.equals("add")) {
                            rs_number = object.getString("rs_num");
                            date = object.getString("date");
                            requi_num.setText(rs_number);
                            txt_encoded.setText(user_name);
                            txt_delivery.setText(selected_branch_name);
                        }
                        json_spinner = jsonObject;
                        initSpinner(jsonObject);

                    } catch (JSONException e) {
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loading_layout.setVisibility(View.GONE);
                    scroll_details.setVisibility(View.GONE);
                    txt_error.setVisibility(View.VISIBLE);
                    txt_error.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getDetails(add_edit);
                        }
                    });
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> hashMap = new HashMap<>();
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
        } catch (Exception e) {
        }
    }

    private void initSpinner(JSONObject jsonObject) {
        try {
            //////////////////////////////////////// PACKAGING /////////////////////////
            JSONArray jsonArray_ = jsonObject.getJSONArray("product_array");
            product_models.add(new Product_model("00", "Select"));
            for (int i = 0; i < jsonArray_.length(); i++) {
                JSONObject jsonObject1 = (JSONObject) jsonArray_.get(i);
                product_models.add(new Product_model(jsonObject1.getString("product_id"),
                        jsonObject1.getString("product")));
            }

            // Populate Spinner
            List<String> lables = new ArrayList<>();
            for (int i = 0; i < product_models.size(); i++) {
                lables.add(product_models.get(i).getProduct());
            }
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_drop, lables);
            spinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_drop);
            spinner_item.setAdapter(spinnerAdapter);
            spinner_item.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    Product_model click = product_models.get(position);
                    if (!click.getProduct().equals("Select")) {
                        selectedProduct = click.getProduct_id();
                        getPackagingSpinner(selectedProduct);
                    } else {
                        selectedProduct = "";
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });


            //////////////////////////////////////// EMPLOYEE //////////////////////////
            JSONArray jsonArray__ = jsonObject.getJSONArray("employee_array");
            employee_models.add(new Employee_model("00", "Please select"));
            for (int i = 0; i < jsonArray__.length(); i++) {
                JSONObject jsonObject11 = (JSONObject) jsonArray__.get(i);
                employee_models.add(new Employee_model(jsonObject11.getString("employee_id"), jsonObject11.getString("emp_lastname")));
            }

            // Populate Spinner
            List<String> lables_ = new ArrayList<>();
            for (int i = 0; i < employee_models.size(); i++) {
                lables_.add(employee_models.get(i).getEmp_lastname());
            }
            ArrayAdapter<String> spinnerAdapter_ = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_drop, lables_);
            spinnerAdapter_.setDropDownViewResource(R.layout.custom_spinner_drop);
            spinner_requested.setAdapter(spinnerAdapter_);
            spinner_requested.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    Employee_model click = employee_models.get(position);
                    if (!click.getEmp_lastname().equals("Please select")) {
                        selectEmployee = click.getEmployee_id();
                    } else {
                        selectEmployee = "";
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

        } catch (JSONException e) {
        }
    }

    public void getPackagingSpinner(final String product_id) {
        try {
            String URL = getString(R.string.URL_online) + "packagingType.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
//                        ((MainActivity) getActivity()).openDialog(product_id);
                        package_models.clear();
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray_ = jsonObject.getJSONArray("packaging_array");
                        package_models.add(new Package_model("00", "Please select"));
                        for (int i = 0; i < jsonArray_.length(); i++) {
                            JSONObject jsonObject1 = (JSONObject) jsonArray_.get(i);
                            package_models.add(new Package_model(jsonObject1.getString("package_id"), jsonObject1.getString("package_name")));
                        }

                        // Populate Spinner
                        List<String> lables = new ArrayList<>();
                        for (int i = 0; i < package_models.size(); i++) {
                            lables.add(package_models.get(i).getPackage_name());
                        }
                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_drop, lables);
                        spinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_drop);
                        spinner_packaging.setAdapter(spinnerAdapter);
                        spinner_packaging.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                Package_model click = package_models.get(position);
                                if (!click.getPackage_name().equals("Select")) {
                                    selectedPackage = click.getPackage_id();
                                } else {
                                    selectedPackage = "";
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });
                    } catch (JSONException e) {
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "Connection Error, please try again.", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> hashMap = new HashMap<>();
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
        } catch (Exception e) {
        }
    }

    public void saveRequi() {
        try {
            btn_add.setEnabled(false);
            ((MainActivity) getActivity()).showLoading(progressDialog, "Saving...").show();
            String URL = getString(R.string.URL_online) + "requisition_add.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    btn_add.setEnabled(true);
                    if (response.equals("1")) {
                        layout_requisition_details.setVisibility(View.VISIBLE);
                        disableForm("add");
                    } else {
                        Toast.makeText(getActivity(), "Failed to save", Toast.LENGTH_SHORT).show();
                    }
                    ((MainActivity) getActivity()).showLoading(progressDialog, "").dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    btn_add.setEnabled(true);
                    ((MainActivity) getActivity()).showLoading(progressDialog, "").dismiss();
                    Toast.makeText(getActivity(), "Connection Error, please try again.", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("rs_number", requi_num.getText().toString());
                    hashMap.put("date_added", selectedDate);
                    hashMap.put("general_remarks", editText_remarks.getText().toString());
                    hashMap.put("req_stat", req_stat);

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
        } catch (Exception e) {
        }
    }

    private void disableForm(String add_edit) {
        btn_add.setVisibility(View.GONE);
        editText_remarks.setEnabled(false);
        chckBox_requisition.setEnabled(false);
        chckBox_purchaseflock.setEnabled(false);
        requi_num.setEnabled(false);
        txt_delivery.setEnabled(false);
        txt_encoded.setEnabled(false);
        txt_date.setEnabled(false);

        if (add_edit.equals("add")) {

        } else {
            layout_requisition_details.setVisibility(View.VISIBLE);
            layout_rs_details.setVisibility(View.VISIBLE);
        }

    }

    public void addItem() {
        try {
            btn_add_item.setEnabled(false);
            ((MainActivity) getActivity()).showLoading(progressDialog, "Saving...").show();
            String URL = getString(R.string.URL_online) + "requisition_add_details.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    btn_add_item.setEnabled(true);

                    if (response.equals("1")) {

                        spinner_packaging.setAdapter(null);
                        initSpinner(json_spinner);
                        editText_qty.setText("");

                        if (rs_add_list.size() > 0) {
                            rs_add_list.clear();
                            req_adapter.notifyDataSetChanged();

                            get_table();
                        } else {
                            get_table();
                        }

                        layout_rs_details.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getActivity(), "Failed to add", Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    btn_add_item.setEnabled(true);
                    ((MainActivity) getActivity()).showLoading(progressDialog, "").dismiss();
                    Toast.makeText(getActivity(), "Connection Error, please try again.", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> hashMap = new HashMap<>();

                    hashMap.put("rs_number", rs_number);
                    hashMap.put("product_id", selectedProduct);
                    hashMap.put("qty", editText_qty.getText().toString());
                    hashMap.put("date_needed", selectedDate);
                    hashMap.put("purpose", "");
                    hashMap.put("needed_by", selectEmployee);
                    hashMap.put("description", editText_descript.getText().toString());
                    hashMap.put("packaging_id", selectedPackage);

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
        } catch (Exception e) {
        }
    }

    public void finish_requisition() {
        rs_add_list = new ArrayList<>();
        try {
            String URL = getString(R.string.URL_online) + "requisition_finish.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if (response.equals("1")) {
                        FragmentManager fm = getFragmentManager();
                        if (fm.getBackStackEntryCount() > 0) {
                            fm.popBackStack();
                        }
                        Toast.makeText(getActivity(), "Successfully finished requisition!", Toast.LENGTH_SHORT).show();

                    } else if (response.equals("2")) {
                        Toast.makeText(getActivity(), "Note: Approved items are ready for PO.", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getActivity(), "Cannot Finish! No Requisition Details Found!", Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "Connection Error, please try again.", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("rs_number", rs_number);

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
        } catch (Exception e) {
        }
    }

    public void openDatePicker(boolean isMinusDays21) {
        DatePickerCustom datePickerFragment = new DatePickerCustom();

        String[] maxDate = date.split(" ");

        Bundle bundle = new Bundle();
        bundle.putString("maxDate", maxDate[0]);
        bundle.putString("maxDate_minus", "");
        bundle.putBoolean("isMinusDays", isMinusDays21);
        datePickerFragment.setArguments(bundle);

        datePickerFragment.delegate = this;
        datePickerFragment.setCancelable(false);
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSelected(String date) {
        selectedDate = date;
        txt_date.setText(selectedDate);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        intent_frag();
    }

    public void intent_frag() {
        Intent intent = new Intent(getContext(), Requisition_add_main.class);
        getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, intent);
        // getFragmentManager().popBackStack();
    }

    public void get_table() {
        rs_add_list = new ArrayList<>();
        try {
            String URL = getString(R.string.URL_online) + "requisition_add_table.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //  ((MainActivity)getActivity()).openDialog(response);
                    try {
                        ((MainActivity) getActivity()).showLoading(progressDialog, "").dismiss();
                        JSONObject Object = new JSONObject(response);
                        JSONArray diag = Object.getJSONArray("data");
                        for (int i = 0; i < diag.length(); i++) {
                            JSONObject Obj = (JSONObject) diag.get(i);
                            String id = Obj.getString("id");
                            String count = Obj.getString("count");
                            String canvas_status = Obj.getString("canvas_status");
                            String product = Obj.getString("product");
                            String quantity = Obj.getString("quantity");
                            String date_needed = Obj.getString("date_needed");
                            String needed_by = Obj.getString("needed_by");
                            String purpose = Obj.getString("purpose");
                            String description = Obj.getString("description");
                            String supplier_id = Obj.getString("supplier_id");
                            String supplier_price = Obj.getString("supplier_price");
                            String package_name = Obj.getString("package_name");
                            String subtotal = Obj.getString("subtotal");
                            String status = Obj.getString("status");
                            String type = Obj.getString("type");
                            String product_id = Obj.getString("product_id");

                            if (supplier_id.equals("null")) {
                                supplier_id = "";
                            }

                            rs_add_list.add(new Requisition_add_model(id, count, canvas_status, product, quantity,
                                    date_needed, needed_by, purpose, description,
                                    supplier_id, supplier_price, package_name, subtotal,
                                    status, type, 0, product_id));
                        }

                        req_adapter = new Requisition_add_adapter(getContext(), rs_add_list, Requisition_add_main.this);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(req_adapter);
                        recyclerView.setNestedScrollingEnabled(false);

                    } catch (JSONException e) {
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "Connection Error, please try again.", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("rs_number", rs_number);
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
        } catch (Exception e) {
        }
    }

    @Override
    public void onChecked(String id) {
        checked_ids.add(id);
    }

    @Override
    public void removeChecked(String id) {
        checked_ids.remove(id);
    }

    @Override
    public void open_canvassing(String id, String product, String package_name, String qty, String product_id) {
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("product", product);
        bundle.putString("product_id", product_id);
        bundle.putString("package_name", package_name);
        bundle.putString("selectedDate", selectedDate);
        bundle.putString("qty", qty);

        DialogFragment selectedDialogFragment = new Requisition_add_canvassing_main();
        selectedDialogFragment.setTargetFragment(Requisition_add_main.this, Constants.Requisition_add);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        selectedDialogFragment.setArguments(bundle);
        selectedDialogFragment.show(ft, "dialog");
    }

    public void remove_selected_rs() {
        counter_delete = checked_ids.size();
        success_delete = 0;
        if (checked_ids.size() > 0) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setMessage("Are you sure you wan't to delete?");
            alertDialog.setNegativeButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                            ((MainActivity) getActivity()).showLoading(progressDialog, "Deleting...").show();

                            for (int i = 0; i < checked_ids.size(); i++) {
                                delete_rs(checked_ids.get(i));
                            }
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

        } else {
            ((MainActivity) getActivity()).showLoading(progressDialog, "").dismiss();
            Toast.makeText(getActivity(), "Please select swine sales to delete", Toast.LENGTH_SHORT).show();
        }
    }


    public void delete_rs(final String rs_id) {
        String URL = getString(R.string.URL_online) + "requisition_add_delete.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("1")) {
                    String dr_id;
                    try {
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(rs_add_list));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject r = jsonArray.getJSONObject(i);
                            dr_id = r.getString("id");
                            if (dr_id.equals(rs_id)) {
                                success_delete++;
                                checked_ids.remove(rs_id);
                                rs_add_list.remove(i);
                                req_adapter.notifyDataSetChanged();
                            }
                        }

                        if (counter_delete == success_delete) {
                            //  btn_remove.setEnabled(true);
                            ((MainActivity) getActivity()).showLoading(progressDialog, "").dismiss();
                            Toast.makeText(getActivity(), "delete success", Toast.LENGTH_SHORT).show();
                            cb_all.setChecked(false);
                        }
                    } catch (JSONException e) {
                    }

                } else {
                    ((MainActivity) getActivity()).showLoading(progressDialog, "").dismiss();
//                    btn_remove.setEnabled(true);
                    Toast.makeText(getActivity(), "some dr sales failed to delete", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((MainActivity) getActivity()).showLoading(progressDialog, "").dismiss();
                Toast.makeText(getActivity(), "some dr sales failed to delete", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("company_code", company_code);
                hashMap.put("branch_id", Constants.branch_id);
                hashMap.put("company_id", company_id);
                hashMap.put("rs_id", rs_id);
                hashMap.put("user_id", user_id);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void check_all() {
        checked_ids.clear();
        try {
            JSONArray jsonArray = new JSONArray(new Gson().toJson(rs_add_list));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject r = jsonArray.getJSONObject(i);
                String id = r.getString("id");
                String count = r.getString("count");
                String canvas_status = r.getString("canvas_status");
                String product = r.getString("product");
                String quantity = r.getString("quantity");
                String date_needed = r.getString("date_needed");
                String needed_by = r.getString("needed_by");
                String purpose = r.getString("purpose");
                String description = r.getString("description");
                String supplier_id = r.getString("supplier_id");
                String supplier_price = r.getString("supplier_price");
                String package_name = r.getString("package_name");
                String subtotal = r.getString("subtotal");
                String status = r.getString("status");
                String type = r.getString("type");
                String product_id = r.getString("product_id");

                rs_add_list.set(i, new Requisition_add_model(id, count, canvas_status, product, quantity,
                        date_needed, needed_by, purpose, description,
                        supplier_id, supplier_price, package_name, subtotal,
                        status, type, 1, product_id));
                checked_ids.add(id);

            }
            req_adapter.notifyDataSetChanged();
        } catch (Exception e) {
        }
    }


    public void uncheck_all() {
        checked_ids.clear();
        try {
            JSONArray jsonArray = new JSONArray(new Gson().toJson(rs_add_list));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject r = jsonArray.getJSONObject(i);
                String id = r.getString("id");
                String count = r.getString("count");
                String canvas_status = r.getString("canvas_status");
                String product = r.getString("product");
                String quantity = r.getString("quantity");
                String date_needed = r.getString("date_needed");
                String needed_by = r.getString("needed_by");
                String purpose = r.getString("purpose");
                String description = r.getString("description");
                String supplier_id = r.getString("supplier_id");
                String supplier_price = r.getString("supplier_price");
                String package_name = r.getString("package_name");
                String subtotal = r.getString("subtotal");
                String status = r.getString("status");
                String type = r.getString("type");
                String product_id = r.getString("product_id");

                rs_add_list.set(i, new Requisition_add_model(id, count, canvas_status, product, quantity,
                        date_needed, needed_by, purpose, description,
                        supplier_id, supplier_price, package_name, subtotal,
                        status, type, 0, product_id));
                checked_ids.add(id);

            }
            req_adapter.notifyDataSetChanged();
        } catch (Exception e) {
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            ((MainActivity) getActivity()).showLoading(progressDialog, "Loading...").show();
            if (requestCode == Constants.Requisition_add) {
                if (rs_add_list.size() > 0) {
                    rs_add_list.clear();
                    req_adapter.notifyDataSetChanged();

                    get_table();
                } else {
                    get_table();
                }
            }
        }
    }
}
