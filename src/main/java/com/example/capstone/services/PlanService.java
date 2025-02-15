package com.example.capstone.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.capstone.authentication.entities.UserEntity;
import com.example.capstone.entities.PlanEntity;
import com.example.capstone.repositories.PlanRepository;

@Service
public class PlanService {
  private final PlanRepository planRepository;
  private final NotificationService notificationService;

  public PlanService(PlanRepository planRepository,
      NotificationService notificationService) {
    this.planRepository = planRepository;
    this.notificationService = notificationService;
  }

  public List<PlanEntity> getAllPlans() {
    return planRepository.findAll();
  }

  @Transactional
  public void upgradePlan(UserEntity user, String planName) {
    // Check if user has a bank account connected
    if (user.getBankAccountUsername() == null || user.getBankAccountNumber() == null) {
      throw new IllegalStateException("Please connect your bank account before upgrading your plan");
    }

    PlanEntity newPlan = planRepository.findByName(planName)
        .orElseThrow(() -> new NoSuchElementException("Plan not found: " + planName));

    PlanEntity currentPlan = planRepository.findByName(user.getPlan())
        .orElseThrow(() -> new NoSuchElementException("Current plan not found: " + user.getPlan()));

    // If user is already on this plan, don't process again
    if (planName.equals(user.getPlan())) {
      throw new IllegalStateException("You are already subscribed to the " + planName + " plan");
    }

    // Verify it's an upgrade (higher price)
    if (newPlan.getPrice() <= currentPlan.getPrice()) {
      throw new IllegalStateException("Please use downgrade endpoint to switch to a lower-tier plan");
    }

    // Check if it's a paid plan
    if (newPlan.getPrice() > 0) {
      // Simulate payment processing
      simulatePaymentProcessing(user, newPlan);
    }

    // Update user's plan details
    updateUserPlan(user, newPlan, true);

    // Send notification about plan upgrade
    notificationService.sendNotification(
        user,
        "Plan Upgraded",
        "Your plan has been upgraded to " + planName,
        Map.of(
            "planName", planName,
            "monthlySpendLimit", newPlan.getMonthlySpendLimit(),
            "dailySpendLimit", newPlan.getDailySpendLimit(),
            "monthlyCardLimit", newPlan.getMonthlyCardIssuanceLimit(),
            "planStartDate", user.getPlanStartDate(),
            "planEndDate", user.getPlanEndDate(),
            "autoRenewal", user.getAutoRenewal()));
  }

  @Transactional
  public void downgradePlan(UserEntity user, String planName) {
    PlanEntity newPlan = planRepository.findByName(planName)
        .orElseThrow(() -> new NoSuchElementException("Plan not found: " + planName));

    PlanEntity currentPlan = planRepository.findByName(user.getPlan())
        .orElseThrow(() -> new NoSuchElementException("Current plan not found: " + user.getPlan()));

    // If user is already on this plan, don't process again
    if (planName.equals(user.getPlan())) {
      throw new IllegalStateException("You are already subscribed to the " + planName + " plan");
    }

    // Verify it's a downgrade (lower or equal price)
    if (newPlan.getPrice() > currentPlan.getPrice()) {
      throw new IllegalStateException("Please use upgrade endpoint to switch to a higher-tier plan");
    }

    // If current plan hasn't expired, allow using it until expiration
    if (user.getPlanEndDate() != null && user.getPlanEndDate().isAfter(LocalDateTime.now())) {
      // Schedule the downgrade for the end of the current billing period
      // For now, we'll just notify the user
      notificationService.sendNotification(
          user,
          "Plan Downgrade Scheduled",
          "Your plan will be downgraded to " + planName + " on " + user.getPlanEndDate(),
          Map.of("effectiveDate", user.getPlanEndDate()));
      return;
    }

    // Update user's plan details
    updateUserPlan(user, newPlan, false);

    // Send notification about plan downgrade
    notificationService.sendNotification(
        user,
        "Plan Downgraded",
        "Your plan has been downgraded to " + planName,
        Map.of(
            "planName", planName,
            "monthlySpendLimit", newPlan.getMonthlySpendLimit(),
            "dailySpendLimit", newPlan.getDailySpendLimit(),
            "monthlyCardLimit", newPlan.getMonthlyCardIssuanceLimit()));
  }

