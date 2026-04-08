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

    @Test
    fun `generated impl maps all fields correctly`() {
        val impl = CertificateMapperImpl()
        val entity = Certificate(
            vesselId = UUID.randomUUID(),
            certificateType = "SAFETY",
            certificateNumber = "CERT-006",
            issuedBy = "Bureau Veritas",
            issueDate = today.minusYears(1),
            expiryDate = today.plusDays(60),
            notes = "Annual renewal",
            isDemoData = false,
            dataSource = DataSource.REAL
        )

        val response = impl.toResponse(entity)

        assertEquals("CERT-006", response.certificateNumber)
        assertEquals("Bureau Veritas", response.issuedBy)
        assertEquals("Annual renewal", response.notes)
        assertFalse(response.isDemoData)
    }

    @Test
    fun `generated impl toResponseList maps each element`() {
        val impl = CertificateMapperImpl()
        val entities = listOf(
            Certificate(
                vesselId = UUID.randomUUID(),
                certificateType = "SAFETY",
                certificateNumber = "CERT-007",
                issuedBy = "Lloyd's Register",
                issueDate = today.minusYears(1),
                expiryDate = today.plusDays(10)
            ),
            Certificate(
                vesselId = UUID.randomUUID(),
                certificateType = "LOAD_LINE",
                certificateNumber = "CERT-008",
                issuedBy = "DNV",
                issueDate = today.minusYears(2),
                expiryDate = today.plusDays(100)
            )
        )

        val responses = impl.toResponseList(entities)

        assertEquals(2, responses.size)
        assertEquals("CERT-007", responses[0].certificateNumber)
        assertEquals("CERT-008", responses[1].certificateNumber)
    }
}
