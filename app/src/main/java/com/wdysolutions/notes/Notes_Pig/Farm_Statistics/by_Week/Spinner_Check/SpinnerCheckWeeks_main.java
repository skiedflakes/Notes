package com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Week.Spinner_Check;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.wdysolutions.notes.Constants;
import com.wdysolutions.notes.R;

import java.util.ArrayList;


public class SpinnerCheckWeeks_main extends DialogFragment {

    RecyclerView recyclerView;
    SpinnerListener spinnerListener;
    CheckBox check_all;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.spinner_check_weeks_main, container, false);
        spinnerListener = (SpinnerListener)getTargetFragment();

        check_all = view.findViewById(R.id.check_all);
        check_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check_all.isChecked()){
                    CheckAll("1");
                } else {
                    CheckAll("0");
                }
            }
        });

        Button btn_save = view.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        setCheckIfSelectedAll();
        initRecyclerView(view);
        return view;
    }

    private void setCheckIfSelectedAll(){
        int countChecked=0;
        for (int i=0; i<Constants.weeks_models.size(); i++){
           Weeks_model weeks_models = Constants.weeks_models.get(i);
           if (weeks_models.getCheck_status().equals("1")){
               countChecked++;
           }
        }
        if (countChecked == 52){ check_all.setChecked(true); }
        else { check_all.setChecked(false); }
    }

    spinnerAdapter spinnerAdapter;
    private void initRecyclerView(View view){
        recyclerView = view.findViewById(R.id.recyclerView);
        spinnerAdapter = new spinnerAdapter(getActivity(), Constants.weeks_models);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(spinnerAdapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void CheckAll(String checkStatus){
        for (int i = 0; i<Constants.weeks_models.size(); i++){
            int count = i+1;
            Constants.weeks_models.set(i, new Weeks_model(String.valueOf(count), "Week "+count, checkStatus));
        }
        spinnerAdapter.notifyDataSetChanged();
    }

    public class spinnerAdapter extends RecyclerView.Adapter<spinnerAdapter.MyHolder>   {
        ArrayList<Weeks_model> mdata;
        private Context context;
        private LayoutInflater inflater;

        public spinnerAdapter(Context context, ArrayList<Weeks_model> data){
            this.context = context;
            this.mdata = data;
            inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = inflater.inflate(R.layout.spinner_checks_weeks_container,viewGroup,false);
            MyHolder holder = new MyHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull final MyHolder myHolder, final int position) {
            final String getId = mdata.get(position).getId();
            final String getName = mdata.get(position).getName();
            final String getCheck_status = mdata.get(position).getCheck_status();

            if (getCheck_status.equals("1")){
                myHolder.chckbox_week.setChecked(true);
            } else {
                myHolder.chckbox_week.setChecked(false);
            }

            myHolder.chckbox_week.setText(getName);
            myHolder.chckbox_week.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myHolder.chckbox_week.isChecked()){
                        mdata.set(position, new Weeks_model(getId, getName, "1"));
                    } else {
                        mdata.set(position, new Weeks_model(getId, getName, "0"));
                    }
                    notifyDataSetChanged();
                    setCheckIfSelectedAll();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mdata.size();
        }

        public class MyHolder extends RecyclerView.ViewHolder{
            CheckBox chckbox_week;
            LinearLayout btn_weeks;
            public MyHolder(View itemView) {
                super(itemView);
                chckbox_week = itemView.findViewById(R.id.chckbox_week);
                btn_weeks = itemView.findViewById(R.id.btn_weeks);
            }
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        spinnerListener.selectedWeeks(Constants.weeks_models);
    }
}
