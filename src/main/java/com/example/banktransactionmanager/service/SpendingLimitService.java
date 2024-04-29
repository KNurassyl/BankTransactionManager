package com.example.banktransactionmanager.service;

import com.example.banktransactionmanager.model.SpendingLimit;
import com.example.banktransactionmanager.repository.SpendingLimitRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SpendingLimitService {
    private final SpendingLimitRepository spendingLimitRepository;

    public SpendingLimitService(SpendingLimitRepository spendingLimitRepository) {
        this.spendingLimitRepository = spendingLimitRepository;
    }

    public SpendingLimit getCurrentLimit() {
        Optional<SpendingLimit> latestLimitOptional = spendingLimitRepository.findFirstByOrderByDateSetDesc();
        return latestLimitOptional.orElseGet(() -> new SpendingLimit(BigDecimal.valueOf(1000), LocalDateTime.now()));
    }

    public void setNewLimit(BigDecimal newLimit) {
        SpendingLimit currentLimit = getCurrentLimit();
        if (newLimit.compareTo(currentLimit.getLimitAmount()) != 0) {
            throw new IllegalArgumentException("New limit must be different from the current limit.");
        }
        SpendingLimit limit = new SpendingLimit(newLimit, LocalDateTime.now());
        spendingLimitRepository.save(limit);
    }
}
