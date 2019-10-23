package com.wdysolutions.notes.Globals.Price_Watch;

public class Price_watch_model {
    String region_color, region, lowest_price,lowest_price_date,highest_price,highest_price_date,lowest_arrow,highest_arrow;

    public Price_watch_model(String region_color,String region,String lowest_price,String lowest_price_date,String highest_price,String highest_price_date,String lowest_arrow,String highest_arrow){
        this.region = region;
        this.lowest_price = lowest_price;
        this.lowest_price_date = lowest_price_date;
        this.highest_price = highest_price;
        this.highest_price_date = highest_price_date;
        this.highest_arrow = highest_arrow;
        this.lowest_arrow = lowest_arrow;
        this.region_color = region_color;
    }

    public String getRegion_color() {
        return region_color;
    }

    public String getHighest_arrow() {
        return highest_arrow;
    }

    public String getLowest_arrow() {
        return lowest_arrow;
    }

    public String getHighest_price() {
        return highest_price;
    }

    public String getHighest_price_date() {
        return highest_price_date;
    }

    public String getLowest_price() {
        return lowest_price;
    }

    public String getLowest_price_date() {
        return lowest_price_date;
    }

    public String getRegion() {
        return region;
    }

}
