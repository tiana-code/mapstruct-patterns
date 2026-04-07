package com.mapstructpatterns.dto.request

import com.mapstructpatterns.model.enums.AuditResult
import java.time.LocalDate

data class UpdateAuditRequest(
    val auditDate: LocalDate? = null,

    val port: String? = null,

    val inspectorName: String? = null,

    val result: AuditResult? = null,

    val detained: Boolean? = null
)
