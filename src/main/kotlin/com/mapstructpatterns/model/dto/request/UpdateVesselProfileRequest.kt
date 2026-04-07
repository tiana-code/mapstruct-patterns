package com.mapstructpatterns.dto.request

data class UpdateVesselProfileRequest(
    val vesselName: String? = null,

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
