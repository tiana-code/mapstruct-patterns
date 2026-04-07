package com.mapstructpatterns.dto.response

import com.mapstructpatterns.model.enums.CIIRating
import java.util.UUID

data class CIICalculationResponse(
    val vesselId: UUID,

    val calculationYear: Int,

    val vesselType: String,

    val dwt: Double,

    val attainedCii: Double,

    val requiredCii: Double,

    val referenceCii: Double,

    val ciiRatio: Double,

    val rating: CIIRating,

    val ratingDescription: String,

    val boundarySuperior: Double,

    val boundaryLower: Double,

    val boundaryUpper: Double,

    val boundaryInferior: Double,

    val reductionNeededPercent: Double,

    val recommendation: String?
)
