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

package com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Week.tableview.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Week.tableview.model.Cell;
import com.wdysolutions.notes.R;

/**
 * Created by evrencoskun on 23/10/2017.
 */

public class CellViewHolder extends AbstractViewHolder {

    public final TextView current, ave, percent;
    public final LinearLayout cell_container;
    private Cell cell;

    public CellViewHolder(View itemView) {
        super(itemView);
        current = itemView.findViewById(R.id.current);
        ave = itemView.findViewById(R.id.ave);
        percent = itemView.findViewById(R.id.percent);
        cell_container = itemView.findViewById(R.id.cell_container);
    }

    public void setCell(Cell cell) {
        this.cell = cell;
        current.setText(String.valueOf(cell.getCurrent()));

        // If your TableView should have auto resize for cells & columns.
        // Then you should consider the below lines. Otherwise, you can ignore them.

        // It is necessary to remeasure itself.
        cell_container.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
        current.requestLayout();
    }
}