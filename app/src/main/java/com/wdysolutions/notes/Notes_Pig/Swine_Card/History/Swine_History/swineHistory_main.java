package com.wdysolutions.notes.Notes_Pig.Swine_Card.History.Swine_History;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.wdysolutions.notes.AppController;
import com.wdysolutions.notes.Constants;
import com.wdysolutions.notes.R;
import com.wdysolutions.notes.SharedPref;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class swineHistory_main extends Fragment {

    RecyclerView recyclerView;
    TextView null_result, error_result;
    ProgressBar loading_;
    ArrayList<swineHistory_model> arrayList = new ArrayList<>();
    swineHistory_adapter adapter;
    String swine_scanned_id;

    SharedPref sharedPref;
    String user_id, company_id, company_code, category_id, selected_branch_id;

    private void initMenu(View view){
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Swine History");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        toolbarChangeColor(toolbar);
    }

    private void toolbarChangeColor(Toolbar toolbar){
        toolbar.setTitleTextColor(Color.WHITE);
        if (Constants.selected_notes.equals(getResources().getString(R.string.eggNOTES))){
            toolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_eggnotes)));
        } else if (Constants.selected_notes.equals(getResources().getString(R.string.feedNOTES))){
            toolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_feednotes)));
        } else if (Constants.selected_notes.equals(getResources().getString(R.string.pigNOTES))){
            toolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_pignotes)));
        } else if (Constants.selected_notes.equals(getResources().getString(R.string.broNOTES))){
            toolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_bronotes)));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.swinehistory_main, container, false);
        sharedPref = new SharedPref(getActivity());
        company_id = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYID);
        company_code = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYCODE);
        category_id = sharedPref.getUserInfo().get(sharedPref.KEY_CATEGORYID);
        selected_branch_id = Constants.branch_id;
        user_id = sharedPref.getUserInfo().get(sharedPref.KEY_USERID);

        swine_scanned_id = getArguments().getString("swine_scanned_id");

        recyclerView = view.findViewById(R.id.recyclerView);
        error_result = view.findViewById(R.id.error_result);
        null_result = view.findViewById(R.id.null_result);
        loading_ = view.findViewById(R.id.loading_);

        error_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading_.setVisibility(View.VISIBLE);
                error_result.setVisibility(View.GONE);
                getSwineHistoryDetails(company_code, company_id, swine_scanned_id);
            }
        });

        initMenu(view);
        getSwineHistoryDetails(company_code, company_id, swine_scanned_id);
        return view;
    }

    public void getSwineHistoryDetails(final String company_code, final String company_id, final String sow_id) {
        String URL = getString(R.string.URL_online)+"scan_eartag/history/swine_history.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    if (!response.equals("{\"data\":[]}")){
                        arrayList.clear();
                        null_result.setVisibility(View.GONE);
                        error_result.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        loading_.setVisibility(View.GONE);
                        JSONObject Object = new JSONObject(response);
                        JSONArray details = Object.getJSONArray("data");

                        for(int i = 0; i < details.length(); i++){
                            JSONObject r = details.getJSONObject(i);
                            arrayList.add(new swineHistory_model(r.getInt("id"),
                                    r.getString("user_id"),
                                    r.getString("remarks"),
                                    r.getString("date"),
                                    r.getString("branch"),
                                    r.getString("detail"),
                                    r.getString("count")));
                        }
                        adapter = new swineHistory_adapter(getContext(), arrayList);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter);
                    } else {
                        null_result.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        error_result.setVisibility(View.GONE);
                        loading_.setVisibility(View.GONE);
                    }
                } catch (Exception e){}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try{
                    error_result.setVisibility(View.VISIBLE);
                    null_result.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    loading_.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Connection Error, please try again.", Toast.LENGTH_SHORT).show();
                } catch (Exception e){}
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("company_code", company_code);
                hashMap.put("company_id", company_id);
                hashMap.put("sow_id", sow_id);
                hashMap.put("category_id", category_id);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public class swineHistory_adapter extends RecyclerView.Adapter<swineHistory_adapter.MyHolder> {

        ArrayList<swineHistory_model> data;
        private Context context;
        private LayoutInflater inflater;
        int lastPosition = -1, num;

        public swineHistory_adapter(Context context, ArrayList<swineHistory_model> data){
            this.context = context;
            this.data = data;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.swinehistory_container, parent,false);
            MyHolder holder = new MyHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyHolder holder, final int position) {
            final int getId = data.get(position).getId();
            final String getDate = data.get(position).getDate();
            final String getBranch = data.get(position).getBranch();
            final String getCount = data.get(position).getCount();
            final String getRemarks = data.get(position).getRemarks();
            final String getDetail = data.get(position).getDetail();
            final String getUser_id = data.get(position).getUser_id();
            num = position;
            num++;

            holder.text_count.setText(String.valueOf(num));
            holder.text_action.setText(getDetail);
            holder.text_remarks.setText(getRemarks);
            holder.text_location.setText(getBranch);
            holder.text_encodedby.setText(getUser_id);
            holder.text_date.setText(getDate);
            //setAnimation(holder.layout, position);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class MyHolder extends RecyclerView.ViewHolder{
            TextView text_location, text_remarks, text_date, text_encodedby, text_action, text_count;
            LinearLayout layout;
            Button btn_delete;
            ProgressBar delete_loading;
            public MyHolder(View itemView) {
                super(itemView);
                text_action = itemView.findViewById(R.id.text_action);
                delete_loading = itemView.findViewById(R.id.delete_loading);
                btn_delete = itemView.findViewById(R.id.btn_delete);
                layout = itemView.findViewById(R.id.layout);
                text_location = itemView.findViewById(R.id.text_location);
                text_remarks = itemView.findViewById(R.id.text_remarks);
                text_date = itemView.findViewById(R.id.text_date);
                text_encodedby = itemView.findViewById(R.id.text_encodedby);
                text_count = itemView.findViewById(R.id.text_count);
            }
        }

        private void setAnimation(View viewToAnimate, int position) {
            if (position > lastPosition) {
                Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
                viewToAnimate.startAnimation(animation);
                lastPosition = position;
            }
        }
    }

}
