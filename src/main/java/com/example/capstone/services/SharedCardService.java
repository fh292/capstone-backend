package com.example.capstone.services;

import com.example.capstone.authentication.repositories.UserRepository;
import com.example.capstone.bo.SharedCardRequest;
import com.example.capstone.entities.SharedCardEntity;
import com.example.capstone.repositories.CardRepository;
import com.example.capstone.repositories.SharedCardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SharedCardService {
    private final SharedCardRepository sharedCardRepository;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    public SharedCardService(SharedCardRepository sharedCardRepository, CardRepository cardRepository, UserRepository userRepository) {
        this.sharedCardRepository = sharedCardRepository;
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
    }
    // Get all shared cards for a given user
    public List<SharedCardEntity> getSharedCardsForUser(Long userId) {
        List<SharedCardEntity> sharedCards = sharedCardRepository.findByUserId(userId);
        return sharedCards.stream().map(sharedCard -> {
            sharedCard.setCard(cardRepository.findById(sharedCard.getCard().getId()).orElse(null));
            sharedCard.setUser(userRepository.findById(sharedCard.getUser().getId()).orElse(null));
            return sharedCard;
        }).toList();
    }

    // Get all users for a given card
    public List<SharedCardRequest> getAllUsersForCard(Long cardId) {
        List<SharedCardEntity> sharedCards = sharedCardRepository.findByCardId(cardId);
        return sharedCards.stream().map(SharedCardRequest::new).toList();
    }

    // Delete a shared card
    public void deleteSharedCard(Long SharedCardId) {
        sharedCardRepository.deleteById(SharedCardId);
    }
}
