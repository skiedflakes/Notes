
package com.wdysolutions.notes.Notes_Pig.Swine_Population.byClass.tableview;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.wdysolutions.notes.Constants;
import com.wdysolutions.notes.MainActivity;
import com.wdysolutions.notes.Notes_Pig.Swine_Population.Dialog_Table.dialogTable_main;
import com.wdysolutions.notes.Notes_Pig.Swine_Population.byClass.tableview.holder.CellViewHolder;
import com.wdysolutions.notes.Notes_Pig.Swine_Population.byClass.tableview.holder.ColumnHeaderViewHolder;
import com.wdysolutions.notes.Notes_Pig.Swine_Population.byClass.tableview.holder.RowHeaderViewHolder;
import com.wdysolutions.notes.Notes_Pig.Swine_Population.byClass.tableview.model.Cell;
import com.wdysolutions.notes.Notes_Pig.Swine_Population.byClass.tableview.model.ColumnHeader_byClassi;
import com.wdysolutions.notes.Notes_Pig.Swine_Population.byClass.tableview.model.RowHeader;
import com.wdysolutions.notes.R;


public class TableViewAdapter extends AbstractTableAdapter<ColumnHeader_byClassi, RowHeader, Cell> {

    private final LayoutInflater mInflater;
    String classtype, selectedDate;


    public TableViewAdapter(Context context, String classtype, String selectedDate) {
        super(context);
        this.mInflater = LayoutInflater.from(mContext);
        this.classtype = classtype;
        this.selectedDate = selectedDate;
    }

    @Override
    public AbstractViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
        View layout = mInflater.inflate(R.layout.byclass_tableview_cell, parent, false);
        return new CellViewHolder(layout);
    }

    @Override
    public void onBindCellViewHolder(AbstractViewHolder holder, Object cellItemModel, final int columnPosition, final int rowPosition) {
        final Cell cell = (Cell) cellItemModel;
        CellViewHolder cellViewHolder = (CellViewHolder)holder;
        cellViewHolder.cell.setText(cell.getCell_data());
        cellViewHolder.percent.setText(cell.getPercent());

        cellViewHolder.cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogTable(cell.getmId(), Constants.byClassi_list.get(columnPosition).getId());
            }
        });
    }


    @Override
    public AbstractViewHolder onCreateColumnHeaderViewHolder(ViewGroup parent, int viewType) {
        View layout = mInflater.inflate(R.layout.byclass_tableview_column_header, parent, false);
        return new ColumnHeaderViewHolder(layout, getTableView());
    }

    @Override
    public void onBindColumnHeaderViewHolder(AbstractViewHolder holder, Object columnHeaderItemModel, int columnPosition) {
        ColumnHeader_byClassi columnHeaderByClassi = (ColumnHeader_byClassi) columnHeaderItemModel;
        ColumnHeaderViewHolder columnHeaderViewHolder = (ColumnHeaderViewHolder)holder;
        columnHeaderViewHolder.text_title.setText(columnHeaderByClassi.getColumn_data());
    }


    @Override
    public AbstractViewHolder onCreateRowHeaderViewHolder(ViewGroup parent, int viewType) {
        View layout = mInflater.inflate(R.layout.byclass_tableview_row_header, parent, false);
        return new RowHeaderViewHolder(layout);
    }

    @Override
    public void onBindRowHeaderViewHolder(final AbstractViewHolder holder, Object rowHeaderItemModel, int rowPosition) {
        final RowHeader rowHeader = (RowHeader) rowHeaderItemModel;
        final RowHeaderViewHolder rowHeaderViewHolder = (RowHeaderViewHolder)holder;
        rowHeaderViewHolder.row_header_textview.setText(rowHeader.getRow_data());
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
        View corner = mInflater.inflate(R.layout.byclass_tableview_corner, null);
        return corner;
    }

    private void openDialogTable(String id, String branch){
        Bundle bundle = new Bundle();
        bundle.putString("table_type", "by_class");
        bundle.putString("id", id);
        bundle.putString("branch", branch);
        bundle.putString("classtype", classtype);
        bundle.putString("selectedDate", selectedDate);
        DialogFragment dialogFragment = new dialogTable_main();
        FragmentTransaction ft = ((MainActivity) mContext).getSupportFragmentManager().beginTransaction();
        Fragment prev = ((MainActivity) mContext).getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {ft.remove(prev);}
        ft.addToBackStack(null);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(ft, "dialog");
    }

}
