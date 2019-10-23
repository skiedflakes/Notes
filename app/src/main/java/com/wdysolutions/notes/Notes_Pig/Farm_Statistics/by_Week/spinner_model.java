package com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Week;

public class spinner_model {

    private String name;
    private String id;

    spinner_model(String name, String id){
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
