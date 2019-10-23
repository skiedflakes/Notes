package com.wdysolutions.notes.Globals.Requisition.Requisition_Add.Canvassing;

public class CanvasTable_model {

    private String id;
    private String supplier_id;
    private String supplier;
    private String price;
    private String date_added;
    private String remarks;
    private String inventory;
    private String consumed;
    private String pending;
    private String status;

    public CanvasTable_model(String id, String supplier_id, String price, String date_added, String remarks, String inventory,
                             String consumed, String pending, String status, String supplier){
        this.id = id;
        this.supplier_id = supplier_id;
        this.price = price;
        this.date_added = date_added;
        this.remarks = remarks;
        this.inventory = inventory;
        this.consumed = consumed;
        this.pending = pending;
        this.status = status;
        this.supplier = supplier;
    }

    public String getSupplier() {
        return supplier;
    }

    public String getSupplier_id() {
        return supplier_id;
    }

    public String getId() {
        return id;
    }

    public String getConsumed() {
        return consumed;
    }

    public String getDate_added() {
        return date_added;
    }

    public String getInventory() {
        return inventory;
    }

    public String getPending() {
        return pending;
    }

    public String getPrice() {
        return price;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getStatus() {
        return status;
    }
}
