package com.wdysolutions.notes.Notes_Pig.Swine_Card.History.Vaccination;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wdysolutions.notes.AppController;
import com.wdysolutions.notes.Constants;
import com.wdysolutions.notes.Notes_Pig.Swine_Card.History.Vaccination.Add_Vaccine.addVaccine_main;
import com.wdysolutions.notes.Notes_Pig.Swine_Card.History.Med_Vac_Schedule.Med_Vac_schedule_main;
import com.wdysolutions.notes.R;
import com.wdysolutions.notes.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Vaccination_main extends Fragment {

    ArrayList<Vaccination_model> arrayList = new ArrayList<>();
    Vaccination_adapter adapter;
    RecyclerView recyclerView;

    LinearLayout layout_;
    TextView null_result, error_result;
    ProgressBar loading_vaccine;
    String swine_scanned_id, array_piglets, selectView, pen_code, checkedCounter, pen_type;
    SharedPref sharedPref;
    String user_id, company_id, company_code, category_id, selected_branch_id;

    private void initMenu(View view){
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Vaccination");
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
        View view = inflater.inflate(R.layout.vaccination_main, container, false);
        sharedPref = new SharedPref(getActivity());
        company_id = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYID);
        company_code = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYCODE);
        category_id = sharedPref.getUserInfo().get(sharedPref.KEY_CATEGORYID);
        selected_branch_id = Constants.branch_id;
        user_id = sharedPref.getUserInfo().get(sharedPref.KEY_USERID);

        swine_scanned_id = getArguments().getString("swine_scanned_id");
        array_piglets = getArguments().getString("array_piglets");
        selectView = getArguments().getString("selectView");
        pen_code = getArguments().getString("pen_code");
        pen_type = getArguments().getString("pen_type");
        checkedCounter = getArguments().getString("checkedCounter");

        error_result = view.findViewById(R.id.error_result);
        loading_vaccine = view.findViewById(R.id.loading_);
        null_result = view.findViewById(R.id.null_result);
        layout_ = view.findViewById(R.id.layout_);
        recyclerView = view.findViewById(R.id.recyclerView);

        Button btn_add = view.findViewById(R.id.btn_add);
        if (pen_type.equals("Deceased") || pen_type.equals("Culled")){btn_add.setVisibility(View.GONE);}
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("checkedCounter", checkedCounter);
                bundle.putString("selectView", selectView);
                bundle.putString("array_piglets", array_piglets);
                bundle.putString("swine_scanned_id", swine_scanned_id);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                addVaccine_main dialogFragment = new addVaccine_main();
                dialogFragment.setArguments(bundle);

                if (isPiglets()){
                    if (Integer.valueOf(checkedCounter) != 0){
                        dialogFragment.show(ft, "dialog");
                    } else {
                        Toast.makeText(getActivity(), "Please select piglet on swine card.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    dialogFragment.show(ft, "dialog");
                }
            }
        });

        Button btn_sched = view.findViewById(R.id.btn_sched);
        btn_sched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("swine_scanned_id", swine_scanned_id);
                bundle.putString("value", "V");
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) { ft.remove(prev); }
                ft.addToBackStack(null);
                Med_Vac_schedule_main dialogFragment = new Med_Vac_schedule_main();
                dialogFragment.setArguments(bundle);
                dialogFragment.show(ft, "dialog");
            }
        });

        error_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading_vaccine.setVisibility(View.VISIBLE);
                error_result.setVisibility(View.GONE);
                getVaccineDetails(company_code, company_id, swine_scanned_id, "3");
            }
        });

        initMenu(view);
        getVaccineDetails(company_code, company_id, swine_scanned_id, "3");
        return view;
    }

    private boolean isPiglets(){
        if (selectView.equals("piglet")){
            return true;
        } else {
            return false;
        }
    }

    public void getVaccineDetails(final String company_code, final String company_id, final String swine_id, final String get_type) {
        String URL = getString(R.string.URL_online)+"scan_eartag/history/"+ (isPiglets() ? "pig_piglets_vaccination.php" : "vaccination_get.php");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    if (!response.equals("{\"response\":[]}")){
                        arrayList.clear();
                        null_result.setVisibility(View.GONE);
                        error_result.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        loading_vaccine.setVisibility(View.GONE);
                        JSONObject Object = new JSONObject(response);
                        JSONArray details = Object.getJSONArray("response");

                        if (isPiglets()){
                            for(int i = 0; i < details.length(); i++){
                                JSONObject r = details.getJSONObject(i);
                                arrayList.add(new Vaccination_model(r.getInt("id"),
                                        r.getString("dosage"),
                                        r.getString("product_id"),
                                        r.getString("cost"),
                                        "",
                                        r.getString("date"),
                                        r.getString("count"),
                                        r.getString("swine_id")));
                            }
                        }
                        // Sow
                        else {
                            for(int i = 0; i < details.length(); i++){
                                JSONObject r = details.getJSONObject(i);
                                arrayList.add(new Vaccination_model(r.getInt("vaccine_id"),
                                        r.getString("dosage"),
                                        r.getString("product_id"),
                                        r.getString("total_cost"),
                                        r.getString("btn"),
                                        r.getString("date"),
                                        r.getString("count"),
                                        ""));
                            }
                        }

                        adapter = new Vaccination_adapter(getContext(), arrayList);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter);
                    } else {
                        null_result.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        error_result.setVisibility(View.GONE);
                        loading_vaccine.setVisibility(View.GONE);
                    }
                }
                catch (JSONException e){}
                catch (Exception e){}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try{
                    error_result.setVisibility(View.VISIBLE);
                    null_result.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    loading_vaccine.setVisibility(View.GONE);
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
                hashMap.put("get_type", get_type);
                hashMap.put("pen_code", pen_code);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


    private class Vaccination_adapter extends RecyclerView.Adapter<Vaccination_adapter.MyHolder> {
        ArrayList<Vaccination_model> data;
        private Context context;
        private LayoutInflater inflater;
        int lastPosition = -1, num;

        public Vaccination_adapter(Context context, ArrayList<Vaccination_model> data){
            this.context = context;
            this.data = data;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.vaccination_container, parent,false);
            MyHolder holder = new MyHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyHolder holder, final int position) {
            final int getId = data.get(position).getId();
            final String getProduct_id = data.get(position).getProduct_id();
            final String getBtn = data.get(position).getBtn();
            final String getDate = data.get(position).getDate();
            final String getDosage = data.get(position).getDosage();
            final String getTotal_cost = data.get(position).getTotal_cost();
            final String getSwine_id = data.get(position).getSwine_id();
            num = position;
            num++;

            holder.text_eartag.setText(getSwine_id);
            holder.text_count.setText(String.valueOf(num));
            holder.text_date.setText(getDate);
            holder.text_dosage.setText(getDosage);
            holder.text_total_cost.setText(getTotal_cost);
            holder.text_vaccine.setText(getProduct_id);
            //setAnimation(holder.layout, position);
            holder.delete_loading.getIndeterminateDrawable()
                    .setColorFilter(ContextCompat.getColor(getContext(), R.color.white), PorterDuff.Mode.SRC_IN );

            holder.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteDialog(String.valueOf(getId), holder, position);
                }
            });

            if (getBtn.equals("disabled")){
                holder.delete_layout.setVisibility(View.GONE);
            } else {
                holder.delete_layout.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class MyHolder extends RecyclerView.ViewHolder{
            TextView text_date, text_dosage, text_total_cost, text_vaccine, text_count, displaytxt_eartag, text_eartag;
            LinearLayout layout;
            Button btn_delete;
            ProgressBar delete_loading;
            RelativeLayout delete_layout;
            public MyHolder(View itemView) {
                super(itemView);
                text_count = itemView.findViewById(R.id.text_count);
                delete_loading = itemView.findViewById(R.id.delete_loading);
                btn_delete = itemView.findViewById(R.id.btn_delete);
                layout = itemView.findViewById(R.id.layout);
                text_date = itemView.findViewById(R.id.text_date);
                text_dosage = itemView.findViewById(R.id.text_dosage);
                text_total_cost = itemView.findViewById(R.id.text_total_cost);
                text_vaccine = itemView.findViewById(R.id.text_vaccine);
                text_eartag = itemView.findViewById(R.id.text_eartag);
                displaytxt_eartag = itemView.findViewById(R.id.displaytxt_eartag);
                delete_layout = itemView.findViewById(R.id.delete_layout);

                if (isPiglets()){
                    displaytxt_eartag.setVisibility(View.VISIBLE);
                    text_eartag.setVisibility(View.VISIBLE);
                } else {
                    displaytxt_eartag.setVisibility(View.GONE);
                    text_eartag.setVisibility(View.GONE);
                }
            }
        }

        private void setAnimation(View viewToAnimate, int position) {
            if (position > lastPosition) {
                Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
                viewToAnimate.startAnimation(animation);
                lastPosition = position;
            }
        }

        public void deleteVaccine(final String id, final MyHolder holder, final int position) {
            holder.btn_delete.setVisibility(View.GONE);
            holder.delete_loading.setVisibility(View.VISIBLE);
            String URL = context.getString(R.string.URL_online)+"scan_eartag/history/pig_delete_vac.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try{
                        holder.btn_delete.setVisibility(View.VISIBLE);
                        holder.delete_loading.setVisibility(View.GONE);
                        if (response.equals("okay")){
                            data.remove(position);
                            notifyDataSetChanged();
                            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e){}
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try{
                        holder.btn_delete.setVisibility(View.VISIBLE);
                        holder.delete_loading.setVisibility(View.GONE);
                        Toast.makeText(context, "Connection Error, please try again.", Toast.LENGTH_SHORT).show();
                    } catch (Exception e){}
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("company_code", company_code);
                    hashMap.put("company_id", company_id);
                    hashMap.put("swine_id", swine_scanned_id);
                    hashMap.put("user_id", user_id);
                    hashMap.put("category_id", category_id);
                    hashMap.put("id", id);
                    return hashMap;
                }
            };
            AppController.getInstance().setVolleyDuration(stringRequest);
            AppController.getInstance().addToRequestQueue(stringRequest);
        }

        void deleteDialog(final String id, final MyHolder holder, final int position){
            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(context);
            alertDialog2.setTitle("Confirm Delete...");
            alertDialog2.setMessage("Are you sure you want delete this vaccine?");
            alertDialog2.setIcon(R.drawable.ic_delete_forever_24dp);
            alertDialog2.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog2.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                deleteVaccine(id, holder, position);
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
            alertDialog2.show();
        }
    }

}
