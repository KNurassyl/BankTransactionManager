package com.example.banktransactionmanager.controller;

import com.example.banktransactionmanager.dto.TransactionDto;
import com.example.banktransactionmanager.model.Transaction;
import com.example.banktransactionmanager.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    @Operation(summary = "Create a transaction", description = "Creates a new transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid transaction data provided"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> createTransaction(@RequestBody Transaction request) {
        transactionService.processTransaction(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/exceeded-limit")
    @Operation(summary = "Get exceeded limit transactions", description = "Retrieves a list of transactions that exceeded the limit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved exceeded limit transactions"),
            @ApiResponse(responseCode = "404", description = "No exceeded limit transactions found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<TransactionDto>> getExceededLimitTransactions() {
        List<TransactionDto> exceededTransactions = transactionService.getTransactionsExceedingLimit();
        return ResponseEntity.ok(exceededTransactions);
    }
}
