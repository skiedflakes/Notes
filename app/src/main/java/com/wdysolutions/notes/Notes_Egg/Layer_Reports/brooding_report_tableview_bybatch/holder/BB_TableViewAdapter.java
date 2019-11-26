
package com.wdysolutions.notes.Notes_Egg.Layer_Reports.brooding_report_tableview_bybatch.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.wdysolutions.notes.Notes_Egg.Layer_Reports.brooding_report_tableview.holder.BR_CellViewHolder;
import com.wdysolutions.notes.Notes_Egg.Layer_Reports.brooding_report_tableview.holder.BR_ColumnHeaderViewHolder;
import com.wdysolutions.notes.Notes_Egg.Layer_Reports.brooding_report_tableview.holder.BR_RowHeaderViewHolder;
import com.wdysolutions.notes.Notes_Egg.Layer_Reports.brooding_report_tableview.model.BR_Cell;
import com.wdysolutions.notes.Notes_Egg.Layer_Reports.brooding_report_tableview.model.BR_ColumnHeader;
import com.wdysolutions.notes.Notes_Egg.Layer_Reports.brooding_report_tableview.model.BR_RowHeader;
import com.wdysolutions.notes.Notes_Egg.Layer_Reports.brooding_report_tableview_bybatch.holder.holder.BB_CellViewHolder;
import com.wdysolutions.notes.Notes_Egg.Layer_Reports.brooding_report_tableview_bybatch.holder.holder.BB_ColumnHeaderViewHolder;
import com.wdysolutions.notes.Notes_Egg.Layer_Reports.brooding_report_tableview_bybatch.holder.holder.BB_RowHeaderViewHolder;
import com.wdysolutions.notes.Notes_Egg.Layer_Reports.brooding_report_tableview_bybatch.holder.model.BB_Cell;
import com.wdysolutions.notes.Notes_Egg.Layer_Reports.brooding_report_tableview_bybatch.holder.model.BB_ColumnHeader;
import com.wdysolutions.notes.Notes_Egg.Layer_Reports.brooding_report_tableview_bybatch.holder.model.BB_RowHeader;
import com.wdysolutions.notes.R;

/**
 * Created by evrencoskun on 11/06/2017.
 * <p>
 * This is a sample of custom TableView Adapter.
 */

public class BB_TableViewAdapter extends AbstractTableAdapter<BB_ColumnHeader, BB_RowHeader, BB_Cell> {
    private final LayoutInflater mInflater;
   final String selected_date;
    public BB_TableViewAdapter(Context context, String selected_date) {
        super(context);
        this.mInflater = LayoutInflater.from(mContext);
        this.selected_date = selected_date;
    }

    @Override
    public AbstractViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
        View layout = mInflater.inflate(R.layout.egg_brooding_report_bb_tableview_main_cell, parent, false);
        return new BB_CellViewHolder(layout);
    }

    @Override
    public void onBindCellViewHolder(AbstractViewHolder holder, Object cellItemModel, final int columnPosition, final int rowPosition) {
        BB_Cell cell = (BB_Cell) cellItemModel;
        BB_CellViewHolder cellViewHolder = (BB_CellViewHolder)holder;


    }


    @Override
    public AbstractViewHolder onCreateColumnHeaderViewHolder(ViewGroup parent, int viewType) {
        View layout = mInflater.inflate(R.layout.egg_brooding_report_bb_tableview_main_column_header, parent, false);
        return new BB_ColumnHeaderViewHolder(layout, getTableView());
    }

    @Override
    public void onBindColumnHeaderViewHolder(AbstractViewHolder holder, Object columnHeaderItemModel, int columnPosition) {
        BB_ColumnHeader columnHeader = (BB_ColumnHeader) columnHeaderItemModel;
        BB_ColumnHeaderViewHolder columnHeaderViewHolder = (BB_ColumnHeaderViewHolder)holder;
        columnHeaderViewHolder.column_header_textview.setText(columnHeader.getData());


    }


    @Override
    public AbstractViewHolder onCreateRowHeaderViewHolder(ViewGroup parent, int viewType) {
        View layout = mInflater.inflate(R.layout.egg_brooding_report_bb_tableview_main_row_header, parent, false);
        return new BB_RowHeaderViewHolder(layout);
    }

    @Override
    public void onBindRowHeaderViewHolder(AbstractViewHolder holder, Object rowHeaderItemModel, int rowPosition) {
        final BB_RowHeader rowHeader = (BB_RowHeader) rowHeaderItemModel;
        final BB_RowHeaderViewHolder rowHeaderViewHolder = (BB_RowHeaderViewHolder)holder;
        rowHeaderViewHolder.data1.setText(rowHeader.getData1());
        rowHeaderViewHolder.data2.setText(rowHeader.getData2());
        rowHeaderViewHolder.data3.setText(rowHeader.getData3());
    }


    @Override
    public int getColumnHeaderItemViewType(int position) {
        return 0;
    }

    @Override
    public int getRowHeaderItemViewType(int position) {
        return 0;
    }

    @Override
    public int getCellItemViewType(int column) {
        return 0;
    }

    @Override
    public View onCreateCornerView() {
        View corner = mInflater.inflate(R.layout.egg_brooding_report_bb_tableview_main_corner, null);
        return corner;
    }



}
