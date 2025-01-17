package com.wdysolutions.notes.Globals.Micro_Filming;

import android.app.Dialog;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wdysolutions.notes.AppController;
import com.wdysolutions.notes.Constants;
import com.wdysolutions.notes.MainActivity;
import com.wdysolutions.notes.R;
import com.wdysolutions.notes.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Microfilming_main extends DialogFragment implements clickRecycler {

    RecyclerView recycler_title, recycler_img;
    TextView txt_selected, txt_back, txt_no_img, txt_title, txt_close;
    String tracking_num, company_id, company_code, selected_branch_id, end_date, start_date;
    ProgressBar loading_;
    LinearLayout layout_main, layout_selected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.microfilming_main, container, false);
        SharedPref sharedPref = new SharedPref(getActivity());
        company_id = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYID);
        company_code = sharedPref.getUserInfo().get(sharedPref.KEY_COMPANYCODE);
        selected_branch_id = Constants.branch_id;
        tracking_num = getArguments().getString("tracking_num");
        start_date = getArguments().getString("start_date");
        end_date = getArguments().getString("end_date");

        layout_selected = view.findViewById(R.id.layout_selected);
        layout_main = view.findViewById(R.id.layout_main);
        loading_ = view.findViewById(R.id.loading_);
        recycler_title = view.findViewById(R.id.recycler_title);
        recycler_img = view.findViewById(R.id.recycler_img);
        txt_selected = view.findViewById(R.id.txt_selected);
        txt_back = view.findViewById(R.id.txt_back);
        txt_no_img = view.findViewById(R.id.txt_no_img);
        txt_title = view.findViewById(R.id.txt_title);
        txt_close = view.findViewById(R.id.txt_close);

        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        txt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        getImage();
        setRecyclerViewMarginUnderline();
        return view;
    }

    private void back(){
        selected_array.remove(selected_array.size()-1);
        selected_array_amount.remove(selected_array_amount.size()-1);
        txt_selected.setText(Html.fromHtml(selected_array.get(selected_array.size()-1)+
                " <font color='red'>₱ "+selected_array_amount.get(selected_array_amount.size()-1)+"</font>"));
        initRecyclerview(selected_array.get(selected_array.size()-1));
        initRecyclerviewImage(selected_array.get(selected_array.size()-1));

        if (selected_array.size() == 1){
            txt_back.setVisibility(View.INVISIBLE);
        }
    }

    private void setRecyclerViewMarginUnderline(){
        // set recycler margin
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.gridview_layout);
        recycler_img.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        // set recyclerview underline
        recycler_title.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    String amount, main_track_num;
    ArrayList<String> selected_array, selected_array_amount;
    ArrayList<title_model> title_models;
    ArrayList<image_model> image_models;
    public void getImage() {
        layout_main.setVisibility(View.GONE);
        loading_.setVisibility(View.VISIBLE);
        String URL = getString(R.string.URL_online) + "micro_filming/micro_filming_data.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    layout_main.setVisibility(View.VISIBLE);
                    loading_.setVisibility(View.GONE);
                    //((MainActivity)getActivity()).openDialog(response);

                    title_models = new ArrayList<>();
                    image_models = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);

                    // tracking num
                    JSONArray jsonArray_title = jsonObject.getJSONArray("response_title");

                    // get main ref num and amount
                    JSONObject getTrackSelected = (JSONObject)jsonArray_title.get(0);
                    main_track_num = getTrackSelected.getString("ref_num");
                    amount = getTrackSelected.getString("amount");

                    // add default
                    selected_array = new ArrayList<>();
                    selected_array_amount = new ArrayList<>();
                    selected_array.add(main_track_num);
                    selected_array_amount.add(amount);

                    // display default selected
                    txt_selected.setText(Html.fromHtml(main_track_num+" <font color='red'>₱ "+amount+"</font>"));
                    txt_selected.setPaintFlags(txt_selected.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                    // add to list ref num ---------------------------------------------
                    for (int i=0; i<jsonArray_title.length(); i++){
                        JSONObject jsonObject1 = (JSONObject)jsonArray_title.get(i);

                        title_models.add(new title_model(jsonObject1.getString("type"),
                                jsonObject1.getString("ref_num"),
                                jsonObject1.getString("top_ref_num"),
                                jsonObject1.getString("amount")));
                    }

                    initRecyclerview(main_track_num);


                    // add to list image ------------------------------------------------
                    JSONArray jsonArray_img = jsonObject.getJSONArray("response_img");
                    for (int i=0; i<jsonArray_img.length(); i++){
                        JSONObject jsonObject1 = (JSONObject)jsonArray_img.get(i);

                        image_models.add(new image_model(jsonObject1.getString("type"),
                                jsonObject1.getString("ref_num"),
                                jsonObject1.getString("value"),
                                url_img(jsonObject1.getString("url_img"))));
                    }

                    initRecyclerviewImage(main_track_num);

                    // count image and display title
                    String count_img = String.valueOf(image_models.size());
                    txt_title.setText(count_img+" Attachments of "+tracking_num);

                }
                catch (JSONException e){}
                catch (Exception e){}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismiss();
                Toast.makeText(getActivity(), getResources().getString(R.string.volley_error_msg), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("mf_ref", tracking_num);
                hashMap.put("company_id", company_id);
                hashMap.put("branch_id", selected_branch_id);
                hashMap.put("company_code", company_code);
                hashMap.put("start_date", start_date);
                hashMap.put("end_date", end_date);
                return hashMap;
            }
        };
        AppController.getInstance().setVolleyDuration(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private String url_img(String url_img){
        if (Constants.variants.equals("main_")){
            return url_img.replace("..", "https://notes.wdysolutions.com/");
        } else if (Constants.variants.equals("demo")){
            return url_img.replace("..", "https://demo.wdysolutions.com/");
        } else { // local
            return url_img.replace("..", "http://192.168.0.110/dev");
        }
    }

    ArrayList<title_model> title_select_models;
    private void initRecyclerview(String top_ref_num){
        title_select_models = new ArrayList<>();
        for (int i=0; i<title_models.size(); i++){
            title_model model = title_models.get(i);

            if (model.getTop_ref_num().equals(top_ref_num)){
                title_select_models.add(new title_model(model.getType(), model.getRef_num(), model.getTop_ref_num(), model.getAmount()));
            }
        }
        recycler_title.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_title.setAdapter(new title_adapter(getActivity(), title_select_models, this));
        recycler_title.setNestedScrollingEnabled(false);
    }

    ArrayList<image_model> image_select_models;
    private void initRecyclerviewImage(String ref_num){
        image_select_models = new ArrayList<>();

        for (int i=0; i<image_models.size(); i++){
            image_model model = image_models.get(i);

            if (model.getRef_num().equals(ref_num)){
                image_select_models.add(new image_model(model.getType(), model.getRef_num(), model.getValue(), model.getUrl_img()));
            }
        }

        if (image_select_models.size() == 0){
            txt_no_img.setVisibility(View.VISIBLE);
            recycler_img.setVisibility(View.GONE);
        } else {
            txt_no_img.setVisibility(View.GONE);
            recycler_img.setVisibility(View.VISIBLE);

            recycler_img.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            recycler_img.setAdapter(new image_adapter(getActivity(), image_select_models));
            recycler_img.setNestedScrollingEnabled(false);
        }
    }

    @Override
    public void clickInterface(String num, String amount) {
        selected_array.add(num);
        selected_array_amount.add(amount);
        txt_selected.setText(Html.fromHtml(num+" <font color='red'>₱ "+amount+"</font>"));
        initRecyclerview(num);
        initRecyclerviewImage(num);

        if (selected_array.size() == 2){
            txt_back.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme()){
            @Override
            public void onBackPressed() {
                if (selected_array.size() > 1){
                    back();
                } else {
                    dismiss();
                }
            }
        };
    }
}
