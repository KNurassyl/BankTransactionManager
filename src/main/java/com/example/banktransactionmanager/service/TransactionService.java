package com.example.banktransactionmanager.service;

import com.example.banktransactionmanager.dto.TransactionDto;
import com.example.banktransactionmanager.model.ExchangeRate;
import com.example.banktransactionmanager.model.ExchangeRatesApiResponse;
import com.example.banktransactionmanager.model.SpendingLimit;
import com.example.banktransactionmanager.model.Transaction;
import com.example.banktransactionmanager.repository.ExchangeRateRepository;
import com.example.banktransactionmanager.repository.SpendingLimitRepository;
import com.example.banktransactionmanager.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final ExchangeRateRepository exchangeRateRepository;
    private final RestTemplate restTemplate;
    private final SpendingLimitRepository spendingLimitRepository;
    private final String exchangeRatesApiUrl;


    public TransactionService(TransactionRepository transactionRepository, ExchangeRateRepository exchangeRateRepository, RestTemplate restTemplate, SpendingLimitRepository spendingLimitRepository, @Value("${exchange.rates.api.url}") String exchangeRatesApiUrl) {
        this.transactionRepository = transactionRepository;
        this.exchangeRateRepository = exchangeRateRepository;
        this.restTemplate = restTemplate;
        this.spendingLimitRepository = spendingLimitRepository;
        this.exchangeRatesApiUrl = exchangeRatesApiUrl;
    }


    public void processTransaction(Transaction transaction) {
        BigDecimal usdAmount = calculateUSDAmount(transaction);
        transaction.setAmount(usdAmount);
        transactionRepository.save(transaction);

        setLimitExceededFlag(transaction);
    }

    private BigDecimal calculateUSDAmount(Transaction transaction) {
        Optional<ExchangeRate> exchangeRate = exchangeRateRepository.findByCurrencyPairAndRateDate(
                transaction.getCurrency() + "/USD", LocalDateTime.from(transaction.getTransactionDate().toLocalDate()));

        BigDecimal rate = exchangeRate.orElseGet(() -> getLatestExchangeRate(transaction.getCurrency())).getRate();

        return transaction.getAmount().multiply(rate);
    }

    public ExchangeRate getLatestExchangeRate(String currency) {
        try {
            ExchangeRatesApiResponse response = restTemplate.getForObject(exchangeRatesApiUrl, ExchangeRatesApiResponse.class);

            BigDecimal rate = (response != null && response.getRates() != null)
                    ? response.getRates().get(currency)
                    : BigDecimal.ONE;

            return new ExchangeRate("USD/" + currency, rate, LocalDate.now());
        } catch (Exception e) {
            System.err.println("Error fetching exchange rate for " + currency + ": " + e.getMessage());
            return new ExchangeRate("USD/" + currency, BigDecimal.ONE, LocalDate.now());
        }
    }

    private void setLimitExceededFlag(Transaction transaction) {
        Optional<SpendingLimit> limit = spendingLimitRepository.findByCategory(transaction.getExpenseCategory());
        if (limit.isPresent() && transaction.getAmount().compareTo(limit.get().getLimitAmount()) > 0) {
            transaction.setLimitExceeded(true);
        }
    }

    public List<TransactionDto> getTransactionsExceedingLimit() {
        List<Transaction> exceededTransactions = transactionRepository.findByLimitExceeded(true);

        return exceededTransactions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private TransactionDto convertToDto(Transaction transaction) {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAccountFrom(transaction.getAccountFrom());
        transactionDto.setAccountTo(transaction.getAccountTo());
        transactionDto.setCurrency(transaction.getCurrency());
        transactionDto.setAmount(transaction.getAmount());
        transactionDto.setExpenseCategory(transaction.getExpenseCategory());
        transactionDto.setTransactionDate(transaction.getTransactionDate());
        transactionDto.setLimitExceeded(transaction.isLimitExceeded());
        return transactionDto;
    }
}

