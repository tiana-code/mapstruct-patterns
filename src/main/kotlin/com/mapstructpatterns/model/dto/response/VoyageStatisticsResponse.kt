package com.mapstructpatterns.dto.response

data class VoyageStatisticsResponse(
    val totalDistanceNm: Double?,

    val totalFuelConsumptionMt: Double?,

    val avgSpeedKnots: Double?,

    val maxSpeedKnots: Double?,

    val minSpeedKnots: Double?,

    val avgSfoc: Double?,

    val sailingHours: Double?,

    val dataPointsCount: Int
)
