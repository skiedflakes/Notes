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

package com.wdysolutions.notes.Notes_Pig.Farm_Statistics.by_Week.tableview.model;

/**
 * Created by evrencoskun on 11/06/2017.
 */

public class Cell {

    private String mId, current, percent, ave, percent_underline, percent_bg, current_underline, current_bg, ave_bg, ave_underline;


    public Cell(String id, String current, String percent, String ave, String percent_underline, String percent_bg,
                String current_underline, String current_bg, String ave_bg, String ave_underline) {
        this.mId = id;
        this.current = current;
        this.percent = percent;
        this.ave = ave;
        this.percent_bg = percent_bg;
        this.percent_underline = percent_underline;
        this.current_bg = current_bg;
        this.current_underline = current_underline;
        this.ave_bg = ave_bg;
        this.ave_underline = ave_underline;
    }

    public String getAve_bg() {
        return ave_bg;
    }

    public String getAve_underline() {
        return ave_underline;
    }

    public String getCurrent_bg() {
        return current_bg;
    }

    public String getCurrent_underline() {
        return current_underline;
    }

    public String getPercent_bg() {
        return percent_bg;
    }

    public String getPercent_underline() {
        return percent_underline;
    }

    public String getmId() {
        return mId;
    }

    public String getAve() {
        return ave;
    }

    public String getCurrent() {
        return current;
    }

    public String getPercent() {
        return percent;
    }
}

