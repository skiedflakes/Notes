package com.wdysolutions.notes.Navigation_Panel.ThreeLevelExpandable;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wdysolutions.notes.Constants;
import com.wdysolutions.notes.Globals.Cash_Voucher.Cash_Voucher_request_main;
import com.wdysolutions.notes.Globals.Check_Voucher.Check_Voucher_request_main;
import com.wdysolutions.notes.Globals.Income_Statement.Periodic.periodic_main;
import com.wdysolutions.notes.Globals.Income_Statement.Perpetual.perpetual_main;

import com.wdysolutions.notes.Globals.Petty_Cash.Liquidation.PettyCash_liquidation_main;
import com.wdysolutions.notes.Globals.Petty_Cash.Replenish.PettyCash_replenish_main;
import com.wdysolutions.notes.Globals.Petty_Cash.Request.PettyCash_request_main;
import com.wdysolutions.notes.Globals.Purchase_Order.Purchase_Order;
import com.wdysolutions.notes.Globals.Requisition.Requisition_main;

import com.wdysolutions.notes.Globals.Revolving_Fund.Liquidation.Revolving_liquidation_main;
import com.wdysolutions.notes.Globals.Revolving_Fund.Replenish.Revolving_replenish_main;
import com.wdysolutions.notes.Globals.Revolving_Fund.Request.Revolving_request_main;
import com.wdysolutions.notes.MainActivity;
import com.wdysolutions.notes.Notes_Egg.Layer_Reports.Brooding_Report;
import com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Month.bymonth_main;
import com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Region.byregion_main;
import com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Week.byweek_main;
import com.wdysolutions.notes.Globals.Price_Watch.Price_watch_main;
import com.wdysolutions.notes.Notes_Pig.Parity_Report.Parity_report_main;
import com.wdysolutions.notes.Notes_Pig.Swine_Delivery_Report.daily_swine_delivery_main;
import com.wdysolutions.notes.Notes_Pig.Swine_Population.LineGraph.lineGraph_main;
import com.wdysolutions.notes.Notes_Pig.Swine_Population.byAge.ByAge_main;
import com.wdysolutions.notes.Notes_Pig.Swine_Population.byClass.byClass_main;
import com.wdysolutions.notes.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ThreeLevelListAdapter extends BaseExpandableListAdapter {

    String[] parentHeaders;
    String[] reportsThirdLevel_SP;
    String[] reportsThirdLevel_IS;
    String[] reportsThirdLevel_SD;
    String[] reportsThirdLevel_FS;

    //eggnotes exclusive
    String[] getReportsThirdLevel_LR;


    String[] CV_module;
    String[] CAV_module;
    String[] PC_module;
    String[] RF_module;


    List<String[]> secondLevel;
    private Context context;
    List<LinkedHashMap<String, String[]>> data;
    TextView text;
    LinearLayout bg_first;

    public ThreeLevelListAdapter(Context context, String[] parentHeader, List<String[]> secondLevel, List<LinkedHashMap<String,
            String[]>> data, String[] reportsThirdLevel_SP,String[] reportsThirdLevel_IS,String[] reportsThirdLevel_SD,String[] reportsThirdLevel_FS,String[] CV_module,
                                 String[] CAV_module, String[] PC_module,String[] RF_module,String[] getReportsThirdLevel_LR) {
        this.context = context;
        this.parentHeaders = parentHeader;
        this.secondLevel = secondLevel;
        this.data = data;
        this.reportsThirdLevel_SP = reportsThirdLevel_SP;
        this.reportsThirdLevel_IS = reportsThirdLevel_IS;
        this.reportsThirdLevel_SD = reportsThirdLevel_SD;
        this.reportsThirdLevel_FS = reportsThirdLevel_FS;
        this.CV_module=CV_module;
        this.CAV_module=CAV_module;
        this.PC_module=PC_module;
        this.RF_module=RF_module;

        //eggnotes exclusive
        this.getReportsThirdLevel_LR = getReportsThirdLevel_LR;

    }

    @Override
    public int getGroupCount() {
        return parentHeaders.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // no idea why this code is working
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupPosition;
    }

    @Override
    public Object getChild(int group, int child) {
        return child;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.nav_row_first, null);
        text = convertView.findViewById(R.id.rowParentText);
        bg_first = convertView.findViewById(R.id.bg_first);
        text.setText(this.parentHeaders[groupPosition]);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final SecondLevelExpandableListView secondLevelELV = new SecondLevelExpandableListView(context);

        final String[] headers = secondLevel.get(groupPosition);
        final List<String[]> childData = new ArrayList<>();
        final HashMap<String, String[]> secondLevelData = data.get(groupPosition);

        for(String key : secondLevelData.keySet()) {
            childData.add(secondLevelData.get(key));
        }

        secondLevelELV.setAdapter(new SecondLevelAdapter(context, headers,childData));
        secondLevelELV.setGroupIndicator(null);
        secondLevelELV.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if(groupPosition != previousGroup)
                    secondLevelELV.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });

        secondLevelELV.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                String selected = headers[i];
                String branch_id = Constants.branch_id;
                String selectednotes = Constants.selected_notes;

                if (branch_id.equals("")||selectednotes.equals("")){
                    Toast.makeText(context, "Please select branch or program", Toast.LENGTH_SHORT).show();
                } else {

                    if (selected.equals("Requisition Slip")){
                        switch_module(new Requisition_main(), context);
                    }else if(selected.equals("Price Watch")){
                        switch_module(new Price_watch_main(), context);
                    }else if(selected.equals("Purchase Order")){
                        switch_module(new Purchase_Order(), context);
                    }else if(selected.equals("Parity Report")){
                        switch_module(new Parity_report_main(), context);
                    }
                }
                return false;
            }
        });

        secondLevelELV.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

                if (Constants.selected_notes.equals("")){
                    Toast.makeText(context, "Please select program", Toast.LENGTH_SHORT).show();
                } else {
                    String selected_secondLevel = headers[i];

                        if(selected_secondLevel.equals("Swine Population")){
                            String selected_thirdlevel = reportsThirdLevel_SP[i1];
                            if(selected_thirdlevel.equals("By Age")) {
                                switch_module(new ByAge_main(), context);
                            } else if (selected_thirdlevel.equals("By Classification")){
                                switch_module(new byClass_main(), context);
                            } else if (selected_thirdlevel.equals("Line Graph")){
                                switch_module(new lineGraph_main(), context);
                            }

                        }else if(selected_secondLevel.equals("Farm Statistics")){
                            String selected_thirdlevel = reportsThirdLevel_FS[i1];
                            if (selected_thirdlevel.equals("By Month")) {
                                switch_module(new bymonth_main(), context);
                            } else if (selected_thirdlevel.equals("By Week")){
                                switch_module(new byweek_main(), context);
                            } else if (selected_thirdlevel.equals("By Region")){
                                switch_module(new byregion_main(), context);
                            }

                        }else if(selected_secondLevel.equals("Swine Delivery Report")){
                            String selected_thirdlevel = reportsThirdLevel_SD[i1];
                            if (selected_thirdlevel.equals("Daily Swine Delivery")){
                               switch_module(new daily_swine_delivery_main(), context);
                             }
                        }else if(selected_secondLevel.equals("Income Statement")){
                            String selected_thirdlevel = reportsThirdLevel_IS[i1];
                            if (selected_thirdlevel.equals("Periodic")) {
                                switch_module(new periodic_main(), context);
                            } else if (selected_thirdlevel.equals("Perpetual")){
                                switch_module(new perpetual_main(), context);

                            }
                        }else if(selected_secondLevel.equals("Check Voucher")){
                            String selected_thirdlevel = CV_module[i1];
                            if (selected_thirdlevel.equals("Request")) {
                                switch_module(new Check_Voucher_request_main(), context);
                            }
                        }else if(selected_secondLevel.equals("Petty Cash")){
                            String selected_thirdlevel = PC_module[i1];
                            if (selected_thirdlevel.equals("Liquidation")) {
                                switch_module(new PettyCash_liquidation_main(), context);
                            }else if (selected_thirdlevel.equals("Request")) {
                                switch_module(new PettyCash_request_main(), context);

                            }else if (selected_thirdlevel.equals("Replenish")) {
                                switch_module(new PettyCash_replenish_main(), context);
                            }
                        }else if(selected_secondLevel.equals("Cash Voucher")){

                            String selected_thirdlevel = CAV_module[i1];
                            if (selected_thirdlevel.equals("Request")) {
                                switch_module(new Cash_Voucher_request_main(), context);
                            }
                        }else if(selected_secondLevel.equals("Revolving Fund")){
                            String selected_thirdlevel = PC_module[i1];
                            if (selected_thirdlevel.equals("Liquidation")) {
                                switch_module(new Revolving_liquidation_main(), context);
                            }else if (selected_thirdlevel.equals("Request")) {
                                switch_module(new Revolving_request_main(), context);
                            }else if (selected_thirdlevel.equals("Replenish")) {
                                switch_module(new Revolving_replenish_main(), context);
                            }
                        }else if(selected_secondLevel.equals("Layer Reports")){
                            String selected_thirdlevel = getReportsThirdLevel_LR[i1];
                            if (selected_thirdlevel.equals("Brooding Report")) {
                                switch_module(new Brooding_Report(), context);
                            }
                        }
                }

                return false;
            }
        });

        return secondLevelELV;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void switch_module(Fragment fragment, Context context){
        moduleCloseNav();
        FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_home, fragment);
        fragmentTransaction.commit();
    }

    public void moduleCloseNav(){
        if(Constants.nav_close_open){
            Constants.nav_close_open = false;
        }
    }

}
