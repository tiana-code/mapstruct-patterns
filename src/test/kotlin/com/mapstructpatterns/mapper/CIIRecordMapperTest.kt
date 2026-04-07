package com.mapstructpatterns.mapper

import com.mapstructpatterns.dto.request.CalculateCIIRequest
import com.mapstructpatterns.dto.response.CIIRecordResponse
import com.mapstructpatterns.model.entity.CIIRecord
import com.mapstructpatterns.model.CIIComputationResult
import com.mapstructpatterns.model.enums.CIIRating
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.util.UUID

class CIIRecordMapperTest {
    private val mapper = object : CIIRecordMapper() {
        override fun toResponse(entity: CIIRecord): CIIRecordResponse = error("stub")
        override fun toResponseList(entities: List<CIIRecord>): List<CIIRecordResponse> = error("stub")
    }

    @Test
    fun `toCalculationResponse produces null recommendation for rating A`() {
        val request = CalculateCIIRequest(
            vesselId = UUID.randomUUID(),
            calculationYear = 2024,
            vesselType = "TANKER",
            dwt = 80000.0,
            distanceTravelled = 45000.0,
            fuelConsumptionMt = 1800.0
        )
        val result = CIIComputationResult(
            attainedCii = 4.5,
            requiredCii = 7.2,
            referenceCii = 7.5,
            ciiRatio = 0.625,
            rating = CIIRating.A,
            boundarySuperior = 5.4,
            boundaryLower = 6.48,
            boundaryUpper = 7.56,
            boundaryInferior = 8.64,
            reductionNeededPercent = 0.0
        )

        val response = mapper.toCalculationResponse(request, result)

        assertNull(response.recommendation)
        assertEquals(CIIRating.A, response.rating)
    }

    @Test
    fun `toCalculationResponse produces SEEMP recommendation for rating D`() {
        val request = CalculateCIIRequest(
            vesselId = UUID.randomUUID(),
            calculationYear = 2024,
            vesselType = "CARGO",
            dwt = 30000.0,
            distanceTravelled = 20000.0,
            fuelConsumptionMt = 2200.0
        )
        val result = CIIComputationResult(
            attainedCii = 9.8,
            requiredCii = 7.2,
            referenceCii = 7.5,
            ciiRatio = 1.36,
            rating = CIIRating.D,
            boundarySuperior = 5.4,
            boundaryLower = 6.48,
            boundaryUpper = 7.56,
            boundaryInferior = 8.64,
            reductionNeededPercent = 26.5
        )

        val response = mapper.toCalculationResponse(request, result)

        assertNotNull(response.recommendation)
        assertEquals(CIIRating.D, response.rating)
        assertEquals(
            "Develop and implement a Ship Energy Efficiency Management Plan (SEEMP) Part III",
            response.recommendation
        )
    }

    @Test
    fun `toCalculationResponse rating descriptions cover all 5 CII ratings`() {
        val ratings = CIIRating.values()
        assertEquals(5, ratings.size)

        val descriptions = mapOf(
            CIIRating.A to "Superior performance",
            CIIRating.B to "Lower performance",
            CIIRating.C to "Moderate performance",
            CIIRating.D to "Inferior performance",
            CIIRating.E to "Significantly inferior"
        )

        descriptions.forEach { (rating, expectedPrefix) ->
            val request = CalculateCIIRequest(
                vesselId = UUID.randomUUID(),
                calculationYear = 2024,
                vesselType = "CARGO",
                dwt = 30000.0,
                distanceTravelled = 20000.0,
                fuelConsumptionMt = 2000.0
            )
            val result = CIIComputationResult(
                attainedCii = 7.0, requiredCii = 7.2, referenceCii = 7.5, ciiRatio = 0.97,
                rating = rating, boundarySuperior = 5.4, boundaryLower = 6.48,
                boundaryUpper = 7.56, boundaryInferior = 8.64, reductionNeededPercent = 0.0
            )

            val response = mapper.toCalculationResponse(request, result)
            assertEquals(rating, response.rating)
            assertEquals(expectedPrefix, response.ratingDescription.substringBefore(" -"))
        }
    }
}
