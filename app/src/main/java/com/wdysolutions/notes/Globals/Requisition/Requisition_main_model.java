package com.wdysolutions.notes.Globals.Requisition;

public class Requisition_main_model {
    String rs_id;
    String remarks;
    String rs_number;
    String date_added;
    String requested_by;
    String status;
    String count;
    int check_status;
    String date_raw;
    String asset;


    public Requisition_main_model(String rs_id,String remarks,String rs_number,String date_added,String requested_by,String status,String count,int check_status,String date_raw, String asset){
        this.rs_id = rs_id;
        this.remarks = remarks;
        this.rs_number = rs_number;
        this.date_added = date_added;
        this.requested_by = requested_by;
        this.status = status;
        this.count = count;
        this.check_status = check_status;
        this.date_raw = date_raw;
        this.asset = asset;
    }

    public String getAsset() {
        return asset;
    }

    public String getDate_raw() {
        return date_raw;
    }

    public String getStatus() {
        return status;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getDate_added() {
        return date_added;
    }

    public String getRequested_by() {
        return requested_by;
    }

    public String getRs_id() {
        return rs_id;
    }

    public String getRs_number() {
        return rs_number;
    }

    public String getCount() {
        return count;
    }

    public int getCheck_status() {
        return check_status;
    }
}
