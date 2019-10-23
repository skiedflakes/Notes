package com.wdysolutions.notes.Globals.Revolving_Fund.Replenish.modal_view;

public class Revolving_replenish_modal_model {

    String id, branch, liquidation, amount, hid;

    public Revolving_replenish_modal_model(String id, String branch, String liquidation, String amount, String hid){
        this.id = id;
        this.branch = branch;
        this.liquidation = liquidation;
        this.amount = amount;
        this.hid = hid;
    }

    public String getBranch() {
        return branch;
    }

    public String getAmount() {
        return amount;
    }

    public String getId() {
        return id;
    }

    public String getHid() {
        return hid;
    }

    public String getLiquidation() {
        return liquidation;
    }

}
