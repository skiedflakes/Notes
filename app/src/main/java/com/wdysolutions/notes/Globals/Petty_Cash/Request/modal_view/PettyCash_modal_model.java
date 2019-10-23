package com.wdysolutions.notes.Globals.Petty_Cash.Request.modal_view;

public class PettyCash_modal_model {

    String id, amount, chart, desc, doc_type, doc_num, doc_date, pcv, chart_id, br_id;

    public PettyCash_modal_model(String id, String amount, String chart, String desc, String doc_type,
                                 String doc_num, String doc_date, String pcv, String chart_id, String br_id){
        this.id = id;
        this.amount = amount;
        this.chart = chart;
        this.chart_id = chart_id;
        this.desc = desc;
        this.doc_type = doc_type;
        this.doc_num = doc_num;
        this.doc_date = doc_date;
        this.pcv = pcv;
        this.br_id = br_id;
    }

    public String getAmount() {
        return amount;
    }

    public String getId() {
        return id;
    }

    public String getBr_id() {
        return br_id;
    }

    public String getPcv() {
        return pcv;
    }

    public String getChart_id() {
        return chart_id;
    }

    public String getChart() {
        return chart;
    }

    public String getDesc() {
        return desc;
    }

    public String getDoc_date() {
        return doc_date;
    }

    public String getDoc_num() {
        return doc_num;
    }

    public String getDoc_type() {
        return doc_type;
    }
}
