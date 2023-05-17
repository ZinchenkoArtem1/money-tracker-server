package com.zinchenko.statistic.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatisticDto {

    @JsonProperty("category")
    private String category;

    @JsonProperty("data")
    private Double data;

    public StatisticDto() {
    }

    public StatisticDto(String category, Double data) {
        this.category = category;
        this.data = data;
    }

    public String getCategory() {
        return category;
    }

    public StatisticDto setCategory(String category) {
        this.category = category;
        return this;
    }

    public Double getData() {
        return data;
    }

    public StatisticDto setData(Double data) {
        this.data = data;
        return this;
    }
}
