package com.wdysolutions.notes.Globals.Petty_Cash.Request;

public class PettyCash_request_model {

    String id, credit_method_test, count, pcv, date_requested, amount, remarks, date_encoded, created_by, br_id, receipt_status,
    receipt_status_color, userID, stats, stats_color, hid, liquidate_stats, liquidate_color, declared_status, declared_status_color,
    rfr_status, rfr_status_color, dnr_stat, liquidation_stat, rep_stats, rep_stat, approved_by;

    public PettyCash_request_model(String id, String credit_method_test, String count, String pcv, String date_requested, String amount,
                                   String remarks, String date_encoded, String created_by, String br_id, String receipt_status, String receipt_status_color,
                                   String userID, String stats, String stats_color, String hid, String liquidate_stats, String liquidate_color, String declared_status, String declared_status_color,
                                   String rfr_status, String rfr_status_color, String dnr_stat, String liquidation_stat, String rep_stats, String rep_stat, String approved_by){
        this.id = id;
        this.credit_method_test = credit_method_test;
        this.count = count;
        this.pcv = pcv;
        this.date_requested = date_requested;
        this.amount = amount;
        this.remarks = remarks;
        this.date_encoded = date_encoded;
        this.created_by = created_by;
        this.br_id = br_id;
        this.receipt_status = receipt_status;
        this.receipt_status_color = receipt_status_color;
        this.userID = userID;
        this.stats = stats;
        this.stats_color = stats_color;
        this.hid = hid;
        this.liquidate_stats = liquidate_stats;
        this.liquidate_color = liquidate_color;
        this.declared_status = declared_status;
        this.declared_status_color = declared_status_color;
        this.rfr_status = rfr_status;
        this.rfr_status_color = rfr_status_color;
        this.dnr_stat = dnr_stat;
        this.liquidation_stat = liquidation_stat;
        this.rep_stats = rep_stats;
        this.rep_stat = rep_stat;
        this.approved_by = approved_by;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getDate_requested() {
        return date_requested;
    }

    public String getCreated_by() {
        return created_by;
    }

    public String getAmount() {
        return amount;
    }

    public String getId() {
        return id;
    }

    public String getApproved_by() {
        return approved_by;
    }

    public String getBr_id() {
        return br_id;
    }

    public String getCount() {
        return count;
    }

    public String getCredit_method_test() {
        return credit_method_test;
    }

    public String getDate_encoded() {
        return date_encoded;
    }

    public String getDeclared_status() {
        return declared_status;
    }

    public String getHid() {
        return hid;
    }

    public String getDeclared_status_color() {
        return declared_status_color;
    }

    public String getDnr_stat() {
        return dnr_stat;
    }

    public String getLiquidate_color() {
        return liquidate_color;
    }

    public String getLiquidate_stats() {
        return liquidate_stats;
    }

    public String getPcv() {
        return pcv;
    }

    public String getLiquidation_stat() {
        return liquidation_stat;
    }

    public String getReceipt_status() {
        return receipt_status;
    }

    public String getReceipt_status_color() {
        return receipt_status_color;
    }

    public String getRep_stat() {
        return rep_stat;
    }

    public String getRep_stats() {
        return rep_stats;
    }

    public String getRfr_status() {
        return rfr_status;
    }

    public String getRfr_status_color() {
        return rfr_status_color;
    }

    public String getStats() {
        return stats;
    }

    public String getStats_color() {
        return stats_color;
    }

    public String getUserID() {
        return userID;
    }
}
