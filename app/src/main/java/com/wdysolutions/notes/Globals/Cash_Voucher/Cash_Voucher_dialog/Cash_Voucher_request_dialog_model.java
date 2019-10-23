package com.wdysolutions.notes.Globals.Cash_Voucher.Cash_Voucher_dialog;

public class Cash_Voucher_request_dialog_model {

    String id;
    String count;
    String expense_id;
    String remarks;
    String debit;
    String credit;

    public  Cash_Voucher_request_dialog_model(String id,String count,String expense_id,
      String remarks,String debit,String credit){
        this.id = id;
        this.count = count;
        this.expense_id = expense_id;
        this.remarks = remarks;
        this.debit = debit;
        this.credit = credit;

    }

    public String getCount() {
        return count;
    }

    public String getId() {
        return id;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getCredit() {
        return credit;
    }

    public String getDebit() {
        return debit;
    }

    public String getExpense_id() {
        return expense_id;
    }

}
