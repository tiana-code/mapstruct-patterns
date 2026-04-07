package com.mapstructpatterns.dto.response

import com.mapstructpatterns.model.enums.DataSource
import java.time.Instant
import java.util.UUID

data class CargoTrackingResponse(
    val id: UUID?,

    val vesselId: UUID,

    val billOfLading: String,

    val containerIds: String?,

    val originPort: String,

    val destinationPort: String,

    val cargoType: String,

    val weightTons: Double?,

    val etd: Instant?,

    val eta: Instant?,

    val ata: Instant?,

    val status: String,

    val statusDescription: String,

    val dataSource: DataSource
)
