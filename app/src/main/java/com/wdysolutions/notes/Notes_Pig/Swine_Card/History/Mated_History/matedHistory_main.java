package com.wdysolutions.notes.Notes_Pig.Swine_Card.History.Mated_History;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
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


public class matedHistory_main extends Fragment {

    RecyclerView recyclerView;
    TextView null_result, error_result, percentage_success, percentage_failed, percentage_grey;
    ProgressBar loading_;
    ArrayList<matedHistory_model> arrayList = new ArrayList<>();
    matedHistory_adapter adapter;
    LinearLayout layout_percentage;
    String company_code, company_id, swine_scanned_id;


    SharedPref sharedPref;
    String user_id, category_id, selected_branch_id;

    private void initMenu(View view){
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Mated History");
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
        View view = inflater.inflate(R.layout.matedhistory_main, container, false);
        sharedPref = new SharedPref(getActivity());
        company_id = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYID);
        company_code = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYCODE);
        category_id = sharedPref.getUserInfo().get(sharedPref.KEY_CATEGORYID);
        selected_branch_id = Constants.branch_id;
        user_id = sharedPref.getUserInfo().get(sharedPref.KEY_USERID);

        swine_scanned_id = getArguments().getString("swine_scanned_id");

        layout_percentage = view.findViewById(R.id.layout_percentage);
        recyclerView = view.findViewById(R.id.recyclerView);
        error_result = view.findViewById(R.id.error_result);
        null_result = view.findViewById(R.id.null_result);
        loading_ = view.findViewById(R.id.loading_);
        percentage_success = view.findViewById(R.id.percentage_success);
        percentage_failed = view.findViewById(R.id.percentage_failed);
        percentage_grey = view.findViewById(R.id.percentage_grey);

        error_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading_.setVisibility(View.VISIBLE);
                error_result.setVisibility(View.GONE);
                getMedicationDetails(company_code, company_id, swine_scanned_id);
            }
        });

        initMenu(view);
        getMedicationDetails(company_code, company_id, swine_scanned_id);
        return view;
    }

    public void getMedicationDetails(final String company_code, final String company_id, final String swine_id) {
        String URL = getString(R.string.URL_online)+"scan_eartag/history/mated_history.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    JSONObject Object = new JSONObject(response);
                    JSONArray details = Object.getJSONArray("data");

                    if (!String.valueOf(details).equals("[]")){

                        arrayList.clear();
                        null_result.setVisibility(View.GONE);
                        error_result.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        layout_percentage.setVisibility(View.VISIBLE);
                        loading_.setVisibility(View.GONE);

                        for(int i = 0; i < details.length(); i++){
                            JSONObject r = details.getJSONObject(i);
                            arrayList.add(new matedHistory_model(r.getInt("id"),
                                    r.getString("count"),
                                    r.getString("breeding_date"),
                                    r.getString("dry_days"),
                                    r.getString("boar_1"),
                                    r.getString("boar_2"),
                                    r.getString("boar_3"),
                                    r.getString("status")));
                        }
                        adapter = new matedHistory_adapter(getContext(), arrayList);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter);
                        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                super.onScrollStateChanged(recyclerView, newState);
                            }

                            @Override
                            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);

                                if (dy > 0) {
                                    layout_percentage.setVisibility(View.GONE);
                                }
                                else {
                                    layout_percentage.setVisibility(View.VISIBLE);
                                }
                            }
                        });


                        // percentage
                        JSONArray percentage_data = Object.getJSONArray("percentage_data");
                        JSONObject object = percentage_data.getJSONObject(0);
                        percentage_success.setText("Success: "+object.getString("success"));
                        percentage_failed.setText("Failed: "+object.getString("failed"));
                        percentage_grey.setText("Success Rate: "+object.getString("success_rate"));
                    }
                    else {
                        null_result.setVisibility(View.VISIBLE);
                        layout_percentage.setVisibility(View.GONE);
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
                    layout_percentage.setVisibility(View.GONE);
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
                hashMap.put("swine_id", swine_id);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public class matedHistory_adapter extends RecyclerView.Adapter<matedHistory_adapter.MyHolder> {

        ArrayList<matedHistory_model> data;
        private Context context;
        private LayoutInflater inflater;
        int lastPosition = -1, num;

        public matedHistory_adapter(Context context, ArrayList<matedHistory_model> data){
            this.context = context;
            this.data = data;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.matedhistory_container, parent,false);
            MyHolder holder = new MyHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyHolder holder, final int position) {
            final int getId = data.get(position).getId();
            final String getBreeding_date = data.get(position).getBreeding_date();
            final String getBoar_1 = data.get(position).getBoar_1();
            final String getBoar_2 = data.get(position).getBoar_2();
            final String getBoar_3 = data.get(position).getBoar_3();
            final String getDry_days = data.get(position).getDry_days();
            final String getCount = data.get(position).getCount();
            final String getStatus = data.get(position).getStatus();
            num = position;
            num++;

            holder.text_count.setText(String.valueOf(num));
            holder.text_date.setText(getBreeding_date);
            holder.text_boar1.setText(getBoar_1);
            holder.text_boar2.setText(getBoar_2);
            holder.text_boar3.setText(getBoar_3);

            if (getStatus.equals("0")){
                holder.text_status.setText("Failed");
            } else if (getStatus.equals("1")){
                holder.text_status.setText("Success");
            }

            //setAnimation(holder.layout, position);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class MyHolder extends RecyclerView.ViewHolder{
            TextView text_date, text_boar1, text_boar2, text_boar3, text_status, text_count;
            LinearLayout layout;
            public MyHolder(View itemView) {
                super(itemView);
                text_count = itemView.findViewById(R.id.text_count);
                layout = itemView.findViewById(R.id.layout);
                text_date = itemView.findViewById(R.id.text_date);
                text_boar1 = itemView.findViewById(R.id.text_boar1);
                text_boar2 = itemView.findViewById(R.id.text_boar2);
                text_boar3 = itemView.findViewById(R.id.text_boar3);
                text_status = itemView.findViewById(R.id.text_status);
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

    void dialogBox(String name){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        final EditText input = new EditText(getActivity());
        alertDialog.setView(input);
        input.setText(name);
        alertDialog.setPositiveButton("Close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }


}
