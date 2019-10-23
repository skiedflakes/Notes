package com.wdysolutions.notes.Globals.Requisition.Requisition_Add.Canvassing;

public class Supplier_model {

    private String supplier;
    private String supplier_id;

    public Supplier_model(String supplier, String supplier_id){
        this.supplier = supplier;
        this.supplier_id = supplier_id;
    }

    public String getSupplier() {
        return supplier;
    }

    public String getSupplier_id() {
        return supplier_id;
    }
}
