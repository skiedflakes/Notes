/*
 * Copyright (c) 2018. Evren Coşkun
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.wdysolutions.notes.Notes_Egg.Layer_Reports.brooding_report_tableview_bybatch.holder.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.wdysolutions.notes.Notes_Egg.Layer_Reports.brooding_report_tableview.model.BR_Cell;
import com.wdysolutions.notes.R;

/**
 * Created by evrencoskun on 23/10/2017.
 */

public class BB_CellViewHolder extends AbstractViewHolder {

    public final TextView data1,data2,data3,data4,data5,data6,data7;
   // public final LinearLayout BR_cell_container;
   public final LinearLayout cell_container;
    private BR_Cell cell;

    public BB_CellViewHolder(View itemView) {
        super(itemView);
        data1 = itemView.findViewById(R.id.data1);
        data2 = itemView.findViewById(R.id.data2);
        data3 = itemView.findViewById(R.id.data3);
        data4 = itemView.findViewById(R.id.data4);
        data5 = itemView.findViewById(R.id.data5);
        data6 = itemView.findViewById(R.id.data6);
        data7 = itemView.findViewById(R.id.data7);
        cell_container = itemView.findViewById(R.id.cell_container);
    }

    public void setCell(BR_Cell cell) {
        this.cell = cell;

        // If your TableView should have auto resize for cells & columns.
        // Then you should consider the below lines. Otherwise, you can ignore them.

        // It is necessary to remeasure itself.
        cell_container.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
        data1.requestLayout();

    }
}