package com.mapstructpatterns.mapper

import com.mapstructpatterns.model.entity.Audit
import com.mapstructpatterns.model.entity.AuditFinding
import com.mapstructpatterns.model.enums.AuditType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.UUID

class AuditMapperTest {
    private val today: LocalDate = LocalDate.parse("2026-04-05")
    private val testUserId: UUID = UUID.randomUUID()

    private val mapper: AuditMapper = object : AuditMapper() {
        override fun toResponse(entity: Audit) = error("stub")
        override fun toResponseWithoutFindings(entity: Audit) = error("stub")
        override fun toResponseList(entities: List<Audit>) = error("stub")
        override fun toFindingResponse(entity: AuditFinding) = error("stub")
        override fun toFindingResponseList(entities: List<AuditFinding>) = error("stub")
    }

    @Test
    fun `isOverdue returns true for open finding past due date`() {
        val finding = AuditFinding(
            code = "DEF-001",
            description = "Test deficiency",
            userId = testUserId,
            dueDate = today.minusDays(1)
        )

        assertTrue(mapper.isOverdue(finding, today))
    }

    @Test
    fun `isOverdue returns false when finding is closed`() {
        val finding = AuditFinding(
            code = "DEF-002",
            description = "Test deficiency",
            userId = testUserId,
            dueDate = today.minusDays(10)
        )
        finding.close(today.minusDays(5))

        assertFalse(mapper.isOverdue(finding, today))
    }

    @Test
    fun `isOverdue returns false when due date is in the future`() {
        val finding = AuditFinding(
            code = "DEF-003",
            description = "Test deficiency",
            userId = testUserId,
            dueDate = today.plusDays(5)
        )

        assertFalse(mapper.isOverdue(finding, today))
    }

    @Test
    fun `isOverdue returns false when due date is null`() {
        val finding = AuditFinding(
            code = "DEF-004",
            description = "Test deficiency",
            userId = testUserId
        )

        assertFalse(mapper.isOverdue(finding, today))
    }

    @Test
    fun `audit detained flag is reflected via markDetained`() {
        val audit = Audit(
            vesselId = UUID.randomUUID(),
            auditType = AuditType.PSC,
            auditDate = today,
            port = "Rotterdam",
            inspectorName = "John Smith",
            userId = testUserId
        )
        audit.markDetained()

        assertTrue(audit.detained)
    }

    @Test
    fun `generated impl maps toResponseList without findings`() {
        val impl = AuditMapperImpl()

        val audit = Audit(
            vesselId = UUID.randomUUID(),
            auditType = AuditType.PSC,
            auditDate = today,
            port = "Rotterdam",
            inspectorName = "John Smith",
            userId = testUserId
        )

        val responses = impl.toResponseList(listOf(audit))

        assertNotNull(responses)
        assertEquals(1, responses.size)
        assertEquals("Rotterdam", responses[0].port)
        assertFalse(responses[0].isDemoData)
    }
}
