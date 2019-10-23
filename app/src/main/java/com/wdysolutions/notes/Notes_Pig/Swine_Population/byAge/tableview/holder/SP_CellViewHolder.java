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

package com.wdysolutions.notes.Notes_Pig.Swine_Population.byAge.tableview.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.wdysolutions.notes.Notes_Pig.Swine_Population.byAge.tableview.model.SP_Cell;
import com.wdysolutions.notes.R;

/**
 * Created by evrencoskun on 23/10/2017.
 */

public class SP_CellViewHolder extends AbstractViewHolder {

    public final TextView data1,data2,data3,data4, data5,data6,data7,data8,
            data9,data10,data11,data12,data13,data14,data15,data16,data17,data18;
    public final LinearLayout SP_cell_container;
    private SP_Cell cell;

    public SP_CellViewHolder(View itemView) {
        super(itemView);
        data1 = itemView.findViewById(R.id.data1);
        data2 = itemView.findViewById(R.id.data2);
        data3 = itemView.findViewById(R.id.data3);
        data4 = itemView.findViewById(R.id.data4);
        data5 = itemView.findViewById(R.id.data5);
        data6 = itemView.findViewById(R.id.data6);
        data7 = itemView.findViewById(R.id.data7);
        data8 = itemView.findViewById(R.id.data8);
        data9 = itemView.findViewById(R.id.data9);
        data10 = itemView.findViewById(R.id.data10);
        data11 = itemView.findViewById(R.id.data11);
        data12 = itemView.findViewById(R.id.data12);

        data13 = itemView.findViewById(R.id.data13);
        data14 = itemView.findViewById(R.id.data14);
        data15 = itemView.findViewById(R.id.data15);
        data16 = itemView.findViewById(R.id.data16);
        data17 = itemView.findViewById(R.id.data17);
        data18 = itemView.findViewById(R.id.data18);
        SP_cell_container = itemView.findViewById(R.id.SP_cell_container);
    }

    public void setCell(SP_Cell cell) {
        this.cell = cell;
        data1.setText(String.valueOf(cell.getData1()));
        data2.setText(String.valueOf(cell.getData2()));
        data3.setText(String.valueOf(cell.getData3()));
        data4.setText(String.valueOf(cell.getData4()));
        data5.setText(String.valueOf(cell.getData5()));
        data6.setText(String.valueOf(cell.getData6()));
        data7.setText(String.valueOf(cell.getData7()));
        data8.setText(String.valueOf(cell.getData8()));
        data9.setText(String.valueOf(cell.getData9()));
        data10.setText(String.valueOf(cell.getData10()));
        data11.setText(String.valueOf(cell.getData11()));
        data12.setText(String.valueOf(cell.getData12()));


        data13.setText(String.valueOf(cell.getData13()));
        data14.setText(String.valueOf(cell.getData14()));
        data15.setText(String.valueOf(cell.getData15()));
        data16.setText(String.valueOf(cell.getData16()));
        data17.setText(String.valueOf(cell.getData17()));
        data18.setText(String.valueOf(cell.getData18()));


        // If your TableView should have auto resize for cells & columns.
        // Then you should consider the below lines. Otherwise, you can ignore them.

        // It is necessary to remeasure itself.
        SP_cell_container.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
        data1.requestLayout();
    }
}