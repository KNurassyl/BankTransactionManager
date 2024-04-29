package com.example.banktransactionmanager.controller;

import com.example.banktransactionmanager.model.SpendingLimit;
import com.example.banktransactionmanager.service.SpendingLimitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/limits")
public class LimitController {

    private final SpendingLimitService limitService;

    public LimitController(SpendingLimitService limitService) {
        this.limitService = limitService;
    }

    @GetMapping("/current")
    @Operation(summary = "Get current spending limit", description = "Retrieves the current spending limit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved current spending limit"),
            @ApiResponse(responseCode = "404", description = "No spending limit found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SpendingLimit> getCurrentLimit() {
        SpendingLimit currentLimit = limitService.getCurrentLimit();
        return ResponseEntity.ok(currentLimit);
    }

    @PostMapping("/set")
    @Operation(summary = "Set new spending limit", description = "Sets a new spending limit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New spending limit set successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid new spending limit provided"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> setNewLimit(@RequestParam BigDecimal newLimit) {
        limitService.setNewLimit(newLimit);
        return ResponseEntity.ok().build();
    }
}