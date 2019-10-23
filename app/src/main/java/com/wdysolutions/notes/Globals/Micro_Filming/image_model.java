package com.wdysolutions.notes.Globals.Micro_Filming;

import java.io.Serializable;

public class image_model implements Serializable {

    String type, ref_num, value, url_img;

    public image_model(String type, String ref_num, String value, String url_img){
        this.type = type;
        this.ref_num = ref_num;
        this.value = value;
        this.url_img = url_img;
    }

    public String getType() {
        return type;
    }

    public String getRef_num() {
        return ref_num;
    }

    public String getUrl_img() {
        return url_img;
    }

    public String getValue() {
        return value;
    }
}
