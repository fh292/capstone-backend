package com.example.capstone.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class DatabaseSeedService {

  private static final Logger logger = LoggerFactory.getLogger(DatabaseSeedService.class);
  private final JdbcTemplate jdbcTemplate;

  @Value("${spring.datasource.username}")
  private String dbUsername;

  @Value("${spring.datasource.password}")
  private String dbPassword;

  @Value("${spring.datasource.url}")
  private String dbUrl;

  public DatabaseSeedService(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public void resetAndSeedDatabase() throws IOException, InterruptedException {
    logger.info("Starting database reset and seed process");

    // Validate database connection
    try {
      jdbcTemplate.queryForObject("SELECT 1", Integer.class);
    } catch (Exception e) {
      String error = "Failed to connect to database: " + e.getMessage();
      logger.error(error);
      throw new RuntimeException(error);
    }

    // Terminate existing connections and reset database
    logger.info("Terminating existing database connections and resetting database");
    try {
      // Terminate other connections
      jdbcTemplate.execute("""
            SELECT pg_terminate_backend(pg_stat_activity.pid)
            FROM pg_stat_activity
            WHERE pg_stat_activity.datname = 'capstone'
              AND pid <> pg_backend_pid();
          """);

      // Drop and recreate schema
      jdbcTemplate.execute("DROP SCHEMA IF EXISTS public CASCADE;");
      jdbcTemplate.execute("CREATE SCHEMA public;");
      jdbcTemplate.execute("GRANT ALL ON SCHEMA public TO " + dbUsername + ";");
      jdbcTemplate.execute("GRANT ALL ON SCHEMA public TO public;");

      logger.info("Database schema reset completed");
    } catch (Exception e) {
      String error = "Failed to reset database: " + e.getMessage();
      logger.error(error);
      throw new RuntimeException(error);
    }

    // Get and validate SQL file from source directory
    logger.info("Locating SQL seed file");
    String projectRoot = new File(".").getAbsolutePath();
    Path sqlPath = Paths.get(projectRoot, "src", "main", "resources", "seed", "seed.sql");

    // Check container path if development path doesn't exist
    if (!Files.exists(sqlPath)) {
      sqlPath = Paths.get("/app/seed/seed.sql");
    }

    if (!Files.exists(sqlPath)) {
      String error = "SQL seed file not found at: " + sqlPath;
      logger.error(error);
      throw new RuntimeException(error);
    }

    logger.info("Found SQL seed file at: {}", sqlPath);

    // Validate SQL file is readable and not empty
    if (!Files.isReadable(sqlPath)) {
      String error = "SQL seed file is not readable: " + sqlPath;
      logger.error(error);
      throw new RuntimeException(error);
    }

    if (Files.size(sqlPath) == 0) {
      String error = "SQL seed file is empty: " + sqlPath;
      logger.error(error);
      throw new RuntimeException(error);
    }

    // Extract database name
    String dbName = extractDatabaseName();
    logger.info("Using database: {}", dbName);

    // Build and execute psql command
    ProcessBuilder pb = new ProcessBuilder(
        "psql",
        "-h", "postgres",
        "-p", "5432",
        "-U", dbUsername,
        "-d", dbName,
        "-f", sqlPath.toString());

    // Set PGPASSWORD environment variable
    pb.environment().put("PGPASSWORD", dbPassword);
    pb.redirectErrorStream(true);

    logger.info("Executing psql command to restore database");
    Process process = pb.start();

    // Read and log the output
    StringBuilder output = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        output.append(line).append("\n");
        logger.debug("psql output: {}", line);
      }
    }

    // Wait for process completion and check exit code
    int exitCode = process.waitFor();
    if (exitCode != 0) {
      String error = String.format("Database seed failed with exit code %d. Output:\n%s",
          exitCode, output.toString());
      logger.error(error);
      throw new RuntimeException(error);
    }

    logger.info("Database reset and seed completed successfully");
  }

  private String extractDatabaseName() {
    try {
      return dbUrl.substring(dbUrl.lastIndexOf("/") + 1);
    } catch (Exception e) {
      String error = "Failed to extract database name from URL: " + dbUrl;
      logger.error(error);
      throw new RuntimeException(error);
    }
  }
}