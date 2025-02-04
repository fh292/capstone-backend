package com.example.capstone.bo;

import com.example.capstone.authentication.entities.UserEntity;
import com.example.capstone.entities.CardEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

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
    private String cardId;
    private List<CardEntity> cards;
    private String gender;
    private LocalDate dateOfBirth;
    private String profilePic;
    private String role = "ROLE_USER";

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
        this.cardId = Objects.toString(userEntity.getCardId(), null);
        this.cards = userEntity.getCards();
        this.gender = userEntity.getGender();
        this.dateOfBirth = userEntity.getDateOfBirth();
        this.profilePic = userEntity.getProfilePic();
        this.role = userEntity.getRole();
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

    public String getCardId() { return cardId; }
    public void setCardId(String cardId) { this.cardId = cardId; }

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
}
