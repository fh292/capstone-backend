package com.example.capstone.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.capstone.authentication.entities.UserEntity;
import com.example.capstone.entities.NotificationEntity;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    Page<NotificationEntity> findByUserOrderByCreatedAtDesc(UserEntity user, Pageable pageable);
    long countByUserAndReadFalse(UserEntity user);
}