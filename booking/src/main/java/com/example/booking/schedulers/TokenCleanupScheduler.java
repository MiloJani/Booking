package com.example.booking.schedulers;

import com.example.booking.dataproviders.entities.TokenBlacklist;
import com.example.booking.dataproviders.repositories.TokenBlacklistRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class TokenCleanupScheduler {

    private final TokenBlacklistRepository tokenBlacklistRepository;

    public TokenCleanupScheduler(TokenBlacklistRepository tokenBlacklistRepository) {
        this.tokenBlacklistRepository = tokenBlacklistRepository;
    }
// sec after hour,min after hour , hours , day , month , week
    @Scheduled(cron = "0 0 3 * * *") // Runs every day at 3 am
//    @Scheduled(cron = "0 0 15 * * *") // Runs every day at 3 pm
//@Scheduled(cron = "0 8 15 * * *")
@Transactional
    public void cleanupExpiredTokens() {
        LocalDateTime currentDateTime = LocalDateTime.now();

        LocalDateTime expiryDateTime = currentDateTime.minus(5, ChronoUnit.HOURS);
        List<TokenBlacklist> expiredTokens = tokenBlacklistRepository.deleteByInvalidatedAtBefore(expiryDateTime);

    }
}

