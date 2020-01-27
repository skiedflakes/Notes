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

package com.wdysolutions.notes.Notes_Egg.Layer_Reports.brooding_report_tableview_bybatch.holder.model;

/**
 * Created by evrencoskun on 11/06/2017.
 */

public class BB_Cell {

    private String id,data1,data2,data3,data4,data5,data6,data7;
    int num_data;

    public BB_Cell(int num_data, String id, String data1, String data2, String data3, String data4, String data5, String data6,String data7) {
        this.num_data=num_data;
        this.id = id;
        this.data1 = data1;
        this.data2=data2;
        this.data3=data3;
        this.data4=data4;
        this.data5=data5;
        this.data6=data6;
        this.data7=data7;

    }

    public String getId() {
        return id;
    }

    public String getData7() {
        return data7;
    }

    public int getNum_data() {
        return num_data;
    }

    public String getData1() {
        return data1;
    }

    public String getData2() { return data2; }

    public String getData3() { return data3; }

    public String getData4() { return data4; }

    public String getData5() { return data5; }

    public String getData6() {
        return data6;
    }




}

