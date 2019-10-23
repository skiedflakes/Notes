package com.wdysolutions.notes.Globals.Requisition.Requisition_Add;

public class Product_model {

    private String product;
    private String product_id;

    public Product_model(String product_id, String product){
        this.product_id = product_id;
        this.product = product;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getProduct() {
        return product;
    }
}
