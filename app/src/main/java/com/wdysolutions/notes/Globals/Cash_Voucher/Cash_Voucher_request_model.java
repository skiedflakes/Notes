package com.wdysolutions.notes.Globals.Cash_Voucher;

public class Cash_Voucher_request_model {
    String id;
    String count;
    String cv_number;
    String account;
    String date;
    String amount;
    String status;
    String encoded_by;
    String approved_by;

    public Cash_Voucher_request_model(String id, String count, String cv_number,String account,String date,String ammount,
                                      String encoded_by,String status,String approved_by){
        this.id=id;
        this.count=count;
        this.cv_number=cv_number;
        this.account=account;
        this.date=date;
        this.amount=ammount;
        this.encoded_by=encoded_by;
        this.status=status;
        this.approved_by=approved_by;

    }

    public String getEncoded_by() {
        return encoded_by;
    }

    public String getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public String getCv_number() {
        return cv_number;
    }

    public String getCount() {
        return count;
    }

    public String getAmount() {
        return amount;
    }


    public String getAccount() {
        return account;
    }

    public String getApproved_by() {
        return approved_by;
    }

    public String getDate() {
        return date;
    }

}
