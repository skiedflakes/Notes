package com.wdysolutions.notes.Notes_Pig.Farm_Statistics.Dialog_Table_Details;

public class dialogTable_model {

    String tx_2, tx_3, tx_4, tx_5, tx_6, swine_id;

    public dialogTable_model(String tx_2, String tx_3, String tx_4, String tx_5, String tx_6, String swine_id){
        this.tx_2 = tx_2;
        this.tx_3 = tx_3;
        this.tx_4 = tx_4;
        this.tx_5 = tx_5;
        this.tx_6 = tx_6;
        this.swine_id = swine_id;
    }

    public String getSwine_id() {
        return swine_id;
    }

    public String getTx_2() {
        return tx_2;
    }

    public String getTx_3() {
        return tx_3;
    }

    public String getTx_4() {
        return tx_4;
    }

    public String getTx_5() {
        return tx_5;
    }

    public String getTx_6() {
        return tx_6;
    }
}
