package com.wdysolutions.notes.Globals.Revolving_Fund.Replenish;

public class Revolving_replenish_model {
    String id, br_id, rplnsh_num, date, amount, remarks, status, status_color, rfr_stats, rfr_stat, rfr_stat_color, dec_stat,
            dec_stat_color, encodedBY, approved_by;

    public Revolving_replenish_model(String id, String br_id, String rplnsh_num, String date, String amount, String remarks, String status,
                                     String status_color, String rfr_stats, String rfr_stat, String rfr_stat_color, String dec_stat,
                                     String dec_stat_color, String encodedBY, String approved_by){
        this.id = id;
        this.br_id = br_id;
        this.rplnsh_num = rplnsh_num;
        this.date = date;
        this.amount = amount;
        this.remarks = remarks;
        this.status = status;
        this.status_color = status_color;
        this.rfr_stats = rfr_stats;
        this.rfr_stat = rfr_stat;
        this.rfr_stat_color = rfr_stat_color;
        this.dec_stat = dec_stat;
        this.dec_stat_color = dec_stat_color;
        this.encodedBY = encodedBY;
        this.approved_by = approved_by;
    }

    public String getRfr_stat_color() {
        return rfr_stat_color;
    }

    public String getRfr_stat() {
        return rfr_stat;
    }

    public String getApproved_by() {
        return approved_by;
    }

    public String getStatus() {
        return status;
    }

    public String getStatus_color() {
        return status_color;
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

    public String getRemarks() {
        return remarks;
    }

    public String getDate() {
        return date;
    }

    public String getDec_stat() {
        return dec_stat;
    }

    public String getDec_stat_color() {
        return dec_stat_color;
    }

    public String getEncodedBY() {
        return encodedBY;
    }

    public String getRfr_stats() {
        return rfr_stats;
    }

    public String getRplnsh_num() {
        return rplnsh_num;
    }
}
