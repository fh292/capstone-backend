package com.example.capstone.bo;

import com.example.capstone.entities.CardEntity;

public class BurnerCardResponse extends CardResponse {
    private String durationLimit;

    public BurnerCardResponse(CardEntity card) {
        super(card);
        this.durationLimit = ""; // Default value since getDurationLimit() is not available in CardEntity
    }

    public String getDurationLimit() {
        return durationLimit;
    }

    public void setDurationLimit(String durationLimit) {
        this.durationLimit = durationLimit;
    }
}
