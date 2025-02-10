package com.example.capstone.authentication.entities;
import com.example.capstone.entities.CardEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String civilId;

    @Column(nullable = false)
    private String phoneNumber;

    @Column
    private String bankAccountUsername;

    @Column
    private String subscription;

    @Column(unique = true)
    private String bankAccountNumber;

    @Column
    private String gender;

    @Column
    private LocalDate dateOfBirth;

    @Column
    private String profilePic;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column
    private Boolean isActive;

    @Column
    private Long cardId;

    @Column(nullable = false)
    private Double monthlySpendLimit = 5000.0;

    @Column(nullable = false)
    private Double dailySpendLimit = 500.0;

    @Column(nullable = false)
    private Integer monthlyCardIssuanceLimit = 10;

    @Column(nullable = false)
    private Double currentMonthlySpend = 0.0;

    @Column(nullable = false)
    private Double currentDailySpend = 0.0;

    @Column(nullable = false)
    private Integer currentMonthCardIssuance = 0;

    @Column(nullable = false)
    private LocalDateTime lastSpendReset;

    @OneToMany(mappedBy = "user", orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "user" })
    private List<CardEntity> cards;

    @Column
    private String role;

    @Column
    private String permission;

    @Column
    private String department;

    public UserEntity() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setActive(Boolean active) {
        isActive = active;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean IsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public List<CardEntity> getCards() {
        return cards;
    }

    public void setCards(List<CardEntity> cards) {
        this.cards = cards;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
