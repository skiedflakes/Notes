package com.wdysolutions.notes.Globals.Income_Statement.Periodic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
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


public class periodic_main extends Fragment implements DatePickerSelectionInterfaceCustom {

    String company_id, company_code, category_id, user_id, selected_branch_id;
    RecyclerView recyclerView, recyclerView_total_cost, recyclerView_operating, recyclerView_other_income,
            recyclerView_income_return, recyclerView_beginning_balance, recyclerView_purchases, recyclerView_ending_balance,
            recyclerView_purchases_return;
    TextView tv_revenue_total, tv_total_gross, tv_total_goods, tv_net_operating_income, tv_total_operating_expe,
            tv_total_other_income, tv_net_other_income, tv_total_income_return, tv_net_income_return, txt_error,
            txt_start_date, txt_end_date, error_main, tv_inventory_adjust, purchase_subtotal, purchase_return_subtotal,
            ending_balance_subtotal, beginning_balance_subtotal;
    Spinner spinner_filter, spinner_branch;
    ScrollView scrollview_;
    ProgressBar progressBar, loading_main;
    Button btn_generate;
    LinearLayout layout_main;
    LinearLayout periodic_table;
    boolean isStartSelected = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.income_statement_periodic_main, container, false);
        SharedPref sharedPref = new SharedPref(getActivity());
        company_id = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYID);
        company_code = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYCODE);
        category_id = sharedPref.getUserInfo().get(sharedPref.KEY_CATEGORYID);
        user_id = sharedPref.getUserInfo().get(sharedPref.KEY_USERID);
        selected_branch_id = Constants.branch_id;

        beginning_balance_subtotal = view.findViewById(R.id.beginning_balance_subtotal);
        ending_balance_subtotal = view.findViewById(R.id.ending_balance_subtotal);
        purchase_return_subtotal = view.findViewById(R.id.purchase_return_subtotal);
        purchase_subtotal = view.findViewById(R.id.purchase_subtotal);
        tv_inventory_adjust = view.findViewById(R.id.tv_inventory_adjust);
        periodic_table = view.findViewById(R.id.periodic_table);
        periodic_table.setVisibility(View.GONE);
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
        recyclerView_purchases_return = view.findViewById(R.id.recyclerView_purchases_return);
        scrollview_ = view.findViewById(R.id.scrollview_);
        txt_error = view.findViewById(R.id.txt_error);
        progressBar = view.findViewById(R.id.progressBar);
        btn_generate = view.findViewById(R.id.btn_generate);
        layout_main = view.findViewById(R.id.layout_main);
        loading_main = view.findViewById(R.id.loading_main);
        error_main = view.findViewById(R.id.error_main);

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
                    getPeriodic();
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

    ArrayList<periodic_model> cost_purchases_return_models;
    ArrayList<periodic_model> cost_ending_balance_models;
    ArrayList<periodic_model> cost_purchases_models;
    ArrayList<periodic_model> cost_beginning_models;
    ArrayList<periodic_model> income_return_models;
    ArrayList<periodic_model> other_income_models;
    ArrayList<periodic_model> operation_expe_models;
    ArrayList<periodic_model> cost_models;
    ArrayList<periodic_model> revenue_models;
    public void getPeriodic() {
        progressBar.setVisibility(View.VISIBLE);
        periodic_table.setVisibility(View.GONE);
        txt_error.setVisibility(View.GONE);
        btn_generate.setEnabled(false);
        String URL = getString(R.string.URL_online) + "income_statement/periodic_table2.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    //((MainActivity)getActivity()).openDialog(response);
                    progressBar.setVisibility(View.GONE);
                    periodic_table.setVisibility(View.VISIBLE);
                    btn_generate.setEnabled(true);
                    JSONObject jsonObject = new JSONObject(response);

                    // Revenue (RE)
                    revenue_models = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("revenue");
                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject1 = (JSONObject)jsonArray.get(i);

                        revenue_models.add(new periodic_model(jsonObject1.getString("name"),
                                jsonObject1.getString("value")));
                    }

                    JSONArray jsonArray_total = jsonObject.getJSONArray("revenue_total");
                    JSONObject jsonObject_revenue_total = (JSONObject)jsonArray_total.get(0);
                    tv_revenue_total.setText(jsonObject_revenue_total.getString("total"));

                    periodic_adapter rs_adapter = new periodic_adapter(getContext(), revenue_models);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(rs_adapter);
                    recyclerView.setNestedScrollingEnabled(false);


                    //////////////////// COST (CE)
                    cost_models = new ArrayList<>();
                    JSONArray jsonArray_cost_beggining = jsonObject.getJSONArray("cost_ce");
                    for (int i=0; i<jsonArray_cost_beggining.length(); i++){
                        JSONObject jsonObject1 = (JSONObject)jsonArray_cost_beggining.get(i);

                        cost_models.add(new periodic_model(jsonObject1.getString("name"),
                                jsonObject1.getString("value")));
                    }

                    JSONArray jsonArray_total_cost = jsonObject.getJSONArray("cost_total");
                    JSONObject jsonObject_revenue_total_cost = (JSONObject)jsonArray_total_cost.get(0);
                    tv_inventory_adjust.setText(jsonObject_revenue_total_cost.getString("inventory_adjust"));
                    tv_total_goods.setText(jsonObject_revenue_total_cost.getString("total_cost_goods"));
                    tv_total_gross.setText(jsonObject_revenue_total_cost.getString("total_gross_profit"));

                    periodic_adapter adapter_cost = new periodic_adapter(getContext(), cost_models);
                    recyclerView_total_cost.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView_total_cost.setAdapter(adapter_cost);
                    recyclerView_total_cost.setNestedScrollingEnabled(false);


                    //------- BEGINNING
                    cost_beginning_models = new ArrayList<>();
                    JSONArray jsonArray_cost_beginning = jsonObject.getJSONArray("cost_beginning");
                    for (int i=0; i<jsonArray_cost_beginning.length(); i++){
                        JSONObject jsonObject1 = (JSONObject)jsonArray_cost_beginning.get(i);

                        cost_beginning_models.add(new periodic_model(jsonObject1.getString("name"),
                                jsonObject1.getString("value")));
                    }
                    periodic_adapter adapter_cost_beginning = new periodic_adapter(getContext(), cost_beginning_models);
                    recyclerView_beginning_balance.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView_beginning_balance.setAdapter(adapter_cost_beginning);
                    recyclerView_beginning_balance.setNestedScrollingEnabled(false);

                    JSONArray jsonArray_begining_subtotal = jsonObject.getJSONArray("cost_beginning_total");
                    JSONObject jsonObject_begining_subtotal = (JSONObject)jsonArray_begining_subtotal.get(0);
                    beginning_balance_subtotal.setText(jsonObject_begining_subtotal.getString("sub_total"));


                    //------- PURCHASES
                    cost_purchases_models = new ArrayList<>();
                    JSONArray jsonArray_cost_purchases = jsonObject.getJSONArray("cost_purchases");
                    for (int i=0; i<jsonArray_cost_purchases.length(); i++){
                        JSONObject jsonObject1 = (JSONObject)jsonArray_cost_purchases.get(i);

                        cost_purchases_models.add(new periodic_model(jsonObject1.getString("name"),
                                jsonObject1.getString("value")));
                    }
                    periodic_adapter adapter_cost_purchases = new periodic_adapter(getContext(), cost_purchases_models);
                    recyclerView_purchases.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView_purchases.setAdapter(adapter_cost_purchases);
                    recyclerView_purchases.setNestedScrollingEnabled(false);

                    JSONArray jsonArray_purchases_subtotal = jsonObject.getJSONArray("cost_purchases_total");
                    JSONObject jsonObject_purchases_subtotal = (JSONObject)jsonArray_purchases_subtotal.get(0);
                    purchase_subtotal.setText(jsonObject_purchases_subtotal.getString("sub_total"));


                    //------- PURCHASES RETURN
                    cost_purchases_return_models = new ArrayList<>();
                    JSONArray jsonArray_cost_purchases_return = jsonObject.getJSONArray("cost_purchases_return");
                    for (int i=0; i<jsonArray_cost_purchases_return.length(); i++){
                        JSONObject jsonObject1 = (JSONObject)jsonArray_cost_purchases_return.get(i);

                        cost_purchases_return_models.add(new periodic_model(jsonObject1.getString("name"),
                                jsonObject1.getString("value")));
                    }
                    periodic_adapter adapter_cost_purchases_return = new periodic_adapter(getContext(), cost_purchases_return_models);
                    recyclerView_purchases_return.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView_purchases_return.setAdapter(adapter_cost_purchases_return);
                    recyclerView_purchases_return.setNestedScrollingEnabled(false);

                    JSONArray jsonArray_purchases_return = jsonObject.getJSONArray("cost_purchases_return_total");
                    JSONObject jsonObject_purchases_return = (JSONObject)jsonArray_purchases_return.get(0);
                    purchase_return_subtotal.setText(jsonObject_purchases_return.getString("sub_total"));


                    //------- ENDING BALANCE
                    cost_ending_balance_models = new ArrayList<>();
                    JSONArray jsonArray_cost_ending = jsonObject.getJSONArray("cost_ending");
                    for (int i=0; i<jsonArray_cost_ending.length(); i++){
                        JSONObject jsonObject1 = (JSONObject)jsonArray_cost_ending.get(i);

                        cost_ending_balance_models.add(new periodic_model(jsonObject1.getString("name"),
                                jsonObject1.getString("value")));
                    }
                    periodic_adapter adapter_cost_ending = new periodic_adapter(getContext(), cost_ending_balance_models);
                    recyclerView_ending_balance.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView_ending_balance.setAdapter(adapter_cost_ending);
                    recyclerView_ending_balance.setNestedScrollingEnabled(false);

                    JSONArray jsonArray_ending_balance = jsonObject.getJSONArray("cost_ending_total");
                    JSONObject jsonObject_ending_balance = (JSONObject)jsonArray_ending_balance.get(0);
                    ending_balance_subtotal.setText(jsonObject_ending_balance.getString("sub_total"));


                    //////////////////// OPERATING EXPENSES (OE)
                    operation_expe_models = new ArrayList<>();
                    JSONArray jsonArray_operating_expe = jsonObject.getJSONArray("operating_expe");
                    for (int i=0; i<jsonArray_operating_expe.length(); i++){
                        JSONObject jsonObject1 = (JSONObject)jsonArray_operating_expe.get(i);

                        operation_expe_models.add(new periodic_model(jsonObject1.getString("name"),
                                jsonObject1.getString("value")));
                    }

                    JSONArray jsonArray_total_operating_expe = jsonObject.getJSONArray("operating_expe_total");
                    JSONObject jsonObject_revenue_total_operating_expe = (JSONObject)jsonArray_total_operating_expe.get(0);
                    tv_total_operating_expe.setText(jsonObject_revenue_total_operating_expe.getString("total_operating_expe"));
                    tv_net_operating_income.setText(jsonObject_revenue_total_operating_expe.getString("total_operating_income"));

                    periodic_adapter adapter_operating_expe = new periodic_adapter(getContext(), operation_expe_models);
                    recyclerView_operating.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView_operating.setAdapter(adapter_operating_expe);
                    recyclerView_operating.setNestedScrollingEnabled(false);


                    //////////////////// OTHER INCOME & EXPENSES (OIE)
                    other_income_models = new ArrayList<>();
                    JSONArray jsonArray_other_expe = jsonObject.getJSONArray("other_income");
                    for (int i=0; i<jsonArray_other_expe.length(); i++){
                        JSONObject jsonObject1 = (JSONObject)jsonArray_other_expe.get(i);

                        other_income_models.add(new periodic_model(jsonObject1.getString("name"),
                                jsonObject1.getString("value")));
                    }

                    JSONArray jsonArray_total_other_expe = jsonObject.getJSONArray("other_income_total");
                    JSONObject jsonObject_other_expe = (JSONObject)jsonArray_total_other_expe.get(0);
                    tv_total_other_income.setText(jsonObject_other_expe.getString("total_other_expe"));
                    tv_net_other_income.setText(jsonObject_other_expe.getString("total_other_income"));

                    periodic_adapter adapter_other = new periodic_adapter(getContext(), other_income_models);
                    recyclerView_other_income.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView_other_income.setAdapter(adapter_other);
                    recyclerView_other_income.setNestedScrollingEnabled(false);


                    //////////////////// INCOME TAX RETURN (ITR)
                    income_return_models = new ArrayList<>();
                    JSONArray jsonArray_income_return = jsonObject.getJSONArray("income_return");
                    for (int i=0; i<jsonArray_income_return.length(); i++){
                        JSONObject jsonObject1 = (JSONObject)jsonArray_income_return.get(i);

                        income_return_models.add(new periodic_model(jsonObject1.getString("name"),
                                jsonObject1.getString("value")));
                    }

                    JSONArray jsonArray_total_income_return = jsonObject.getJSONArray("income_return_total");
                    JSONObject jsonObject_income_return = (JSONObject)jsonArray_total_income_return.get(0);
                    tv_total_income_return.setText(jsonObject_income_return.getString("total_return"));
                    tv_net_income_return.setText(jsonObject_income_return.getString("total_return_net"));

                    periodic_adapter adapter_income_return = new periodic_adapter(getContext(), income_return_models);
                    recyclerView_income_return.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView_income_return.setAdapter(adapter_income_return);
                    recyclerView_income_return.setNestedScrollingEnabled(false);

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
                hashMap.put("category_id", category_id);
                hashMap.put("user_id", user_id);
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
}
