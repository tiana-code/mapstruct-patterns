package com.mapstructpatterns.dto.response

import com.mapstructpatterns.model.enums.DataSource
import java.time.Instant
import java.util.UUID

data class VesselProfileResponse(
    val id: UUID?,

    val profileCode: String,

    val vesselId: UUID,

    val vesselName: String,

    val imoNumber: String?,

    val mmsi: String?,

    val vesselType: String,

    val flag: String?,

    val grossTonnage: Double?,

    val netTonnage: Double?,

    val dwt: Double?,

    val lengthOverall: Double?,

    val beam: Double?,

    val draft: Double?,

    val yearBuilt: Int?,

    val engineType: String?,

    val enginePowerKw: Int?,

    val isActive: Boolean,

    val isDemoData: Boolean,

    val dataSource: DataSource,

    val userId: UUID?,

    val createdAt: Instant?,

    val updatedAt: Instant?
)
