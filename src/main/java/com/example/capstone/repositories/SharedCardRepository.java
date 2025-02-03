package com.example.capstone.repositories;

import com.example.capstone.entities.SharedCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SharedCardRepository extends JpaRepository <SharedCardEntity, Long> {
    List<SharedCardEntity> findByCardId(Long cardId);
    List<SharedCardEntity> findByUserId(Long userId);
    Optional<SharedCardEntity> findByCardIdAndUserId(Long cardId, Long userId);
}
