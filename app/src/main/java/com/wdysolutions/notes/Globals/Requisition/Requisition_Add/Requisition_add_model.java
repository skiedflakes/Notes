package com.wdysolutions.notes.Globals.Requisition.Requisition_Add;

public class Requisition_add_model {
    String id;
    String count;
    String canvas_status;
    String product;
    String product_id;
    String quantity;
    String date_needed;
    String needed_by;
    String purpose;
    String description;
    String supplier_id;
    String supplier_price;
    String package_name;
    String subtotal;
    String status;
    String type;
    int check_status;

    public Requisition_add_model(String id,String count,String canvas_status,String product,String quantity,
                                String date_needed, String needed_by, String purpose, String description,
                                 String supplier_id, String supplier_price, String package_name, String subtotal,
                                String status,String type,int check_status, String product_id){
        this.product_id = product_id;
        this.id = id;
        this.count = count;
        this.canvas_status = canvas_status;
        this.product = product;
        this.quantity = quantity;
        this.date_needed = date_needed;
        this.needed_by = needed_by;
        this.purpose = purpose;
        this.description = description;
        this.supplier_id = supplier_id;
        this.supplier_price = supplier_price;
        this.package_name = package_name;
        this.subtotal = subtotal;
        this.status = status;
        this.type = type;
        this.check_status=check_status;
    }

    public String getProduct_id() {
        return product_id;
    }

    public int getCheck_status() {
        return check_status;
    }

    public String getCount() {
        return count;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getCanvas_status() {
        return canvas_status;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public String getDate_needed() {
        return date_needed;
    }

    public String getDescription() {
        return description;
    }

    public String getNeeded_by() {
        return needed_by;
    }

    public String getPackage_name() {
        return package_name;
    }

    public String getProduct() {
        return product;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getSupplier_id() {
        return supplier_id;
    }

    public String getSupplier_price() {
        return supplier_price;
    }

    public String getType() {
        return type;
    }

}
