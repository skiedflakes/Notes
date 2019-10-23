package com.wdysolutions.notes;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class SharedPref {

    public SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private final String PREF_NAME = "QRcode_sharedpref_";

    private final String isUSERSAVED = "isUSERSAVED";
    public final String KEY_USERID = "user_id";
    public final String KEY_COMPANYID = "company_id";
    public final String KEY_COMPANYCODE = "company_code";
    public final String KEY_NAME = "name";
    public final String KEY_USERCODE = "user_code";
    public final String KEY_CATEGORYID = "category_id";
    public final String KEY_PIG = "program_pig";
    public final String KEY_FEEDS = "program_feeds";
    public final String KEY_EGG = "program_egg";
    public final String KEY_BROILER = "program_broiler";
    public final String KEY_STATUS_PREVILAGE = "status_previlige";

    public SharedPref(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void saveUserInfo(String user_code, String company_code, String name, String user_id, String company_id,
                             String category_id,String program_pig,String program_feeds,String program_egg,
                             String program_broiler,String status_previlige){
        editor.putBoolean(isUSERSAVED, true);
        editor.putString(KEY_COMPANYID, company_id);
        editor.putString(KEY_USERID, user_id);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_COMPANYCODE, company_code);
        editor.putString(KEY_USERCODE, user_code);
        editor.putString(KEY_CATEGORYID, category_id);

        editor.putString(KEY_PIG, program_pig);
        editor.putString(KEY_FEEDS, program_feeds);
        editor.putString(KEY_EGG, program_egg);
        editor.putString(KEY_BROILER, program_broiler);
        editor.putString(KEY_STATUS_PREVILAGE, status_previlige);
        editor.commit();
    }

    public HashMap<String, String> getUserInfo(){
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_COMPANYCODE, pref.getString(KEY_COMPANYCODE, null));
        user.put(KEY_USERCODE, pref.getString(KEY_USERCODE, null));
        user.put(KEY_USERID, pref.getString(KEY_USERID, null));
        user.put(KEY_COMPANYID, pref.getString(KEY_COMPANYID, null));
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_CATEGORYID, pref.getString(KEY_CATEGORYID, null));

        user.put(KEY_PIG, pref.getString(KEY_PIG, null));
        user.put(KEY_FEEDS, pref.getString(KEY_FEEDS, null));
        user.put(KEY_EGG, pref.getString(KEY_EGG, null));
        user.put(KEY_BROILER, pref.getString(KEY_BROILER, null));
        user.put(KEY_STATUS_PREVILAGE, pref.getString(KEY_STATUS_PREVILAGE, null));
        return user;
    }

    public boolean isLogin(){
        return pref.getBoolean(isUSERSAVED, false);
    }

    public void clear(){
        editor.clear();
        editor.commit();
    }



}