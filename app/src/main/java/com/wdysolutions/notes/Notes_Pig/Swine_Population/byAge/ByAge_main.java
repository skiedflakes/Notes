package com.wdysolutions.notes.Notes_Pig.Swine_Population.byAge;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

public class ByAge_main extends Fragment implements DatePickerSelectionInterfaceCustom {
    //user session
    SharedPref sharedPref;
    String user_id, company_id, company_code, category_id, selected_branch_id,date="",date_limit="",selected_date="";
    SP_TableViewAdapter mTableViewAdapter;
    TableView mTableView;
    TextView txt_date;
    Button btn_generate;
    ProgressBar loading_main,table_loading;
    View view;
    LinearLayout layout_main;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.swine_population_by_age_main, container, false);

        //user
        sharedPref = new SharedPref(getActivity());
        company_id = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYID);
        company_code = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYCODE);
        category_id = sharedPref.getUserInfo().get(sharedPref.KEY_CATEGORYID);
        selected_branch_id = Constants.branch_id;
        user_id = sharedPref.getUserInfo().get(sharedPref.KEY_USERID);

        //layout
        mTableView = view.findViewById(R.id.SP_tableview);

        //loading
        layout_main = view.findViewById(R.id.layout_main);
        loading_main = view.findViewById(R.id.loading_main);
        table_loading = view.findViewById(R.id.table_loading);
        txt_date = view.findViewById(R.id.txt_date);
        btn_generate = view.findViewById(R.id.btn_generate);


        btn_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selected_date.equals("")){
                    Toast.makeText(getActivity(), "Please Select date", Toast.LENGTH_SHORT).show();
                }else{
                    getDetails();
                }

            }
        });

        get_datelimits();
        create_list();
        return view;
    }

    public void openDatePicker() {
        DatePickerCustom datePickerFragment = new DatePickerCustom();

        Bundle bundle = new Bundle();
        bundle.putString("maxDate", date);
        bundle.putString("minDate", date_limit);
        bundle.putBoolean("isSetMinDate",true);
        datePickerFragment.setArguments(bundle);

        datePickerFragment.delegate = this;
        datePickerFragment.setCancelable(false);
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initializeTableView() {
        mTableViewAdapter = new SP_TableViewAdapter(getContext(),selected_date);
        mTableView.setAdapter(mTableViewAdapter);
        //mTableView.setTableViewListener(new TableViewListener(mTableView));
        mTableView.setNestedScrollingEnabled(false);
        mTableView.setRowHeaderWidth(300);
    }
    public void get_datelimits(){
        loading_main.setVisibility(View.VISIBLE);
        layout_main.setVisibility(View.GONE);
        String URL = getString(R.string.URL_online) + "dateLimits.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    loading_main.setVisibility(View.GONE);
                    layout_main.setVisibility(View.VISIBLE);
                    txt_date.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openDatePicker();
                        }
                    });


                    JSONObject Object = new JSONObject(response);
                    JSONArray jsonArray = Object.getJSONArray("dateLimit");

                    JSONObject Obj = (JSONObject) jsonArray.get(0);
                    date_limit = Obj.getString("restrictedD");
                    date = Obj.getString("current_date");
                }
                catch (JSONException e){}
                catch (Exception e){}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading_main.setVisibility(View.GONE);
                layout_main.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "Connection Error, please try again.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                //user
                hashMap.put("company_id", company_id);
                hashMap.put("company_code", company_code);

                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void getDetails(){
        table_loading.setVisibility(View.VISIBLE);
        mTableView.setVisibility(View.GONE);
        String URL = getString(R.string.URL_online) + "swine_population/by_age/by_age_details.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    //((MainActivity)getActivity()).openDialog(response);
                    table_loading.setVisibility(View.GONE);
                    mTableView.setVisibility(View.VISIBLE);

                    JSONObject Object = new JSONObject(response);
                    initializeTableView();
                    generate_table(Object);
                }
                catch (JSONException e){}
                catch (Exception e){}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                table_loading.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Connection Error, please try again.", Toast.LENGTH_SHORT).show();
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
                hashMap.put("selected_date", selected_date);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void generate_table(JSONObject jObject){
       Constants.SP_ColumnHeader = new ArrayList<>();
        List<List<SP_Cell>> rowList = new ArrayList<>();

        try {
            JSONArray title_data = jObject.getJSONArray("title_array");
            JSONArray actual_data = jObject.getJSONArray("data_array");

            if (title_data.length() == 0 && actual_data.length() == 0){
                Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
            }
            else {
                List<SP_Cell> actual_list = new ArrayList<>();
                for (int i=0; i<title_data.length(); i++) {

                    JSONObject Obj = (JSONObject) title_data.get(i);
                    String branch_data = Obj.getString("branch");
                    String branch_id = Obj.getString("branch_id");

                    Constants.SP_ColumnHeader.add(new SP_ColumnHeader(branch_id, branch_data));

                        JSONObject Obj2 = (JSONObject) actual_data.get(i);
                        String junior_boar = Obj2.getString("junior_boar");
                        String senior_boar = Obj2.getString("senior_boar");
                        String gilt = Obj2.getString("gilt");
                        String sow = Obj2.getString("sow");
                        String _30days = Obj2.getString("_30days");
                        String _60days = Obj2.getString("_60days");
                        String _90days = Obj2.getString("_90days");
                        String _120days = Obj2.getString("_120days");
                        String _136days = Obj2.getString("_136days");
                        String _143days = Obj2.getString("_143days");
                        String _150days = Obj2.getString("_150days");
                        String _157days = Obj2.getString("_157days");
                        String _164days = Obj2.getString("_164days");
                        String _171days = Obj2.getString("_171days");
                        String _180days = Obj2.getString("_180days");
                        String greater_that_180days = Obj2.getString("greater_that_180days");
                        String sched_culled = Obj2.getString("sched_culled");
                        String sum_branch_count = Obj2.getString("sum_branch_count");

                        actual_list.add(new SP_Cell(String.valueOf(i), junior_boar,senior_boar,gilt,sow,
                                _30days, _60days,_90days,_120days,_136days,_143days,_150days,_157days,
                                _164days,_171days,_180days,greater_that_180days,sched_culled,sum_branch_count));

                }
                rowList.add(actual_list);
                mTableViewAdapter.setAllItems(Constants.SP_ColumnHeader,
                        getRowHeaderList(),
                        rowList);
            }
        }catch (Exception e){}
    }

