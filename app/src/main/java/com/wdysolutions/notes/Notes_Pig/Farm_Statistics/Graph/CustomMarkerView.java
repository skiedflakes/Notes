package com.wdysolutions.notes.Notes_Pig.Farm_Statistics.Graph;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.wdysolutions.notes.R;

import java.util.ArrayList;

public class CustomMarkerView extends MarkerView {

    private TextView tvContent;
    ArrayList<String> xAxes_label;


    public CustomMarkerView(Context context, int layoutResource, ArrayList<String> xAxes_label) {
        super(context, layoutResource);
        this.xAxes_label = xAxes_label;
        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvContent.setText("" + xAxes_label.get(highlight.getXIndex()));
    }

    @Override
    public int getXOffset(float v) {
        return -(getWidth() / 2);
    }

    @Override
    public int getYOffset(float v) {
        return -getHeight();
    }
}
