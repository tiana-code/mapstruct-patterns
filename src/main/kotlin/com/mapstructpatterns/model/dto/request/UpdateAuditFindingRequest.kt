package com.mapstructpatterns.dto.request

import com.mapstructpatterns.model.enums.FindingSeverity
import com.mapstructpatterns.model.enums.FindingStatus
import java.time.LocalDate

data class UpdateAuditFindingRequest(
    val description: String? = null,

    val severity: FindingSeverity? = null,

    val status: FindingStatus? = null,

    val dueDate: LocalDate? = null,

    val closedDate: LocalDate? = null,

    val verifiedBy: String? = null,

    val evidenceUrl: String? = null
)
