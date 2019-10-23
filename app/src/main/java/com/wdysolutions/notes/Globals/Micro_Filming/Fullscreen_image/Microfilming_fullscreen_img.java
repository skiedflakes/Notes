package com.wdysolutions.notes.Globals.Micro_Filming.Fullscreen_image;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.wdysolutions.notes.Globals.Micro_Filming.image_model;
import com.wdysolutions.notes.R;

import java.util.ArrayList;

public class Microfilming_fullscreen_img extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.microfilming_fullscreen_img);
        int selected = getIntent().getExtras().getInt("selected");
        ArrayList<image_model> img_array = (ArrayList<image_model>) this.getIntent().getExtras().getSerializable("data");

        ViewPager pager = findViewById(R.id.pager);
        pager.setAdapter(new imageViewPagerAdapter(this, img_array));
        pager.setCurrentItem(selected);
    }
}
