package com.wdysolutions.notes.Notes_Egg.Layer_Reports;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

import com.wdysolutions.notes.MainActivity;
import com.wdysolutions.notes.Notes_Egg.Layer_Reports.brooding_report_tableview.BR_TableViewAdapter;
import com.wdysolutions.notes.Notes_Egg.Layer_Reports.brooding_report_tableview.model.BR_Cell;
import com.wdysolutions.notes.Notes_Egg.Layer_Reports.brooding_report_tableview.model.BR_ColumnHeader;
import com.wdysolutions.notes.Notes_Egg.Layer_Reports.brooding_report_tableview.model.BR_RowHeader;
import com.wdysolutions.notes.Notes_Egg.Layer_Reports.brooding_report_tableview_bybatch.holder.BB_TableViewAdapter;
import com.wdysolutions.notes.Notes_Egg.Layer_Reports.brooding_report_tableview_bybatch.holder.model.BB_Cell;
import com.wdysolutions.notes.Notes_Egg.Layer_Reports.brooding_report_tableview_bybatch.holder.model.BB_ColumnHeader;
import com.wdysolutions.notes.Notes_Egg.Layer_Reports.brooding_report_tableview_bybatch.holder.model.BB_RowHeader;
import com.wdysolutions.notes.R;
import com.wdysolutions.notes.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Brooding_Report extends Fragment implements DatePickerSelectionInterfaceCustom {

    //user
    SharedPref sharedPref;
    String user_id, company_id,company_code,category_id,selected_branch_id,user_name;

    //ALL selected
    String selected_Batch;

    // ALL BATCH VARIABLE DECLARATIONS
    String selected_date;

    //tv
    TextView tv_date;

    //spinner array list
    ArrayList<all_batch_model> all_batch_models;
    Spinner spinner_batch;
    LinearLayout btn_generate_report,all_batch_layout;

    //tableview
    List<List<BR_Cell>> rowList = new ArrayList<>();
    ArrayList<BR_RowHeader> br_rowheader;
    BR_TableViewAdapter mTableViewAdapter;
    TableView mTableView;
    ProgressBar table_loading;

    //BY BATCH VARIABLE DECLARATIONS
    LinearLayout by_batch_layout,btn_by_batch_generate;
    Spinner spinner_type,spinner_from,spinner_to;
    ArrayList<spinner_type_model> type_list;
    ArrayList<spinner_type_model> from_list,to_list;
    String selected_from,selected_to,selected_type,bg_date;


    //tableview
    List<List<BB_Cell>> bb_rowList = new ArrayList<>();
    ArrayList<BB_RowHeader> bb_rowheader;
    BB_TableViewAdapter bb_mTableViewAdapter;
    TableView bb_mTableView;
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

        //ALL BATCH DECLARATIONS
        //module
        btn_generate_report = view.findViewById(R.id.btn_generate_report);
        tv_date = view.findViewById(R.id.tv_date);
        table_loading = view.findViewById(R.id.table_loading);

        //layout
        mTableView = view.findViewById(R.id.BR_tableview);
        all_batch_layout = view.findViewById(R.id.all_batch_layout);

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
        all_batch_brooding_details();


        //BY BATCH DECLARATION
        by_batch_layout = view.findViewById(R.id.by_batch_layout);
        spinner_type =  view.findViewById(R.id.spinner_type);
        spinner_from=  view.findViewById(R.id.spinner_from);
        spinner_to=  view.findViewById(R.id.spinner_to);
        btn_by_batch_generate = view.findViewById(R.id.btn_by_batch_generate);

        bb_mTableView = view.findViewById(R.id.BR_tableview_by_batch);

        //onlcick
        btn_by_batch_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_brooding_guide();
            }
        });

        return view;
    }

    public void all_batch_brooding_details(){

        String URL = getString(R.string.URL_online)+"brooding_report/brooding_details.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(response);

                    //populate all batch spinner

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

                            if(selected_Batch.equals("0")){
                                all_batch_layout.setVisibility(View.VISIBLE);
                                by_batch_layout.setVisibility(View.GONE);
                            }else{

                                single_batch_brooding_details();
                            }

                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                    JSONArray response_date = jsonObject1.getJSONArray("date");
                    JSONObject jsonObject_date = (JSONObject)response_date.get(0);
                    selected_date = jsonObject_date.getString("current_date");
                    tv_date.setText(selected_date);
                    get_brooding_overall();




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

    public void single_batch_brooding_details(){

        //populate single batch spinners

        //type spinner
        type_list= new ArrayList<>();
        type_list.add(new spinner_type_model("Please Choose", "0"));
        type_list.add(new spinner_type_model("Daily", "Daily"));
        type_list.add(new spinner_type_model("Weekly", "Weekly"));

        ArrayAdapter<String> spinner_type_adpter = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_drop, populateType());
        spinner_type_adpter.setDropDownViewResource(R.layout.custom_spinner_drop);
        spinner_type.setAdapter(spinner_type_adpter);
        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                spinner_type_model type_model = type_list.get(position);
                selected_type = type_model.getId();
                if(!selected_type.equals("0")){
                    get_from_spinner();
                }else{
                    spinner_from.setAdapter(null);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        all_batch_layout.setVisibility(View.GONE);
        by_batch_layout.setVisibility(View.VISIBLE);

    }

    public void get_from_spinner(){

        String URL = getString(R.string.URL_online)+"brooding_report/from_spinner.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(response);

                    //from spinner
                    from_list = new ArrayList<>();
                    from_list.add(new spinner_type_model("Please Choose", "0"));
                    JSONArray jsonArray = jsonObject1.getJSONArray("data");
                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject_ = (JSONObject)jsonArray.get(i);
                        String name = jsonObject_.getString("name");
                        String id = jsonObject_.getString("id");
                        from_list.add(new spinner_type_model(name,id));
                    }

                    ArrayAdapter<String> spinner_from_adpter = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_drop, populateFrom());
                    spinner_from_adpter.setDropDownViewResource(R.layout.custom_spinner_drop);
                    spinner_from.setAdapter(spinner_from_adpter);
                    spinner_from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            spinner_type_model from_model = from_list.get(position);
                            selected_from = from_model.getId();
                            if(!selected_from.equals("0")){
                                get_to_spinner();
                            }else{

                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

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

                hashMap.put("growing_id", selected_Batch);


                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void get_to_spinner(){

        String URL = getString(R.string.URL_online)+"brooding_report/to_spinner.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                all_batch_layout.setVisibility(View.GONE);
                by_batch_layout.setVisibility(View.VISIBLE);

                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(response);

                    //from spinner
                    to_list = new ArrayList<>();
                    to_list.add(new spinner_type_model("Please Choose", "0"));
                    JSONArray jsonArray = jsonObject1.getJSONArray("data");
                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject_ = (JSONObject)jsonArray.get(i);
                        String name = jsonObject_.getString("name");
                        String id = jsonObject_.getString("id");
                        to_list.add(new spinner_type_model(name,id));
                    }

                    ArrayAdapter<String> spinner_from_adpter = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_drop, populateTo());
                    spinner_from_adpter.setDropDownViewResource(R.layout.custom_spinner_drop);
                    spinner_to.setAdapter(spinner_from_adpter);
                    spinner_to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            spinner_type_model from_model = from_list.get(position);
                            selected_to = from_model.getId();
                          //  get_to_spinner();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });


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

                hashMap.put("growing_id", selected_Batch);
                hashMap.put("from_week", selected_from);

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

    private List<String> populateType(){
        List<String> lables_ = new ArrayList<>();
        for (int i = 0; i < type_list.size(); i++) {
            lables_.add(type_list.get(i).getData1());
        }
        return lables_;
    }

    private List<String> populateFrom(){
        List<String> lables_ = new ArrayList<>();
        for (int i = 0; i < from_list.size(); i++) {
            lables_.add(from_list.get(i).getData1());
        }
        return lables_;
    }

    private List<String> populateTo(){
        List<String> lables_ = new ArrayList<>();
        for (int i = 0; i < to_list.size(); i++) {
            lables_.add(to_list.get(i).getData1());
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

        table_loading.setVisibility(View.VISIBLE);
        mTableView.setVisibility(View.GONE);

        btn_generate_report.setEnabled(false);


        String URL = getString(R.string.URL_online)+"brooding_report/brooding_overall.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    table_loading.setVisibility(View.GONE);
                    mTableView.setVisibility(View.VISIBLE);
                    btn_generate_report.setEnabled(true);


                    JSONObject jsonObject = new JSONObject(response);
                    initializeTableView();
                    generate_allbatch_table(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                table_loading.setVisibility(View.GONE);
                mTableView.setVisibility(View.VISIBLE);
                btn_generate_report.setEnabled(true);
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

    public void get_brooding_guide(){
        String URL = getString(R.string.URL_online)+"brooding_report/getBrooding_guide.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    JSONObject jsonObject_ = (JSONObject)jsonArray.get(0);
                    String status = jsonObject_.getString("status");
                    if(status.equals("1")){
                        String gDate = jsonObject_.getString("gDate");
                        String gCDate = jsonObject_.getString("gCDate");
                        get_brooding_bybatch(gDate);

                    }else{
                        Toast.makeText(getContext(), "Data not found!", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error get_brooding_guide", Toast.LENGTH_SHORT).show();

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

                hashMap.put("growing_id", selected_Batch);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void get_brooding_bybatch(final String start_date){
        String URL = getString(R.string.URL_online)+"brooding_report/get_by_batch_daily.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ((MainActivity)getActivity()).openDialog(response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    initializeTableView_by_batch();
                    generate_bybatch_table(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(), "Error Connection get_brooding_bybatch", Toast.LENGTH_SHORT).show();

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

                hashMap.put("start_date", start_date);
                hashMap.put("growing_id", selected_Batch);

                hashMap.put("end_week", selected_to);
                hashMap.put("from_week", selected_from);
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

        mTableView.setRowHeaderWidth(250);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initializeTableView_by_batch() {
        bb_mTableViewAdapter = new BB_TableViewAdapter(getContext(),selected_date);
        bb_mTableView.setAdapter(bb_mTableViewAdapter);

        //mTableView.setTableViewListener(new TableViewListener(mTableView));

        bb_mTableView.setRowHeaderWidth(450);
    }


    int ColumnHeader_size =8;
    String static_title_row;

    int total_ip = 0;
    int total_rp = 0;
    int total_ma = 0;
    int total_cumm = 0;
    int total_dc = 0;
    double total_amp = 0.00;
    double total_cummp = 0;
    double total_da = 0;
    double total_fa=0;
    public void generate_allbatch_table(@NonNull JSONObject jObject) throws JSONException {
        total_ip = 0;
        total_rp = 0;
        total_ma = 0;
        total_amp = 0.00;
        total_cumm = 0;
        total_cummp =  0.00;
        total_dc = 0;
        total_da=0;
        total_fa=0;
        rowList = new ArrayList<>();
        //------- BATCH NAME
        br_rowheader = new ArrayList<>();
        JSONArray jsonArray_data = jObject.getJSONArray("data");


        for (int i=0; i<jsonArray_data.length(); i++){
            JSONObject jsonObject1 = (JSONObject)jsonArray_data.get(i);

            br_rowheader.add( new BR_RowHeader(String.valueOf(i),jsonObject1.getString("batch_name")));
        }

        br_rowheader.add( new BR_RowHeader(String.valueOf(jsonArray_data.length()),"TOTAL"));

        Constants.BR_ColumnHeader = new ArrayList<>();

        for (int i=0; i<ColumnHeader_size; i++){

            if(i==0){
                static_title_row="Breed";
                mTableView.setColumnWidth(0,100);
            }else if(i==1){
                static_title_row="INITIAL POPULATION";
                mTableView.setColumnWidth(1,100);
            }else if(i==2){
                static_title_row="RUNNING POPULATION";
                mTableView.setColumnWidth(2,100);
            }else if(i==3){
                static_title_row="AGE IN WEEKS";
                mTableView.setColumnWidth(3,100);
            }else if(i==4){
                static_title_row="MORTALITY";
                mTableView.setColumnWidth(4,750);
            }else if(i==5){
                static_title_row="DEPOPULATE";
                mTableView.setColumnWidth(5,250);
            }else if(i==6){
                static_title_row="FEEDS (g)";
                mTableView.setColumnWidth(6,450);
            }else if(i==7){
                static_title_row="Body Weight";
                mTableView.setColumnWidth(7,750);
            }
            Constants.BR_ColumnHeader.add(new BR_ColumnHeader(String.valueOf(i), static_title_row));
        }


        for(int i=0; i<jsonArray_data.length(); i++){
            List<BR_Cell> cell_list = new ArrayList<>();
            JSONObject jsonObject1 = (JSONObject)jsonArray_data.get(i);
            String breed = jsonObject1.getString("breed");
            String ip = jsonObject1.getString("initial_population");
            String rp = jsonObject1.getString("running_population");
            String aiw = jsonObject1.getString("age_in_weeks");

            //mortality cell
            String mbs = jsonObject1.getString("mortality_breed_std");
            String mfs = jsonObject1.getString("mortality_farm_std");
            String ma = jsonObject1.getString("mortality_actual");
            String amp = jsonObject1.getString("actual_mortality_percent");
            String cumm = jsonObject1.getString("cumm_mortality");
            String cummp = jsonObject1.getString("cumm_mortality_percent");

            //deopulate cell
            String da = jsonObject1.getString("depopulate_actual");
            String dc = jsonObject1.getString("depopulate_cumm");

            //feeds cell
            String fbs = jsonObject1.getString("feeds_breed_std");
            String ffs = jsonObject1.getString("feeds_farm_std");
            String fa = jsonObject1.getString("feed_actual");

            //body weight cell
            String bwb = jsonObject1.getString("body_weight_bs");
            String bwf = jsonObject1.getString("body_weight_fs");
            String bwa = jsonObject1.getString("body_weight_actual");
            String abvb = jsonObject1.getString("actual_bw_vs_bs");
            String abvf = jsonObject1.getString("actual_bw_vs_fs");

            cell_list.add( new BR_Cell(false,false,false,false,String.valueOf(i),breed,"","","","",""));
            cell_list.add( new BR_Cell(false,false,false,false,String.valueOf(i),ip,"","","","",""));
            cell_list.add( new BR_Cell(false,false,false,false,String.valueOf(i),rp,"","","","",""));
            cell_list.add( new BR_Cell(false,false,false,false,String.valueOf(i),aiw,"","","","",""));

            //mortality cell
            cell_list.add( new BR_Cell(false,false,false,true,String.valueOf(i),mbs,mfs,ma,amp,cumm,cummp));

            //depopulate cell
            cell_list.add( new BR_Cell(false,false,true,false,String.valueOf(i),da,dc,"","","",""));

            //feeds cell
            cell_list.add( new BR_Cell(false,true,false,false,String.valueOf(i),fbs,ffs,fa,"","",""));

            //body weight cell
            cell_list.add( new BR_Cell(true,false,false,false,String.valueOf(i),
                    bwb,bwf,bwa,abvb,abvf,""));

           try {
               //totals
               if (!ip.equals("")) {
                   String t_ip = ip.replaceAll("\\D+", "");
                   total_ip += Integer.valueOf(t_ip);
               }
               if (!rp.equals("")) {
                   String t_rp = rp.replaceAll("\\D+", "");
                   total_rp += Integer.valueOf(t_rp);
               }
               if (ma.equals("") ||ma.equals("ACTUAL")) {

               }else{
                   String t_ma = ma.replaceAll("\\D+", "");
                   total_ma += Integer.valueOf(t_ma);
               }

               if(amp.equals("")||amp.equals("ACTUAL (%)")){
               }else{
                   String sub_amp =amp.replaceAll(",", "");
                   total_amp += Double.valueOf(sub_amp);
               }

               if(cumm.equals("")||cumm.equals("CUMM")){
               }else{
                   String t_cumm = cumm.replaceAll("\\D+", "");
                   total_cumm += Integer.valueOf(t_cumm);
               }

               if(cummp.equals("")||cummp.equals("CUMM MORT (%)")){
               }else{
                   String sub_cummp =cummp.replaceAll(",", "");
                   total_cummp += Double.valueOf(sub_cummp);
               }

               if(dc.equals("")||dc.equals("CUMM")){
               }else{
                   String sub_dc =dc.replaceAll(",", "");
                   total_dc += Integer.valueOf(sub_dc);
               }

               if(da.equals("")||da.equals("ACTUAL")){
               }else{
                   String sub_da =da.replaceAll(",", "");
                   total_da += Integer.valueOf(sub_da);
               }
               if(fa.equals("")||fa.equals("ACTUAL")){
               }else{
                   String sub_fa =fa.replaceAll(",", "");
                   total_fa += Integer.valueOf(sub_fa);
               }

           }catch (Exception e){
               Toast.makeText(getActivity(), "error  "+amp, Toast.LENGTH_SHORT).show();
           }
            rowList.add(cell_list);
        }
        //without decimal
        NumberFormat formatter = new DecimalFormat("###,###,###");
        String total_initial_population = formatter.format(total_ip);
        String total_running_population = formatter.format(total_rp);
        String total_mortality_actual = formatter.format(total_ma);
        String total_cumm_mortality = formatter.format(total_cumm);
        String total_depopulate_cumm = formatter.format(total_dc);

        //with decimal.
        NumberFormat formatter_dec = new DecimalFormat("###,###,###.00");
        String total_depopulate_actual;
        String total_cumm_mortality_percent;
        String total_actual_mortality_percent;
        String feed_actual;

        if(total_da!=0){
            total_depopulate_actual =  formatter_dec.format(total_da);
        }else{
             total_depopulate_actual="0";
        }
        if(total_cummp!=0){
            total_cumm_mortality_percent  =  formatter_dec.format(total_cummp);
        }else{
            total_cumm_mortality_percent="0";
        }
        if(total_amp!=0){
            total_actual_mortality_percent =  formatter_dec.format(total_amp);
        }else{
            total_actual_mortality_percent="0";
        }

        if(total_fa!=0){
            feed_actual =  formatter_dec.format(total_fa);
        }else{
            feed_actual="0";
        }



        List<BR_Cell> cell_list = new ArrayList<>();
        int last_index = jsonArray_data.length()-1;

        cell_list.add( new BR_Cell(false,false,false,false,String.valueOf(last_index),"","","","","",""));
        cell_list.add( new BR_Cell(false,false,false,false,String.valueOf(last_index),String.valueOf(total_initial_population),"","","","",""));
        cell_list.add( new BR_Cell(false,false,false,false,String.valueOf(last_index),total_running_population,"","","","",""));
        cell_list.add( new BR_Cell(false,false,false,false,String.valueOf(last_index),"","","","","",""));

        //mortality cell
        cell_list.add( new BR_Cell(false,false,false,true,String.valueOf(last_index),"","",total_mortality_actual,total_actual_mortality_percent,total_cumm_mortality,total_cumm_mortality_percent));

        //depopulate cell
        cell_list.add( new BR_Cell(false,false,true,false,String.valueOf(last_index),total_depopulate_actual,total_depopulate_cumm,"","","",""));

        //feeds cell
        cell_list.add( new BR_Cell(false,true,false,false,String.valueOf(last_index),"","",feed_actual,"","",""));

        //body weight cell
        cell_list.add( new BR_Cell(true,false,false,false,String.valueOf(last_index),
                "","","","","",""));
        rowList.add(cell_list);
        mTableViewAdapter.setAllItems(Constants.BR_ColumnHeader,
                br_rowheader,
                rowList);
    }

    int bb_ColumnHeader_size = 1;
    String bb_static_title_row="";

    public void generate_bybatch_table(@NonNull JSONObject jObject) throws JSONException {
        JSONArray jsonArray_data = jObject.getJSONArray("data");
        bb_rowheader = new ArrayList<>();

        for (int i = 0; i < jsonArray_data.length(); i++) {
            JSONObject jsonObject1 = (JSONObject) jsonArray_data.get(i);
            String week = jsonObject1.getString("week");
            String day = jsonObject1.getString("day");
            String date = jsonObject1.getString("date");

            bb_rowheader.add(new BB_RowHeader(String.valueOf(i),week,day,date));

        }
        Constants.BB_ColumnHeader = new ArrayList<>();


        for (int i=0; i<bb_ColumnHeader_size; i++){

            if(i==0){
                bb_static_title_row="AGE";
                bb_mTableView.setColumnWidth(0,100);
            }
            Constants.BB_ColumnHeader.add(new BB_ColumnHeader(String.valueOf(i), bb_static_title_row));
        }



        bb_mTableViewAdapter.setAllItems(Constants.BB_ColumnHeader,
                bb_rowheader,
                bb_rowList);

    }

}
