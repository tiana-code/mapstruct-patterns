package com.mapstructpatterns.dto.response

import com.mapstructpatterns.model.enums.DataSource
import com.mapstructpatterns.model.enums.FindingSeverity
import com.mapstructpatterns.model.enums.FindingStatus
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

data class AuditFindingResponse(
    val id: UUID?,

    val auditId: UUID?,

    val code: String,

    val description: String,

    val severity: FindingSeverity,

    val status: FindingStatus,

    val dueDate: LocalDate?,

    val closedDate: LocalDate?,

    val verifiedBy: String?,

    val evidenceUrl: String?,

    val isOverdue: Boolean,

    val isDemoData: Boolean,

    val dataSource: DataSource,

    val createdAt: Instant?,

    val updatedAt: Instant?
)
