package com.wdysolutions.notes.Notes_Egg.Layer_Reports;

public class all_batch_data_model {
    private String breed, running_population,initial_population;

    public all_batch_data_model(String breed, String running_population,String initial_population){
       this.breed = breed;
       this.running_population=running_population;
       this.initial_population=initial_population;
    }

    public String getRunning_population() {
        return running_population;
    }

    public String getInitial_population() {
        return initial_population;
    }

    public String getBreed() {
        return breed;
    }
}
