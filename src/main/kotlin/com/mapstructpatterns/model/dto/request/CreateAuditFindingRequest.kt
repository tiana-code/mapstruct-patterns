package com.mapstructpatterns.dto.request

import com.mapstructpatterns.model.enums.FindingSeverity
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

data class CreateAuditFindingRequest(
    @field:NotBlank
    val code: String,

    @field:NotBlank
    val description: String,

    val severity: FindingSeverity = FindingSeverity.MINOR,

    val dueDate: LocalDate? = null
)
