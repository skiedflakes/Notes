
package com.wdysolutions.notes.Notes_Egg.Layer_Reports.brooding_report_tableview;

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
import com.wdysolutions.notes.R;

/**
 * Created by evrencoskun on 11/06/2017.
 * <p>
 * This is a sample of custom TableView Adapter.
 */

public class BR_TableViewAdapter extends AbstractTableAdapter<BR_ColumnHeader, BR_RowHeader, BR_Cell> {
    private final LayoutInflater mInflater;
   final String selected_date;
    public BR_TableViewAdapter(Context context, String selected_date) {
        super(context);
        this.mInflater = LayoutInflater.from(mContext);
        this.selected_date = selected_date;
    }

    @Override
    public AbstractViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
        View layout = mInflater.inflate(R.layout.egg_brooding_report_tableview_main_cell, parent, false);
        return new BR_CellViewHolder(layout);
    }

    @Override
    public void onBindCellViewHolder(AbstractViewHolder holder, Object cellItemModel, final int columnPosition, final int rowPosition) {
        BR_Cell cell = (BR_Cell) cellItemModel;
        BR_CellViewHolder cellViewHolder = (BR_CellViewHolder)holder;

        cellViewHolder.data1.setText(cell.getData1());
        cellViewHolder.data2.setText(cell.getData2());
        cellViewHolder.data3.setText(cell.getData3());
        cellViewHolder.data4.setText(cell.getData4());
        cellViewHolder.data5.setText(cell.getData5());
        cellViewHolder.data6.setText(cell.getData6());

        //cell visibility
        if(cell.getMortality()){ // if cell is mortality
            cellViewHolder.data2.setVisibility(View.VISIBLE);
            cellViewHolder.data3.setVisibility(View.VISIBLE);
            cellViewHolder.data4.setVisibility(View.VISIBLE);
            cellViewHolder.data5.setVisibility(View.VISIBLE);
            cellViewHolder.data6.setVisibility(View.VISIBLE);

        }else if(cell.getDepopulation()){ // if cell is depopulationBR_ColumnHeaderViewHolder
            cellViewHolder.data2.setVisibility(View.VISIBLE);
            cellViewHolder.data3.setVisibility(View.GONE);
            cellViewHolder.data4.setVisibility(View.GONE);
            cellViewHolder.data5.setVisibility(View.GONE);
            cellViewHolder.data6.setVisibility(View.GONE);

        }else if(cell.getFeeds()){ // if cell is feeds
            cellViewHolder.data2.setVisibility(View.VISIBLE);
            cellViewHolder.data3.setVisibility(View.VISIBLE);
            cellViewHolder.data4.setVisibility(View.GONE);
            cellViewHolder.data5.setVisibility(View.GONE);
            cellViewHolder.data6.setVisibility(View.GONE);

        }else if(cell.getBody_weight()){ // if cell is body weight
            cellViewHolder.data2.setVisibility(View.VISIBLE);
            cellViewHolder.data3.setVisibility(View.VISIBLE);
            cellViewHolder.data4.setVisibility(View.VISIBLE);
            cellViewHolder.data5.setVisibility(View.VISIBLE);
            cellViewHolder.data6.setVisibility(View.GONE);

        }else{
            cellViewHolder.data2.setVisibility(View.GONE);
            cellViewHolder.data3.setVisibility(View.GONE);
            cellViewHolder.data4.setVisibility(View.GONE);
            cellViewHolder.data5.setVisibility(View.GONE);
            cellViewHolder.data6.setVisibility(View.GONE);

        }

        //set row color
        if(rowPosition==0){
            cellViewHolder.data1.setBackgroundColor(mContext.getResources().getColor(R.color.egg_reports_side_header));
            cellViewHolder.data2.setBackgroundColor(mContext.getResources().getColor(R.color.egg_reports_side_header));
            cellViewHolder.data3.setBackgroundColor(mContext.getResources().getColor(R.color.egg_reports_side_header));
            cellViewHolder.data4.setBackgroundColor(mContext.getResources().getColor(R.color.egg_reports_side_header));
            cellViewHolder.data5.setBackgroundColor(mContext.getResources().getColor(R.color.egg_reports_side_header));
            cellViewHolder.data6.setBackgroundColor(mContext.getResources().getColor(R.color.egg_reports_side_header));

        }else{
            cellViewHolder.data1.setBackgroundColor(mContext.getResources().getColor(R.color.egg_reports_cell));
            cellViewHolder.data2.setBackgroundColor(mContext.getResources().getColor(R.color.egg_reports_cell));
            cellViewHolder.data3.setBackgroundColor(mContext.getResources().getColor(R.color.egg_reports_cell));
            cellViewHolder.data4.setBackgroundColor(mContext.getResources().getColor(R.color.egg_reports_cell));
            cellViewHolder.data5.setBackgroundColor(mContext.getResources().getColor(R.color.egg_reports_cell));
            cellViewHolder.data6.setBackgroundColor(mContext.getResources().getColor(R.color.egg_reports_cell));

        }

    }


    @Override
    public AbstractViewHolder onCreateColumnHeaderViewHolder(ViewGroup parent, int viewType) {
        View layout = mInflater.inflate(R.layout.egg_brooding_report_tableview_main_column_header, parent, false);
        return new BR_ColumnHeaderViewHolder(layout, getTableView());
    }

    @Override
    public void onBindColumnHeaderViewHolder(AbstractViewHolder holder, Object columnHeaderItemModel, int columnPosition) {
        BR_ColumnHeader columnHeader = (BR_ColumnHeader) columnHeaderItemModel;
        BR_ColumnHeaderViewHolder columnHeaderViewHolder = (BR_ColumnHeaderViewHolder)holder;

        columnHeaderViewHolder.column_header_textview.setText(columnHeader.getData());

    }


    @Override
    public AbstractViewHolder onCreateRowHeaderViewHolder(ViewGroup parent, int viewType) {
        View layout = mInflater.inflate(R.layout.egg_brooding_report_tableview_main_row_header, parent, false);
        return new BR_RowHeaderViewHolder(layout);
    }

    @Override
    public void onBindRowHeaderViewHolder(AbstractViewHolder holder, Object rowHeaderItemModel, int rowPosition) {
        final BR_RowHeader rowHeader = (BR_RowHeader) rowHeaderItemModel;
        final BR_RowHeaderViewHolder rowHeaderViewHolder = (BR_RowHeaderViewHolder)holder;

        rowHeaderViewHolder.data1.setText(rowHeader.getData1());
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
        View corner = mInflater.inflate(R.layout.egg_brooding_report_tableview_main_corner, null);
        return corner;
    }



}
