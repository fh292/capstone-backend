package com.example.capstone.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.capstone.services.DatabaseSeedService;

@RestController
@RequestMapping("/seed")
public class SeedController {

  private final DatabaseSeedService databaseSeedService;

  public SeedController(DatabaseSeedService databaseSeedService) {
    this.databaseSeedService = databaseSeedService;
  }

  @PostMapping
  public ResponseEntity<?> seedDatabase() {
    try {
      databaseSeedService.resetAndSeedDatabase();
      return ResponseEntity.ok().body("Database reset and seeded successfully");
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body("Error seeding database: " + e.getMessage());
    }
  }
}