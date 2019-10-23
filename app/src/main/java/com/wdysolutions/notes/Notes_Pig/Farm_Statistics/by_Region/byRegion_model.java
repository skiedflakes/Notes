package com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Region;

public class byRegion_model {

    private String max_indic, max, min_indic, min, region_colors, region;

    public byRegion_model(String max, String max_indic, String min, String min_indic, String region, String region_colors){
        this.max = max;
        this.max_indic = max_indic;
        this.min = min;
        this.min_indic = min_indic;
        this.region = region;
        this.region_colors = region_colors;
    }

    public String getMax() {
        return max;
    }

    public String getMin() {
        return min;
    }

    public String getMax_indic() {
        return max_indic;
    }

    public String getMin_indic() {
        return min_indic;
    }

    public String getRegion() {
        return region;
    }

    public String getRegion_colors() {
        return region_colors;
    }
}
