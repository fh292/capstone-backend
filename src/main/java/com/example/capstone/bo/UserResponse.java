package com.example.capstone.bo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.example.capstone.authentication.entities.UserEntity;
import com.example.capstone.entities.CardEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String civilId;
    private String phoneNumber;
    private String bankAccountUsername;
    private String subscription;
    private String bankAccountNumber;
    @JsonIgnore
    private List<CardEntity> cards;
    private String gender;
    private LocalDate dateOfBirth;
    private String profilePic;
    private String role = "ROLE_USER";
    private Boolean isActive;
    private LocalDateTime createdAt;
    private Double monthlySpendLimit;
    private Double dailySpendLimit;
    private Integer monthlyCardIssuanceLimit;
    private Double currentMonthlySpend;
    private Double currentDailySpend;
    private Integer currentMonthCardIssuance;
    private LocalDateTime lastSpendReset;
    private Integer activeCardsCount;

    public UserResponse() {}

    public UserResponse(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.firstName = userEntity.getFirstName();
        this.lastName = userEntity.getLastName();
        this.email = userEntity.getEmail();
        this.civilId = userEntity.getCivilId();
        this.phoneNumber = userEntity.getPhoneNumber();
        this.bankAccountUsername = userEntity.getBankAccountUsername();
        this.subscription = userEntity.getSubscription();
        this.bankAccountNumber = userEntity.getBankAccountNumber();
        this.cards = userEntity.getCards();
        this.gender = userEntity.getGender();
        this.dateOfBirth = userEntity.getDateOfBirth();
        this.profilePic = userEntity.getProfilePic();
        this.role = userEntity.getRole();
        this.isActive = userEntity.IsActive();
        this.createdAt = userEntity.getCreatedAt();
        this.monthlySpendLimit = userEntity.getMonthlySpendLimit();
        this.dailySpendLimit = userEntity.getDailySpendLimit();
        this.monthlyCardIssuanceLimit = userEntity.getMonthlyCardIssuanceLimit();
        this.currentMonthlySpend = userEntity.getCurrentMonthlySpend();
        this.currentDailySpend = userEntity.getCurrentDailySpend();
        this.currentMonthCardIssuance = userEntity.getCurrentMonthCardIssuance();
        this.lastSpendReset = userEntity.getLastSpendReset();

        // Calculate active cards count
        this.activeCardsCount = userEntity.getCards() == null ? 0 :
            (int) userEntity.getCards().stream()
                .filter(card -> !card.getPaused() && !card.getClosed())
                .count();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCivilId() { return civilId; }
    public void setCivilId(String civilId) { this.civilId = civilId; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getBankAccountUsername() { return bankAccountUsername; }
    public void setBankAccountUsername(String bankAccountUsername) { this.bankAccountUsername = bankAccountUsername; }

    public String getSubscription() { return subscription; }
    public void setSubscription(String subscription) { this.subscription = subscription; }

    public String getBankAccountNumber() { return bankAccountNumber; }
    public void setBankAccountNumber(String bankAccountNumber) { this.bankAccountNumber = bankAccountNumber; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getProfilePic() { return profilePic; }
    public void setProfilePic(String profilePic) { this.profilePic = profilePic; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public List<CardEntity> getCards() {
        return cards;
    }

    public void setCards(List<CardEntity> cards) {
        this.cards = cards;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
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

    public Double getCurrentMonthlySpend() {
        return currentMonthlySpend;
    }

    public void setCurrentMonthlySpend(Double currentMonthlySpend) {
        this.currentMonthlySpend = currentMonthlySpend;
    }

    public Double getCurrentDailySpend() {
        return currentDailySpend;
    }

    public void setCurrentDailySpend(Double currentDailySpend) {
        this.currentDailySpend = currentDailySpend;
    }

    public Integer getCurrentMonthCardIssuance() {
        return currentMonthCardIssuance;
    }

    public void setCurrentMonthCardIssuance(Integer currentMonthCardIssuance) {
        this.currentMonthCardIssuance = currentMonthCardIssuance;
    }

    public LocalDateTime getLastSpendReset() {
        return lastSpendReset;
    }

    public void setLastSpendReset(LocalDateTime lastSpendReset) {
        this.lastSpendReset = lastSpendReset;
    }

    public Integer getActiveCardsCount() {
        return activeCardsCount;
    }

    public void setActiveCardsCount(Integer activeCardsCount) {
        this.activeCardsCount = activeCardsCount;
    }
}
