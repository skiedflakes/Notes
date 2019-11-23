package com.wdysolutions.notes;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.EditText;

import com.wdysolutions.notes.Globals.Micro_Filming.Microfilming_main;
import com.wdysolutions.notes.Home.frag_Home_main;
import com.wdysolutions.notes.Login.frag_Login_main;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity {
    SQLite sqLite;
    SharedPref sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setDefaultFragment(savedInstanceState);
        handleSSLHandshake();
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        sqLite = new SQLite(getBaseContext());
        sharedPref = new SharedPref(getBaseContext());

        //openDialogDebug();
    }

    public void openMicro(String tracking_num, String start_date, String end_date){
        Bundle bundle = new Bundle();
        bundle.putString("tracking_num", tracking_num);
        bundle.putString("start_date", start_date);
        bundle.putString("end_date", end_date);
        DialogFragment dialogFragment = new Microfilming_main();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {ft.remove(prev);}
        ft.addToBackStack(null);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(ft, "dialog");
    }

    public ProgressDialog showLoading(ProgressDialog loading, String msg){
        loading.setMessage(msg);
        loading.setCancelable(false);
        return loading;
    }

    public void openDialog(String test){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        EditText editText = new EditText(MainActivity.this);
        editText.setText(test);
        alertDialog.setView(editText);
        alertDialog.setPositiveButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public void openDialog_logout(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Are you sure you want to logout?");
        alertDialog.setPositiveButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.setNegativeButton("OK",  new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {

                //sqlite
                sqLite.delete_all();

                //shared preference
                sharedPref.clear();

                //constants delete all
                Constants.selected_notes = "";
                Constants.branch="";
                Constants.branch_id ="";
                Constants.branch_current_pos = 0;
                Constants.program_code ="";

                //
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, new frag_Login_main());
                fragmentTransaction.commit();

            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public void openDialogDebug(){
        SharedPref sharedPref = new SharedPref(MainActivity.this);
        String company_id = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYID);
        String company_code = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYCODE);
        String user_id = sharedPref.getUserInfo().get(sharedPref.KEY_USERID);
        String categ_id = sharedPref.getUserInfo().get(sharedPref.KEY_CATEGORYID);
        String user_code = sharedPref.getUserInfo().get(sharedPref.KEY_USERCODE);
        String user_name = sharedPref.getUserInfo().get(sharedPref.KEY_NAME);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setMessage("Company Id: "+company_id+
                "\nComapany Code: "+company_code+
                "\nUser Id: "+user_id+
                "\nCategory Id: "+categ_id+
                "\nUser Code: "+user_code+
                "\nUser Name: "+user_name);
        alertDialog.setPositiveButton("Close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void setDefaultFragment(Bundle savedInstanceState){
        if (savedInstanceState == null){
            SharedPref sharedPref = new SharedPref(MainActivity.this);
            Fragment selectedFrag;
            if (sharedPref.isLogin()){
                selectedFrag = new frag_Home_main();
            } else {
                selectedFrag = new frag_Login_main();
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container, selectedFrag);
            fragmentTransaction.commit();
        }
    }

    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception ignored) {}
    }
}
