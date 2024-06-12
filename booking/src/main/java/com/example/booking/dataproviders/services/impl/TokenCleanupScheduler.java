package com.example.booking.dataproviders.services.impl;

import com.example.booking.dataproviders.entities.TokenBlacklist;
import com.example.booking.dataproviders.repositories.TokenBlacklistRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class TokenCleanupScheduler {

    private final TokenBlacklistRepository tokenBlacklistRepository;

    public TokenCleanupScheduler(TokenBlacklistRepository tokenBlacklistRepository) {
        this.tokenBlacklistRepository = tokenBlacklistRepository;
    }

    @Scheduled(cron = "0 3 * * *") // Runs every day at 3 am
    public void cleanupExpiredTokens() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        // Calculate the expiry timestamp based on the expiration duration of the token (e.g., 5 hours)
        LocalDateTime expiryDateTime = currentDateTime.minus(5, ChronoUnit.HOURS); // Change 5 to the actual expiration duration
        List<TokenBlacklist> expiredTokens = tokenBlacklistRepository.deleteByInvalidatedAtBefore(expiryDateTime);
        // Optionally, you can log or handle the deleted tokens here
    }
}

