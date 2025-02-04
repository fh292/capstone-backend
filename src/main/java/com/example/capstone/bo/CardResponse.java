package com.example.capstone.bo;

import java.time.LocalDate;

public class CardResponse {

    private Long id;
    private String cardName;
    private String cardType;
    private String bankAccountNumber;
    private Double spendingLimit;
    private Double remainingLimit;
    private LocalDate expiryDate;
    private Boolean isPaused;
    private Boolean isClosed;
    private String cardColor;
    private String cardIcon;
    private Double per_transaction;
    private Double per_day;
    private Double per_week;
    private Double per_month;
    private Double per_year;
    private Double total;
    protected Boolean paused;
    protected Boolean closed;

    public CardResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Double getRemainingLimit() {
        return remainingLimit;
    }

    public void setRemainingLimit(Double remainingLimit) {
        this.remainingLimit = remainingLimit;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Boolean getPaused() {
        return isPaused;
    }

    public void setPaused(Boolean paused) {
        isPaused = paused;
    }

    public Boolean getClosed() {
        return isClosed;
    }

    public void setClosed(Boolean closed) {
        isClosed = closed;
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
