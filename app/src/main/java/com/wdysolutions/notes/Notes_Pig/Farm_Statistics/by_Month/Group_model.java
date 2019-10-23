package com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Month;

public class Group_model {
    String group;
    String group_value;

    public Group_model(String group,String group_value){
        this.group = group;
        this.group_value = group_value;
    }

    public String getGroup() {
        return group;
    }

    public String getGroup_value() {
        return group_value;
    }
}
