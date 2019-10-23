package com.wdysolutions.notes.Globals.Income_Statement.Periodic;

public class periodic_model {

    private String name;
    private String sub_total;

    public periodic_model(String name, String sub_total){
        this.name = name;
        this.sub_total = sub_total;
    }

    public String getName() {
        return name;
    }

    public String getSub_total() {
        return sub_total;
    }
}
