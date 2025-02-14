package com.example.capstone.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.capstone.authentication.entities.UserEntity;
import com.example.capstone.entities.CardEntity;
import com.example.capstone.entities.TransactionEntity;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> findByUserId(Long userId);
    List<TransactionEntity> findByUser(UserEntity user);
    List<TransactionEntity> findByCard(CardEntity card);
    Page<TransactionEntity> findAll(Pageable pageable);

    // we can add custom queries here:
    // List<TransactionEntity> findByStatus(String status);
    // List<TransactionEntity> findByUserId(Long userId);
}
