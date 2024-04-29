package com.example.banktransactionmanager.repository;

import com.example.banktransactionmanager.model.SpendingLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpendingLimitRepository extends JpaRepository<SpendingLimit, Long> {
    Optional<SpendingLimit> findByCategory(String category);
    Optional<SpendingLimit> findFirstByOrderByDateSetDesc();
}
