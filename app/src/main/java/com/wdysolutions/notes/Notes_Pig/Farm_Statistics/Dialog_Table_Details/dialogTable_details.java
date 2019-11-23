package com.wdysolutions.notes.Notes_Pig.Farm_Statistics.Dialog_Table_Details;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wdysolutions.notes.AppController;
import com.wdysolutions.notes.Constants;
import com.wdysolutions.notes.R;
import com.wdysolutions.notes.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class dialogTable_details extends DialogFragment implements dialogTable_adapter.EventListener {

    String company_id, company_code, category_id, selected_branch_id, user_id, stat_year, week_month, module, table_selected;
    TextView tv_2, tv_3, tv_4, tv_5, tv_6, txt_module, txt_null;
    RecyclerView recyclerView;
    LinearLayout layout_main;
    ProgressBar progressBar;
    ArrayList<dialogTable_model> dialogTable_models = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.farm_stats_dialogtable_details, container, false);
        SharedPref sharedPref = new SharedPref(getActivity());
        company_id = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYID);
        company_code = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYCODE);
        category_id = sharedPref.getUserInfo().get(sharedPref.KEY_CATEGORYID);
        user_id = sharedPref.getUserInfo().get(sharedPref.KEY_USERID);
        selected_branch_id = Constants.branch_id;

        week_month = getArguments().getString("week_month");
        stat_year = getArguments().getString("year");
        table_selected = getArguments().getString("table_selected");
        module = getArguments().getString("module");

        txt_null = view.findViewById(R.id.txt_null);
        progressBar = view.findViewById(R.id.progressBar);
        layout_main = view.findViewById(R.id.layout_main);
        recyclerView = view.findViewById(R.id.recyclerView);
        txt_module = view.findViewById(R.id.txt_module);
        tv_2 = view.findViewById(R.id.tv_2);
        tv_3 = view.findViewById(R.id.tv_3);
        tv_4 = view.findViewById(R.id.tv_4);
        tv_5 = view.findViewById(R.id.tv_5);
        tv_6 = view.findViewById(R.id.tv_6);

        view.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        getTableDetails();
        return view;
    }

    private String detailsPHP(){
        if (table_selected.equals("week")){
            return "farm_stats_byweek_details_dialog.php";
        } else {
            return "farm_stats_bymonth_details_dialog.php";
        }
    }
    
    public void getTableDetails() {
        progressBar.setVisibility(View.VISIBLE);
        layout_main.setVisibility(View.GONE);
        String URL = getString(R.string.URL_online) + "farmstatistics/"+detailsPHP();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    //((MainActivity)getActivity()).openDialog(response);

                    if (response.equals("no table")){
                        txt_null.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                    else {
                        progressBar.setVisibility(View.GONE);
                        layout_main.setVisibility(View.VISIBLE);

                        JSONObject jsonObject = new JSONObject(response);

                        // Title
                        JSONArray jsonArray_t = jsonObject.getJSONArray("title_table");
                        JSONObject title = (JSONObject) jsonArray_t.get(0);
                        if (!title.getString("tv_2").equals("")){ tv_2.setText(title.getString("tv_2")); }
                        if (!title.getString("tv_3").equals("")){ tv_3.setText(title.getString("tv_3")); }
                        if (!title.getString("tv_4").equals("")){ tv_4.setText(title.getString("tv_4")); tv_4.setVisibility(View.VISIBLE); }
                        if (!title.getString("tv_5").equals("")){ tv_5.setText(title.getString("tv_5")); tv_5.setVisibility(View.VISIBLE); }
                        if (!title.getString("tv_6").equals("")){ tv_6.setText(title.getString("tv_6")); tv_6.setVisibility(View.VISIBLE); }

                        // Data
                        JSONArray jsonArray_d = jsonObject.getJSONArray("data_table");
                        for (int i=0; i<jsonArray_d.length(); i++){
                            JSONObject data = (JSONObject) jsonArray_d.get(i);
                            dialogTable_models.add(new dialogTable_model(data.getString("tx_2"),
                                    data.getString("tx_3"),
                                    data.getString("tx_4"),
                                    data.getString("tx_5"),
                                    data.getString("tx_6"),
                                    data.getString("swine_id")));
                        }

                        txt_module.setText(module+" for "+str_week(Integer.valueOf(week_month)));

                        dialogTable_adapter rs_adapter = new dialogTable_adapter(getContext(), dialogTable_models, title.getString("tv_4"), title.getString("tv_5"), title.getString("tv_6"), dialogTable_details.this);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(rs_adapter);
                        recyclerView.setNestedScrollingEnabled(false);
                    }
                }
                catch (JSONException e){}
                catch (Exception e){}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismiss();
                Toast.makeText(getActivity(), getResources().getString(R.string.volley_error_msg), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("module", module);
                hashMap.put("stat_year", stat_year);

                if (table_selected.equals("week")){
                    hashMap.put("WEEKOFYEAR", week_month);
                } else {
                    hashMap.put("MONTH", week_month);
                }

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

    private String str_week(int selected){
        if (table_selected.equals("week")){
            String[] weeks = {"1st Week", "2nd Week", "3rd Week", "4th Week", "5th Week", "6th Week", "7th Week",
                    "8th Week", "9th Week", "10th Week", "11th Week", "12th Week", "13th Week", "14th Week", "15th Week",
                    "16th Week", "17th Week", "18th Week", "19th Week", "20th Week", "21st Week", "22nd Week", "23rd Week",
                    "24th Week", "25th Week", "26th Week", "27th Week", "28th Week", "29th Week", "30th Week", "31st Week",
                    "32nd Week", "33rd Week", "34th Week", "35th Week", "36th Week", "37th Week", "38th Week", "39th Week",
                    "40th Week", "41st Week", "42nd Week", "43rd Week", "44th Week", "45th Week", "46th Week", "47th Week",
                    "48th Week", "49th Week", "50th Week", "51st Week", "52nd Week"};
            return weeks[selected - 1];
        }
        else { // month
            String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
            return months[selected - 1];
        }
    }

    @Override
    public void close_dismiss() {
        dismiss();
    }

}
