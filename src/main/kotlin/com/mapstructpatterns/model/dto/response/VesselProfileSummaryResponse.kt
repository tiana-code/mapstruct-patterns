package com.mapstructpatterns.dto.response

import java.util.UUID

data class VesselProfileSummaryResponse(
    val id: UUID?,

    val profileCode: String,

    val vesselName: String,

    val imoNumber: String?,

    val vesselType: String,

    val flag: String?
)
