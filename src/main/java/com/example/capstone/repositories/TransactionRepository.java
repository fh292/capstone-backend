package com.example.capstone.repositories;

import com.example.capstone.entities.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> findByUserId(Long userId);
    // we can add custom queries here:
    // List<TransactionEntity> findByStatus(String status);
    // List<TransactionEntity> findByUserId(Long userId);
}
