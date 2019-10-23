package com.wdysolutions.notes.Navigation_Panel;

public class Branch_model {

    String branch_id;
    String branch_name;

    public Branch_model(String branch_id, String branch_name){
        this.branch_id = branch_id;
        this.branch_name = branch_name;
    }

    public String getBranch_id() {
        return branch_id;
    }

    public String getBranch_name() {
        return branch_name;
    }
}
