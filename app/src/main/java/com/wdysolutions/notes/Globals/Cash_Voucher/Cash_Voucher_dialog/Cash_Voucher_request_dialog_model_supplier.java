package com.wdysolutions.notes.Globals.Cash_Voucher.Cash_Voucher_dialog;

public class Cash_Voucher_request_dialog_model_supplier {

    String id;
    String count;
    String supplier;
    String ref_num;
    String desc;
    String amount;

    public  Cash_Voucher_request_dialog_model_supplier(String id,String count,String supplier,
                                              String ref_num,String desc,String amount){
        this.id = id;
        this.count = count;
        this.supplier = supplier;
        this.ref_num = ref_num;
        this.desc = desc;
        this.amount = amount;

    }

    public String getId() {
        return id;
    }

    public String getCount() {
        return count;
    }

    public String getAmount() {
        return amount;
    }

    public String getDesc() {
        return desc;
    }

    public String getRef_num() {
        return ref_num;
    }

    public String getSupplier() {
        return supplier;
    }

}
