package com.wdysolutions.notes.Login;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wdysolutions.notes.AppController;
import com.wdysolutions.notes.Home.frag_Home_main;
import com.wdysolutions.notes.R;
import com.wdysolutions.notes.SQLite;
import com.wdysolutions.notes.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class frag_Login_main extends Fragment {

    SharedPref sharedPref;
    SQLite sqLite;
    Button btn_save;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_main, container, false);
        sharedPref = new SharedPref(getActivity());
        sqLite = new SQLite(getActivity());

        btn_save = view.findViewById(R.id.btn_save);
        TextView text_copyright = view.findViewById(R.id.text_copyright);
        final EditText edtxt_username = view.findViewById(R.id.edtxt_username);
        final EditText edtxt_password = view.findViewById(R.id.edtxt_password);

        text_copyright.setText(String.valueOf("©Copyright NOTES "+ getCurrentYear()+". All Rights Reserved. \nPowered by Bacolod Four Leaf Clover Corporation."));

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtxt_username.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Please enter username", Toast.LENGTH_SHORT).show();
                } else if (edtxt_username.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Please enter password", Toast.LENGTH_SHORT).show();
                } else {
                    checkAccount(edtxt_username.getText().toString(), edtxt_password.getText().toString());
                }
            }
        });

        return view;
    }

    private int getCurrentYear(){
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    public void checkAccount(final String username, final String password){
        btn_save.setText("Checking...");
        btn_save.setClickable(false);
        String URL =  getString(R.string.URL_online)+"login3.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //((MainActivity)getActivity()).openDialog(response);
           
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response_");
                    JSONObject object = (JSONObject)jsonArray.get(0);
                    String company_code = object.getString("company_code");
                    String response_login = object.getString("response_login");
                    String company_id = object.getString("company_id");
                    String user_id = object.getString("user_id");
                    String user_code = object.getString("user_code");
                    String status = object.getString("status");
                    String name = object.getString("name");
                    String category_id = object.getString("category_id");
                   // Toast.makeText(getActivity(), category_id, Toast.LENGTH_SHORT).show();

                    if (status.equals("1")){
                        JSONArray jsonArray2 = jsonObject.getJSONArray("data_program");
                        JSONObject object2 = (JSONObject)jsonArray2.get(0);
                        String program_pig = object2.getString("program_pig");
                        String program_egg = object2.getString("program_egg");
                        String program_feed = object2.getString("program_feed");
                        String program_bro = object2.getString("program_bro");

                        JSONArray jsonArray_2 = jsonObject.getJSONArray("response_branch");
                        for (int i=0; i<jsonArray_2.length(); i++){
                            JSONObject object_2 = (JSONObject)jsonArray_2.get(i);
                            String branch_id = object_2.getString("branch_id");
                            String branch = object_2.getString("branch");
                            sqLite.saveBranch(branch_id, branch);
                        }

                        JSONArray jsonArray_3 = jsonObject.getJSONArray("data_menu_privilage");
                        JSONObject object_status = (JSONObject)jsonArray_3.get(0);
                        String status_previlage = object_status.getString("status");

                        for (int i=0; i<jsonArray_3.length(); i++){
                            JSONObject object_3 = (JSONObject)jsonArray_3.get(i);
                            String menu_id = object_3.getString("menu_id");
                            String level = object_3.getString("level");
                            String parent = object_3.getString("parent");
                            String menu_name = object_3.getString("menu_name");
                            String program = object_3.getString("program");
                            String branch_id = object_3.getString("branch_id");
                            sqLite.insert_menu(menu_id,level,parent,program,menu_name,branch_id);

                        }
                       // Toast.makeText(getActivity(), program_pig+"  "+program_egg+"  "+program_feed+"  "+program_bro, Toast.LENGTH_SHORT).show();

                        sharedPref.saveUserInfo(user_code, company_code, name, user_id, company_id, category_id,
                                program_pig,program_feed,program_egg,program_bro,status_previlage);

                        goToHome();

                    } else {
                        Toast.makeText(getActivity(), "Account not found.", Toast.LENGTH_SHORT).show();
                        btn_save.setText("Login");
                        btn_save.setClickable(true);
                    }

                } catch (JSONException e) {}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                btn_save.setText("Login");
                btn_save.setClickable(true);
                Toast.makeText(getActivity(), "Error Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("username", username);
                hashMap.put("password", password);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void goToHome(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, new frag_Home_main());
        fragmentTransaction.commit();
    }

}
