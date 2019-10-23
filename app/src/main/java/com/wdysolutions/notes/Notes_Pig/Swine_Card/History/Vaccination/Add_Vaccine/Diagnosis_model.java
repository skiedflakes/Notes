package com.wdysolutions.notes.Notes_Pig.Swine_Card.History.Vaccination.Add_Vaccine;

/**
 * Created by aronandrada on 12/7/18.
 */

public class Diagnosis_model {

    private int id;
    private String name;

    public Diagnosis_model(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
