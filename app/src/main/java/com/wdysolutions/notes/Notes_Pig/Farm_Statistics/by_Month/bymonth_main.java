package com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Month;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.wdysolutions.notes.MainActivity;
import com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Month.tableview.TableViewAdapter;
import com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Month.tableview.model.Cell;
import com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Month.tableview.model.ColumnHeader;
import com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Month.tableview.model.RowHeader_byMonth;
import com.wdysolutions.notes.R;
import com.wdysolutions.notes.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class bymonth_main extends Fragment {

    //user session
    SharedPref sharedPref;
    String user_id, company_id, company_code, category_id, selected_branch_id;

    //spinner
    String selected_year="0",selected_month="0",month="",selected_group="0",months_data="",months_all="0", selectedPage="1";
    Spinner spinner_year,spinner_month,spinner_group;
    ArrayList<Group_model> group_models;
    List<String> lables = new ArrayList<>();
    int current_year;
    Button btn_generate;
    TableView mTableView;
    TableViewAdapter mTableViewAdapter;
    LinearLayout layout_page, layout_main, layout_selection;
    TextView txt_result_title, txt_error_msg;
    Spinner spinner_page;
    ProgressBar table_loading, loading_main;

    int cell_size = 12; //number of months


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.farm_stats_bymonth_main, container, false);
        sharedPref = new SharedPref(getActivity());
        company_id = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYID);
        company_code = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYCODE);
        category_id = sharedPref.getUserInfo().get(sharedPref.KEY_CATEGORYID);
        selected_branch_id = Constants.branch_id;
        user_id = sharedPref.getUserInfo().get(sharedPref.KEY_USERID);

        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        layout_selection = view.findViewById(R.id.layout_selection);
        layout_page = view.findViewById(R.id.layout_page);
        txt_result_title = view.findViewById(R.id.txt_result_title);
        txt_result_title.setText("Farm Statistics");
        spinner_year = view.findViewById(R.id.spinner_year);
        spinner_month = view.findViewById(R.id.spinner_month);
        spinner_group = view.findViewById(R.id.spinner_group);
        spinner_page = view.findViewById(R.id.spinner_page);
        table_loading = view.findViewById(R.id.table_loading);
        txt_error_msg = view.findViewById(R.id.txt_error_msg);
        loading_main = view.findViewById(R.id.loading_main);
        btn_generate = view.findViewById(R.id.btn_generate);
        layout_main = view.findViewById(R.id.layout_main);

        btn_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!selected_group.equals("0")){
                    get_table();
                }else {
                    Toast.makeText(getActivity(), "Please Select Group", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txt_error_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentYear();
            }
        });

        txt_result_title.setOnClickListener(new View.OnClickListener() {
            boolean isToggle = false;
            @Override
            public void onClick(View v) {
                if (isToggle){
                    isToggle = false;
                    Drawable img = getContext().getResources().getDrawable( R.drawable.ic_arrow_drop_down_24dp );
                    img.setBounds(0, 0, 48, 48);
                    txt_result_title.setCompoundDrawables(null, null, img, null);
                    layout_selection.setVisibility(View.VISIBLE);
                } else {
                    isToggle = true;
                    Drawable img = getContext().getResources().getDrawable( R.drawable.ic_arrow_drop_up_24dp );
                    img.setBounds(0, 0, 48, 48);
                    txt_result_title.setCompoundDrawables(null, null, img, null);
                    layout_selection.setVisibility(View.GONE);
                }
            }
        });


        initializeTableView(view);
        getCurrentYear();
        return view;
    }

    public void getCurrentYear() {
        loading_main.setVisibility(View.VISIBLE);
        layout_main.setVisibility(View.GONE);
        txt_error_msg.setVisibility(View.GONE);
        String URL = getString(R.string.URL_online) + "farmstatistics/farm_stats_bymonth_details.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    //((MainActivity)getActivity()).openDialog(response);
                    loading_main.setVisibility(View.GONE);
                    layout_main.setVisibility(View.VISIBLE);
                    txt_error_msg.setVisibility(View.GONE);

                    JSONObject  jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data_details");
                    JSONObject object = (JSONObject) jsonArray.get(0);
                    current_year  = Integer.valueOf(object.getString("current_year"));
                    Constants.current_year_online = current_year;


                    ////////////////////////////////// Group spinner
                    group_models = new ArrayList<>();
                    group_models.add(new Group_model("Select Group","0"));
                    group_models.add(new Group_model("Breeding Stats","breeding_stats"));
                    group_models.add(new Group_model("Farrowing Stats","farrowing_stats"));
                    group_models.add(new Group_model("Production Stats","production_stats"));
//                    JSONArray jsonArray_group = jsonObject.getJSONArray("data_group");
//                    for (int i=0; i<jsonArray_group.length(); i++){
//                        JSONObject jsonObject_group = (JSONObject) jsonArray_group.get(i);
//
//                        group_models.add(new Group_model(jsonObject_group.getString("group_name"),
//                                jsonObject_group.getString("farm_group_id")));
//                    }

                    init_all_spinner();
                }
                catch (JSONException e) {}
                catch (Exception e) {}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading_main.setVisibility(View.GONE);
                layout_main.setVisibility(View.VISIBLE);
                txt_error_msg.setVisibility(View.VISIBLE);
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
    }

    ArrayList<Month_model> month_models;
    public void init_all_spinner()  {

        //year spinner
        for (int i = 0; i < 5; i++) {
            lables.add(String.valueOf(current_year));
            current_year--;
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_drop, lables);
        spinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_drop);
        spinner_year.setAdapter(spinnerAdapter);
        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selected_year = lables.get(position);
                Constants.selectedYear = selected_year;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //month spinner
        month_models = new ArrayList<>();
        month_models.add(new Month_model("All Months","00"));
        month_models.add(new Month_model("January","01"));
        month_models.add(new Month_model("February","02"));
        month_models.add(new Month_model("March","03"));
        month_models.add(new Month_model("April","04"));
        month_models.add(new Month_model("May","05"));
        month_models.add(new Month_model("June","06"));
        month_models.add(new Month_model("July","07"));
        month_models.add(new Month_model("August","08"));
        month_models.add(new Month_model("September","09"));
        month_models.add(new Month_model("October","10"));
        month_models.add(new Month_model("November","11"));
        month_models.add(new Month_model("December","12"));


        // Populate Spinner
        List<String> lables = new ArrayList<>();
        for (int i = 0; i < month_models.size(); i++) {
            lables.add(month_models.get(i).getMonth());
        }
        ArrayAdapter<String> spinnerAdapter_year = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_drop, lables);
        spinnerAdapter_year.setDropDownViewResource(R.layout.custom_spinner_drop);
        spinner_month.setAdapter(spinnerAdapter_year);
        spinner_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Month_model click = month_models.get(position);
                selected_month = click.getDate_month();
                if(click.getDate_month().equals("00")){
                    months_all = "1";
                    month="";
                    cell_size = 12;
                }else{
                    months_all = "0";
                    months_data = click.getDate_month();
                    cell_size = 1;
                    month = click.getMonth();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });


        List<String> lable_group = new ArrayList<>();
        for (int i = 0; i < group_models.size(); i++) {
            lable_group.add(group_models.get(i).getGroup());
        }
        ArrayAdapter<String> spinnerAdapter_group = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_drop, lable_group);
        spinnerAdapter_group.setDropDownViewResource(R.layout.custom_spinner_drop);
        spinner_group.setAdapter(spinnerAdapter_group);
        spinner_group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Group_model click = group_models.get(position);
                selected_group = click.getGroup_value();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ArrayAdapter<String> spinnerAdapter_page = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_drop, populatePage());
        spinnerAdapter_page.setDropDownViewResource(R.layout.custom_spinner_drop);
        spinner_page.setAdapter(spinnerAdapter_page);
        spinner_page.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedPage = list_page.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    ArrayList<String> list_page;
    private List<String> populatePage(){
        list_page = new ArrayList<>();
        list_page.add("1");
        list_page.add("2");
        list_page.add("3");
        list_page.add("4");
        return list_page;
    }

    private void hideSpinnerPage(){
        if (selected_group.equals("production_stats")){
            layout_page.setVisibility(View.VISIBLE);
        } else {
            layout_page.setVisibility(View.GONE);
        }
    }

    public void get_table() {
        table_loading.setVisibility(View.VISIBLE);
        mTableView.setVisibility(View.GONE);
        spinner_page.setEnabled(false);
        btn_generate.setEnabled(false);
        String URL = getString(R.string.URL_online) + "farmstatistics/farm_stats_generate.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    hideSpinnerPage();
                    spinner_page.setEnabled(true);
                    btn_generate.setEnabled(true);
                    table_loading.setVisibility(View.GONE);
                    mTableView.setVisibility(View.VISIBLE);

                    JSONObject Object = new JSONObject(response);
                    generate_table(Object);

                }
                catch (JSONException e) {}
                catch (Exception e) {}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                table_loading.setVisibility(View.GONE);
                mTableView.setVisibility(View.VISIBLE);
                spinner_page.setEnabled(true);
                btn_generate.setEnabled(true);
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
                hashMap.put("page", selectedPage);

                hashMap.put("selected_year", selected_year);
                hashMap.put("months_all", months_all);
                hashMap.put("months_data", months_data);
                hashMap.put("group_type", selected_group);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initializeTableView(View layout) {
        mTableView = layout.findViewById(R.id.tableview);
        mTableViewAdapter = new TableViewAdapter(getContext());
        mTableView.setAdapter(mTableViewAdapter);
        //mTableView.setTableViewListener(new TableViewListener(mTableView));
        mTableView.setNestedScrollingEnabled(false);
    }

    int counter_clickable;
    public void generate_table(JSONObject jObject){
        if(months_all.equals("1")){
            txt_result_title.setText("Farm Statistics "+selected_year);
        }else{
            txt_result_title.setText("Farm Statistics "+month+", "+selected_year);
        }

        Constants.rowHeaders_bymonth = new ArrayList<>();
        List<List<Cell>> rowList = new ArrayList<>();
        try {
            JSONArray group_data = jObject.getJSONArray("group_type_data");
            JSONArray actual_data = jObject.getJSONArray("group_actual_data");

            if (group_data.length() == 0 && actual_data.length() == 0){

                Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
                mTableView.setVisibility(View.GONE);
            }
            else {

                mTableView.setVisibility(View.VISIBLE);

                for (int i=0; i<group_data.length(); i++){
                    JSONObject Obj = (JSONObject) group_data.get(i);

                    counter_clickable = 0;

                    ///////////////////////////////// Cell_br month
                    List<Cell> actual_list = new ArrayList<>();
                    for (int ii=0; ii<cell_size; ii++){

                        //renew_list.get(ii+12*i);
                        JSONObject Obj2 = (JSONObject) actual_data.get(ii+cell_size*i);
                        String actual_ave = Obj2.getString("actual_ave");
                        String actual_current = Obj2.getString("actual_current");
                        String actual_percent = Obj2.getString("actual_percent");
                        String actual_current_underline = Obj2.getString("actual_current_underline");
                        String actual_current_bgred = Obj2.getString("actual_current_bgred");
                        String actual_perc_underline = Obj2.getString("actual_perc_underline");
                        String actual_perc_bgred = Obj2.getString("actual_perc_bgred");
                        String actual_ave_underline = Obj2.getString("actual_ave_underline");
                        String actual_ave_bgred = Obj2.getString("actual_ave_bgred");
                        String id = String.valueOf(ii+i+12);
                        String cell_data = "cell "+String.valueOf(ii);
                        actual_list.add(new Cell(id, actual_current, actual_percent, actual_ave,actual_perc_underline,actual_perc_bgred,actual_current_underline,actual_current_bgred,actual_ave_bgred,actual_ave_underline));

                        if (actual_ave_underline.equals("yes") || actual_current_underline.equals("yes") || actual_perc_underline.equals("yes")){
                            counter_clickable++;
                        }
                    }
                    rowList.add(actual_list);


                    ///////////////////////////////// Target
                    String farm_statistics_row_name = Obj.getString("farm_statistics_row_name");
                    String ave = Obj.getString("ave");
                    String current = Obj.getString("current");
                    String percent = Obj.getString("percent");
                    String dropdown_perc_graph = Obj.getString("dropdown_perc_graph");
                    String dropdown_ave_graph = Obj.getString("dropdown_ave_graph");
                    String dropdown_current_graph = Obj.getString("dropdown_current_graph");
                    String farm_statistics_curr = Obj.getString("farm_statistics_curr");
                    String farm_statistics_ave = Obj.getString("farm_statistics_ave");
                    String farm_statistics_perc = Obj.getString("farm_statistics_perc");
                    String farm_statistics_unique = Obj.getString("farm_statistics_unique");

                    Constants.rowHeaders_bymonth.add(new RowHeader_byMonth("1",
                            current,
                            ave,
                            percent,
                            farm_statistics_row_name,
                            dropdown_perc_graph,
                            dropdown_ave_graph,
                            dropdown_current_graph,
                            farm_statistics_curr,
                            farm_statistics_ave,
                            farm_statistics_perc,
                            farm_statistics_unique,
                            counter_clickable));

                }

                mTableViewAdapter.setAllItems(getColumnHeaderList(),
                        Constants.rowHeaders_bymonth,
                        rowList);
            }

        } catch (JSONException e) {}
    }

