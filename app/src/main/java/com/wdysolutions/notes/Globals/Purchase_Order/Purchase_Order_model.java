package com.wdysolutions.notes.Globals.Purchase_Order;

public class Purchase_Order_model {
    String id;
    String count;
    String br_id;
    String purchase_num;
    String date;
    String supplier;
    String remarks;
    String category;
    String po_status;

    String rr_status;
    String unrecieved_total;
    String encoded_by;
    String declared_status;

    //color
    String po_status_color;
    String rr_status_color;
    String dec_status_color;

    String approved_by;
    String checked_by;

    public Purchase_Order_model(String id,String count,String br_id,String purchase_num,String date, String supplier, String remarks
            ,String category,String po_status,String rr_status,String unrecieved_total
            ,String encoded_by,String declared_status,String po_status_color,String rr_status_color
            ,String dec_status_color,String approved_by,String checked_by){
        this.date=date;
        this.id=id;
        this.count=count;
        this.br_id=br_id;
        this.purchase_num=purchase_num;
        this.supplier=supplier;
        this.remarks=remarks;
        this.category=category;
        this.po_status=po_status;
        this.rr_status=rr_status;
        this.unrecieved_total=unrecieved_total;
        this.encoded_by=encoded_by;
        this.declared_status=declared_status;
        this.po_status_color=po_status_color;
        this.rr_status_color=rr_status_color;
        this.dec_status_color=dec_status_color;
        this.approved_by=approved_by;
        this.checked_by=checked_by;

    }


    public String getApproved_by() {
        return approved_by;
    }

    public String getChecked_by() {
        return checked_by;
    }

    public String getDec_status_color() {
        return dec_status_color;
    }

    public String getRr_status_color() {
        return rr_status_color;
    }

    public String getPo_status_color() {
        return po_status_color;
    }

    public String getDate() {
        return date;
    }

    public String getSupplier() {
        return supplier;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getCategory() {
        return category;
    }

    public String getPo_status() {
        return po_status;
    }

    public String getEncoded_by() {
        return encoded_by;
    }

    public String getDeclared_status() {
        return declared_status;
    }

    public String getCount() {
        return count;
    }

    public String getId() {
        return id;
    }

    public String getBr_id() {
        return br_id;
    }

    public String getPurchase_num() {
        return purchase_num;
    }

    public String getRr_status() {
        return rr_status;
    }

    public String getUnrecieved_total() {
        return unrecieved_total;
    }
}
