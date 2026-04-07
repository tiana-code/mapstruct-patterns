package com.mapstructpatterns.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.Instant
import java.util.UUID

data class CreateCargoShipmentRequest(
    @field:NotNull
    val vesselId: UUID,

    @field:NotBlank
    val billOfLading: String,

    val containerIds: List<String>? = null,

    @field:NotBlank
    val originPortName: String,

    @field:NotBlank
    val destinationPortName: String,

    @field:NotBlank
    val cargoType: String,

    val weightTons: Double? = null,

    val etd: Instant? = null,

    val eta: Instant? = null
)
