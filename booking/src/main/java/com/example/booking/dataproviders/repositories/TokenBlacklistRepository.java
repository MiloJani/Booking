package com.example.booking.dataproviders.repositories;

import com.example.booking.dataproviders.entities.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Long> {
        boolean existsByToken(String token);
        List<TokenBlacklist> deleteByInvalidatedAtBefore(LocalDateTime expiryDateTime);
}
