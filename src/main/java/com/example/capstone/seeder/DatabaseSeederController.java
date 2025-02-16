package com.example.capstone.seeder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DatabaseSeederController {

    private final DatabaseSeederService databaseSeederService;

    public DatabaseSeederController(DatabaseSeederService databaseSeederService) {
        this.databaseSeederService = databaseSeederService;
    }

    @PostMapping("/seed")
    public ResponseEntity<String> seedDatabase() {
        databaseSeederService.executeSqlFile("database_dump.sql"); // Change the filename if needed
        return ResponseEntity.ok("Database seeding executed successfully.");
    }
}
