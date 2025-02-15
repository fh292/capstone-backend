package com.example.capstone.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.capstone.authentication.entities.UserEntity;
import com.example.capstone.entities.PlanEntity;
import com.example.capstone.services.PlanService;
import com.example.capstone.services.UserService;

@RestController
@RequestMapping("/plans")
public class PlanController {
  private final PlanService planService;
  private final UserService userService;

  public PlanController(PlanService planService,
      UserService userService) {
    this.planService = planService;
    this.userService = userService;
  }

  @GetMapping
  public ResponseEntity<List<PlanEntity>> getAllPlans() {
    return ResponseEntity.ok(planService.getAllPlans());
  }

  @PostMapping("/upgrade")
  public ResponseEntity<?> upgradePlan(Authentication authentication,
      @RequestBody Map<String, String> request) {
    try {
      String planName = request.get("planName");
      if (planName == null) {
        return ResponseEntity.badRequest().body("Plan name is required");
      }

      UserEntity user = userService.findByEmail(authentication.getName())
          .orElseThrow(() -> new RuntimeException("User not found"));

      planService.upgradePlan(user, planName.toUpperCase());
      return ResponseEntity.ok(Map.of("message", "Successfully upgraded to " + planName + " plan"));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("/downgrade")
  public ResponseEntity<?> downgradePlan(Authentication authentication,
      @RequestBody Map<String, String> request) {
    try {
      String planName = request.get("planName");
      if (planName == null) {
        return ResponseEntity.badRequest().body("Plan name is required");
      }

      UserEntity user = userService.findByEmail(authentication.getName())
          .orElseThrow(() -> new RuntimeException("User not found"));

      planService.downgradePlan(user, planName.toUpperCase());
      return ResponseEntity.ok(Map.of("message", "Successfully initiated downgrade to " + planName + " plan"));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("/auto-renewal/toggle")
  public ResponseEntity<?> toggleAutoRenewal(Authentication authentication) {
    try {
      UserEntity user = userService.findByEmail(authentication.getName())
          .orElseThrow(() -> new RuntimeException("User not found"));

      planService.toggleAutoRenewal(user);
      return ResponseEntity.ok(Map.of(
          "autoRenewal", user.getAutoRenewal(),
          "message", "Auto-renewal has been " + (user.getAutoRenewal() ? "enabled" : "disabled")));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}