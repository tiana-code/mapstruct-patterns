package com.mapstructpatterns.mapper

import com.mapstructpatterns.model.entity.Certificate
import com.mapstructpatterns.model.enums.DataSource
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.UUID

class CertificateMapperTest {
    private val today: LocalDate = LocalDate.parse("2026-04-05")

    private val mapper: CertificateMapper = object : CertificateMapper() {
        override fun toResponse(entity: Certificate) = error("stub")
        override fun toResponseList(entities: List<Certificate>) = error("stub")
    }

    private val realMapper: CertificateMapper = CertificateMapperImpl()

    @Test
    fun `calculateDaysUntilExpiry returns correct number of days`() {
        val entity = Certificate(
            vesselId = UUID.randomUUID(),
            certificateType = "SAFETY",
            certificateNumber = "CERT-001",
            issuedBy = "Lloyd's Register",
            issueDate = today.minusYears(1),
            expiryDate = today.plusDays(15)
        )

        val days = mapper.calculateDaysUntilExpiry(entity, today)

        assertEquals(15L, days)
    }

    @Test
    fun `isExpiringSoon returns true when expiry within 30 days`() {
        val entity = Certificate(
            vesselId = UUID.randomUUID(),
            certificateType = "SAFETY",
            certificateNumber = "CERT-002",
            issuedBy = "Lloyd's Register",
            issueDate = today.minusYears(1),
            expiryDate = today.plusDays(20)
        )

        assertTrue(mapper.isExpiringSoon(entity, today))
    }

    @Test
    fun `isExpiringSoon returns false when expiry beyond 30 days`() {
        val entity = Certificate(
            vesselId = UUID.randomUUID(),
            certificateType = "SAFETY",
            certificateNumber = "CERT-003",
            issuedBy = "Lloyd's Register",
            issueDate = today.minusYears(1),
            expiryDate = today.plusDays(45)
        )

        assertFalse(mapper.isExpiringSoon(entity, today))
    }

    @Test
    fun `isExpiringSoon returns false for already expired certificate`() {
        val entity = Certificate(
            vesselId = UUID.randomUUID(),
            certificateType = "SAFETY",
            certificateNumber = "CERT-004",
            issuedBy = "Lloyd's Register",
            issueDate = today.minusYears(2),
            expiryDate = today.minusDays(5)
        )

        assertFalse(mapper.isExpiringSoon(entity, today))
    }

    @Test
    fun `isDemoData is mapped via is-prefix expression`() {
        val entity = Certificate(
            vesselId = UUID.randomUUID(),
            certificateType = "SAFETY",
            certificateNumber = "CERT-005",
            issuedBy = "Lloyd's Register",
            issueDate = today.minusYears(1),
            expiryDate = today.plusYears(1),
            isDemoData = true,
            dataSource = DataSource.REAL
        )

        val response = realMapper.toResponse(entity)

        assertTrue(response.isDemoData)
    }
}
