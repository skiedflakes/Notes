package com.wdysolutions.notes.Globals.Income_Statement.Perpetual;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.wdysolutions.notes.AppController;
import com.wdysolutions.notes.Constants;
import com.wdysolutions.notes.DatePicker.DatePickerCustom;
import com.wdysolutions.notes.DatePicker.DatePickerSelectionInterfaceCustom;
import com.wdysolutions.notes.Globals.Income_Statement.Periodic.branch_model;
import com.wdysolutions.notes.R;
import com.wdysolutions.notes.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class perpetual_main extends Fragment implements DatePickerSelectionInterfaceCustom {
    String company_id, company_code, category_id, user_id, selected_branch_id;
    RecyclerView recyclerView, recyclerView_total_cost, recyclerView_operating, recyclerView_other_income,
            recyclerView_income_return, recyclerView_beginning_balance, recyclerView_purchases, recyclerView_ending_balance;
    TextView tv_revenue_total, tv_total_gross, tv_total_goods, tv_net_operating_income, tv_total_operating_expe,
            tv_total_other_income, tv_net_other_income, tv_total_income_return, tv_net_income_return, txt_error,
            txt_start_date, txt_end_date, error_main;
    Spinner spinner_filter, spinner_branch;
    ScrollView scrollview_;
    ProgressBar progressBar, loading_main;
    Button btn_generate;
    LinearLayout layout_main,perpetual_table;
    //HorizontalScrollView periodic_table;
    boolean isStartSelected = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.income_statement_perpetual_main, container, false);
        SharedPref sharedPref = new SharedPref(getActivity());
        company_id = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYID);
        company_code = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYCODE);
        category_id = sharedPref.getUserInfo().get(sharedPref.KEY_CATEGORYID);
        user_id = sharedPref.getUserInfo().get(sharedPref.KEY_USERID);
        selected_branch_id = Constants.branch_id;

        perpetual_table = view.findViewById(R.id.perpetual_table);
        perpetual_table.setVisibility(View.GONE);
        spinner_branch = view.findViewById(R.id.spinner_branch);
        spinner_filter = view.findViewById(R.id.spinner_filter);
        txt_end_date = view.findViewById(R.id.txt_end_date);
        txt_start_date = view.findViewById(R.id.txt_start_date);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView_operating = view.findViewById(R.id.recyclerView_operating);
        recyclerView_total_cost = view.findViewById(R.id.recyclerView_total_cost);
        recyclerView_other_income = view.findViewById(R.id.recyclerView_other_income);
        tv_revenue_total = view.findViewById(R.id.tv_revenue_total);
        tv_total_gross = view.findViewById(R.id.tv_total_gross);
        tv_total_goods = view.findViewById(R.id.tv_total_goods);
        tv_net_operating_income = view.findViewById(R.id.tv_net_operating_income);
        tv_total_operating_expe = view.findViewById(R.id.tv_total_operating_expe);
        tv_total_other_income = view.findViewById(R.id.tv_total_other_income);
        tv_net_other_income = view.findViewById(R.id.tv_net_other_income);
        recyclerView_income_return = view.findViewById(R.id.recyclerView_income_return);
        recyclerView_beginning_balance = view.findViewById(R.id.recyclerView_beginning_balance);
        recyclerView_purchases = view.findViewById(R.id.recyclerView_purchases);
        recyclerView_ending_balance = view.findViewById(R.id.recyclerView_ending_balance);
        tv_total_income_return = view.findViewById(R.id.tv_total_income_return);
        tv_net_income_return = view.findViewById(R.id.tv_net_income_return);
        scrollview_ = view.findViewById(R.id.scrollview_);
        txt_error = view.findViewById(R.id.txt_error);
        progressBar = view.findViewById(R.id.progressBar);
        btn_generate = view.findViewById(R.id.btn_generate);
        error_main = view.findViewById(R.id.error_main);
        loading_main = view.findViewById(R.id.loading_main);
        layout_main = view.findViewById(R.id.layout_main);

        txt_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartSelected = true;
                openDatePicker();
            }
        });

        txt_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartSelected = false;
                openDatePicker();
            }
        });

        btn_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startDate.equals("")){
                    Toast.makeText(getActivity(), "Please select start date", Toast.LENGTH_SHORT).show();
                } else if (endDate.equals("")){
                    Toast.makeText(getActivity(), "Please select end date", Toast.LENGTH_SHORT).show();
                } else {
                    getPerpetual();
                }
            }
        });

        error_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBranch();
            }
        });

        getBranch();
        return view;
    }
    private void openDatePicker() {
        DatePickerCustom datePickerFragment = new DatePickerCustom();

        Bundle bundle = new Bundle();
        bundle.putString("maxDate", "");
        bundle.putString("minDate", "");
        bundle.putBoolean("isSetMinDate",false);
        bundle.putBoolean("isFutureDateTrue", true);
        datePickerFragment.setArguments(bundle);

        datePickerFragment.delegate = this;
        datePickerFragment.setCancelable(false);
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }

    String selectedBranch = "", selectedFilter = "", declared, current_date;
    ArrayList<branch_model> branch_models;
    public void getBranch(){
        loading_main.setVisibility(View.VISIBLE);
        layout_main.setVisibility(View.GONE);
        error_main.setVisibility(View.GONE);
        String URL = getString(R.string.URL_online)+"income_statement/periodic_details.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    loading_main.setVisibility(View.GONE);
                    layout_main.setVisibility(View.VISIBLE);
                    JSONObject jsonObject1 = new JSONObject(response);

                    branch_models = new ArrayList<>();
                    branch_models.add(new branch_model("All Branch", "0"));
                    JSONArray jsonArray = jsonObject1.getJSONArray("array_branch");
                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject_ = (JSONObject)jsonArray.get(i);

                        branch_models.add(new branch_model(jsonObject_.getString("branch"),
                                jsonObject_.getString("branch_id")));
                    }

                    ArrayAdapter<String> spinnerAdapter_branch = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_drop, populateBranch());
                    spinnerAdapter_branch.setDropDownViewResource(R.layout.custom_spinner_drop);
                    spinner_branch.setAdapter(spinnerAdapter_branch);
                    spinner_branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            branch_model branch_model = branch_models.get(position);
                            selectedBranch = branch_model.getBranch_id();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });


                    JSONArray access = jsonObject1.getJSONArray("access");
                    JSONObject jsonObject_access = (JSONObject)access.get(0);
                    declared = jsonObject_access.getString("declared_label");


                    JSONArray response_date = jsonObject1.getJSONArray("response_date");
                    JSONObject jsonObject_date = (JSONObject)response_date.get(0);
                    current_date = jsonObject_date.getString("current_date");


                    ArrayAdapter<String> spinnerAdapter_filter = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_drop, populateFilter());
                    spinnerAdapter_filter.setDropDownViewResource(R.layout.custom_spinner_drop);
                    spinner_filter.setAdapter(spinnerAdapter_filter);
                    spinner_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                            if (list_filter.get(position).equals("All")){
                                selectedFilter = "A";
                            } else if (list_filter.get(position).equals("Declared")){
                                selectedFilter = "D";
                            } else if (list_filter.get(position).equals("Undeclared")){
                                selectedFilter = "U";
                            }

                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                }
                catch (JSONException e){}
                catch (Exception e){}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading_main.setVisibility(View.GONE);
                error_main.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), getResources().getString(R.string.volley_error_msg), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("company_code", company_code);
                hashMap.put("company_id", company_id);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    private List<String> populateBranch(){
        List<String> lables_ = new ArrayList<>();
        for (int i = 0; i < branch_models.size(); i++) {
            lables_.add(branch_models.get(i).getBranch());
        }
        return lables_;
    }

    List<String> list_filter;
    private List<String> populateFilter(){
        list_filter = new ArrayList<>();
        if (declared.equals("")) { list_filter.add("All"); }
        list_filter.add("Declared");
        if (declared.equals("")) { list_filter.add("Undeclared"); }
        return list_filter;
    }

    ArrayList<perpetual_model> cost_ending_balance_models;
    ArrayList<perpetual_model> cost_purchases_models;
    ArrayList<perpetual_model> cost_beginning_models;
    ArrayList<perpetual_model> income_return_models;
    ArrayList<perpetual_model> other_income_models;
    ArrayList<perpetual_model> operation_expe_models;
    ArrayList<perpetual_model> cost_models;
    ArrayList<perpetual_model> revenue_models;
    String startDate = "", endDate = "";
    @Override
    public void onDateSelected(String date) {
        if (isStartSelected){
            startDate = date;
            txt_start_date.setText(startDate);
        } else {
            endDate = date;
            txt_end_date.setText(endDate);
        }
    }


    public void getPerpetual() {
        progressBar.setVisibility(View.VISIBLE);
        perpetual_table.setVisibility(View.GONE);
        txt_error.setVisibility(View.GONE);
        btn_generate.setEnabled(false);
        String URL = getString(R.string.URL_online) + "income_statement/perpetual_table.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // ((MainActivity)getActivity()).openDialog(response);
                try{
                    progressBar.setVisibility(View.GONE);
                    perpetual_table.setVisibility(View.VISIBLE);
                    btn_generate.setEnabled(true);
                    JSONObject jsonObject = new JSONObject(response);

                    // Revenue (RE)
                    revenue_models = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("data_revenue");
                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject1 = (JSONObject)jsonArray.get(i);

                        revenue_models.add(new perpetual_model(jsonObject1.getString("name"),
                                jsonObject1.getString("value")));
                    }

                    perpetual_adapter rs_adapter = new perpetual_adapter(getContext(), revenue_models);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(rs_adapter);
                    recyclerView.setNestedScrollingEnabled(false);

                    // Cost (CE)
                    cost_models = new ArrayList<>();
                    JSONArray jsonArray_cost_beggining = jsonObject.getJSONArray("data_cost");
                    for (int i=0; i<jsonArray_cost_beggining.length(); i++){
                        JSONObject jsonObject1 = (JSONObject)jsonArray_cost_beggining.get(i);

                        cost_models.add(new perpetual_model(jsonObject1.getString("name"),
                                jsonObject1.getString("value")));
                    }

                    perpetual_adapter adapter_cost = new perpetual_adapter(getContext(), cost_models);
                    recyclerView_total_cost.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView_total_cost.setAdapter(adapter_cost);
                    recyclerView_total_cost.setNestedScrollingEnabled(false);

                    //////////////////// OPERATING EXPENSES (OE)
                    operation_expe_models = new ArrayList<>();
                    JSONArray jsonArray_operating_expe = jsonObject.getJSONArray("data_OE");
                    for (int i=0; i<jsonArray_operating_expe.length(); i++){
                        JSONObject jsonObject1 = (JSONObject)jsonArray_operating_expe.get(i);

                        operation_expe_models.add(new perpetual_model(jsonObject1.getString("name"),
                                jsonObject1.getString("value")));
                    }

                    perpetual_adapter adapter_operating_expe = new perpetual_adapter(getContext(), operation_expe_models);
                    recyclerView_operating.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView_operating.setAdapter(adapter_operating_expe);
                    recyclerView_operating.setNestedScrollingEnabled(false);


                    //////////////////// OTHER INCOME & EXPENSES (OIE)
                    other_income_models = new ArrayList<>();
                    JSONArray jsonArray_other_expe = jsonObject.getJSONArray("data_OIE");
                    for (int i=0; i<jsonArray_other_expe.length(); i++){
                        JSONObject jsonObject1 = (JSONObject)jsonArray_other_expe.get(i);

                        other_income_models.add(new perpetual_model(jsonObject1.getString("name"),
                                jsonObject1.getString("value")));
                    }

                    perpetual_adapter adapter_other = new perpetual_adapter(getContext(), other_income_models);
                    recyclerView_other_income.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView_other_income.setAdapter(adapter_other);
                    recyclerView_other_income.setNestedScrollingEnabled(false);

                    //INCOME TAX RETURN (ITR)
                    income_return_models = new ArrayList<>();
                    JSONArray jsonArray_income_return = jsonObject.getJSONArray("data_ITR");

                        for (int i=0; i<jsonArray_income_return.length(); i++){
                            JSONObject jsonObject1 = (JSONObject)jsonArray_income_return.get(i);

                            income_return_models.add(new perpetual_model(jsonObject1.getString("name"),
                                    jsonObject1.getString("value")));
                        }

                        perpetual_adapter adapter_income_return = new perpetual_adapter(getContext(), income_return_models);
                        recyclerView_income_return.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView_income_return.setAdapter(adapter_income_return);
                        recyclerView_income_return.setNestedScrollingEnabled(false);

                    //totals
                    JSONArray jsonArray_total = jsonObject.getJSONArray("data_total");
                    JSONObject jsonObject_total = (JSONObject)jsonArray_total.get(0);
                    tv_revenue_total.setText(jsonObject_total.getString("total_revenue"));
                    tv_total_goods.setText(jsonObject_total.getString("total_cost"));
                    tv_total_gross.setText(jsonObject_total.getString("gross_profit"));
                    tv_total_operating_expe.setText(jsonObject_total.getString("total_OE"));
                    tv_net_operating_income.setText(jsonObject_total.getString("NOI"));
                    tv_total_other_income.setText(jsonObject_total.getString("total_OIE"));
                    tv_net_other_income.setText(jsonObject_total.getString("NI_before_IT"));
                    tv_total_income_return.setText(jsonObject_total.getString("total_ITR"));
                    tv_net_income_return.setText(jsonObject_total.getString("NI_after_IT"));

                }
                catch (JSONException e){}
                catch (Exception e){}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                txt_error.setVisibility(View.VISIBLE);
                btn_generate.setEnabled(true);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("company_id", company_id);
                hashMap.put("company_code", company_code);
                hashMap.put("branch_id", selectedBranch);
                hashMap.put("start_date", startDate);
                hashMap.put("end_date", endDate);
                hashMap.put("sel1", selectedFilter);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


}
