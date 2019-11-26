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

/**
 * Created by evrencoskun on 23/10/2017.
 */

import android.view.View;
import android.widget.TextView;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.wdysolutions.notes.R;

public class BB_RowHeaderViewHolder extends AbstractViewHolder {
    public final TextView data1,data2,data3;

    public BB_RowHeaderViewHolder(View itemView) {
        super(itemView);
        data1 = itemView.findViewById(R.id.data1);
        data2 = itemView.findViewById(R.id.data2);
        data3 = itemView.findViewById(R.id.data3);
    }
}