package com.wdysolutions.notes.Globals.Revolving_Fund.Liquidation;

public class Revolving_liquidation_model {

    String id, br_id, tracking_num, date_covered, amount, date_liquidated, stats, stats_color, rfr_stat, rfr_stat_color,
    status, status_color, liquidate_by, approved_by;

    public Revolving_liquidation_model(String id, String br_id, String tracking_num, String date_covered, String amount, String date_liquidated,
                                       String stats, String stats_color, String rfr_stat, String rfr_stat_color, String status,
                                       String status_color, String liquidate_by, String approved_by){
        this.id = id;
        this.br_id = br_id;
        this.tracking_num = tracking_num;
        this.date_covered = date_covered;
        this.amount = amount;
        this.date_liquidated = date_liquidated;
        this.stats = stats;
        this.stats_color = stats_color;
        this.rfr_stat = rfr_stat;
        this.rfr_stat_color = rfr_stat_color;
        this.status = status;
        this.status_color = status_color;
        this.liquidate_by = liquidate_by;
        this.approved_by = approved_by;
    }

    public String getId() {
        return id;
    }

    public String getAmount() {
        return amount;
    }

    public String getBr_id() {
        return br_id;
    }

    public String getStatus_color() {
        return status_color;
    }

    public String getStatus() {
        return status;
    }

    public String getRfr_stat() {
        return rfr_stat;
    }

    public String getRfr_stat_color() {
        return rfr_stat_color;
    }

    public String getApproved_by() {
        return approved_by;
    }

    public String getTracking_num() {
        return tracking_num;
    }

    public String getLiquidate_by() {
        return liquidate_by;
    }

    public String getDate_liquidated() {
        return date_liquidated;
    }

    public String getDate_covered() {
        return date_covered;
    }

    public String getStats_color() {
        return stats_color;
    }

    public String getStats() {
        return stats;
    }
}
