
package com.wdysolutions.notes.Notes_Pig.Swine_Population.byAge.tableview;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.wdysolutions.notes.Constants;
import com.wdysolutions.notes.MainActivity;
import com.wdysolutions.notes.Notes_Pig.Swine_Population.Dialog_Table.dialogTable_main;
import com.wdysolutions.notes.Notes_Pig.Swine_Population.byAge.tableview.holder.SP_CellViewHolder;
import com.wdysolutions.notes.Notes_Pig.Swine_Population.byAge.tableview.holder.SP_ColumnHeaderViewHolder;
import com.wdysolutions.notes.Notes_Pig.Swine_Population.byAge.tableview.holder.SP_RowHeaderViewHolder;
import com.wdysolutions.notes.Notes_Pig.Swine_Population.byAge.tableview.model.SP_Cell;
import com.wdysolutions.notes.Notes_Pig.Swine_Population.byAge.tableview.model.SP_ColumnHeader;
import com.wdysolutions.notes.Notes_Pig.Swine_Population.byAge.tableview.model.SP_RowHeader;
import com.wdysolutions.notes.R;

/**
 * Created by evrencoskun on 11/06/2017.
 * <p>
 * This is a sample of custom TableView Adapter.
 */

public class SP_TableViewAdapter extends AbstractTableAdapter<SP_ColumnHeader, SP_RowHeader, SP_Cell> {
    private final LayoutInflater mInflater;
   final String selected_date;
    public SP_TableViewAdapter(Context context,String selected_date) {
        super(context);
        this.mInflater = LayoutInflater.from(mContext);
        this.selected_date = selected_date;
    }

