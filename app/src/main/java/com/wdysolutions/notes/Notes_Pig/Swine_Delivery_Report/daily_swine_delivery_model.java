package com.wdysolutions.notes.Notes_Pig.Swine_Delivery_Report;

public class daily_swine_delivery_model {

    private String date, customer, reference_num, heads, total_weight, total_cost, ave_cost, swine_type, ave_age, price_per_kg,
    adg, gross_total, gross_ave, total_sales, ave_weight, delivery_number;

    public daily_swine_delivery_model(String date, String customer, String reference_num, String heads, String total_weight, String total_cost,
                                      String ave_cost, String swine_type, String ave_age, String price_per_kg, String adg, String gross_total, String gross_ave,
                                      String total_sales, String ave_weight, String delivery_number){
        this.date = date;
        this.customer = customer;
        this.reference_num = reference_num;
        this.heads = heads;
        this.total_weight = total_weight;
        this.total_cost = total_cost;
        this.ave_cost = ave_cost;
        this.swine_type = swine_type;
        this.ave_age = ave_age;
        this.price_per_kg = price_per_kg;
        this.adg = adg;
        this.gross_total = gross_total;
        this.gross_ave = gross_ave;
        this.total_sales = total_sales;
        this.ave_weight = ave_weight;
        this.delivery_number = delivery_number;
    }

    public String getDelivery_number() {
        return delivery_number;
    }

    public String getAve_age() {
        return ave_age;
    }

    public String getAdg() {
        return adg;
    }

    public String getAve_cost() {
        return ave_cost;
    }

    public String getCustomer() {
        return customer;
    }

    public String getDate() {
        return date;
    }

    public String getGross_ave() {
        return gross_ave;
    }

    public String getGross_total() {
        return gross_total;
    }

    public String getHeads() {
        return heads;
    }

    public String getPrice_per_kg() {
        return price_per_kg;
    }

    public String getReference_num() {
        return reference_num;
    }

    public String getSwine_type() {
        return swine_type;
    }

    public String getTotal_cost() {
        return total_cost;
    }

    public String getTotal_sales() {
        return total_sales;
    }

    public String getAve_weight() {
        return ave_weight;
    }

    public String getTotal_weight() {
        return total_weight;
    }
}
