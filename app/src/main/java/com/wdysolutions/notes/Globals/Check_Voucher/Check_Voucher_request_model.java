package com.wdysolutions.notes.Globals.Check_Voucher;

public class Check_Voucher_request_model {
String id;
String cv_number;
String count;
String bank;
String check_number;
String account;
String issued_date;
String check_date;
String remarks;
String amount;
String status;
String stat;
String encoded_by;
String cv_type;
String br;
String approved_by;

public Check_Voucher_request_model(
        String id,String cv_number,String count,
        String bank,String check_number,
        String account,String issued_date,String check_date,
        String remarks,String amount,String status,
        String stat,String encoded_by,String cv_type,
        String br,String approved_by){

    this.id=id;
    this.cv_number = cv_number;
    this.count = count;
    this.bank = bank;
    this.check_number = check_number;
    this.account=account;
    this.issued_date=issued_date;
    this.check_date=check_date;
    this.remarks = remarks;
    this.amount = amount;
    this.status=status;
    this.stat=stat;
    this.encoded_by=encoded_by;
    this.cv_type=cv_type;
    this.br=br;
    this.approved_by=approved_by;
}

    public String getApproved_by() {
        return approved_by;
    }

    public String getAccount() {
        return account;
    }

    public String getBank() {
        return bank;
    }

    public String getCheck_date() {
        return check_date;
    }

    public String getCheck_number() {
        return check_number;
    }

    public String getAmount() {
        return amount;
    }

    public String getCount() {
        return count;
    }

    public String getCv_number() {
        return cv_number;
    }

    public String getId() {
        return id;
    }

    public String getIssued_date() {
        return issued_date;
    }

    public String getBr() {
        return br;
    }

    public String getCv_type() {
        return cv_type;
    }

    public String getEncoded_by() {
        return encoded_by;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getStat() {
        return stat;
    }

    public String getStatus() {
        return status;
    }

}
