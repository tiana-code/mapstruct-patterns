package com.mapstructpatterns.dto.response

import com.mapstructpatterns.model.enums.DataSource
import com.mapstructpatterns.model.enums.VoyageStatus
import java.time.Instant
import java.util.UUID

data class VoyageResponse(
    val id: UUID,

    val vesselId: UUID,

    val voyageNumber: String,

    val departurePort: String,

    val arrivalPort: String,

    val departureTime: Instant?,

    val arrivalTime: Instant?,

    val status: VoyageStatus,

    val statistics: VoyageStatisticsResponse?,

    val isDemoData: Boolean,

    val dataSource: DataSource,

    val userId: UUID?,

    val createdAt: Instant?,

    val updatedAt: Instant?
)
