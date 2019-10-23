package com.wdysolutions.notes.Globals.Revolving_Fund.Request;

public class Revolving_request_model {

    String credit_method_test, id, count, pcv, date_requested, amount, remarks, date_encoded, br, br_id, credit_method, created_by,
            userID, stats, stats_color, hid, liquidate_stats, liquidate_stats_color, status, status_color, rfr_status,
            rfr_status_color, dnr_stat, liquidation_stat, rep_stats, rep_stat, approved_by;

    public Revolving_request_model(String credit_method_test, String id, String count, String pcv, String date_requested, String amount,
                                   String remarks, String date_encoded, String br, String br_id, String credit_method, String created_by,
                                   String userID, String stats, String stats_color, String hid, String liquidate_stats, String liquidate_stats_color,
                                   String status, String status_color, String rfr_status, String rfr_status_color, String dnr_stat, String liquidation_stat,
                                   String rep_stats, String rep_stat, String approved_by){
        this.credit_method_test = credit_method_test;
        this.id = id;
        this.count = count;
        this.pcv = pcv;
        this.date_requested = date_requested;
        this.amount = amount;
        this.remarks = remarks;
        this.date_encoded = date_encoded;
        this.br = br;
        this.br_id = br_id;
        this.credit_method = credit_method;
        this.created_by = created_by;
        this.userID = userID;
        this.stats = stats;
        this.stats_color = stats_color;
        this.hid = hid;
        this.liquidate_stats = liquidate_stats;
        this.liquidate_stats_color = liquidate_stats_color;
        this.status = status;
        this.status_color = status_color;
        this.rfr_status = rfr_status;
        this.rfr_status_color = rfr_status_color;
        this.dnr_stat = dnr_stat;
        this.liquidation_stat = liquidation_stat;
        this.rep_stats = rep_stats;
        this.rep_stat = rep_stat;
        this.approved_by = approved_by;
    }

    public String getPcv() {
        return pcv;
    }

    public String getDate_encoded() {
        return date_encoded;
    }

    public String getUserID() {
        return userID;
    }

    public String getCredit_method_test() {
        return credit_method_test;
    }

    public String getCount() {
        return count;
    }

    public String getBr_id() {
        return br_id;
    }

    public String getId() {
        return id;
    }

    public String getAmount() {
        return amount;
    }

    public String getCreated_by() {
        return created_by;
    }

    public String getDate_requested() {
        return date_requested;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getStats_color() {
        return stats_color;
    }

    public String getStats() {
        return stats;
    }

    public String getBr() {
        return br;
    }

    public String getCredit_method() {
        return credit_method;
    }

    public String getHid() {
        return hid;
    }

    public String getLiquidate_stats() {
        return liquidate_stats;
    }

    public String getRfr_status() {
        return rfr_status;
    }

    public String getRfr_status_color() {
        return rfr_status_color;
    }

    public String getStatus() {
        return status;
    }

    public String getRep_stats() {
        return rep_stats;
    }

    public String getLiquidate_stats_color() {
        return liquidate_stats_color;
    }

    public String getRep_stat() {
        return rep_stat;
    }

    public String getStatus_color() {
        return status_color;
    }

    public String getLiquidation_stat() {
        return liquidation_stat;
    }

    public String getDnr_stat() {
        return dnr_stat;
    }

    public String getApproved_by() {
        return approved_by;
    }
}
