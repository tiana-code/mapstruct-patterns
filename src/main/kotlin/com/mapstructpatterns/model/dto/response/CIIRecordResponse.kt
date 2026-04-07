package com.mapstructpatterns.dto.response

import com.mapstructpatterns.model.enums.CIIRating
import com.mapstructpatterns.model.enums.DataSource
import java.time.Instant
import java.util.UUID

data class CIIRecordResponse(
    val id: UUID?,

    val vesselId: UUID,

    val calculationYear: Int,

    val vesselType: String,

    val dwt: Double,

    val distanceTravelled: Double,

    val fuelConsumptionMt: Double,

    val attainedCii: Double?,

    val requiredCii: Double?,

    val referenceCii: Double?,

    val ciiRatio: Double?,

    val rating: CIIRating?,

    val boundarySuperior: Double?,

    val boundaryLower: Double?,

    val boundaryUpper: Double?,

    val boundaryInferior: Double?,

    val reductionNeededPercent: Double?,

    val isDemoData: Boolean,

    val dataSource: DataSource,

    val createdAt: Instant?,

    val updatedAt: Instant?
)
