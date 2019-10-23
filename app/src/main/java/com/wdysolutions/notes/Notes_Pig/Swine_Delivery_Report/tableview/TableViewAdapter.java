
package com.wdysolutions.notes.Notes_Pig.Swine_Delivery_Report.tableview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.wdysolutions.notes.Notes_Pig.Swine_Delivery_Report.tableview.holder.CellViewHolder;
import com.wdysolutions.notes.Notes_Pig.Swine_Delivery_Report.tableview.holder.ColumnHeaderViewHolder;
import com.wdysolutions.notes.Notes_Pig.Swine_Delivery_Report.tableview.holder.RowHeaderViewHolder;
import com.wdysolutions.notes.Notes_Pig.Swine_Delivery_Report.tableview.model.Cell;
import com.wdysolutions.notes.Notes_Pig.Swine_Delivery_Report.tableview.model.ColumnHeader_swineDelivery;
import com.wdysolutions.notes.Notes_Pig.Swine_Delivery_Report.tableview.model.RowHeader_swineDelivery;
import com.wdysolutions.notes.R;

/**
 * Created by evrencoskun on 11/06/2017.
 * <p>
 * This is a sample of custom TableView Adapter.
 */

public class TableViewAdapter extends AbstractTableAdapter<ColumnHeader_swineDelivery, RowHeader_swineDelivery, Cell> {

    private final LayoutInflater mInflater;


    public TableViewAdapter(Context context) {
        super(context);
        this.mInflater = LayoutInflater.from(mContext);
    }


    @Override
    public AbstractViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
        View layout = mInflater.inflate(R.layout.swine_delivery_report_table_cell, parent, false);
        return new CellViewHolder(layout);
    }

    @Override
    public void onBindCellViewHolder(AbstractViewHolder holder, Object cellItemModel, final int columnPosition, final int rowPosition) {
        Cell cell = (Cell) cellItemModel;
        CellViewHolder cellViewHolder = (CellViewHolder)holder;
        cellViewHolder.tv_reference.setText(cell.getReference_num());
        cellViewHolder.tv_total_weight.setText(cell.getTotal_weight());
        cellViewHolder.tv_ave_weight.setText(cell.getAve_weight());
        cellViewHolder.tv_swine_total.setText(cell.getTotal_cost());
        cellViewHolder.tv_swine_ave.setText(cell.getAve_cost());
        cellViewHolder.tv_swine_type.setText(cell.getSwine_type());
        cellViewHolder.tv_ave_age.setText(cell.getAve_age());
        cellViewHolder.tv_per_kg.setText(cell.getPrice_per_kg());
        cellViewHolder.tv_adg.setText(cell.getAdg());
        cellViewHolder.tv_total_gross.setText(cell.getGross_total());
        cellViewHolder.tv_ave_gross.setText(cell.getGross_ave());
        cellViewHolder.tv_total_sales.setText(cell.getTotal_sales());
    }


    @Override
    public AbstractViewHolder onCreateColumnHeaderViewHolder(ViewGroup parent, int viewType) {
        View layout = mInflater.inflate(R.layout.swine_delivery_report_table_column_header, parent, false);
        return new ColumnHeaderViewHolder(layout, getTableView());
    }

    @Override
    public void onBindColumnHeaderViewHolder(AbstractViewHolder holder, Object columnHeaderItemModel, int columnPosition) {
        ColumnHeader_swineDelivery columnHeaderSwineDelivery = (ColumnHeader_swineDelivery) columnHeaderItemModel;
        ColumnHeaderViewHolder columnHeaderViewHolder = (ColumnHeaderViewHolder)holder;
    }


    @Override
    public AbstractViewHolder onCreateRowHeaderViewHolder(ViewGroup parent, int viewType) {
        View layout = mInflater.inflate(R.layout.swine_delivery_report_table_row_header, parent, false);
        return new RowHeaderViewHolder(layout);
    }

    @Override
    public void onBindRowHeaderViewHolder(AbstractViewHolder holder, Object rowHeaderItemModel, int rowPosition) {
        final RowHeader_swineDelivery rowHeaderByMonth = (RowHeader_swineDelivery) rowHeaderItemModel;
        final RowHeaderViewHolder rowHeaderViewHolder = (RowHeaderViewHolder)holder;
        rowHeaderViewHolder.counter.setText(String.valueOf(rowPosition+1));
        rowHeaderViewHolder.tv_customer.setText(rowHeaderByMonth.getCustomer());
        rowHeaderViewHolder.tv_date.setText(rowHeaderByMonth.getDate());
        rowHeaderViewHolder.tv_heads.setText(rowHeaderByMonth.getHeads());
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
        View corner = mInflater.inflate(R.layout.swine_delivery_report_table_corner, null);
        return corner;
    }

}
