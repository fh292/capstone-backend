package com.example.capstone.bo;

import com.example.capstone.entities.CardEntity;

public class CategoryLockedCardResponse extends CardResponse {
    private String categoryName;

    public CategoryLockedCardResponse() {
        super();
    }

    public CategoryLockedCardResponse(CardEntity card) {
        super(card);
        this.categoryName = card.getCategoryName();
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
