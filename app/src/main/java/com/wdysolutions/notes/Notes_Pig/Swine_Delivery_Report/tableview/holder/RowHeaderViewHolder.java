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

/**
 * Created by evrencoskun on 23/10/2017.
 */

import android.view.View;
import android.widget.TextView;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.wdysolutions.notes.R;

public class RowHeaderViewHolder extends AbstractViewHolder {

    public TextView counter, tv_date, tv_customer, tv_heads;

    public RowHeaderViewHolder(View itemView) {
        super(itemView);
        counter = itemView.findViewById(R.id.counter);
        tv_date = itemView.findViewById(R.id.tv_date);
        tv_customer = itemView.findViewById(R.id.tv_customer);
        tv_heads = itemView.findViewById(R.id.tv_heads);
    }
}