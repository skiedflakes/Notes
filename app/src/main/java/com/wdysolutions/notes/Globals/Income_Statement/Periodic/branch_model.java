package com.wdysolutions.notes.Globals.Income_Statement.Periodic;

public class branch_model {

    private String branch, branch_id;

    public branch_model(String branch, String branch_id){
        this.branch = branch;
        this.branch_id = branch_id;
    }

    public String getBranch() {
        return branch;
    }

    public String getBranch_id() {
        return branch_id;
    }
}
