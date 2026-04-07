package com.mapstructpatterns.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.Instant
import java.util.UUID

data class CreateVoyageRequest(
    @field:NotNull
    val vesselId: UUID,

    @field:NotBlank
    val voyageNumber: String,

    @field:NotBlank
    val departurePort: String,

    @field:NotBlank
    val arrivalPort: String,

    val departureTime: Instant? = null
)
