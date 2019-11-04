package com.wdysolutions.notes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wdysolutions.notes.Navigation_Panel.Branch_model;
import com.wdysolutions.notes.Navigation_Panel.Menu_model;

import java.util.ArrayList;

public class SQLite extends SQLiteOpenHelper {

    private Context context;
    private static final int DB_VERSION = 3;
    private static final String DB_NAME = "wdysolutions_notes.db";

    private static final String TABLE_BRANCH = "tbl_branch";
    private static final String ID = "ID";
    private static final String BRANCH_ID = "BRANCH_ID";
    private static final String BRANCH_NAME = "NAME";
    private static final String CREATE_TABLE_BRANCH = "CREATE TABLE " + TABLE_BRANCH + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + BRANCH_ID + " VARCHAR(255) , " + BRANCH_NAME + " VARCHAR(255));";
    private static final String DROP_TABLE_BRANCH = "DROP TABLE IF EXISTS " + TABLE_BRANCH;


    private static final String TABLE_MENU = "tbl_menu";
    private static final String M_ID = "ID";
    private static final String MENU_ID = "MENU_ID";
    private static final String LEVEL = "LEVEL";
    private static final String PARENT = "PARENT";
    private static final String MENU_NAME = "NAME";
    private static final String PROGRAM_TYPE = "PROGRAM";
    private static final String MENU_BRANCH = "BRANCH_ID";
    private static final String CREATE_TABLE_MENU ="CREATE TABLE "
            + TABLE_MENU + " ( "
            + M_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
            + MENU_ID + " VARCHAR(255) , "
            + LEVEL + " VARCHAR(255) , "
            + PARENT + " VARCHAR(255) , "
            + PROGRAM_TYPE + " VARCHAR(255) , "
            + MENU_NAME + " VARCHAR(255) , "
            + MENU_BRANCH+" VARCHAR(255));";
    private static final String DROP_TABLE_MENU = "DROP TABLE IF EXISTS " + TABLE_MENU;