//    public List<List<Cell_br>> getCellList() {
//        List<List<Cell_br>> rowList = new ArrayList<>();
//
//        for (int i=0; i<cell_size; i++){
//            List<Cell_br> cellList = new ArrayList<>();
//            for (int ii=0; ii<cell_size; ii++){
//                String id = String.valueOf(ii);
//                String cell_data = "cell "+String.valueOf(ii);
//
//                cellList.add(new Cell_br(id, "", "0", "0","","","","","",""));
//            }
//            rowList.add(cellList);
//        }
//        return rowList;
//    }

    String load_month;
    public List<ColumnHeader> getColumnHeaderList() {
        if(months_all.equals("1")){
            List<ColumnHeader> list = new ArrayList<>();
            for (int i=0; i<cell_size; i++){
                if(i==0){
                    load_month="January";
                }else if(i==1){
                    load_month="Febuary";
                }else if(i==2){
                    load_month="March";
                }else if(i==3){
                    load_month="April";
                }else if(i==4){
                    load_month="May";
                }else if(i==5){
                    load_month="June";
                }else if(i==6){
                    load_month="July";
                }else if(i==7){
                    load_month="August";
                }else if(i==8){
                    load_month="September";
                }else if(i==9){
                    load_month="October";
                }else if(i==10){
                    load_month="November";
                }else if(i==11){
                    load_month="December";
                }

                list.add(new ColumnHeader("0", "Current","Ave","%", load_month));
            }
            return list;

        } else {
            List<ColumnHeader> list = new ArrayList<>();
            for (int i=0; i<cell_size; i++){

                list.add(new ColumnHeader("0", "Current","Ave","%", month));
            }
            return list;
        }
    }

}
