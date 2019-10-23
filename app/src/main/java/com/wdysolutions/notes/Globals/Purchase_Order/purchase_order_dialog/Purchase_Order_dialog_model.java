package com.wdysolutions.notes.Globals.Purchase_Order.purchase_order_dialog;

public class Purchase_Order_dialog_model {
    String count;
    String id;
    String btn_update;
    String product_id;
    String quantity;
    String date_needed;
    String needed_by;
    String remaining_qty;
    String remarks;
    String purpose;
    String description;
    String supplier_id;
    String supplier_price;
    String packaging_id;
    String subtotal;

    public Purchase_Order_dialog_model(String count,
                                       String id,
                                       String product_id,
                                       String btn_update,
                                       String quantity,
                                       String date_needed,
                                       String needed_by,
                                       String remaining_qty,
                                       String remarks,
                                       String purpose,
                                       String description,
                                       String supplier_id,
                                       String supplier_price,
                                       String packaging_id,
                                       String subtotal){

        this.count=count;
        this.id=id;
        this.product_id=product_id;
        this.btn_update=btn_update;
        this.quantity=quantity;
        this.date_needed=date_needed;
        this.needed_by=needed_by;
        this.remaining_qty=remaining_qty;
        this.remarks=remarks;
        this.purpose=purpose;
        this.description=description;
        this.supplier_id=supplier_id;
        this.supplier_price=supplier_price;
        this.packaging_id=packaging_id;
        this.subtotal=subtotal;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getId() {
        return id;
    }

    public String getCount() {
        return count;
    }

    public String getBtn_update() {
        return btn_update;
    }

    public String getRemarks() {
        return remarks;
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

    public String getPackaging_id() {
        return packaging_id;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getRemaining_qty() {
        return remaining_qty;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public String getSupplier_id() {
        return supplier_id;
    }

    public String getSupplier_price() {
        return supplier_price;
    }

}
