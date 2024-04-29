package com.example.banktransactionmanager.controller;

import com.example.banktransactionmanager.service.ExchangeRateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/exchange-rates")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @PostMapping("/fetch")
    @Operation(summary = "Fetch and store exchange rates", description = "Fetches exchange rates from an external API and stores them in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exchange rates fetched and stored successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> fetchAndStoreExchangeRates() {
        try {
            Map<String, BigDecimal> rates = exchangeRateService.fetchRatesFromExternalAPI();
            return ResponseEntity.ok("Exchange rates fetched and stored successfully.\n" + "Number of rates stored: " + rates.size());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch and store exchange rates: " + e.getMessage());
        }
    }
}
