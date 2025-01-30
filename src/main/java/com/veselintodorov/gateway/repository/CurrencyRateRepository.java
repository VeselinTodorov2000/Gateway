package com.veselintodorov.gateway.repository;

import com.veselintodorov.gateway.entity.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Long> {
    @Query("SELECT c FROM CurrencyRate c WHERE c.currency = :currency AND c.baseCurrency = :base ORDER BY c.timestamp DESC LIMIT 1")
    Optional<CurrencyRate> findLatestByCurrencyAndBaseCurrency(@Param("currency") String currency, @Param("base") String base);

    @Query("""
        SELECT c FROM CurrencyRate c
        WHERE c.currency = :currency
        AND c.baseCurrency = :base
        AND c.timestamp >= :timeThreshold
        ORDER BY c.timestamp DESC
    """)
    List<CurrencyRate> findRecentByCurrencyAndBaseCurrency(@Param("currency") String currencyCode,
                                                           @Param("base") String base,
                                                           @Param("timeThreshold") Instant timeThreshold);
}