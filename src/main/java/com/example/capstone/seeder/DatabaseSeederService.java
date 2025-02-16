package com.example.capstone.seeder;

import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Service
public class DatabaseSeederService {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseSeederService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void executeSqlFile(String fileName) {
        try {
            // Load the SQL file from resources
            InputStream inputStream = new ClassPathResource(fileName).getInputStream();
            String sql = new BufferedReader(new InputStreamReader(inputStream))
                    .lines().collect(Collectors.joining("\n"));

            // Execute SQL statements
            jdbcTemplate.execute(sql);

            System.out.println("Database seeded successfully from " + fileName);
        } catch (Exception e) {
            throw new RuntimeException("Error executing SQL file: " + fileName, e);
        }
    }
}
