package com.example.capstone.seeder;
import com.example.capstone.authentication.repositories.UserRepository;
import com.example.capstone.authentication.entities.UserEntity;
import com.example.capstone.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.capstone.entities.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Component
public class DatabaseSeeder {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);
    private UserRepository userRepository;
    private CardRepository cardRepository;
    private SharedCardRepository sharedCardRepository;
    private TransactionRepository transactionRepository;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseSeeder(UserRepository userRepository, CardRepository cardRepository, SharedCardRepository sharedCardRepository, TransactionRepository transactionRepository, JdbcTemplate jdbcTemplate) {
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
        this.sharedCardRepository = sharedCardRepository;
        this.transactionRepository = transactionRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedUsersTable();
        seedCardsTable();
        seedSharedCardsTable();
        seedTransactionsTable();
    }

    private void seedUsersTable() {
        String sql = "SELECT COUNT(*) FROM \"user_entity\" WHERE email = 'farah@cvrd.com'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);

        if (count == null || count == 0) {
            UserEntity user = new UserEntity();
            user.setFirstName("farah");
            user.setLastName("Doe");
            user.setEmail("farah@cvrd.com");
            user.setPassword(new BCryptPasswordEncoder().encode("123456"));
            user.setCivilId("123456789123");
            user.setPhoneNumber("+1234567890");
            user.setBankAccountUsername("farah");
            user.setBankAccountNumber("1234567890");
            user.setGender("female");
            user.setDateOfBirth(LocalDate.parse("1990-01-01"));
            user.setProfilePic("https://example.com/profile.jpg");
            user.setRole("USER");
            user.setLastSpendReset(LocalDateTime.now());

            userRepository.save(user);
            logger.info("Users table seeded.");
        } else {
            logger.info("Users seeding not required.");
        }
    }

    private void seedCardsTable() {
        String sql = "SELECT COUNT(*) FROM \"card_entity\"";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        UserEntity user = userRepository.findByEmail("farah@cvrd.com")
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (count == null || count == 0) {
            CardEntity card1 = new CardEntity();
            card1.setCardNumber("1234-5678-9012-3456");
            card1.setCardName("Farah's Visa");
            card1.setCardType("Visa");
            card1.setExpiryDate(LocalDate.of(2025, 12, 31));
            card1.setBankAccountNumber("1234567890");
            card1.setCvv("123");
            card1.setSpendingLimit(5000.0);
            card1.setRemainingLimit(5000.0);
//            card1.setIsShared(false);
//            card1.setIsPaused(false);
//            card1.setIsClosed(false);
            card1.setUser(user);
            card1.setCreatedAt(LocalDateTime.now());
            cardRepository.save(card1);
            logger.info("Cards table seeded.");
        } else {
            logger.info("Cards seeding not required.");
        }
    }

    private void seedSharedCardsTable() {
        String sql = "SELECT COUNT(*) FROM \"shared_card_entity\"";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        UserEntity user = userRepository.findByEmail("farah@cvrd.com")
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (count == null || count == 0) {
            SharedCardEntity sharedCard = new SharedCardEntity();
            sharedCard.setSharedAt(LocalDateTime.now());
            sharedCard.setExpiresAt(LocalDateTime.now().plusDays(30));
            sharedCard.setUser(user);
            sharedCardRepository.save(sharedCard);
            logger.info("Shared Cards table seeded.");
        } else {
            logger.info("Shared Cards seeding not required.");
        }
    }

    private void seedTransactionsTable() {
        String sql = "SELECT COUNT(*) FROM \"transaction_entity\"";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);

        if (count == null || count == 0) {
            TransactionEntity transaction = new TransactionEntity();
            transaction.setAmount(100.50);
            transaction.setMerchant("Amazon");
            transaction.setStatus("APPROVED");
//            transaction.setIsRecurring(false);
            transaction.setDescription("Online Purchase");
            transaction.setType("DEBIT");
            transaction.setCategory("Shopping");
            transaction.setCreatedAt(LocalDateTime.now());

            transactionRepository.save(transaction);
            logger.info("Transactions table seeded.");
        } else {
            logger.info("Transactions seeding not required.");
        }
    }
}
