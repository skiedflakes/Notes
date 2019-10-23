package com.wdysolutions.notes.Notes_Pig.Swine_Population.byClass;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.evrencoskun.tableview.TableView;
import com.wdysolutions.notes.AppController;
import com.wdysolutions.notes.Constants;
import com.wdysolutions.notes.DatePicker.DatePickerCustom;
import com.wdysolutions.notes.DatePicker.DatePickerSelectionInterfaceCustom;
import com.wdysolutions.notes.Notes_Pig.Swine_Population.byClass.tableview.TableViewAdapter;
import com.wdysolutions.notes.Notes_Pig.Swine_Population.byClass.tableview.model.Cell;
import com.wdysolutions.notes.Notes_Pig.Swine_Population.byClass.tableview.model.ColumnHeader_byClassi;
import com.wdysolutions.notes.Notes_Pig.Swine_Population.byClass.tableview.model.RowHeader;
import com.wdysolutions.notes.R;
import com.wdysolutions.notes.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class byClass_main extends Fragment implements DatePickerSelectionInterfaceCustom {

    String company_id, company_code, selected_branch_id;
    TableView tableview;
    TableViewAdapter mTableViewAdapter;
    Spinner spinner_generate;
    TextView txt_year;
    String selectedClass = "", selectedDate="", classType = "";
    LinearLayout layout_main;
    ProgressBar loading_main, progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.swine_population_by_class_main, container, false);
        SharedPref sharedPref = new SharedPref(getActivity());
        company_id = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYID);
        company_code = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYCODE);
        selected_branch_id = Constants.branch_id;

        spinner_generate = view.findViewById(R.id.spinner_generate);
        txt_year = view.findViewById(R.id.txt_year_);
        layout_main = view.findViewById(R.id.layout_main);
        loading_main = view.findViewById(R.id.loading_main);
        progressBar = view.findViewById(R.id.progressBar);
        tableview = view.findViewById(R.id.tableview);

        txt_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });

        initSpinner(view);
        getDateLimit();
        return view;
    }

    private void initSpinner(View view){
        final List<String> classi = new ArrayList<>();
        classi.add("Generate");
        classi.add("Genetic Breed");
        classi.add("Genetic Line");
        classi.add("Progeny");
        Spinner spinner_generate = view.findViewById(R.id.spinner_generate);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_blue, classi);
        spinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_drop);
        spinner_generate.setAdapter(spinnerAdapter);
        spinner_generate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedClass = classi.get(position);
                if (selectedClass.equals("Generate")){
                    selectedClass = "";
                } else {
                    if (selectedClass.equals("Genetic Breed")){
                        classType = "0";
                        generateReport(classType);
                    } else if (selectedClass.equals("Genetic Line")){
                        classType = "1";
                        generateReport(classType);
                    } else if (selectedClass.equals("Progeny")){
                        classType = "2";
                        generateReport(classType);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    String restrictedDate, currentDate;
    public void getDateLimit() {
        layout_main.setVisibility(View.GONE);
        loading_main.setVisibility(View.VISIBLE);
        String URL = getString(R.string.URL_online) + "dateLimits.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    layout_main.setVisibility(View.VISIBLE);
                    loading_main.setVisibility(View.GONE);

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("dateLimit");
                    JSONObject jsonObject_ = (JSONObject)jsonArray.get(0);
                    restrictedDate = jsonObject_.getString("restrictedD");
                    currentDate = jsonObject_.getString("current_date");

                    selectedDate = currentDate;
                    txt_year.setText(selectedDate);
                }
                catch (JSONException e){}
                catch (Exception e){}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                layout_main.setVisibility(View.VISIBLE);
                loading_main.setVisibility(View.GONE);
                Toast.makeText(getActivity(), getResources().getString(R.string.volley_error_msg), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("company_id", company_id);
                hashMap.put("company_code", company_code);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initializeTableView() {
        mTableViewAdapter = new TableViewAdapter(getContext(), classType, selectedDate);
        tableview.setAdapter(mTableViewAdapter);
        tableview.setNestedScrollingEnabled(false);
        tableview.setRowHeaderWidth(270);
    }

    private int cell_size = 0;
    public void generateReport(final String selectedClass) {
        progressBar.setVisibility(View.VISIBLE);
        tableview.setVisibility(View.GONE);
        spinner_generate.setEnabled(false);
        String URL = getString(R.string.URL_online) + "swine_population/by_class/by_class_details.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    progressBar.setVisibility(View.GONE);
                    tableview.setVisibility(View.VISIBLE);
                    spinner_generate.setEnabled(true);

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray_title = jsonObject.getJSONArray("title_array");
                    JSONArray jsonArray_ = jsonObject.getJSONArray("static_array");
                    JSONArray data_array = jsonObject.getJSONArray("data_array");

                    cell_size = jsonArray_title.length();
                    List<RowHeader> rowHeader_list = new ArrayList<>();
                    List<List<Cell>> cell_List = new ArrayList<>();

                    for(int i=0;i<jsonArray_.length();i++){
                        JSONObject Obj = (JSONObject) jsonArray_.get(i);
                        String class_type = Obj.getString("class_type");
                        rowHeader_list.add(new RowHeader(String.valueOf(i),class_type));

                        List<Cell> actual_list = new ArrayList<>();
                        for(int ii=0;ii<cell_size;ii++) {

                            JSONObject Obj2 = (JSONObject) data_array.get(ii+cell_size*i);
                            String population = Obj2.getString("population");
                            String percent = Obj2.getString("percent");
                            String id = Obj2.getString("id");

                            actual_list.add(new Cell(id, population, percent));
                        }
                        cell_List.add(actual_list);
                    }

                    Constants.byClassi_list = new ArrayList<>();
                    for (int i=0; i<jsonArray_title.length(); i++) {
                        JSONObject Obj = (JSONObject) jsonArray_title.get(i);
                        String branch = Obj.getString("branch");
                        String id = Obj.getString("branch_id");

                        Constants.byClassi_list.add(new ColumnHeader_byClassi(id,branch));
                    }

                    initializeTableView();

                    mTableViewAdapter.setAllItems(Constants.byClassi_list,
                            rowHeader_list,
                            cell_List);

                }
                catch (JSONException e){}
                catch (Exception e){}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                tableview.setVisibility(View.VISIBLE);
                spinner_generate.setEnabled(true);
                Toast.makeText(getActivity(), getResources().getString(R.string.volley_error_msg), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("company_id", company_id);
                hashMap.put("company_code", company_code);
                hashMap.put("branch_id", selected_branch_id);
                hashMap.put("start_date", selectedDate);
                hashMap.put("class_type", selectedClass);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void openDatePicker() {
        DatePickerCustom datePickerFragment = new DatePickerCustom();

        Bundle bundle = new Bundle();
        bundle.putString("maxDate", currentDate);
        bundle.putString("minDate", restrictedDate);
        bundle.putBoolean("isSetMinDate", true);
        datePickerFragment.setArguments(bundle);

        datePickerFragment.delegate = this;
        datePickerFragment.setCancelable(false);
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSelected(String date) {
        selectedDate = date;
        txt_year.setText(selectedDate);
    }

}
