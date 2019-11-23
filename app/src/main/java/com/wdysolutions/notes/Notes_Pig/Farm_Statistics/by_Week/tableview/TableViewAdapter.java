
package com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Week.tableview;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.wdysolutions.notes.Constants;
import com.wdysolutions.notes.MainActivity;
import com.wdysolutions.notes.Notes_Pig.Farm_Statistics.Dialog_Table_Details.dialogTable_details;
import com.wdysolutions.notes.Notes_Pig.Farm_Statistics.Graph.graph_main;
import com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Week.tableview.holder.CellViewHolder;
import com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Week.tableview.holder.ColumnHeaderViewHolder;
import com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Week.tableview.holder.RowHeaderViewHolder;
import com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Week.tableview.model.Cell;
import com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Week.tableview.model.ColumnHeader;
import com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Week.tableview.model.RowHeader;
import com.wdysolutions.notes.R;


public class TableViewAdapter extends AbstractTableAdapter<ColumnHeader, RowHeader, Cell> {

    private final LayoutInflater mInflater;


    public TableViewAdapter(Context context) {
        super(context);
        this.mInflater = LayoutInflater.from(mContext);
    }


    @Override
    public AbstractViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
        View layout = mInflater.inflate(R.layout.tableview_main_cell, parent, false);
        return new CellViewHolder(layout);
    }

    @Override
    public void onBindCellViewHolder(AbstractViewHolder holder, Object cellItemModel, final int columnPosition, final int rowPosition) {
        final Cell cell = (Cell) cellItemModel;
        CellViewHolder cellViewHolder = (CellViewHolder)holder;

        // Percent -----------------------------------
        cellViewHolder.percent.setText(cell.getPercent());

        if (cell.getPercent_bg().equals("red") && cell.getPercent_underline().equals("yes")){
            cellViewHolder.percent.setBackground(mContext.getResources().getDrawable(R.drawable.custom_textview_underline_red_dotted));
            cellViewHolder.percent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDialogTable(Constants.weeks_models.get(columnPosition).getId(),
                            Constants.rowHeaders.get(rowPosition).getFarm_statistics_unique(),
                            Constants.selectedYear, "week");
                }
            });
        } else if (cell.getPercent_bg().equals("red")){
            cellViewHolder.percent.setBackgroundColor(mContext.getResources().getColor(R.color.color_table_color_red));
            cellViewHolder.percent.setOnClickListener(null);
        } else if (cell.getPercent_underline().equals("yes")){
            cellViewHolder.percent.setBackground(mContext.getResources().getDrawable(R.drawable.custom_textview_underline_dotted));
            cellViewHolder.percent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDialogTable(Constants.weeks_models.get(columnPosition).getId(),
                            Constants.rowHeaders.get(rowPosition).getFarm_statistics_unique(),
                            Constants.selectedYear, "week");
                }
            });
        } else {
            cellViewHolder.percent.setBackgroundColor(mContext.getResources().getColor(R.color.color_table_color_light_blue));
            cellViewHolder.percent.setOnClickListener(null);
        }

        // Current -----------------------------------
        cellViewHolder.current.setText(cell.getCurrent());

        if (cell.getCurrent_bg().equals("red") && cell.getCurrent_underline().equals("yes")){
            cellViewHolder.current.setBackground(mContext.getResources().getDrawable(R.drawable.custom_textview_underline_red_dotted));
            cellViewHolder.current.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDialogTable(Constants.weeks_models.get(columnPosition).getId(),
                            Constants.rowHeaders.get(rowPosition).getFarm_statistics_unique(),
                            Constants.selectedYear, "week");
                }
            });
        } else if (cell.getCurrent_bg().equals("red")){
            cellViewHolder.current.setBackgroundColor(mContext.getResources().getColor(R.color.color_table_color_red));
            cellViewHolder.current.setOnClickListener(null);
        } else if (cell.getCurrent_underline().equals("yes")){
            cellViewHolder.current.setBackground(mContext.getResources().getDrawable(R.drawable.custom_textview_underline_dotted));
            cellViewHolder.current.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDialogTable(Constants.weeks_models.get(columnPosition).getId(),
                            Constants.rowHeaders.get(rowPosition).getFarm_statistics_unique(),
                            Constants.selectedYear, "week");
                }
            });
        } else {
            cellViewHolder.current.setBackgroundColor(mContext.getResources().getColor(R.color.color_table_color_light_blue));
            cellViewHolder.current.setOnClickListener(null);
        }


        // Ave -----------------------------------
        cellViewHolder.ave.setText(cell.getAve());

        if (cell.getAve_bg().equals("red") && cell.getAve_underline().equals("yes")){
            cellViewHolder.ave.setBackground(mContext.getResources().getDrawable(R.drawable.custom_textview_underline_red_dotted));
            cellViewHolder.ave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDialogTable(Constants.weeks_models.get(columnPosition).getId(),
                            Constants.rowHeaders.get(rowPosition).getFarm_statistics_unique(),
                            Constants.selectedYear, "week");
                }
            });
        } else if (cell.getAve_bg().equals("red")){
            cellViewHolder.ave.setBackgroundColor(mContext.getResources().getColor(R.color.color_table_color_red));
            cellViewHolder.ave.setOnClickListener(null);
        } else if (cell.getAve_underline().equals("yes")){
            cellViewHolder.ave.setBackground(mContext.getResources().getDrawable(R.drawable.custom_textview_underline_dotted));
            cellViewHolder.ave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDialogTable(Constants.weeks_models.get(columnPosition).getId(),
                            Constants.rowHeaders.get(rowPosition).getFarm_statistics_unique(),
                            Constants.selectedYear, "week");
                }
            });
        } else {
            cellViewHolder.ave.setBackgroundColor(mContext.getResources().getColor(R.color.color_table_color_light_blue));
            cellViewHolder.ave.setOnClickListener(null);
        }
    }


    @Override
    public AbstractViewHolder onCreateColumnHeaderViewHolder(ViewGroup parent, int viewType) {
        View layout = mInflater.inflate(R.layout.tableview_main_column_header, parent, false);
        return new ColumnHeaderViewHolder(layout, getTableView());
    }

    @Override
    public void onBindColumnHeaderViewHolder(AbstractViewHolder holder, Object columnHeaderItemModel, int columnPosition) {
        ColumnHeader columnHeader = (ColumnHeader) columnHeaderItemModel;
        ColumnHeaderViewHolder columnHeaderViewHolder = (ColumnHeaderViewHolder)holder;

        columnHeaderViewHolder.column_header_textview.setText(columnHeader.getData());
        columnHeaderViewHolder.column_header_textView2.setText(columnHeader.getData2());
        columnHeaderViewHolder.column_header_textView3.setText(columnHeader.getData3());
        columnHeaderViewHolder.text_week.setText(columnHeader.getWeek());
    }


    @Override
    public AbstractViewHolder onCreateRowHeaderViewHolder(ViewGroup parent, int viewType) {
        View layout = mInflater.inflate(R.layout.tableview_main_row_header, parent, false);
        return new RowHeaderViewHolder(layout);
    }

    @Override
    public void onBindRowHeaderViewHolder(final AbstractViewHolder holder, Object rowHeaderItemModel, int rowPosition) {
        final RowHeader rowHeader = (RowHeader) rowHeaderItemModel;
        final RowHeaderViewHolder rowHeaderViewHolder = (RowHeaderViewHolder)holder;

        rowHeaderViewHolder.row_header_textview.setText(rowHeader.getCurrent());
        rowHeaderViewHolder.row_header_textview_ave.setText(rowHeader.getAve());
        rowHeaderViewHolder.row_header_textview_percent.setText(rowHeader.getPercent());
        rowHeaderViewHolder.row_header_textview_name.setText(rowHeader.getName());

        if (rowHeader.getCounter_clickable() > 0){
            rowHeaderViewHolder.row_header_textview_name.setTypeface(rowHeaderViewHolder.row_header_textview_name.getTypeface(), Typeface.BOLD_ITALIC);
            rowHeaderViewHolder.row_header_textview_name.setAllCaps(true);
        } else {
            rowHeaderViewHolder.row_header_textview_name.setTypeface(Typeface.DEFAULT);
            rowHeaderViewHolder.row_header_textview_name.setAllCaps(false);
        }

        rowHeaderViewHolder.row_header_textview_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenuGraph(rowHeaderViewHolder, rowHeader, "byweek");
            }
        });
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
        View corner = mInflater.inflate(R.layout.tableview_main_corner, null);
        return corner;
    }

    private void openMenuGraph(RowHeaderViewHolder holder, final RowHeader rowHeader, final String table_selected){
        PopupMenu popup = new PopupMenu(mContext, holder.row_header_textview_name);
        popup.inflate(R.menu.menu_table);

        if (rowHeader.getDropdown_current_graph().equals("1")){ popup.getMenu().getItem(0).setVisible(true); }
        if (rowHeader.getDropdown_ave_graph().equals("1")){ popup.getMenu().getItem(1).setVisible(true); }
        if (rowHeader.getDropdown_perc_graph().equals("1")){ popup.getMenu().getItem(2).setVisible(true); }
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String selected;

                if (item.getItemId() == R.id.action_curr){ // current
                    selected = rowHeader.getFarm_statistics_curr();
                } else if (item.getItemId() == R.id.action_ave){ // average
                    selected = rowHeader.getFarm_statistics_ave();
                } else { // percentage
                    selected = rowHeader.getFarm_statistics_perc();
                }

                Bundle bundle = new Bundle();
                bundle.putString("table_is", table_selected);
                bundle.putString("farm_statistics", selected);
                DialogFragment dialogFragment = new graph_main();
                FragmentTransaction ft = ((MainActivity) mContext).getSupportFragmentManager().beginTransaction();
                Fragment prev = ((MainActivity) mContext).getSupportFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {ft.remove(prev);}
                ft.addToBackStack(null);
                dialogFragment.setArguments(bundle);
                dialogFragment.show(ft, "dialog");
                
                return true;
            }
        });
        popup.show();
    }

    private void openDialogTable(String week, String module, String year, String table_selected){
        Bundle bundle = new Bundle();
        bundle.putString("table_selected", table_selected);
        bundle.putString("week_month", week);
        bundle.putString("module", module);
        bundle.putString("year", year);
        DialogFragment dialogFragment = new dialogTable_details();
        FragmentTransaction ft = ((MainActivity) mContext).getSupportFragmentManager().beginTransaction();
        Fragment prev = ((MainActivity) mContext).getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {ft.remove(prev);}
        ft.addToBackStack(null);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(ft, "dialog");
    }

}
