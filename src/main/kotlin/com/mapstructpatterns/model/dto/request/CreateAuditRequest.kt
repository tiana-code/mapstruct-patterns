package com.mapstructpatterns.dto.request

import com.mapstructpatterns.model.enums.AuditResult
import com.mapstructpatterns.model.enums.AuditType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate
import java.util.UUID

data class CreateAuditRequest(
    @field:NotNull
    val vesselId: UUID,

    @field:NotNull
    val auditType: AuditType,

    @field:NotNull
    val auditDate: LocalDate,

    @field:NotBlank
    val port: String,

    @field:NotBlank
    val inspectorName: String,

    val result: AuditResult = AuditResult.PASS,

    val detained: Boolean = false
)
