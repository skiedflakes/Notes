package com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Month;

public class Month_model {
    String month;
    String date_month;

    public Month_model(String month,String date_month){
        this.month = month;
        this.date_month = date_month;
    }

    public String getDate_month() {
        return date_month;
    }
    public String getMonth() {
        return month;
    }
}
