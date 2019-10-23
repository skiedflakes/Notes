package com.wdysolutions.notes.Navigation_Panel;

public class Program_model {
     String Pig,Feeds,Broiler,Egg;
    public Program_model(String Pig,String Feeds,String Broiler,String Egg){
        this.Pig = Pig;
        this.Feeds = Feeds;
        this.Broiler = Broiler;
        this.Egg = Egg;
    }

    public String getBroiler() {
        return Broiler;
    }

    public String getEgg() {
        return Egg;
    }

    public String getFeeds() {
        return Feeds;
    }

    public String getPig() {
        return Pig;
    }
}
