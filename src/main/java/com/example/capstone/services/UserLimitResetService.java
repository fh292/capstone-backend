package com.example.capstone.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.capstone.authentication.repositories.UserRepository;

@Service
public class UserLimitResetService {
    private static final Logger logger = LoggerFactory.getLogger(UserLimitResetService.class);
    private final UserRepository userRepository;

    public UserLimitResetService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Scheduled(cron = "0 0 0 1 * *") // Run at midnight on the first day of every month
    @Transactional
    public void resetMonthlyLimits() {
        try {
            logger.info("Starting monthly limits reset");
            userRepository.resetMonthlyLimits();
            logger.info("Successfully reset monthly limits");
        } catch (Exception e) {
            logger.error("Error resetting monthly limits", e);
            throw e; // Re-throw to ensure transaction rollback
        }
    }

    @Scheduled(cron = "0 0 0 * * *") // Run at midnight every day
    @Transactional
    public void resetDailyLimits() {
        try {
            logger.info("Starting daily limits reset");
            userRepository.resetDailyLimits();
            logger.info("Successfully reset daily limits");
        } catch (Exception e) {
            logger.error("Error resetting daily limits", e);
            throw e; // Re-throw to ensure transaction rollback
        }
    }
}