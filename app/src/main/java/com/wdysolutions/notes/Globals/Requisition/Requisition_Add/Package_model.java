package com.wdysolutions.notes.Globals.Requisition.Requisition_Add;

public class Package_model {

    private String package_id;
    private String package_name;

    public Package_model(String package_id, String package_name){
        this.package_id = package_id;
        this.package_name = package_name;
    }

    public String getPackage_id() {
        return package_id;
    }

    public String getPackage_name() {
        return package_name;
    }
}
