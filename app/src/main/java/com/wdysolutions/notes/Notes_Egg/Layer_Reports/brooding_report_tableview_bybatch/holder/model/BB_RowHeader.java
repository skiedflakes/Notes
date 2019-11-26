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

public class BB_RowHeader {

    private String id, data1,data2,data3;

    public BB_RowHeader(String id, String data1,String data2,String data3) {
        this.id = id;
        this.data1 = data1;
        this.data2 = data2;
        this.data3 = data3;
    }

    public String getId() {
        return id;
    }

    public String getData1() {
        return data1;
    }

    public String getData3() {
        return data3;
    }

    public String getData2() {
        return data2;
    }
}
