package com.wdysolutions.notes.Notes_Pig.Parity_Report;

import org.json.JSONArray;

public class Parity_report_model {

    private String branch, company_name;
    private JSONArray jsonArray;

    public Parity_report_model(String branch, String company_name, JSONArray jsonArray){
        this.branch = branch;
        this.company_name = company_name;
        this.jsonArray = jsonArray;
    }

    public String getCompany_name() {
        return company_name;
    }

    public String getBranch() {
        return branch;
    }

    public JSONArray getJsonArray() {
        return jsonArray;
    }
}
