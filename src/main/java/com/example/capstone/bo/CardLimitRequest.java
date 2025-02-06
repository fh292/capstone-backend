package com.example.capstone.bo;

public class CardLimitRequest {

    private Double spendingLimit;
    private Double perTransaction;
    private Double per_transaction;
    private Double per_day;
    private Double per_week;
    private Double per_month;
    private Double per_year;
    private Double total;

    public Double getSpendingLimit() {
        return spendingLimit;
    }

    public void setSpendingLimit(Double spendingLimit) {
        this.spendingLimit = spendingLimit;
    }

    public Double getPerTransaction() {
        return perTransaction;
    }

    public void setPerTransaction(Double perTransaction) {
        this.perTransaction = perTransaction;
    }

    public Double getPer_transaction() {
        return per_transaction;
    }

    public void setPer_transaction(Double per_transaction) {
        this.per_transaction = per_transaction;
    }

    public Double getPer_day() {
        return per_day;
    }

    public void setPer_day(Double per_day) {
        this.per_day = per_day;
    }

    public Double getPer_week() {
        return per_week;
    }

    public void setPer_week(Double per_week) {
        this.per_week = per_week;
    }

    public Double getPer_month() {
        return per_month;
    }

    public void setPer_month(Double per_month) {
        this.per_month = per_month;
    }

    public Double getPer_year() {
        return per_year;
    }

    public void setPer_year(Double per_year) {
        this.per_year = per_year;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
