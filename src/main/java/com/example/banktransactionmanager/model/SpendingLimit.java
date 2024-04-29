package com.example.banktransactionmanager.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class SpendingLimit extends BaseEntity{

    private BigDecimal limitAmount;

    private String category;

    @Column(name = "date_set")
    private LocalDateTime dateSet;

    public SpendingLimit(BigDecimal limitAmount, LocalDateTime dateSet) {
        super();
        this.limitAmount = limitAmount;
        this.dateSet = dateSet;
    }

    public SpendingLimit() {

    }

    public BigDecimal getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(BigDecimal limitAmount) {
        this.limitAmount = limitAmount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getDateSet() {
        return dateSet;
    }

    public void setDateSet(LocalDateTime dateSet) {
        this.dateSet = dateSet;
    }
}
