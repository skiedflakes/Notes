package com.wdysolutions.notes;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class Authenticate_DialogFragment extends DialogFragment {
    SharedPref sharedPref;
    EditText et_username,et_password;
    Button btn_continue,btn_close;
    //user session
    String user_id, company_id,company_code,category_id,selected_branch_id;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.authenticate_dialog, container, false);

        et_username = view.findViewById(R.id.et_username);
        et_password = view.findViewById(R.id.et_password);
        btn_continue = view.findViewById(R.id.btn_continue);
        btn_close = view.findViewById(R.id.btn_close);

        et_username.setText("");
        et_password.setText("");

        //user
        sharedPref = new SharedPref(getActivity());
        company_id = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYID);
        company_code = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYCODE);
        category_id = sharedPref.getUserInfo().get(sharedPref.KEY_CATEGORYID);
        selected_branch_id = Constants.branch_id;
        user_id = sharedPref.getUserInfo().get(sharedPref.KEY_USERID);


        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();
                if(username.equals("")||password.equals("")){
                    Toast.makeText(getContext(), "Please enter missing fields", Toast.LENGTH_SHORT).show();
                }else{
                    authenticate_owner(username,password);
                }

            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        return view;
    }


    public void authenticate_owner(final String username,final String password){
        String URL = getString(R.string.URL_online)+"owner_authenticate.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("1")){
                    dismiss();
                    interfaceObj.senddata("okay");

                } else { //authenticate
                    Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(), "Error connection", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();
                //user
                hashMap.put("company_id", company_id);
                hashMap.put("user_id", user_id);
                hashMap.put("company_code", company_code);
                hashMap.put("module", "requisition_approval");

                hashMap.put("branch_id", selected_branch_id);
                hashMap.put("owner_username", username);
                hashMap.put("owner_password", password);

                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    //send back data to parent fragment
    public interface uploadDialogInterface
    {
        void senddata(String check_dialog);
    }

    uploadDialogInterface interfaceObj;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        interfaceObj= (uploadDialogInterface) getTargetFragment();
    }


}
