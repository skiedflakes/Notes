package com.wdysolutions.notes.Notes_Pig.Swine_Population.Dialog_Table;

public class dialogTable_model {

    private String sw_id, sw_type, current_pen, age, sw_code;

    dialogTable_model(String sw_id, String sw_type, String current_pen, String age, String sw_code){
        this.sw_id = sw_id;
        this.sw_type = sw_type;
        this.current_pen = current_pen;
        this.age = age;
        this.sw_code = sw_code;
    }

    public String getSw_code() {
        return sw_code;
    }

    public String getAge() {
        return age;
    }

    public String getCurrent_pen() {
        return current_pen;
    }

    public String getSw_id() {
        return sw_id;
    }

    public String getSw_type() {
        return sw_type;
    }
}
