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

public class RowHeader_byMonth {

    private String id, current, ave, percent, name,
            dropdown_current_graph, dropdown_ave_graph, dropdown_perc_graph,
            farm_statistics_curr, farm_statistics_ave, farm_statistics_perc, farm_statistics_unique;
    private int counter_clickable;

    public RowHeader_byMonth(String id, String current, String ave, String percent, String name,
                             String dropdown_perc_graph, String dropdown_ave_graph, String dropdown_current_graph,
                             String farm_statistics_curr, String farm_statistics_ave, String farm_statistics_perc,
                             String farm_statistics_unique, int counter_clickable) {
        this.id = id;
        this.current = current;
        this.ave = ave;
        this.percent = percent;
        this.name = name;
        this.farm_statistics_curr = farm_statistics_curr;
        this.farm_statistics_ave = farm_statistics_ave;
        this.farm_statistics_perc = farm_statistics_perc;
        this.dropdown_ave_graph = dropdown_ave_graph;
        this.dropdown_current_graph = dropdown_current_graph;
        this.dropdown_perc_graph = dropdown_perc_graph;
        this.farm_statistics_unique = farm_statistics_unique;
        this.counter_clickable = counter_clickable;
    }

    public int getCounter_clickable() {
        return counter_clickable;
    }

    public String getFarm_statistics_unique() {
        return farm_statistics_unique;
    }

    public String getFarm_statistics_perc() {
        return farm_statistics_perc;
    }

    public String getFarm_statistics_ave() {
        return farm_statistics_ave;
    }

    public String getDropdown_ave_graph() {
        return dropdown_ave_graph;
    }

    public String getDropdown_current_graph() {
        return dropdown_current_graph;
    }

    public String getDropdown_perc_graph() {
        return dropdown_perc_graph;
    }

    public String getFarm_statistics_curr() {
        return farm_statistics_curr;
    }

    public String getName() {
        return name;
    }

    public String getPercent() {
        return percent;
    }

    public String getCurrent() {
        return current;
    }

    public String getAve() {
        return ave;
    }

    public String getId() {
        return id;
    }
}
