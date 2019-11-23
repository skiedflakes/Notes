package com.wdysolutions.notes;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

public class Dialog_Action extends DialogFragment {
    Button btn_view_details,btn_approve,btn_micro_filming,btn_disapprove;

    //views
    int micro_filming,approve,view_details,disapprove,position;
    RelativeLayout l_view_details,l_approve,l_micro_filming,l_disapprove;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dialog_action, container, false);
        btn_approve = view.findViewById(R.id.btn_approve);
        btn_micro_filming = view.findViewById(R.id.btn_micro_filming);
        btn_view_details = view.findViewById(R.id.btn_view_details);
        btn_disapprove = view.findViewById(R.id.btn_disapprove);

        l_view_details = view.findViewById(R.id.l_view_details);
        l_approve = view.findViewById(R.id.l_approve);
        l_micro_filming = view.findViewById(R.id.l_micro_filming);
        l_disapprove = view.findViewById(R.id.l_disapprove);


        getBundle();

        btn_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                interfaceObj.senddata("approve",position);
                dismiss();
            }
        });
        btn_micro_filming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfaceObj.senddata("micro_filming",position);
                dismiss();
            }
        });
        btn_view_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfaceObj.senddata("view_details",position);
                dismiss();
            }
        });

        btn_disapprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfaceObj.senddata("disapprove",position);
                dismiss();
            }
        });

        //set visibilty
        if(view_details!=1){l_view_details.setVisibility(View.GONE);}else{l_view_details.setVisibility(View.VISIBLE);}
        if(approve!=1){l_approve.setVisibility(View.GONE);}else{l_approve.setVisibility(View.VISIBLE);}
        if(micro_filming!=1){l_micro_filming.setVisibility(View.GONE);}else{l_micro_filming.setVisibility(View.VISIBLE);}
        if(disapprove!=1){l_disapprove.setVisibility(View.GONE);}else{l_disapprove.setVisibility(View.VISIBLE);}

        return view;
    }

    private void getBundle(){
        Bundle bundle = getArguments();
        if(bundle != null) {
            micro_filming = bundle.getInt("micro_filming");
            approve = bundle.getInt("approve");
            view_details = bundle.getInt("view_details");
            disapprove= bundle.getInt("disapprove");
            position= bundle.getInt("position");
        }
    }

    //send back data to parent fragment
    public interface uploadDialogInterface
    {
        void senddata(String chosen,int position);
    }

    uploadDialogInterface interfaceObj;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        interfaceObj= (uploadDialogInterface) getTargetFragment();
    }

}