int cell_size = 18;
String static_title_row;
    public List<SP_RowHeader> getRowHeaderList() {

            List<SP_RowHeader> list = new ArrayList<>();
            for (int i=0; i<cell_size; i++){
                if(i==0){
                    static_title_row="JUNIOR-BOAR";
                }else if(i==1){
                    static_title_row="SENIOR-BOAR";
                }else if(i==2){
                    static_title_row="GILTS";
                }else if(i==3){
                    static_title_row="SOWS";
                }else if(i==4){
                    static_title_row="30 DAYS OLD";
                }else if(i==5){
                    static_title_row="60 DAYS OLD";
                }else if(i==6){
                    static_title_row="90 DAYS OLD";
                }else if(i==7){
                    static_title_row="120 DAYS OLD";
                }else if(i==8){
                    static_title_row="136 DAYS OLD";
                }else if(i==9){
                    static_title_row="143 DAYS OLD";
                }else if(i==10){
                    static_title_row="150 DAYS OLD";
                }else if(i==11){
                    static_title_row="157 DAYS OLD";
                }else if(i==12){
                    static_title_row="164 DAYS OLD";
                }else if(i==13){
                    static_title_row="171 DAYS OLD";
                }else if(i==14){
                    static_title_row="180 DAYS OLD";
                }else if(i==15){
                    static_title_row=" > 180 DAYS OLD";
                }else if(i==16){
                    static_title_row="Schedule for Culling";
                }else if(i==17){
                    static_title_row="TOTAL COUNT";
                }

                list.add(new SP_RowHeader(String.valueOf(i),static_title_row));
            }
            return list;
    }


    @Override
    public void onDateSelected(String date) {
        selected_date = date;
        txt_date.setText(date);
    }

    public void create_list(){
        Constants.SP_type = new ArrayList<>();
        Constants.SP_type.add("Junior-boar");
        Constants.SP_type.add("Senior-boar");
        Constants.SP_type.add("Gilt");
        Constants.SP_type.add("Sow");
        Constants.SP_type.add("30");
        Constants.SP_type.add("60");
        Constants.SP_type.add("90");
        Constants.SP_type.add("120");
        Constants.SP_type.add("136");
        Constants.SP_type.add("143");
        Constants.SP_type.add("150");
        Constants.SP_type.add("157");
        Constants.SP_type.add("164");
        Constants.SP_type.add("171");
        Constants.SP_type.add("180");
        Constants.SP_type.add(">180");
        Constants.SP_type.add("sched_culled");
        Constants.SP_type.add("total_count");

    }


    public void get_modal_data(){

        String URL = getString(R.string.URL_online) + "swine_population/by_age/by_age_modal_details.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getActivity(),response, Toast.LENGTH_SHORT).show();
                try {

                    JSONObject Object = new JSONObject(response);

                }
                catch (JSONException e) {}
                catch (Exception e) {}

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                table_loading.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Connection Error, please try again.", Toast.LENGTH_SHORT).show();
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
                hashMap.put("selected_date", selected_date);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
