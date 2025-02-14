package com.example.capstone.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.capstone.authentication.entities.UserEntity;
import com.example.capstone.entities.SubscriptionEntity;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {
    List<SubscriptionEntity> findByUserAndIsActiveTrue(UserEntity user);
    List<SubscriptionEntity> findByUser(UserEntity user);
}