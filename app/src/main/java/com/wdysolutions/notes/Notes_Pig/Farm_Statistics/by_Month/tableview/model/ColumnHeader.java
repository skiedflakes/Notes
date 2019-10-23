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

package com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Month.tableview.model;

/**
 * Created by evrencoskun on 11/06/2017.
 */

public class ColumnHeader {

    String id, data, data2, data3, week;

    public ColumnHeader(String id, String data,String data2, String data3, String week) {
        this.id = id;
        this.data = data;
        this.data2 = data2;
        this.data3 = data3;
        this.week = week;
    }

    public String getWeek() {
        return week;
    }

    public String getData3() {
        return data3;
    }

    public String getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public String getData2() {
        return data2;
    }
}

