package com.mapstructpatterns.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate
import java.util.UUID

data class CreateCertificateRequest(
    @field:NotNull
    val vesselId: UUID,

    @field:NotBlank
    val certificateType: String,

    @field:NotBlank
    val certificateNumber: String,

    @field:NotBlank
    val issuedBy: String,

    @field:NotNull
    val issueDate: LocalDate,

    @field:NotNull
    val expiryDate: LocalDate,

    val notes: String? = null
)
