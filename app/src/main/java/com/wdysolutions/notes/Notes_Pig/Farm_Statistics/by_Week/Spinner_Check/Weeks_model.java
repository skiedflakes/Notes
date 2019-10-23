package com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Week.Spinner_Check;

public class Weeks_model {

    private String id, name, check_status;

    public Weeks_model(String id, String name, String check_status){
        this.id = id;
        this.name = name;
        this.check_status = check_status;
    }

    public String getCheck_status() {
        return check_status;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
