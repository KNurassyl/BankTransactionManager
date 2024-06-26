package com.example.banktransactionmanager.repository;

import com.example.banktransactionmanager.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    Optional<ExchangeRate> findByCurrencyPairAndRateDate(String currencyPair, LocalDateTime date);

}