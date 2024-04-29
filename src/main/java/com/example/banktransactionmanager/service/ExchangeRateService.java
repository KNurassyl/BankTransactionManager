package com.example.banktransactionmanager.service;

import com.example.banktransactionmanager.model.ExchangeRate;
import com.example.banktransactionmanager.model.ExchangeRatesApiResponse;
import com.example.banktransactionmanager.repository.ExchangeRateRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExchangeRateService {
    private final ExchangeRateRepository exchangeRateRepository;
    private final RestTemplate restTemplate;
    private final String exchangeRatesApiUrl;

    public ExchangeRateService(ExchangeRateRepository exchangeRateRepository, RestTemplate restTemplate,
                               @Value("${exchange.rates.api.url}") String exchangeRatesApiUrl) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.restTemplate = restTemplate;
        this.exchangeRatesApiUrl = exchangeRatesApiUrl;
    }


    public Map<String, BigDecimal> fetchRatesFromExternalAPI() {
        try {
            ExchangeRatesApiResponse response = restTemplate.getForObject(exchangeRatesApiUrl, ExchangeRatesApiResponse.class);

            Map<String, BigDecimal> rates = new HashMap<>();
            if (response != null && response.getRates() != null) {
                rates = response.getRates().entrySet().stream()
                        .map(entry -> {
                            String currencyPair = entry.getKey();
                            BigDecimal rate = entry.getValue();

                            ExchangeRate exchangeRate = new ExchangeRate();
                            exchangeRate.setCurrencyPair(currencyPair);
                            exchangeRate.setRate(rate);
                            exchangeRate.setRateDate(LocalDateTime.now());
                            exchangeRateRepository.save(exchangeRate);

                            return Map.entry(currencyPair, rate);
                        })
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            }

            return rates;
        } catch (Exception e) {
            System.err.println("Error fetching exchange rates: " + e.getMessage());
            return new HashMap<>();
        }
    }
}