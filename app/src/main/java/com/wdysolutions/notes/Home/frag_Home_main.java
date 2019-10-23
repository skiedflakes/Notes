package com.wdysolutions.notes.Home;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wdysolutions.notes.Constants;
import com.wdysolutions.notes.MainActivity;
import com.wdysolutions.notes.Navigation_Panel.frag_Nav_main;
import com.wdysolutions.notes.Notes_Bro.frag_Bro_notes_main;
import com.wdysolutions.notes.Notes_Egg.frag_Egg_notes_main;
import com.wdysolutions.notes.Notes_Feed.frag_Feed_notes_main;
import com.wdysolutions.notes.Notes_Pig.frag_Pig_notes_main;
import com.wdysolutions.notes.R;
import com.wdysolutions.notes.SharedPref;


public class frag_Home_main extends Fragment {

    ImageView menu_selection, eggnotes, pignotes, feednotes, bronotes;
    String selectedNOTES="";
    SharedPref sharedPref;

////changes ko sa debuging hello

    private void initMenu(View view){
        final Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        MenuItem searchItem = toolbar.getMenu().findItem(R.id.menu_action);
        searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                ((MainActivity)getActivity()).openDialog_logout();
                return false;
            }
        });

        final String category = sharedPref.getUserInfo().get(sharedPref.KEY_CATEGORYID);
        String categoryType="";
        if (category.equals("0")){
            categoryType = "(Owner)";
        }
        final String username = sharedPref.getUserInfo().get(sharedPref.KEY_NAME);
        TextView txt_user = view.findViewById(R.id.txt_user);
        txt_user.setText(username+categoryType);
        txt_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        menu_selection = view.findViewById(R.id.menu_selection);
        menu_selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Constants.nav_close_open){
                    Constants.nav_close_open = false;
                    closeNavigationPanel();
                }else{
                    Constants.nav_close_open = true;
                    openNavigationPanel();
                }
            }
        });

        eggnotes = view.findViewById(R.id.eggnotes);
        eggnotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_eggnotes)));
                selectedNOTES = getResources().getString(R.string.eggNOTES);

                openCategory(selectedNOTES);
                Constants.selected_notes = selectedNOTES;
                Constants.program_code = "E";
                Constants.nav_close_open = false;
            }
        });

        pignotes = view.findViewById(R.id.pignotes);
        pignotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_pignotes)));
                selectedNOTES = getResources().getString(R.string.pigNOTES);
                openCategory(selectedNOTES);
                Constants.program_code = "P";
                Constants.selected_notes = selectedNOTES;
                Constants.nav_close_open = false;
            }
        });

        feednotes = view.findViewById(R.id.feednotes);
        feednotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_feednotes)));
                selectedNOTES = getResources().getString(R.string.feedNOTES);
                openCategory(selectedNOTES);
                Constants.program_code = "F";
                Constants.selected_notes = selectedNOTES;
                Constants.nav_close_open = false;
            }
        });

        bronotes = view.findViewById(R.id.bronotes);
        bronotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_bronotes)));
                selectedNOTES = getResources().getString(R.string.broNOTES);
                openCategory(selectedNOTES);
                Constants.program_code = "B";
                Constants.selected_notes = selectedNOTES;
                Constants.nav_close_open = false;
            }
        });


        String program_pig = sharedPref.getUserInfo().get(sharedPref.KEY_PIG);
        String program_feed = sharedPref.getUserInfo().get(sharedPref.KEY_FEEDS);
        String program_egg = sharedPref.getUserInfo().get(sharedPref.KEY_EGG);
        String program_broiler = sharedPref.getUserInfo().get(sharedPref.KEY_BROILER);


        if(program_pig.equals("0")){
            pignotes.setVisibility(View.GONE);
        }

        if(program_feed.equals("0")){
            feednotes.setVisibility(View.GONE);
        }

        if(program_egg.equals("0")){
            eggnotes.setVisibility(View.GONE);
        }

        if(program_broiler.equals("0")){
            bronotes.setVisibility(View.GONE);
        }
    }

    private void openCategory(String tap){
        Fragment selectedFragment = null;
        if (tap.equals(getResources().getString(R.string.pigNOTES))){
            selectedFragment = new frag_Pig_notes_main();
        } else if (tap.equals(getResources().getString(R.string.eggNOTES))){
            selectedFragment = new frag_Egg_notes_main();
        } else if (tap.equals(getResources().getString(R.string.feedNOTES))){
            selectedFragment = new frag_Feed_notes_main();
        } else if (tap.equals(getResources().getString(R.string.broNOTES))){
            selectedFragment = new frag_Bro_notes_main();
        }
        if (selectedFragment != null){
            moduleCloseNav();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_home, selectedFragment);
            fragmentTransaction.commit();
        }
    }

    private void openNavigationPanel(){
        Bundle bundle = new Bundle();
        bundle.putString("notes", selectedNOTES);
        FragmentManager fragmentManager = getFragmentManager();
        frag_Nav_main frag_nav_main = new frag_Nav_main();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction.add(R.id.container_home, frag_nav_main);
        fragmentTransaction.addToBackStack(null);
        frag_nav_main.setArguments(bundle);
        fragmentTransaction.commit();
    }

    private void closeNavigationPanel(){
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_main, container, false);
        sharedPref = new SharedPref(getActivity());


        //((MainActivity)getActivity()).openDialogDebug();
        initMenu(view);
        return view;
    }


    public void moduleCloseNav(){
        if(Constants.nav_close_open){
            Constants.nav_close_open = false;
            closeNavigationPanel();
        }
    }


}