    public SQLite(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_BRANCH);
        sqLiteDatabase.execSQL(CREATE_TABLE_MENU);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE_BRANCH);
        sqLiteDatabase.execSQL(DROP_TABLE_MENU);
        onCreate(sqLiteDatabase);
    }

    public void delete_all(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MENU,null,null);
        db.delete(TABLE_BRANCH,null,null);
    }



    public void saveBranch(String id, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BRANCH_ID, id);
        contentValues.put(BRANCH_NAME, name);
        db.insert(TABLE_BRANCH, null, contentValues);
        db.close();
    }

    public ArrayList<Branch_model> getBranch() {
        ArrayList<Branch_model> questionsList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        String coloumn[] = {BRANCH_ID, BRANCH_NAME};
        Cursor cursor = db.query(TABLE_BRANCH, coloumn, null, null, null, null, null);

        while (cursor.moveToNext()) {
            Branch_model model = new Branch_model(cursor.getString(0), cursor.getString(1));
            questionsList.add(model);
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        cursor.close();
        db.close();
        return questionsList;
    }



    public void insert_menu(String menu_id, String level,String parent,String program_type, String menu_name, String branch_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MENU_ID, menu_id);
        contentValues.put(LEVEL, level);
        contentValues.put(PARENT, parent);
        contentValues.put(PROGRAM_TYPE, program_type);
        contentValues.put(MENU_NAME, menu_name);
        contentValues.put(MENU_BRANCH, branch_id);
        db.insert(TABLE_MENU, null, contentValues);
        db.close();
    }

    public ArrayList<Menu_model> getMenu() {
        ArrayList<Menu_model> menuList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        String coloumn[] = {MENU_ID, LEVEL,PARENT,PROGRAM_TYPE,MENU_NAME,MENU_BRANCH};
        Cursor cursor = db.query(TABLE_MENU, coloumn, null, null, null, null, null);

        while (cursor.moveToNext()) {
            Menu_model model = new Menu_model(cursor.getString(0), cursor.getString(1),cursor.getString(2), cursor.getString(3), cursor.getString(4));
            menuList.add(model);
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        cursor.close();
        db.close();
        return menuList;
    }


    public ArrayList<Menu_model> get_levelzero(String program,String branch_id) {
        ArrayList<Menu_model> menuList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        String coloumn[] = {MENU_ID, LEVEL,PARENT,PROGRAM_TYPE,MENU_NAME,MENU_BRANCH};
        Cursor cursor = db.query(TABLE_MENU, coloumn,  LEVEL+" = '0' and "+MENU_BRANCH+" = "+branch_id, null, null, null, null);

        while (cursor.moveToNext()) {
            if(cursor.getString(3).equals(program)||cursor.getString(3).equals("G")) {
                if (cursor.getString(4).equals("TRANSACTIONS")
                        || cursor.getString(4).equals("REPORTS")) {
                    Menu_model model = new Menu_model(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
                    menuList.add(model);
                }

            }

        }

        db.setTransactionSuccessful();
        db.endTransaction();
        cursor.close();
        db.close();
        return menuList;
    }

    public ArrayList<Menu_model> get_Transactions(String parent,String program,String branch_id) {
        ArrayList<Menu_model> menuList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        String coloumn[] = {MENU_ID, LEVEL,PARENT,PROGRAM_TYPE,MENU_NAME,MENU_BRANCH};
        Cursor cursor = db.query(TABLE_MENU, coloumn,  PARENT+" = "+parent+" and "+MENU_BRANCH+" = "+branch_id, null, null, null, null);

        while (cursor.moveToNext()) {
            if(cursor.getString(3).equals(program)||cursor.getString(3).equals("G")) {
                if (cursor.getString(4).equals("Purchase Order")||cursor.getString(4).equals("Requisition Slip")||cursor.getString(4).equals("Expense")) {
                    Menu_model model = new Menu_model(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
                    menuList.add(model);
                }
            }
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        cursor.close();
        db.close();
        return menuList;
    }

    public ArrayList<Menu_model> get_Reports(String parent,String program,String branch_id) {
        ArrayList<Menu_model> menuList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        String coloumn[] = {MENU_ID, LEVEL,PARENT,PROGRAM_TYPE,MENU_NAME,MENU_BRANCH};
        Cursor cursor = db.query(TABLE_MENU, coloumn,  PARENT+" = "+parent+" and "+MENU_BRANCH+" = "+branch_id, null, null, null, null);

        while (cursor.moveToNext()) {
            if(cursor.getString(3).equals(program)||cursor.getString(3).equals("G")) {
                if (cursor.getString(4).equals("Farm Reports")
                        || cursor.getString(4).equals("Swine Delivery Report")
                        || cursor.getString(4).equals("Farm Statistics")
                        || cursor.getString(4).equals("Swine Population")
                        || cursor.getString(4).equals("Parity Report")
                        || cursor.getString(4).equals("Income Statement")
                        || cursor.getString(4).equals("Price Watch")) {
                    Menu_model model = new Menu_model(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
                    menuList.add(model);

                }
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        cursor.close();
        db.close();
        return menuList;
    }


    public ArrayList<Menu_model> get_farm_reports(String Parent,String program,String branch_id) {
        ArrayList<Menu_model> menuList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        String coloumn[] = {MENU_ID, LEVEL,PARENT,PROGRAM_TYPE,MENU_NAME,MENU_BRANCH};
        Cursor cursor = db.query(TABLE_MENU, coloumn,  PARENT+" = "+Parent+" and "+MENU_BRANCH+" = "+branch_id, null, null, null, null);

        while (cursor.moveToNext()) {
            if(cursor.getString(3).equals(program)||cursor.getString(3).equals("G")) {
                if (cursor.getString(4).equals("Farm Statistics") || cursor.getString(4).equals("Swine Population") || cursor.getString(4).equals("Parity Report")) {
                    Menu_model model = new Menu_model(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
                    menuList.add(model);
                }
            }

        }

        db.setTransactionSuccessful();
        db.endTransaction();
        cursor.close();
        db.close();
        return menuList;
    }

    public ArrayList<Menu_model> get_child(String Parent,String program,String branch_id) {
        ArrayList<Menu_model> menuList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        String coloumn[] = {MENU_ID, LEVEL,PARENT,PROGRAM_TYPE,MENU_NAME,MENU_BRANCH};
        Cursor cursor = db.query(TABLE_MENU, coloumn, PARENT+" = "+Parent+" and "+MENU_BRANCH+" = "+branch_id, null, null, null, null);

        while (cursor.moveToNext()) {
            if(cursor.getString(3).equals(program)||cursor.getString(3).equals("G")) {
                if(cursor.getString(4).equals("Line Graph")){ // hide line graph

                }else{

                    Menu_model model = new Menu_model(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
                    menuList.add(model);
                }

            }
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        cursor.close();
        db.close();
        return menuList;
    }


    public ArrayList<Menu_model> get_swine_del_reports(String Parent,String program,String branch_id) {
        ArrayList<Menu_model> menuList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        String coloumn[] = {MENU_ID, LEVEL,PARENT,PROGRAM_TYPE,MENU_NAME,MENU_BRANCH};
        Cursor cursor = db.query(TABLE_MENU, coloumn, PARENT+" = "+Parent+" and "+MENU_BRANCH+" = "+branch_id, null, null, null, null);

        while (cursor.moveToNext()) {
            if(cursor.getString(3).equals(program)||cursor.getString(3).equals("G")) {
                if (cursor.getString(4).equals("Daily Swine Delivery")) {
                    Menu_model model = new Menu_model(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
                    menuList.add(model);
                }
            }
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        cursor.close();
        db.close();
        return menuList;
    }



    public ArrayList<Menu_model> get_income_statement(String Parent,String program,String branch_id) {
        ArrayList<Menu_model> menuList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        String coloumn[] = {MENU_ID, LEVEL,PARENT,PROGRAM_TYPE,MENU_NAME,MENU_BRANCH};
        Cursor cursor = db.query(TABLE_MENU, coloumn, PARENT+" = "+Parent+" and "+MENU_BRANCH+" = "+branch_id, null, null, null, null);

        while (cursor.moveToNext()) {
            if(cursor.getString(3).equals(program)||cursor.getString(3).equals("G")) {
                if (cursor.getString(4).equals("Perpetual")||cursor.getString(4).equals("Periodic")) {
                    Menu_model model = new Menu_model(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
                    menuList.add(model);
                }
            }
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        cursor.close();
        db.close();
        return menuList;
    }


    public ArrayList<Menu_model> get_Expense(String Parent,String program,String branch_id) {
        ArrayList<Menu_model> menuList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        String coloumn[] = {MENU_ID, LEVEL,PARENT,PROGRAM_TYPE,MENU_NAME,MENU_BRANCH};
        Cursor cursor = db.query(TABLE_MENU, coloumn,  PARENT+" = "+Parent+" and "+MENU_BRANCH+" = "+branch_id, null, null, null, null);

        while (cursor.moveToNext()) {
            if(cursor.getString(3).equals(program)||cursor.getString(3).equals("G")) {
                if (cursor.getString(4).equals("Check Voucher")||
                        cursor.getString(4).equals("Petty Cash")||
                        cursor.getString(4).equals("Revolving Fund")||
                        cursor.getString(4).equals("Cash Voucher")) {
                    Menu_model model = new Menu_model(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
                    menuList.add(model);
                }
            }

        }

        db.setTransactionSuccessful();
        db.endTransaction();
        cursor.close();
        db.close();
        return menuList;
    }


}



