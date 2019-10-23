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

package com.wdysolutions.notes.Notes_Pig.Swine_Population.byClass.tableview.model;

/**
 * Created by evrencoskun on 11/06/2017.
 */

public class Cell {

    private String mId, cell_data, percent;

    public Cell(String id, String cell_data, String percent) {
        this.mId = id;
        this.cell_data = cell_data;
        this.percent = percent;
    }

    public String getPercent() {
        return percent;
    }

    public String getmId() {
        return mId;
    }

    public String getCell_data() {
        return cell_data;
    }
}

