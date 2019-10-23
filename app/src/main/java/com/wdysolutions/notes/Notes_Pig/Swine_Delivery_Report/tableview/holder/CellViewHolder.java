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

package com.wdysolutions.notes.Notes_Pig.Swine_Delivery_Report.tableview.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.wdysolutions.notes.Notes_Pig.Swine_Delivery_Report.tableview.model.Cell;
import com.wdysolutions.notes.R;

/**
 * Created by evrencoskun on 23/10/2017.
 */

public class CellViewHolder extends AbstractViewHolder {

    public TextView tv_reference, tv_total_weight, tv_ave_weight, tv_adg, tv_total_gross, tv_ave_gross,
            tv_total_sales, tv_per_kg, tv_ave_age, tv_swine_type, tv_swine_ave, tv_swine_total;

    public CellViewHolder(View itemView) {
        super(itemView);
        tv_reference = itemView.findViewById(R.id.tv_reference);
        tv_total_weight = itemView.findViewById(R.id.tv_total_weight);
        tv_ave_weight = itemView.findViewById(R.id.tv_ave_weight);
        tv_adg = itemView.findViewById(R.id.tv_adg);
        tv_total_gross = itemView.findViewById(R.id.tv_total_gross);
        tv_ave_gross = itemView.findViewById(R.id.tv_ave_gross);
        tv_total_sales = itemView.findViewById(R.id.tv_total_sales);
        tv_per_kg = itemView.findViewById(R.id.tv_per_kg);
        tv_ave_age = itemView.findViewById(R.id.tv_ave_age);
        tv_swine_type = itemView.findViewById(R.id.tv_swine_type);
        tv_swine_ave = itemView.findViewById(R.id.tv_swine_ave);
        tv_swine_total = itemView.findViewById(R.id.tv_swine_total);
    }

}