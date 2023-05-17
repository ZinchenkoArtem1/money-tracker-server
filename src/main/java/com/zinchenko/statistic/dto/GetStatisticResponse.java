package com.zinchenko.statistic.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GetStatisticResponse {

    @JsonProperty("income_statistic")
    private List<StatisticDto> incomeStatistic;

    @JsonProperty("expense_statistic")
    private List<StatisticDto> expenseStatistic;

    public List<StatisticDto> getIncomeStatistic() {
        return incomeStatistic;
    }

    public GetStatisticResponse setIncomeStatistic(List<StatisticDto> incomeStatistic) {
        this.incomeStatistic = incomeStatistic;
        return this;
    }

    public List<StatisticDto> getExpenseStatistic() {
        return expenseStatistic;
    }

    public GetStatisticResponse setExpenseStatistic(List<StatisticDto> expenseStatistic) {
        this.expenseStatistic = expenseStatistic;
        return this;
    }
}
