package com.example.banktransactionmanager.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class ExchangeRate extends BaseEntity{

    @Column(name = "currency_pair")
    private String currencyPair;

    private BigDecimal rate;

    @Column(name = "rate_date")
    private LocalDateTime rateDate;

    public ExchangeRate(String currencyPair, BigDecimal rate, LocalDate rateDate) {
        super();
        this.currencyPair = currencyPair;
        this.rate = rate;
        this.rateDate = rateDate.atStartOfDay();
    }

    public ExchangeRate() {
    }

    public String getCurrencyPair() {
        return currencyPair;
    }

    public void setCurrencyPair(String currencyPair) {
        this.currencyPair = currencyPair;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public LocalDateTime getRateDate() {
        return rateDate;
    }

    public void setRateDate(LocalDateTime rateDate) {
        this.rateDate = rateDate;
    }
}
