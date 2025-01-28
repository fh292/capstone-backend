package com.example.capstone.entities;

import com.example.capstone.authentication.entities.UserEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class SharedCardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime sharedAt;
    private LocalDateTime expiresAt;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private CardEntity card;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getSharedAt() {
        return sharedAt;
    }

    public void setSharedAt(LocalDateTime sharedAt) {
        this.sharedAt = sharedAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public CardEntity getCard() {return card;}

    public void setCard(CardEntity card) {this.card = card;}

    public UserEntity getUser() {return user;}

    public void setUser(UserEntity user) {this.user = user;}
}
