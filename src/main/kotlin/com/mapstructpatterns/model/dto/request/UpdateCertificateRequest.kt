package com.mapstructpatterns.dto.request

import java.time.LocalDate

data class UpdateCertificateRequest(
    val certificateNumber: String? = null,

    val issuedBy: String? = null,

    val issueDate: LocalDate? = null,

    val expiryDate: LocalDate? = null,

    val status: String? = null,

    val notes: String? = null
)
