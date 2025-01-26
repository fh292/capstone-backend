package com.example.capstone.authentication.bo;

import java.time.LocalDate;
import java.util.Date;

public class RegisterUserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String civilId;
    private String phoneNumber;
    private String bankAccountUsername;
    private String subscription;
    private String bankAccountNumber;
    private String card;
    private String gender;
    private LocalDate dateOfBirth;
    private String profilePic;
    private String role = "ROLE_USER";

    public RegisterUserRequest(String firstName, String lastName, String email, String password, String civilId, String phoneNumber, String bankAccountUsername, String subscription, String bankAccountNumber, String card, String gender, LocalDate dateOfBirth, String profilePic, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.civilId = civilId;
        this.phoneNumber = phoneNumber;
        this.bankAccountUsername = bankAccountUsername;
        this.subscription = subscription;
        this.bankAccountNumber = bankAccountNumber;
        this.card = card;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.profilePic = profilePic;
        this.role = role;
    }

    public RegisterUserRequest() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCivilId() {
        return civilId;
    }

    public void setCivilId(String civilId) {
        this.civilId = civilId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBankAccountUsername() {
        return bankAccountUsername;
    }

    public void setBankAccountUsername(String bankAccountUsername) {
        this.bankAccountUsername = bankAccountUsername;
    }

    public String getSubscription() {
        return subscription;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
