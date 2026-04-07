package com.mapstructpatterns.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.util.UUID

data class CreateVesselProfileRequest(
    @field:NotNull
    val vesselId: UUID,

    @field:NotBlank
    val profileCode: String,

    @field:NotBlank
    val vesselName: String,

    val imoNumber: String? = null,

    val mmsi: String? = null,

    @field:NotBlank
    val vesselType: String,

    val flag: String? = null,

    val grossTonnage: Double? = null,

    val netTonnage: Double? = null,

    val dwt: Double? = null,

    val lengthOverall: Double? = null,

    val beam: Double? = null,

    val draft: Double? = null,

    val yearBuilt: Int? = null,

    val engineType: String? = null,

    val enginePowerKw: Int? = null
)
