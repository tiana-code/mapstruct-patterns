package com.mapstructpatterns.dto.response

import com.mapstructpatterns.model.enums.AuditResult
import com.mapstructpatterns.model.enums.AuditType
import com.mapstructpatterns.model.enums.DataSource
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

data class AuditResponse(
    val id: UUID?,

    val vesselId: UUID,

    val vesselName: String?,

    val auditType: AuditType,

    val auditDate: LocalDate,

    val port: String,

    val inspectorName: String,

    val result: AuditResult,

    val isDetained: Boolean,

    val deficienciesCount: Int,

    val findings: List<AuditFindingResponse>?,

    val isDemoData: Boolean,

    val dataSource: DataSource,

    val createdAt: Instant?,

    val updatedAt: Instant?
)
