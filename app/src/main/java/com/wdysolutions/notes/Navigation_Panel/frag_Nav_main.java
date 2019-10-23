package com.wdysolutions.notes.Navigation_Panel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wdysolutions.notes.Constants;
import com.wdysolutions.notes.Navigation_Panel.ThreeLevelExpandable.ThreeLevelListAdapter;
import com.wdysolutions.notes.R;
import com.wdysolutions.notes.SQLite;
import com.wdysolutions.notes.SharedPref;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


public class frag_Nav_main extends Fragment {
    Spinner spinner;
    SQLite sqLite;
    String selectedBranch="";
    SharedPref sharedPref;
    ArrayList<Branch_model> branch_models = new ArrayList<>();
    ArrayList<Menu_model> menu_model = new ArrayList<>();
    ExpandableListView expandableListView;

    String category,access_type;
    View view_layout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view_layout = inflater.inflate(R.layout.nav_main, container, false);
        sqLite = new SQLite(getActivity());
        sharedPref = new SharedPref(getActivity());
        final String username = sharedPref.getUserInfo().get(sharedPref.KEY_NAME);
        expandableListView = view_layout.findViewById(R.id.expandible_listview);
        spinner = view_layout.findViewById(R.id.spinner);
        TextView txt_username = view_layout.findViewById(R.id.txt_username);
        txt_username.setText(username);
        category =  sharedPref.getUserInfo().get(sharedPref.KEY_CATEGORYID);

        //get_menu();


        access_type = sharedPref.getUserInfo().get(sharedPref.KEY_STATUS_PREVILAGE);


