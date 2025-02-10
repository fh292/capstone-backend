package com.example.capstone.bo;

import java.time.LocalDate;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CardRequest {
    @Size(min = 1, max = 50)
    private String cardName;
    private String cardType;
    private String bankAccountNumber;
    private Double spendingLimit;
    private LocalDate expiryDate;
    @Pattern(regexp = "^#([A-Fa-f0-9]{6})$")
    private String cardColor;
    @Pattern(regexp = "^[\\p{Emoji}]$")
    private String cardIcon;
    private Boolean isShared;
    private Double per_transaction;
    private Double per_day;
    private Double per_week;
    private Double per_month;
    private Double per_year;
    private Double total;

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public Double getSpendingLimit() {
        return spendingLimit;
    }

    public void setSpendingLimit(Double spendingLimit) {
        this.spendingLimit = spendingLimit;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCardColor() {
        return cardColor;
    }

    public void setCardColor(String cardColor) {
        this.cardColor = cardColor;
    }

    public String getCardIcon() {
        return cardIcon;
    }

    public void setCardIcon(String cardIcon) {
        this.cardIcon = cardIcon;
    }

    public Boolean getIsShared() {
        return isShared;
    }

    public void setIsShared(Boolean shared) {
        isShared = shared;
    }

    public Boolean getShared() {
        return isShared;
    }

    public void setShared(Boolean shared) {
        isShared = shared;
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
