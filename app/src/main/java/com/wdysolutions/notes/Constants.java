package com.wdysolutions.notes;

import com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Month.tableview.model.RowHeader_byMonth;
import com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Week.Spinner_Check.Weeks_model;
import com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Week.tableview.model.RowHeader;
import com.wdysolutions.notes.Notes_Pig.Swine_Population.byAge.tableview.model.SP_ColumnHeader;
import com.wdysolutions.notes.Notes_Pig.Swine_Population.byClass.tableview.model.ColumnHeader_byClassi;

import java.util.ArrayList;
import java.util.List;

public class Constants {
   public static String variants = BuildConfig.BASE_URL;
   public static String branch = "";
   public static String branch_id = "";
   public static int branch_current_pos = 0;
   public static String selected_notes = "";
   public static String program_code = "";
   public static Boolean nav_close_open = false;
   public static ArrayList<Weeks_model> weeks_models; // Farm Stats by Week
   public static ArrayList<RowHeader> rowHeaders; // Farm Stats by Week
   public static ArrayList<RowHeader_byMonth> rowHeaders_bymonth; // Farm Stats by Month
   public static String selectedYear; // Farm Stats by Week
   public static int current_year_online;
   public static List<ColumnHeader_byClassi> byClassi_list; // Swine Population by classification
   public static ArrayList<SP_ColumnHeader> SP_ColumnHeader; // Swine Population by age
   public static ArrayList<String> SP_type; // Swine Population by age


   //dialog request codes
   public static int cash_voucher_request_main_dialog = 200;
   public static int check_voucher_request_main_dialog = 201;
   public static int Requisition_add = 203;

   public static int purchase_order = 100;

}
