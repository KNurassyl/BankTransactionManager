package com.example.banktransactionmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import com.example.banktransactionmanager.model.ExchangeRate;
import com.example.banktransactionmanager.model.SpendingLimit;
import com.example.banktransactionmanager.model.Transaction;
import com.example.banktransactionmanager.repository.ExchangeRateRepository;
import com.example.banktransactionmanager.repository.SpendingLimitRepository;
import com.example.banktransactionmanager.repository.TransactionRepository;
import com.example.banktransactionmanager.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

public class TransactionServiceTests {

    private TransactionService transactionService;
    private ExchangeRateRepository exchangeRateRepository;
    private SpendingLimitRepository spendingLimitRepository;

    @BeforeEach
    void setUp() {
        TransactionRepository transactionRepository = mock(TransactionRepository.class);
        exchangeRateRepository = mock(ExchangeRateRepository.class);
        spendingLimitRepository = mock(SpendingLimitRepository.class);
        RestTemplate restTemplate = mock(RestTemplate.class);
        transactionService = new TransactionService(transactionRepository, exchangeRateRepository, restTemplate,
                spendingLimitRepository, "exchangeRatesApiUrl");
    }

    @Test
    void testProcessTransaction() {
        Transaction transaction = new Transaction();
        transaction.setCurrency("EUR");
        transaction.setAmount(BigDecimal.TEN);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setExpenseCategory("Category");

        ExchangeRate exchangeRate = new ExchangeRate("EUR/USD", new BigDecimal("1.1"), LocalDate.now());
        when(exchangeRateRepository.findByCurrencyPairAndRateDate(any(), any())).thenReturn(Optional.of(exchangeRate));

        transactionService.processTransaction(transaction);

        assertEquals(new BigDecimal("11.0"), transaction.getAmount());
    }

    @Test
    void testSetLimitExceededFlag() {
        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal("110"));
        transaction.setExpenseCategory("Category");

        SpendingLimit limit = new SpendingLimit();
        limit.setLimitAmount(new BigDecimal("100"));
        when(spendingLimitRepository.findByCategory("Category")).thenReturn(Optional.of(limit));

        transactionService.setLimitExceededFlag(transaction);

        assertTrue(transaction.isLimitExceeded());
    }

}

