package com.wdysolutions.notes.Notes_Egg.Layer_Reports;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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
import com.evrencoskun.tableview.TableView;
import com.wdysolutions.notes.AppController;
import com.wdysolutions.notes.Constants;
import com.wdysolutions.notes.DatePicker.DatePickerCustom;
import com.wdysolutions.notes.DatePicker.DatePickerSelectionInterfaceCustom;
import com.wdysolutions.notes.MainActivity;

import com.wdysolutions.notes.Notes_Egg.Layer_Reports.brooding_report_tableview.BR_TableViewAdapter;
import com.wdysolutions.notes.Notes_Egg.Layer_Reports.brooding_report_tableview.model.BR_Cell;
import com.wdysolutions.notes.Notes_Egg.Layer_Reports.brooding_report_tableview.model.BR_ColumnHeader;
import com.wdysolutions.notes.Notes_Egg.Layer_Reports.brooding_report_tableview.model.BR_RowHeader;
import com.wdysolutions.notes.Notes_Pig.Swine_Population.byAge.tableview.SP_TableViewAdapter;
import com.wdysolutions.notes.Notes_Pig.Swine_Population.byAge.tableview.model.SP_Cell;
import com.wdysolutions.notes.Notes_Pig.Swine_Population.byAge.tableview.model.SP_ColumnHeader;
import com.wdysolutions.notes.Notes_Pig.Swine_Population.byAge.tableview.model.SP_RowHeader;
import com.wdysolutions.notes.R;
import com.wdysolutions.notes.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Brooding_Report extends Fragment implements DatePickerSelectionInterfaceCustom {

    //user
    SharedPref sharedPref;
    String user_id, company_id,company_code,category_id,selected_branch_id,user_name;

    //module
    //strings
    String selected_Batch;
    String selected_date;

    //tv
    TextView tv_date;

    //spinner array list
    ArrayList<all_batch_model> all_batch_models;
    Spinner spinner_batch;
    LinearLayout btn_generate_report;

    //tableview
    List<List<BR_Cell>> rowList = new ArrayList<>();
    ArrayList<BR_RowHeader> br_rowheader;
    BR_TableViewAdapter mTableViewAdapter;
    TableView mTableView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.egg_brooding_report, container, false);

        //user
        sharedPref = new SharedPref(getActivity());
        company_id = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYID);
        company_code = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYCODE);
        category_id = sharedPref.getUserInfo().get(sharedPref.KEY_CATEGORYID);
        selected_branch_id = Constants.branch_id;
        user_id = sharedPref.getUserInfo().get(sharedPref.KEY_USERID);
        user_name= sharedPref.getUserInfo().get(sharedPref.KEY_NAME);
        spinner_batch = view.findViewById(R.id.spinner_batch);

        //module
        btn_generate_report = view.findViewById(R.id.btn_generate_report);
        tv_date = view.findViewById(R.id.tv_date);

        //layout
        mTableView = view.findViewById(R.id.BR_tableview);

        //onclick
        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });
        btn_generate_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_brooding_overall();
            }
        });
        get_brooding_details();
        return view;
    }

    public void get_brooding_details(){


        String URL = getString(R.string.URL_online)+"brooding_report/brooding_details.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(response);
                    all_batch_models = new ArrayList<>();
                    all_batch_models.add(new all_batch_model("All Batch", "0"));
                    JSONArray jsonArray = jsonObject1.getJSONArray("data");
                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject_ = (JSONObject)jsonArray.get(i);
                        String name = jsonObject_.getString("name");
                        String id = jsonObject_.getString("id");
                        all_batch_models.add(new all_batch_model(name,id));
                    }

                    ArrayAdapter<String> spinnerAdapter_branch = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_drop, populateBranch());
                    spinnerAdapter_branch.setDropDownViewResource(R.layout.custom_spinner_drop);
                    spinner_batch.setAdapter(spinnerAdapter_branch);
                    spinner_batch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            all_batch_model branch_model = all_batch_models.get(position);
                            selected_Batch = branch_model.getId();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                    JSONArray response_date = jsonObject1.getJSONArray("date");
                    JSONObject jsonObject_date = (JSONObject)response_date.get(0);
                    selected_date = jsonObject_date.getString("current_date");
                    tv_date.setText(selected_date);
                } catch (JSONException e) {
                    e.printStackTrace();
                }



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

                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }




    private List<String> populateBranch(){
        List<String> lables_ = new ArrayList<>();
        for (int i = 0; i < all_batch_models.size(); i++) {
            lables_.add(all_batch_models.get(i).getName());
        }
        return lables_;
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

    @Override
    public void onDateSelected(String date) {
        selected_date = date;
        tv_date.setText(selected_date);
    }


    public void get_brooding_overall(){

        String URL = getString(R.string.URL_online)+"brooding_report/brooding_overall.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ((MainActivity)getActivity()).openDialog(response);

                try {
                    JSONObject jsonObject = new JSONObject(response);


                    //------- BATCH NAME
//                    br_rowheader = new ArrayList<>();
//                    JSONArray jsonArray_data = jsonObject.getJSONArray("data");
//                    int cell_size = jsonArray_data.length();
//                    for (int i=0; i<jsonArray_data.length(); i++){
//                        JSONObject jsonObject1 = (JSONObject)jsonArray_data.get(i);
//
//                        br_rowheader.add( new BR_RowHeader(String.valueOf(i),jsonObject1.getString("batch_name")));
//                    }

//                    //------- DATA

//                    List<BR_Cell> actual_list = new ArrayList<>();
//                    for (int i=0; i<jsonArray_data.length(); i++){
//                        JSONObject jsonObject1 = (JSONObject)jsonArray_data.get(i);
//
//                        String breed = jsonObject1.getString("breed");
//                        String running_population = jsonObject1.getString("running_population");
//                        String initial_population = jsonObject1.getString("initial_population");
//
//                        actual_list.add( new BR_Cell(String.valueOf(i),breed,running_population,initial_population));
//                    }
//
//                    rowList.add(actual_list);


//                    for (int i=0; i<3; i++){
//
//                        List<BR_Cell> actual_list = new ArrayList<>();
//
//                        for (int ii=0; ii<cell_size; ii++){
//                            JSONObject Obj2 = (JSONObject) jsonArray_data.get(ii+cell_size*i);
//                            if(i==0){
//                                String breed = Obj2.getString("breed");
//                                actual_list.add( new BR_Cell(String.valueOf(i),breed));
//                            }else if(i==1){
//                                String running_population = Obj2.getString("running_population");
//                                actual_list.add( new BR_Cell(String.valueOf(i),running_population));
//                            }else if (i==2){
//                                String initial_population = Obj2.getString("initial_population");
//                                actual_list.add( new BR_Cell(String.valueOf(i),initial_population));
//                            }
//
//
//                        }
//                        rowList.add(actual_list);
//                    }



                    initializeTableView();
                    generate_table(jsonObject);




                } catch (JSONException e) {
                    e.printStackTrace();
                }

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

                hashMap.put("chosen_date", selected_date);

                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initializeTableView() {
        mTableViewAdapter = new BR_TableViewAdapter(getContext(),selected_date);
        mTableView.setAdapter(mTableViewAdapter);
        //mTableView.setTableViewListener(new TableViewListener(mTableView));
        mTableView.setNestedScrollingEnabled(false);
        mTableView.setRowHeaderWidth(300);
    }

    int ColumnHeader_size =3;
    String static_title_row;

    public void generate_table(JSONObject jObject) throws JSONException {

        //------- BATCH NAME
        br_rowheader = new ArrayList<>();
        JSONArray jsonArray_data = jObject.getJSONArray("data");
        int cell_size = jsonArray_data.length();

        for (int i=0; i<jsonArray_data.length(); i++){
            JSONObject jsonObject1 = (JSONObject)jsonArray_data.get(i);

            br_rowheader.add( new BR_RowHeader(String.valueOf(i),jsonObject1.getString("batch_name")));
        }

        Constants.BR_ColumnHeader = new ArrayList<>();

        for (int i=0; i<ColumnHeader_size; i++){

            if(i==0){
                static_title_row="Breed";
            }else if(i==1){
                static_title_row="INITIAL POPULATION";
            }else if(i==2){
                static_title_row="RUNNING POPULATION";
            }
            Constants.BR_ColumnHeader.add(new BR_ColumnHeader(String.valueOf(i), static_title_row));
        }




        for(int i=0; i<jsonArray_data.length(); i++){
            List<BR_Cell> cell_list = new ArrayList<>();


            JSONObject jsonObject1 = (JSONObject)jsonArray_data.get(i);
            String breed = jsonObject1.getString("breed");
            String ip = jsonObject1.getString("initial_population");
            String rp = jsonObject1.getString("running_population");

            cell_list.add( new BR_Cell(String.valueOf(i),breed));
            cell_list.add( new BR_Cell(String.valueOf(i),ip));
            cell_list.add( new BR_Cell(String.valueOf(i),rp));


            rowList.add(cell_list);

        }




        mTableViewAdapter.setAllItems(Constants.BR_ColumnHeader,
                br_rowheader,
                rowList);


    }



}