  @Transactional
  public void toggleAutoRenewal(UserEntity user) {
    // Can't toggle auto-renewal for BASIC plan
    if ("BASIC".equals(user.getPlan())) {
      throw new IllegalStateException("Auto-renewal is not available for the Basic plan");
    }

    user.setAutoRenewal(!user.getAutoRenewal());

    notificationService.sendNotification(
        user,
        "Auto-Renewal " + (user.getAutoRenewal() ? "Enabled" : "Disabled"),
        "Auto-renewal has been " + (user.getAutoRenewal() ? "enabled" : "disabled") + " for your " + user.getPlan()
            + " plan.",
        Map.of("autoRenewal", user.getAutoRenewal()));
  }

  public void initializeDefaultPlans() {
    if (planRepository.count() == 0) {
      // Create Basic Plan
      PlanEntity basicPlan = new PlanEntity();
      basicPlan.setName("BASIC");
      basicPlan.setPrice(0.0);
      basicPlan.setMonthlySpendLimit(5000.0);
      basicPlan.setDailySpendLimit(500.0);
      basicPlan.setMonthlyCardIssuanceLimit(10);
      basicPlan.setHasLocationLocking(false);
      basicPlan.setHasCategoryLocking(false);
      basicPlan.setHasMerchantLocking(true);
      planRepository.save(basicPlan);

      // Create Premium Plan
      PlanEntity premiumPlan = new PlanEntity();
      premiumPlan.setName("PREMIUM");
      premiumPlan.setPrice(5.0);
      premiumPlan.setMonthlySpendLimit(20000.0);
      premiumPlan.setDailySpendLimit(2000.0);
      premiumPlan.setMonthlyCardIssuanceLimit(50);
      premiumPlan.setHasLocationLocking(true);
      premiumPlan.setHasCategoryLocking(true);
      premiumPlan.setHasMerchantLocking(true);
      planRepository.save(premiumPlan);
    }
  }

  private void updateUserPlan(UserEntity user, PlanEntity plan, boolean isUpgrade) {
    LocalDateTime now = LocalDateTime.now();
    user.setPlan(plan.getName());
    user.setPlanStartDate(now);

    // Set plan end date for paid plans
    if (plan.getPrice() > 0) {
      // If upgrading mid-cycle, prorate the time remaining
      if (isUpgrade && user.getPlanEndDate() != null && user.getPlanEndDate().isAfter(now)) {
        // Add remaining days to a new 30-day period
        long daysToAdd = 30 + java.time.Duration.between(now, user.getPlanEndDate()).toDays();
        user.setPlanEndDate(now.plusDays(daysToAdd));
      } else {
        // Standard 30-day period
        user.setPlanEndDate(now.plusDays(30));
      }
    } else {
      // Basic plan has no end date
      user.setPlanEndDate(null);
      user.setAutoRenewal(false);
    }

    // Update user's limits and features
    user.setMonthlySpendLimit(plan.getMonthlySpendLimit());
    user.setDailySpendLimit(plan.getDailySpendLimit());
    user.setMonthlyCardIssuanceLimit(plan.getMonthlyCardIssuanceLimit());
  }

  private void simulatePaymentProcessing(UserEntity user, PlanEntity plan) {
    // In a real implementation, you would:
    // 1. Call your payment processor (e.g., Stripe)
    // 2. Create a subscription record
    // 3. Handle payment failures

    // For now, we'll just validate the bank account connection
    if (user.getBankAccountUsername() == null || user.getBankAccountNumber() == null) {
      throw new IllegalStateException("Payment failed: No bank account connected");
    }

    // // Simulate a random payment failure (1% chance)
    // if (Math.random() < 0.01) {
    // throw new IllegalStateException("Payment failed: Insufficient funds");
    // }
  }
}