    @Override
    public AbstractViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
        View layout = mInflater.inflate(R.layout.swine_population_tableview_cell, parent, false);
        return new SP_CellViewHolder(layout);
    }

    @Override
    public void onBindCellViewHolder(AbstractViewHolder holder, Object cellItemModel, final int columnPosition, final int rowPosition) {
        SP_Cell cell = (SP_Cell) cellItemModel;
        SP_CellViewHolder cellViewHolder = (SP_CellViewHolder)holder;
        cellViewHolder.data1.setText(cell.getData1());
        cellViewHolder.data2.setText(cell.getData2());
        cellViewHolder.data3.setText(cell.getData3());
        cellViewHolder.data4.setText(cell.getData4());
        cellViewHolder.data5.setText(cell.getData5());
        cellViewHolder.data6.setText(cell.getData6());
        cellViewHolder.data7.setText(cell.getData7());
        cellViewHolder.data8.setText(cell.getData8());
        cellViewHolder.data9.setText(cell.getData9());
        cellViewHolder.data10.setText(cell.getData10());
        cellViewHolder.data11.setText(cell.getData11());
        cellViewHolder.data12.setText(cell.getData12());
        cellViewHolder.data13.setText(cell.getData13());
        cellViewHolder.data14.setText(cell.getData14());
        cellViewHolder.data15.setText(cell.getData15());
        cellViewHolder.data16.setText(cell.getData16());
        cellViewHolder.data17.setText(cell.getData17());
        cellViewHolder.data18.setText(cell.getData18());

        cellViewHolder.data1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String selected_branch_id = Constants.SP_ColumnHeader.get(columnPosition).getId();
                final String swine_type  = Constants.SP_type.get(0);
                openDialogTable(selected_date,swine_type,selected_branch_id);
            }
        });

        cellViewHolder.data2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String selected_branch_id = Constants.SP_ColumnHeader.get(columnPosition).getId();
                final String swine_type  = Constants.SP_type.get(1);
                openDialogTable(selected_date,swine_type,selected_branch_id);
            }
        });
        cellViewHolder.data3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String selected_branch_id = Constants.SP_ColumnHeader.get(columnPosition).getId();
                final String swine_type  = Constants.SP_type.get(2);
                openDialogTable(selected_date,swine_type,selected_branch_id);
            }
        });
        cellViewHolder.data4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String selected_branch_id = Constants.SP_ColumnHeader.get(columnPosition).getId();
                final String swine_type  = Constants.SP_type.get(3);
                openDialogTable(selected_date,swine_type,selected_branch_id);
            }
        });
        cellViewHolder.data5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String selected_branch_id = Constants.SP_ColumnHeader.get(columnPosition).getId();
                final String swine_type  = Constants.SP_type.get(4);
                openDialogTable(selected_date,swine_type,selected_branch_id);
            }
        });
        cellViewHolder.data6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String selected_branch_id = Constants.SP_ColumnHeader.get(columnPosition).getId();
                final String swine_type  = Constants.SP_type.get(5);
                openDialogTable(selected_date,swine_type,selected_branch_id);
            }
        });
        cellViewHolder.data7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String selected_branch_id = Constants.SP_ColumnHeader.get(columnPosition).getId();
                final String swine_type  = Constants.SP_type.get(6);
                openDialogTable(selected_date,swine_type,selected_branch_id);
            }
        });
        cellViewHolder.data8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String selected_branch_id = Constants.SP_ColumnHeader.get(columnPosition).getId();
                final String swine_type  = Constants.SP_type.get(7);
                openDialogTable(selected_date,swine_type,selected_branch_id);
            }
        });
        cellViewHolder.data9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String selected_branch_id = Constants.SP_ColumnHeader.get(columnPosition).getId();
                final String swine_type  = Constants.SP_type.get(8);
                openDialogTable(selected_date,swine_type,selected_branch_id);
            }
        });
        cellViewHolder.data10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String selected_branch_id = Constants.SP_ColumnHeader.get(columnPosition).getId();
                final String swine_type  = Constants.SP_type.get(9);
                openDialogTable(selected_date,swine_type,selected_branch_id);
            }
        });
        cellViewHolder.data11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String selected_branch_id = Constants.SP_ColumnHeader.get(columnPosition).getId();
                final String swine_type  = Constants.SP_type.get(10);
                openDialogTable(selected_date,swine_type,selected_branch_id);
            }
        });
        cellViewHolder.data12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String selected_branch_id = Constants.SP_ColumnHeader.get(columnPosition).getId();
                final String swine_type  = Constants.SP_type.get(11);
                openDialogTable(selected_date,swine_type,selected_branch_id);
            }
        });
        cellViewHolder.data13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String selected_branch_id = Constants.SP_ColumnHeader.get(columnPosition).getId();
                final String swine_type  = Constants.SP_type.get(12);
                openDialogTable(selected_date,swine_type,selected_branch_id);
            }
        });
        cellViewHolder.data14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String selected_branch_id = Constants.SP_ColumnHeader.get(columnPosition).getId();
                final String swine_type  = Constants.SP_type.get(13);
                openDialogTable(selected_date,swine_type,selected_branch_id);
            }
        });
        cellViewHolder.data15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String selected_branch_id = Constants.SP_ColumnHeader.get(columnPosition).getId();
                final String swine_type  = Constants.SP_type.get(14);
                openDialogTable(selected_date,swine_type,selected_branch_id);
            }
        });
        cellViewHolder.data16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String selected_branch_id = Constants.SP_ColumnHeader.get(columnPosition).getId();
                final String swine_type  = Constants.SP_type.get(15);
                openDialogTable(selected_date,swine_type,selected_branch_id);
            }
        });
        cellViewHolder.data17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String selected_branch_id = Constants.SP_ColumnHeader.get(columnPosition).getId();
                final String swine_type  = Constants.SP_type.get(16);
               // Toast.makeText(mContext, swine_type, Toast.LENGTH_SHORT).show();
                openDialogTable(selected_date,swine_type,selected_branch_id);
            }
        });
        cellViewHolder.data18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String selected_branch_id = Constants.SP_ColumnHeader.get(columnPosition).getId();
                final String swine_type  = Constants.SP_type.get(17);
                openDialogTable(selected_date,swine_type,selected_branch_id);
            }
        });
    }


    @Override
    public AbstractViewHolder onCreateColumnHeaderViewHolder(ViewGroup parent, int viewType) {
        View layout = mInflater.inflate(R.layout.swine_population_tableview_column_header, parent, false);
        return new SP_ColumnHeaderViewHolder(layout, getTableView());
    }

    @Override
    public void onBindColumnHeaderViewHolder(AbstractViewHolder holder, Object columnHeaderItemModel, int columnPosition) {
        SP_ColumnHeader columnHeader = (SP_ColumnHeader) columnHeaderItemModel;
        SP_ColumnHeaderViewHolder columnHeaderViewHolder = (SP_ColumnHeaderViewHolder)holder;
        columnHeaderViewHolder.column_header_textview.setText(columnHeader.getData());

    }


    @Override
    public AbstractViewHolder onCreateRowHeaderViewHolder(ViewGroup parent, int viewType) {
        View layout = mInflater.inflate(R.layout.swine_population_tableview_row_header, parent, false);
        return new SP_RowHeaderViewHolder(layout);
    }

    @Override
    public void onBindRowHeaderViewHolder(AbstractViewHolder holder, Object rowHeaderItemModel, int rowPosition) {
        final SP_RowHeader rowHeaderByMonth = (SP_RowHeader) rowHeaderItemModel;
        final SP_RowHeaderViewHolder rowHeaderViewHolder = (SP_RowHeaderViewHolder)holder;

        rowHeaderViewHolder.data1.setText(rowHeaderByMonth.getData1());
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
        View corner = mInflater.inflate(R.layout.swine_population_tableview_corner, null);
        return corner;
    }


    private void openDialogTable(final String selectedDate,final String swine_type,final String branch){
        Bundle bundle = new Bundle();
        bundle.putString("table_type", "by_age");
        bundle.putString("selectedDate", selectedDate);
        bundle.putString("swine_type", swine_type);
        bundle.putString("branch", branch);
        DialogFragment dialogFragment = new dialogTable_main();
        FragmentTransaction ft = ((MainActivity) mContext).getSupportFragmentManager().beginTransaction();
        Fragment prev = ((MainActivity) mContext).getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {ft.remove(prev);}
        ft.addToBackStack(null);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(ft, "dialog");
    }


}
