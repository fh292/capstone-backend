package com.example.capstone.bo;

import com.example.capstone.entities.CardEntity;

public class LocationLockedCardResponse extends CardResponse {
    private Double latitude;
    private Double longitude;
    private Double radius;

    public LocationLockedCardResponse(CardEntity card) {
        super(card);
        this.latitude = card.getLatitude();
        this.longitude = card.getLongitude();
        this.radius = card.getRadius();
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getRadius() {
        return radius;
    }
}
