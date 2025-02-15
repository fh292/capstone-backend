package com.example.capstone.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class PlanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Double monthlySpendLimit;

    @Column(nullable = false)
    private Double dailySpendLimit;

    @Column(nullable = false)
    private Integer monthlyCardIssuanceLimit;

    @Column(nullable = false)
    private Boolean hasLocationLocking;

    @Column(nullable = false)
    private Boolean hasCategoryLocking;

    @Column(nullable = false)
    private Boolean hasMerchantLocking;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getMonthlySpendLimit() {
        return monthlySpendLimit;
    }

    public void setMonthlySpendLimit(Double monthlySpendLimit) {
        this.monthlySpendLimit = monthlySpendLimit;
    }

    public Double getDailySpendLimit() {
        return dailySpendLimit;
    }

    public void setDailySpendLimit(Double dailySpendLimit) {
        this.dailySpendLimit = dailySpendLimit;
    }

    public Integer getMonthlyCardIssuanceLimit() {
        return monthlyCardIssuanceLimit;
    }

    public void setMonthlyCardIssuanceLimit(Integer monthlyCardIssuanceLimit) {
        this.monthlyCardIssuanceLimit = monthlyCardIssuanceLimit;
    }

    public Boolean getHasLocationLocking() {
        return hasLocationLocking;
    }

    public void setHasLocationLocking(Boolean hasLocationLocking) {
        this.hasLocationLocking = hasLocationLocking;
    }

    public Boolean getHasCategoryLocking() {
        return hasCategoryLocking;
    }

    public void setHasCategoryLocking(Boolean hasCategoryLocking) {
        this.hasCategoryLocking = hasCategoryLocking;
    }

    public Boolean getHasMerchantLocking() {
        return hasMerchantLocking;
    }

    public void setHasMerchantLocking(Boolean hasMerchantLocking) {
        this.hasMerchantLocking = hasMerchantLocking;
    }
}