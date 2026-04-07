package com.mapstructpatterns.model

import com.mapstructpatterns.model.enums.CIIRating

data class CIIComputationResult(
    val attainedCii: Double,

    val requiredCii: Double,

    val referenceCii: Double,

    val ciiRatio: Double,

    val rating: CIIRating,

    val boundarySuperior: Double,

    val boundaryLower: Double,

    val boundaryUpper: Double,

    val boundaryInferior: Double,

    val reductionNeededPercent: Double
)
