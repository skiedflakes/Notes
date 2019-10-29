package com.wdysolutions.notes.Globals.Micro_Filming;

public class title_model {

    String type, top_ref_num, ref_num, amount;

    public title_model(String type, String ref_num, String top_ref_num, String amount){
        this.type = type;
        this.ref_num = ref_num;
        this.top_ref_num = top_ref_num;
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }

    public String getTop_ref_num() {
        return top_ref_num;
    }

    public String getRef_num() {
        return ref_num;
    }

    public String getType() {
        return type;
    }
}
