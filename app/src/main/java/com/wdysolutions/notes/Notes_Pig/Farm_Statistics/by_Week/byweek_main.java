package com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Week;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.google.gson.Gson;
import com.wdysolutions.notes.AppController;
import com.wdysolutions.notes.Constants;
import com.wdysolutions.notes.MainActivity;
import com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Month.Group_model;
import com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Week.Spinner_Check.SpinnerCheckWeeks_main;
import com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Week.Spinner_Check.SpinnerListener;
import com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Week.Spinner_Check.Weeks_model;
import com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Week.tableview.TableViewAdapter;
import com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Week.tableview.model.Cell;
import com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Week.tableview.model.ColumnHeader;
import com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Week.tableview.model.RowHeader;
import com.wdysolutions.notes.R;
import com.wdysolutions.notes.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class byweek_main extends Fragment implements SpinnerListener {

    Spinner spinner_year, spinner_group, spinner_page;
    Button btn_generate;
    TextView txt_result_title, txt_week, txt_error_msg;

    ArrayList<spinner_model> spinner_models_group = new ArrayList<>();
    List<String> array_year;
    List<String> list_page;
    String selectedGroup = "", selectedYear = "", selectedPage="1";
    int current_year;
    String company_id, company_code, category_id, selected_branch_id, user_id;
    ProgressBar table_loading, loading_main;
    TableView mTableView;
    LinearLayout layout_page, layout_main;
    LinearLayout layout_selection;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.farm_stats_byweek_main, container, false);
        SharedPref sharedPref = new SharedPref(getActivity());
        company_id = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYID);
        company_code = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYCODE);
        category_id = sharedPref.getUserInfo().get(sharedPref.KEY_CATEGORYID);
        user_id = sharedPref.getUserInfo().get(sharedPref.KEY_USERID);
        selected_branch_id = Constants.branch_id;

        layout_main = view.findViewById(R.id.layout_main);
        layout_selection = view.findViewById(R.id.layout_selection);
        layout_page = view.findViewById(R.id.layout_page);
        btn_generate = view.findViewById(R.id.btn_generate);
        txt_result_title = view.findViewById(R.id.txt_result_title);
        txt_week = view.findViewById(R.id.txt_week);
        table_loading = view.findViewById(R.id.table_loading);
        loading_main = view.findViewById(R.id.loading_main);
        txt_error_msg = view.findViewById(R.id.txt_error_msg);

        txt_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        btn_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedGroup.equals("")){
                    Toast.makeText(getActivity(), "Please select group", Toast.LENGTH_SHORT).show();
                } else if (num_weeks == 0){
                    Toast.makeText(getActivity(), "Please select weeks", Toast.LENGTH_SHORT).show();
                } else {
                    getTable();
                }
            }
        });

        txt_error_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentYear(view);
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
        getCurrentYear(view);
        return view;
    }

    public void getCurrentYear(final View view) {
        loading_main.setVisibility(View.VISIBLE);
        layout_main.setVisibility(View.GONE);
        txt_error_msg.setVisibility(View.GONE);
        String URL = getString(R.string.URL_online) + "farmstatistics/farm_stats_bymonth_details.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    loading_main.setVisibility(View.GONE);
                    layout_main.setVisibility(View.VISIBLE);
                    txt_error_msg.setVisibility(View.GONE);

                    JSONObject  jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data_details");
                    JSONObject object = (JSONObject) jsonArray.get(0);
                    current_year  = Integer.valueOf(object.getString("current_year"));
                    Constants.current_year_online = current_year;


                    ////////////////////////////////// Group spinner
                    spinner_models_group.add(new spinner_model("Please select group", ""));
                    spinner_models_group.add(new spinner_model("Breeding Stats", "breeding_stats"));
                    spinner_models_group.add(new spinner_model("Farrowing Stats", "farrowing_stats"));
                    spinner_models_group.add(new spinner_model("Production Stats", "production_stats"));
//                    JSONArray jsonArray_group = jsonObject.getJSONArray("data_group");
//                    for (int i=0; i<jsonArray_group.length(); i++){
//                        JSONObject jsonObject_group = (JSONObject) jsonArray_group.get(i);
//
//                        spinner_models_group.add(new spinner_model(jsonObject_group.getString("group_name"),
//                                jsonObject_group.getString("farm_group_id")));
//                    }

                    initSpinner(view);

                } catch (JSONException e) {}
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

    private void getWeeks(){
        Constants.weeks_models = new ArrayList<>();
        for (int i=1; i<=52; i++){
            Constants.weeks_models.add(new Weeks_model(String.valueOf(i), "Week "+i, "0"));
        }
    }

    private void initSpinner(View view){
        spinner_page = view.findViewById(R.id.spinner_page);
        spinner_year = view.findViewById(R.id.spinner_year);
        spinner_group = view.findViewById(R.id.spinner_group);

        ArrayAdapter<String> spinnerAdapter_ = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_drop, populateGroupName());
        spinnerAdapter_.setDropDownViewResource(R.layout.custom_spinner_drop);
        spinner_group.setAdapter(spinnerAdapter_);
        spinner_group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                spinner_model click = spinner_models_group.get(position);
                if (!click.getName().equals("Please select group")){
                    selectedGroup = click.getId();
                } else {
                    selectedGroup = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        getWeeks();
        ArrayAdapter<String> spinnerAdapter_year = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_drop, populateYearName());
        spinnerAdapter_year.setDropDownViewResource(R.layout.custom_spinner_drop);
        spinner_year.setAdapter(spinnerAdapter_year);
        spinner_year.setSelection(array_year.size()-1);
        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedYear = array_year.get(position);
                Constants.selectedYear = selectedYear;
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

    private void hideSpinnerPage(){
        if (selectedGroup.equals("production_stats")){
            layout_page.setVisibility(View.VISIBLE);
        } else {
            layout_page.setVisibility(View.GONE);
        }
    }

    private List<String> populatePage(){
        list_page = new ArrayList<>();
        list_page.add("1");
        list_page.add("2");
        list_page.add("3");
        list_page.add("4");
        return list_page;
    }

    private List<String> populateGroupName(){
        List<String> lables_ = new ArrayList<>();
        for (int i = 0; i < spinner_models_group.size(); i++) {
            lables_.add(spinner_models_group.get(i).getName());
        }
        return lables_;
    }

    private List<String> populateYearName(){
        array_year = new ArrayList<>();
        for (int i = 2014; i <= current_year; i++) {
            array_year.add(String.valueOf(i));
        }
        return array_year;
    }

    TableViewAdapter mTableViewAdapter;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initializeTableView(View layout) {
        mTableView = layout.findViewById(R.id.tableview);
        mTableViewAdapter = new TableViewAdapter(getContext());
        mTableView.setAdapter(mTableViewAdapter);
        //mTableView.setNestedScrollingEnabled(false);
        //mTableView.setTableViewListener(new TableViewListener(mTableView));
    }


    int num_weeks = 0, counter_clickable = 0;
    List<List<Cell>> cellList;

    ArrayList<String> column_weeks;
    public void getTable(){
        table_loading.setVisibility(View.VISIBLE);
        mTableView.setVisibility(View.GONE);
        btn_generate.setClickable(false);
        spinner_page.setEnabled(false);
        txt_result_title.setText("Farm Statistics");
        String URL = getString(R.string.URL_online)+"farmstatistics/farm_stats_byweek_generate.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                   // ((MainActivity)getActivity()).openDialog(response);
                    table_loading.setVisibility(View.GONE);
                    mTableView.setVisibility(View.VISIBLE);
                    btn_generate.setClickable(true);
                    spinner_page.setEnabled(true);
                    hideSpinnerPage();

                    cellList = new ArrayList<>();
                    Constants.rowHeaders = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray_target = jsonObject.getJSONArray("data_target");
                    JSONArray jsonArray_week = jsonObject.getJSONArray("data_week");


                    if (jsonArray_target.length() == 0 && jsonArray_week.length() == 0){

                        mTableView.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        mTableView.setVisibility(View.VISIBLE);

                        for (int i=0; i<jsonArray_target.length(); i++){
                            JSONObject jsonObject1 = jsonArray_target.getJSONObject(i);

                            counter_clickable = 0;

                            ///////////////////////////////// Cell week
                            List<Cell> cell = new ArrayList<>();
                            for (int j=0; j<num_weeks; j++){
                                JSONObject json = jsonArray_week.getJSONObject(j+(i*num_weeks));

                                cell.add(new Cell(String.valueOf(j),
                                        json.getString("actual_current"),
                                        json.getString("actual_perc"),
                                        json.getString("actual_ave"),
                                        json.getString("actual_perc_underline"),
                                        json.getString("actual_perc_bgred"),
                                        json.getString("actual_current_underline"),
                                        json.getString("actual_current_bgred"),
                                        json.getString("actual_ave_bgred"),
                                        json.getString("actual_ave_underline")));

                                if(json.getString("actual_ave_underline").equals("yes") ||
                                        json.getString("actual_current_underline").equals("yes") ||
                                        json.getString("actual_perc_underline").equals("yes")){
                                    counter_clickable++;
                                }
                            }
                            cellList.add(cell);


                            ///////////////////////////////// Target
                            Constants.rowHeaders.add(new RowHeader(String.valueOf(i),
                                    jsonObject1.getString("target_current"),
                                    jsonObject1.getString("target_ave"),
                                    jsonObject1.getString("target_perc"),
                                    jsonObject1.getString("farm_statistics"),
                                    jsonObject1.getString("dropdown_perc_graph"),
                                    jsonObject1.getString("dropdown_ave_graph"),
                                    jsonObject1.getString("dropdown_current_graph"),
                                    jsonObject1.getString("farm_statistics_curr"),
                                    jsonObject1.getString("farm_statistics_ave"),
                                    jsonObject1.getString("farm_statistics_perc"),
                                    jsonObject1.getString("farm_statistics_unique"),
                                    counter_clickable));
                        }


                        ///////////////////////////////// Column
                        List<ColumnHeader> columnHeaders = new ArrayList<>();
                        for (int i=0; i<num_weeks; i++){
                            String weeks_selected = column_weeks.get(i);
                            columnHeaders.add(new ColumnHeader("0", "Current","Ave.","%", "Week "+weeks_selected));
                        }

                        txt_result_title.setText("Farm Statistics "+selectedYear);
                        mTableViewAdapter.setAllItems(columnHeaders,
                                Constants.rowHeaders,
                                cellList);
                    }

                }
                catch (JSONException e){}
                catch (Exception e){}

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                txt_result_title.setText(getString(R.string.volley_error_msg));
                table_loading.setVisibility(View.GONE);
                btn_generate.setClickable(true);
                spinner_page.setEnabled(true);
                Toast.makeText(getActivity(), getString(R.string.volley_error_msg), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("company_id", company_id);
                hashMap.put("category_id", category_id);
                hashMap.put("user_id", user_id);
                hashMap.put("company_code", company_code);
                hashMap.put("branch_id", selected_branch_id);
                hashMap.put("stat_year", selectedYear);
                hashMap.put("stat_week", new Gson().toJson(Constants.weeks_models));
                hashMap.put("sel_grp", selectedGroup);
                hashMap.put("paging", selectedPage);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void openDialog(){
        DialogFragment selectedDialogFragment = new SpinnerCheckWeeks_main();
        selectedDialogFragment.setTargetFragment(this, 0);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {ft.remove(prev);}
        ft.addToBackStack(null);
        selectedDialogFragment.show(ft, "dialog");
    }

    @Override
    public void selectedWeeks(ArrayList<Weeks_model> weeks) {
        Constants.weeks_models = weeks;
        column_weeks = new ArrayList<>();

        num_weeks = 0;
        String str_selected="";
        for (int i = 0; i<Constants.weeks_models.size(); i++){
            Weeks_model selected_weeks = Constants.weeks_models.get(i);
            if (selected_weeks.getCheck_status().equals("1")){
                num_weeks++;
                str_selected += selected_weeks.getName()+",";
                column_weeks.add(selected_weeks.getId());
            }
        }

        if (num_weeks == 52) {
            txt_week.setText("All selected");
        } else if (num_weeks >= 3){
            String selected = String.valueOf(num_weeks);
            txt_week.setText(selected+" of 52 selected");
        } else if (num_weeks == 0){
            txt_week.setText("Select weeks");
        } else {
            txt_week.setText(str_selected);
        }
    }

}
