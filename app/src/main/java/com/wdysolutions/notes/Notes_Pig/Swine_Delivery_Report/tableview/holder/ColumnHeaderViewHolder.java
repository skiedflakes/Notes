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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractSorterViewHolder;
import com.wdysolutions.notes.R;


public class ColumnHeaderViewHolder extends AbstractSorterViewHolder {

    private static final String LOG_TAG = ColumnHeaderViewHolder.class.getSimpleName();

    public final LinearLayout column_header_container;
    public final TextView column_header_textview, column_header_textView2, column_header_textView3, text_week;
    public final ImageButton column_header_sortButton;
    public final ITableView tableView;

    public ColumnHeaderViewHolder(View itemView, ITableView tableView) {
        super(itemView);
        this.tableView = tableView;
        column_header_textview = itemView.findViewById(R.id.column_header_textView);
        column_header_container = itemView.findViewById(R.id.column_header_container);
        column_header_sortButton = itemView.findViewById(R.id.column_header_sortButton);
        column_header_textView2 = itemView.findViewById(R.id.column_header_textView2);
        column_header_textView3 = itemView.findViewById(R.id.column_header_textView3);
        text_week = itemView.findViewById(R.id.text_week);
    }
}
