package com.wdysolutions.notes.Notes_Pig.Swine_Delivery_Report.Swine_Sales_Report;

public class Swine_delivery_sales_model {

    String dr_swine, swine_type, weight, swine_age, swine_weight, dr_total_weight, adg, total_amount_sold, dr_row, gross_profit,swine_id;

    Swine_delivery_sales_model(String dr_swine, String swine_type, String weight, String swine_age, String swine_weight,
                               String dr_total_weight, String adg, String total_amount_sold, String dr_row, String gross_profit,String swine_id){
        this.dr_swine = dr_swine;
        this.swine_type = swine_type;
        this.weight = weight;
        this.swine_age = swine_age;
        this.swine_weight = swine_weight;
        this.dr_total_weight = dr_total_weight;
        this.adg = adg;
        this.total_amount_sold = total_amount_sold;
        this.dr_row = dr_row;
        this.gross_profit = gross_profit;
        this.swine_id = swine_id;
    }

    public String getSwine_id() {
        return swine_id;
    }

    public String getSwine_type() {
        return swine_type;
    }

    public String getAdg() {
        return adg;
    }

    public String getDr_row() {
        return dr_row;
    }

    public String getDr_swine() {
        return dr_swine;
    }

    public String getDr_total_weight() {
        return dr_total_weight;
    }

    public String getGross_profit() {
        return gross_profit;
    }

    public String getSwine_age() {
        return swine_age;
    }

    public String getSwine_weight() {
        return swine_weight;
    }

    public String getTotal_amount_sold() {
        return total_amount_sold;
    }

    public String getWeight() {
        return weight;
    }
}
