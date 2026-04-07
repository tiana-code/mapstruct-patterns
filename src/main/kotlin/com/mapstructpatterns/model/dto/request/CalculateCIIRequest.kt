package com.mapstructpatterns.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.util.UUID

data class CalculateCIIRequest(
    @field:NotNull
    val vesselId: UUID,

    @field:Positive val
    calculationYear: Int,

    @field:NotBlank val
    vesselType: String,

    @field:Positive val
    dwt: Double,

    @field:Positive val
    distanceTravelled: Double,

    @field:Positive val
    fuelConsumptionMt: Double
)
