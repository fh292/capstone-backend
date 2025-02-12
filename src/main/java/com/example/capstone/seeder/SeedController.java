package com.example.capstone.seeder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SeedController {
    @Autowired
    private DatabaseSeeder databaseSeeder;


    //this is for injecting the seed manually (the seed is supposed to work automatically)
    @GetMapping("/seed")
    public ResponseEntity<String> testSeeding() {
        databaseSeeder.seed(null);
        return ResponseEntity.ok("Seeder executed.");
    }
}
