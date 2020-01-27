

package com.wdysolutions.notes.Notes_Egg.Layer_Reports.brooding_report_tableview_bybatch.holder.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractSorterViewHolder;
import com.wdysolutions.notes.R;


public class BB_ColumnHeaderViewHolder extends AbstractSorterViewHolder {

    private static final String LOG_TAG = BB_ColumnHeaderViewHolder.class.getSimpleName();


    public final TextView column_header_textview;
    public final ITableView tableView;
    public final LinearLayout column_header_container;

    public BB_ColumnHeaderViewHolder(View itemView, ITableView tableView) {
        super(itemView);
        this.tableView = tableView;
        column_header_textview = itemView.findViewById(R.id.column_header_textView);
        column_header_container= itemView.findViewById(R.id.column_header_container);
    }


}
