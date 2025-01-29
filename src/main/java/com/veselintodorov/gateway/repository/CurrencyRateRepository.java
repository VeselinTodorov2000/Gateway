package com.veselintodorov.gateway.repository;

import com.veselintodorov.gateway.entity.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Long> {
    @Query("SELECT c FROM CurrencyRate c WHERE c.currency = :currency ORDER BY c.timestamp DESC LIMIT 1")
    Optional<CurrencyRate> findLatestByCurrencyAndBaseCurrency(@Param("currency") String currency);
}