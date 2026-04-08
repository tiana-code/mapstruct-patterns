package com.mapstructpatterns.policy

import org.springframework.stereotype.Component
import java.time.Instant
import java.time.temporal.ChronoUnit

@Component
class CertificateExpiryPolicy {

    fun isExpiringSoon(expiryDate: Instant?, referenceDate: Instant = Instant.now()): Boolean {
        if (expiryDate == null) return false
        val daysUntilExpiry = ChronoUnit.DAYS.between(referenceDate, expiryDate)
        return daysUntilExpiry in 0..90
    }

    fun daysUntilExpiry(expiryDate: Instant?, referenceDate: Instant = Instant.now()): Long {
        if (expiryDate == null) return -1
        return ChronoUnit.DAYS.between(referenceDate, expiryDate)
    }
}
