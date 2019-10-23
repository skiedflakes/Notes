package com.wdysolutions.notes.Globals.Petty_Cash.Liquidation;

public class PettyCash_liquidation_model {

     String id, tracking_num, br_id, date_covered, branch, amount, date_liquidated, stats, stats_color, rfr_stat, rfr_stat_color,
    status, status_color, liquidate_by, rfr_status, approved_by;

     public PettyCash_liquidation_model(String id, String tracking_num, String br_id, String date_covered, String branch, String amount,
                                        String date_liquidated, String stats, String stats_color, String rfr_stat, String rfr_stat_color,
                                        String status, String status_color, String liquidate_by, String rfr_status, String approved_by){
         this.id = id;
         this.tracking_num = tracking_num;
         this.br_id = br_id;
         this.date_covered = date_covered;
         this.branch = branch;
         this.amount = amount;
         this.date_liquidated = date_liquidated;
         this.stats = stats;
         this.stats_color = stats_color;
         this.rfr_stat = rfr_stat;
         this.rfr_stat_color = rfr_stat_color;
         this.status = status;
         this.status_color = status_color;
         this.liquidate_by = liquidate_by;
         this.rfr_status = rfr_status;
         this.approved_by = approved_by;
     }

    public String getAmount() {
        return amount;
    }

    public String getId() {
        return id;
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

    public String getApproved_by() {
        return approved_by;
    }

    public String getRfr_status() {
        return rfr_status;
    }

    public String getStats() {
        return stats;
    }

    public String getStats_color() {
        return stats_color;
    }

    public String getBranch() {
        return branch;
    }

    public String getDate_covered() {
        return date_covered;
    }

    public String getDate_liquidated() {
        return date_liquidated;
    }

    public String getLiquidate_by() {
        return liquidate_by;
    }

    public String getRfr_stat() {
        return rfr_stat;
    }

    public String getRfr_stat_color() {
        return rfr_stat_color;
    }

    public String getTracking_num() {
        return tracking_num;
    }
}
