package com.example.capstone.repositories;

import com.example.capstone.entities.SharedCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SharedCardRepository extends JpaRepository <SharedCardEntity, Long> {
}