        initSpinnerBranch();
        return view_layout;
    }


    private void initSpinnerBranch(){
        getSQliteData();

        if(!Constants.branch_id.equals("")){

            expandableListView.setVisibility(View.VISIBLE);
            spinner.setSelection(Constants.branch_current_pos);
        }

        List<String> list = new ArrayList<>();
        for (int i=0; i<branch_models.size(); i++){
            list.add(branch_models.get(i).getBranch_name());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner, list);
        spinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_drop);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Branch_model click = branch_models.get(position);
                if (!click.getBranch_name().equals("Select Farm Location")){

                    selectedBranch = String.valueOf(click.getBranch_id());
                    Constants.branch = click.getBranch_name();
                    Constants.branch_id = selectedBranch;
                    Constants.branch_current_pos = position;
                    expandableListView.setVisibility(View.VISIBLE);

                    if(Integer.valueOf(category)>0){
                        if(access_type.equals("all_access")){
                            initExpandableListview(view_layout);

                            //if pig notes show transactions and reports
                            //else show mo lng income statement and transcations

                        }else{
                            // Toast.makeText(getContext(), String.valueOf(sqLite.get_levelzero("P").size()), Toast.LENGTH_SHORT).show();
                            limited_initExpandableListview(view_layout);
                            //limited
                        }
                    }else{
                        //if pig notes show transactions and reports
                        initExpandableListview(view_layout);
                    }

                } else {
                    spinner.setSelection(Constants.branch_current_pos);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void getSQliteData(){
        branch_models.add(new Branch_model("00", "Select Farm Location"));
        for (int i=0; i<sqLite.getBranch().size(); i++){
            Branch_model branch = sqLite.getBranch().get(i);
            branch_models.add(new Branch_model(branch.getBranch_id(), branch.getBranch_name()));
        }
    }


    private void initExpandableListview(View view){

        String selected_notes = Constants.selected_notes;

        List<LinkedHashMap<String, String[]>> data = new ArrayList<>();
        List<String[]> secondLevel = new ArrayList<>();
        String[] parent = new String[]{"Transactions","Reports"};
        LinkedHashMap<String, String[]> thirdLevelTransactions = new LinkedHashMap<>();
        LinkedHashMap<String, String[]> thirdLevelReports = new LinkedHashMap<>();

        String[] transactionsSecondLevel = new String[]{"Requisition Slip","Purchase Order","Check Voucher","Cash Voucher","Petty Cash","Revolving Fund"};

        ////////////////////////////// third level category names
        secondLevel.add(transactionsSecondLevel);


        ////////////////////////////// third level category names
        //globals
        thirdLevelTransactions.put(transactionsSecondLevel[0], null);
        thirdLevelTransactions.put(transactionsSecondLevel[1], null);

        String CV_module[] =  new String[]{"Request"};
        String CAV_module[] =  new String[]{"Request"};
        String PC_module[] =  new String[]{"Liquidation","Request","Replenish"};
        String RF_module[] =  new String[]{"Liquidation","Request","Replenish"};
        thirdLevelTransactions.put(transactionsSecondLevel[2], CV_module);
        thirdLevelTransactions.put(transactionsSecondLevel[3], CAV_module);
        thirdLevelTransactions.put(transactionsSecondLevel[4], PC_module);
        thirdLevelTransactions.put(transactionsSecondLevel[5], RF_module);

        //thirdLevelMovies.put(transactions[1], action);


        if(selected_notes.equals(getResources().getString(R.string.pigNOTES))){
            String[] reportsSecondLevel = new String[]{"Income Statement"
                    ,"Farm Statistics", "Swine Population", "Swine Delivery Report","Price Watch"};
            secondLevel.add(reportsSecondLevel);
            reportsThirdLevel_SP = new String[]{"By Age", "By Classification","Line Graph"};
            reportsThirdLevel_FS = new String[]{"By Month", "By Week", "By Region"};
            reportsThirdLevel_SD = new String[]{"Daily Swine Delivery"};
            reportsThirdLevel_IS = new String[]{"Periodic","Perpetual"};
            thirdLevelReports.put(reportsSecondLevel[0], reportsThirdLevel_IS);
            thirdLevelReports.put(reportsSecondLevel[1], reportsThirdLevel_FS);
            thirdLevelReports.put(reportsSecondLevel[2], reportsThirdLevel_SP);
            thirdLevelReports.put(reportsSecondLevel[3], reportsThirdLevel_SD);
            thirdLevelReports.put(reportsSecondLevel[4], null);

        }else if(selected_notes.equals(getResources().getString(R.string.feedNOTES))){
            String[] reportsSecondLevel = new String[]{"Income Statement","Price Watch"};
            secondLevel.add(reportsSecondLevel);
            reportsThirdLevel_IS = new String[]{"Periodic","Perpetual"};
            thirdLevelReports.put(reportsSecondLevel[0], reportsThirdLevel_IS);
            thirdLevelReports.put(reportsSecondLevel[1], null);
        }else if(selected_notes.equals(getResources().getString(R.string.eggNOTES))){
            String[] reportsSecondLevel = new String[]{"Income Statement","Price Watch"};
            secondLevel.add(reportsSecondLevel);
            reportsThirdLevel_IS = new String[]{"Periodic","Perpetual"};
            thirdLevelReports.put(reportsSecondLevel[0], reportsThirdLevel_IS);
            thirdLevelReports.put(reportsSecondLevel[1], null);
        }else if(selected_notes.equals(getResources().getString(R.string.broNOTES))){
            String[] reportsSecondLevel = new String[]{"Income Statement","Price Watch"};
            secondLevel.add(reportsSecondLevel);
            reportsThirdLevel_IS = new String[]{"Periodic","Perpetual"};
            thirdLevelReports.put(reportsSecondLevel[0], reportsThirdLevel_IS);
            thirdLevelReports.put(reportsSecondLevel[1], null);
        }


        data.add(thirdLevelTransactions);
        data.add(thirdLevelReports);


        ThreeLevelListAdapter threeLevelListAdapterAdapter = new ThreeLevelListAdapter(getActivity(), parent, secondLevel, data,reportsThirdLevel_SP,
                reportsThirdLevel_IS,reportsThirdLevel_SD,reportsThirdLevel_FS,CV_module,CAV_module,PC_module,RF_module);
        expandableListView.setAdapter(threeLevelListAdapterAdapter);

        // OPTIONAL : Show one list at a time
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;
            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup)
                    expandableListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });
    }

    String[] parent;
    String[] transactionsSecondLevel;
    String[] reportsSecondLevel;
    LinkedHashMap<String, String[]> thirdLevelTransactions = new LinkedHashMap<>();
    LinkedHashMap<String, String[]> thirdLevelReports = new LinkedHashMap<>();
    List<String[]> secondLevel = new ArrayList<>();
    String[] reportsThirdLevel_SP;
    String[] reportsThirdLevel_IS;
    String[] reportsThirdLevel_SD;
    String[] reportsThirdLevel_FS;
    String[] reportsThirdLevel_PW;

    String[] expense_CV;
    String[] expense_PC;
    String[] expense_RF;
    String[] expense_CAV;

    private void limited_initExpandableListview(View view){

        String program_code = Constants.program_code;
       // Toast.makeText(getContext(), String.valueOf(sqLite.get_levelzero(program_code,selectedBranch).size()), Toast.LENGTH_SHORT).show();
        List<LinkedHashMap<String, String[]>> data = new ArrayList<>();
        parent = new String[sqLite.get_levelzero(program_code,selectedBranch).size()];

        for (int i=0; i<sqLite.get_levelzero(program_code,selectedBranch).size(); i++){
            Menu_model menu = sqLite.get_levelzero(program_code,selectedBranch).get(i);
            String sql_menu_id = menu.getMenu_id();
            String sql_level = menu.getLevel();
            String sql_parent = menu.getParent();
            String sql_name = menu.getMenu_name();
            parent[i] = sql_name;

            if(sql_name.equals("TRANSACTIONS")){

                int expense_counter = 0;
                int initial_expense = 0;

                //count expense
                for (int x=0; x<sqLite.get_Transactions(sql_menu_id,program_code,selectedBranch).size(); x++){
                    Menu_model trans_menu = sqLite.get_Transactions(sql_menu_id,program_code,selectedBranch).get(x);
                    String t_menu_id = trans_menu.getMenu_id();
                    String t_name = trans_menu.getMenu_name();
                    if(t_name.equals("Expense")) {
                        initial_expense = sqLite.get_Expense(t_menu_id,program_code,selectedBranch).size();

                    }

                }

                int transaction =sqLite.get_Transactions(sql_menu_id,program_code,selectedBranch).size();

                transactionsSecondLevel = new String[(transaction-1)+initial_expense];
                for (int x=0; x<sqLite.get_Transactions(sql_menu_id,program_code,selectedBranch).size(); x++){
                    Menu_model trans_menu = sqLite.get_Transactions(sql_menu_id,program_code,selectedBranch).get(x);
                    String t_menu_id = trans_menu.getMenu_id();
                    String t_level = trans_menu.getLevel();
                    String t_parent = trans_menu.getParent();
                    String t_name = trans_menu.getMenu_name();
                    String prog = trans_menu.getProgram_type();

//                    Toast.makeText(getContext(), prog, Toast.LENGTH_SHORT).show();



                    if(t_name.equals("Expense")){

                        for (int y=0; y<sqLite.get_Expense(t_menu_id,program_code,selectedBranch).size(); y++){
                            Menu_model expense_menu = sqLite.get_Expense(t_menu_id,program_code,selectedBranch).get(y);
                            String emenu_id = expense_menu.getMenu_id();
                            String e_name = expense_menu.getMenu_name();

                            if(e_name.equals("Check Voucher")){
                                expense_CV = new String[sqLite.get_child(emenu_id,program_code,selectedBranch).size()];
                                for (int z=0; z<sqLite.get_child(emenu_id,program_code,selectedBranch).size(); z++){
                                    Menu_model cv_menu = sqLite.get_child(emenu_id,program_code,selectedBranch).get(z);
                                    String cv_name = cv_menu.getMenu_name();
                                    expense_CV[z] = cv_name;
                                }
//
                                transactionsSecondLevel[expense_counter] = e_name;
                                thirdLevelTransactions.put(transactionsSecondLevel[expense_counter], expense_CV);
                                expense_counter++;
                            }else if(e_name.equals("Petty Cash")){

                                expense_PC = new String[sqLite.get_child(emenu_id,program_code,selectedBranch).size()];
                                for (int z=0; z<sqLite.get_child(emenu_id,program_code,selectedBranch).size(); z++){
                                    Menu_model cv_menu = sqLite.get_child(emenu_id,program_code,selectedBranch).get(z);
                                    String cv_name = cv_menu.getMenu_name();
                                    expense_PC[z] = cv_name;
                                }

                                transactionsSecondLevel[expense_counter] = e_name;
                                thirdLevelTransactions.put(transactionsSecondLevel[expense_counter], expense_PC);
                                expense_counter++;
                            }else if(e_name.equals("Revolving Fund")){

                                expense_RF = new String[sqLite.get_child(emenu_id,program_code,selectedBranch).size()];
                                for (int z=0; z<sqLite.get_child(emenu_id,program_code,selectedBranch).size(); z++){
                                    Menu_model cv_menu = sqLite.get_child(emenu_id,program_code,selectedBranch).get(z);
                                    String cv_name = cv_menu.getMenu_name();
                                    expense_RF[z] = cv_name;
                                }

                                transactionsSecondLevel[expense_counter] = e_name;
                                thirdLevelTransactions.put(transactionsSecondLevel[expense_counter], expense_RF);
                                expense_counter++;
                            }else if(e_name.equals("Cash Voucher")){

                                expense_CAV = new String[sqLite.get_child(emenu_id,program_code,selectedBranch).size()];
                                for (int z=0; z<sqLite.get_child(emenu_id,program_code,selectedBranch).size(); z++){
                                    Menu_model cv_menu = sqLite.get_child(emenu_id,program_code,selectedBranch).get(z);
                                    String cv_name = cv_menu.getMenu_name();
                                    expense_CAV[z] = cv_name;
                                }

                                transactionsSecondLevel[expense_counter] = e_name;
                                thirdLevelTransactions.put(transactionsSecondLevel[expense_counter], expense_CAV);
                                expense_counter++;
                            }
                        }

                    }else{

                        transactionsSecondLevel[expense_counter] = t_name;
                        thirdLevelTransactions.put(transactionsSecondLevel[expense_counter], null);
                        expense_counter++;

                    }


                }
                secondLevel.add(transactionsSecondLevel);
                data.add(thirdLevelTransactions);

            }else if(sql_name.equals("REPORTS")){

                int count_farm_reports = 0;
                int count_reports = 0;
                int report_counter = 0;
                int last_counter =0;

                //exceptions counting farm reports
                count_reports = sqLite.get_Reports(sql_menu_id,program_code,selectedBranch).size();

                for (int x=0; x<sqLite.get_Reports(sql_menu_id,program_code,selectedBranch).size(); x++){
                    Menu_model rep_menu = sqLite.get_Reports(sql_menu_id,program_code,selectedBranch).get(x);
                    String r_menu_id = rep_menu.getMenu_id();
                    String r_name = rep_menu.getMenu_name();
                    if(r_name.equals("Farm Reports")){
                        count_reports--;
                        count_farm_reports = sqLite.get_farm_reports(r_menu_id,program_code,selectedBranch).size();
                    }

                }

                reportsSecondLevel = new String[count_farm_reports+count_reports];
                 for (int x=0; x<sqLite.get_Reports(sql_menu_id,program_code,selectedBranch).size(); x++){

                     Menu_model rep_menu = sqLite.get_Reports(sql_menu_id,program_code,selectedBranch).get(x);
                     String r_menu_id = rep_menu.getMenu_id();
                     String r_level = rep_menu.getLevel();
                     String r_parent = rep_menu.getParent();
                     String r_name = rep_menu.getMenu_name();

                     if(r_name.equals("Farm Reports")){

                         for (int y=0; y<sqLite.get_farm_reports(r_menu_id,program_code,selectedBranch).size(); y++){
                             Menu_model f_menu = sqLite.get_farm_reports(r_menu_id,program_code,selectedBranch).get(y);
                             String f_menu_id = f_menu.getMenu_id();
                             String f_level = f_menu.getLevel();
                             String f_parent = f_menu.getParent();
                             String f_name = f_menu.getMenu_name();
                             if(f_name.equals("Farm Statistics")){
                                 reportsThirdLevel_FS = new String[sqLite.get_child(f_menu_id,program_code,selectedBranch).size()];
                                 for (int z=0; z<sqLite.get_child(f_menu_id,program_code,selectedBranch).size(); z++){
                                     Menu_model fs_menu = sqLite.get_child(f_menu_id,program_code,selectedBranch).get(z);
                                     String fs_name = fs_menu.getMenu_name();
                                     reportsThirdLevel_FS[z] = fs_name;
                                 }

                                 reportsSecondLevel[report_counter] = f_name;
                                 thirdLevelReports.put(reportsSecondLevel[report_counter], reportsThirdLevel_FS);
                                 report_counter++;

                             }else if(f_name.equals("Swine Population")){

                                 reportsThirdLevel_SP = new String[sqLite.get_child(f_menu_id,program_code,selectedBranch).size()];
                                 for (int z=0; z<sqLite.get_child(f_menu_id,program_code,selectedBranch).size(); z++){
                                     Menu_model sp_menu = sqLite.get_child(f_menu_id,program_code,selectedBranch).get(z);
                                     String fs_name = sp_menu.getMenu_name();
                                     reportsThirdLevel_SP[z] = fs_name;
                                 }

                                 reportsSecondLevel[report_counter] = f_name;
                                 thirdLevelReports.put(reportsSecondLevel[report_counter], reportsThirdLevel_SP);
                                 report_counter++;
                             }
                         }
                     }else if(r_name.equals("Swine Delivery Report")){
                         reportsThirdLevel_SD = new String[sqLite.get_swine_del_reports(r_menu_id,program_code,selectedBranch).size()];
                         for (int y=0; y<sqLite.get_swine_del_reports(r_menu_id,program_code,selectedBranch).size(); y++){
                             Menu_model d_menu = sqLite.get_swine_del_reports(r_menu_id,program_code,selectedBranch).get(y);
                             String d_menu_id = d_menu.getMenu_id();
                             String d_level = d_menu.getLevel();
                             String d_parent = d_menu.getParent();
                             String d_name = d_menu.getMenu_name();

                             if(d_name.equals("Daily Swine Delivery")){
                                 reportsThirdLevel_SD[y] = d_name;
                             }
                         }
                         reportsSecondLevel[report_counter] = r_name;
                         thirdLevelReports.put(reportsSecondLevel[report_counter], reportsThirdLevel_SD);
                         report_counter++;

                     }else if(r_name.equals("Income Statement")){
                         reportsThirdLevel_IS = new String[sqLite.get_income_statement(r_menu_id,program_code,selectedBranch).size()];
                         for (int y=0; y<sqLite.get_income_statement(r_menu_id,program_code,selectedBranch).size(); y++){
                             Menu_model d_menu = sqLite.get_income_statement(r_menu_id,program_code,selectedBranch).get(y);
                             String d_menu_id = d_menu.getMenu_id();
                             String d_level = d_menu.getLevel();
                             String d_parent = d_menu.getParent();
                             String d_name = d_menu.getMenu_name();

                             if(d_name.equals("Perpetual")||d_name.equals("Periodic")){
                                 reportsThirdLevel_IS[y] = d_name;
                             }
                         }
                         reportsSecondLevel[report_counter] = r_name;
                         thirdLevelReports.put(reportsSecondLevel[report_counter], reportsThirdLevel_IS);
                         report_counter++;
                     }else if(r_name.equals("Price Watch")){

                         reportsSecondLevel[report_counter] = r_name;
                         thirdLevelReports.put(reportsSecondLevel[report_counter], null);
                         report_counter++;
                     }
                }
                secondLevel.add(reportsSecondLevel);
                data.add(thirdLevelReports);
            }

        }

        expandableListView = view.findViewById(R.id.expandible_listview);
        ThreeLevelListAdapter threeLevelListAdapterAdapter = new ThreeLevelListAdapter(getActivity(), parent, secondLevel, data, reportsThirdLevel_SP,reportsThirdLevel_IS,reportsThirdLevel_SD
                ,reportsThirdLevel_FS,expense_CV,expense_CAV,
                expense_PC, expense_RF);
        expandableListView.setAdapter(threeLevelListAdapterAdapter);

        // OPTIONAL : Show one list at a time
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;
            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup)
                    expandableListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });
    }

}
