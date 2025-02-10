package com.example.capstone.bo;

public class CardLimitUpdateRequest {
    private String limitType; // "PER_TRANSACTION", "PER_DAY", "PER_WEEK", "PER_MONTH", "PER_YEAR", "TOTAL"
    private Double amount;
    private Boolean removeLimit; // If true, removes the limit by setting it to null

    public String getLimitType() {
        return limitType;
    }

    public void setLimitType(String limitType) {
        this.limitType = limitType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Boolean getRemoveLimit() {
        return removeLimit != null && removeLimit;
    }

    public void setRemoveLimit(Boolean removeLimit) {
        this.removeLimit = removeLimit;
    }
}