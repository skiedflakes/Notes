/*
 * Copyright (c) 2018. Evren Coşkun
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.wdysolutions.notes.Notes_Pig.Swine_Delivery_Report.tableview.model;

/**
 * Created by evrencoskun on 11/06/2017.
 */

public class Cell {

    private String reference_num, total_weight, total_cost, ave_cost, swine_type, ave_age, price_per_kg,
            adg, gross_total, gross_ave, total_sales, ave_weight, delivery_number;

    public Cell(String reference_num, String total_weight, String total_cost, String ave_cost, String swine_type, String ave_age,
                String price_per_kg, String adg, String gross_total, String gross_ave, String total_sales, String ave_weight,
                String delivery_number){
        this.reference_num = reference_num;
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

    public String getSwine_type() {
        return swine_type;
    }

    public String getAdg() {
        return adg;
    }

    public String getTotal_weight() {
        return total_weight;
    }

    public String getTotal_sales() {
        return total_sales;
    }

    public String getTotal_cost() {
        return total_cost;
    }

    public String getReference_num() {
        return reference_num;
    }

    public String getPrice_per_kg() {
        return price_per_kg;
    }

    public String getGross_total() {
        return gross_total;
    }

    public String getAve_cost() {
        return ave_cost;
    }

    public String getGross_ave() {
        return gross_ave;
    }

    public String getAve_age() {
        return ave_age;
    }

    public String getDelivery_number() {
        return delivery_number;
    }

    public String getAve_weight() {
        return ave_weight;
    }
}

