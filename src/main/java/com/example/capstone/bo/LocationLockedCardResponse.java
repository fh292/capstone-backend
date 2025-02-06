package com.example.capstone.bo;

import com.example.capstone.entities.CardEntity;

public class LocationLockedCardResponse extends CardResponse{
    private Double latitude;
    private Double longitude;
    private Double radius;

    public LocationLockedCardResponse() {
        super();
    }

    public LocationLockedCardResponse(CardEntity card) {
        super(card);
        this.setId(card.getId());
        this.setCardName(card.getCardName());
        this.setCardType(card.getCardType());
        this.setBankAccountNumber(card.getBankAccountNumber());
        this.setSpendingLimit(card.getSpendingLimit());
        this.setRemainingLimit(card.getRemainingLimit());
        this.setExpiryDate(card.getExpiryDate());
        this.setCardColor(card.getCardColor());
        this.setCardIcon(card.getCardIcon());
        this.setPer_transaction(card.getPer_transaction());
        this.setPer_day(card.getPer_day());
        this.setPer_week(card.getPer_week());
        this.setPer_month(card.getPer_month());
        this.setPer_year(card.getPer_year());
        this.setTotal(card.getTotal());
        this.latitude = card.getLatitude();
        this.longitude = card.getLongitude();
        this.radius = card.getRadius();
        this.paused = card.getPaused();
        this.closed = card.getClosed();

    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }
}
