package com.example.capstone.controllers;
import com.example.capstone.authentication.entities.UserEntity;
import com.example.capstone.services.TransactionService;
import com.example.capstone.services.DeepSeekService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/ai")
public class DeepSeekController {
    private final TransactionService transactionService;
    private final DeepSeekService deepSeekService;

    public DeepSeekController(TransactionService transactionService, DeepSeekService deepSeekService) {
        this.transactionService = transactionService;
        this.deepSeekService = deepSeekService;
    }

    @GetMapping("/analyze")
    public Map<String, Object> analyzeTransactions(Principal principal) {
        UserEntity user = (UserEntity) principal; // Assume principal contains authenticated user
        Map<String, Object> insights = transactionService.analyzeTransactions(user);
        String suggestion = deepSeekService.getSavingsSuggestion(insights);
        insights.put("suggestion", suggestion);
        return insights;
    }
}
