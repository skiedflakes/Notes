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

package com.wdysolutions.notes.Notes_Egg.Layer_Reports.brooding_report_tableview.model;

import java.util.List;

/**
 * Created by evrencoskun on 11/06/2017.
 */

public class BR_Cell {

    private String id, data1,data2,data3;


    public BR_Cell(String id, String data1) {
        this.id = id;
        this.data1 = data1;

    }

    public String getData1() {
        return data1;
    }


}

