package com.wdysolutions.notes.Notes_Pig.Swine_Card.Action.Transfer_Pen;

/**
 * Created by aronandrada on 1/24/19.
 */

public class Locations_model {

    private String branch_id;
    private String branch;

    public Locations_model(String branch_id, String branch){
        this.branch_id = branch_id;
        this.branch = branch;
    }

    public String getBranch() {
        return branch;
    }

    public String getBranch_id() {
        return branch_id;
    }
}
