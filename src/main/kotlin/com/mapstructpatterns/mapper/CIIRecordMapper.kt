package com.mapstructpatterns.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import com.mapstructpatterns.dto.request.CalculateCIIRequest
import com.mapstructpatterns.dto.response.CIICalculationResponse
import com.mapstructpatterns.dto.response.CIIRecordResponse
import com.mapstructpatterns.model.CIIComputationResult
import com.mapstructpatterns.model.CIIRatingPolicy
import com.mapstructpatterns.model.entity.CIIRecord

/**
 * Pattern 3: Kotlin extension function on a MapStruct mapper + enum `when` dispatch
 *
 * THE PROBLEM WITH COMPLEX POST-PROCESSING:
 *   Sometimes the response requires business logic that combines the request AND a calculated
 *   result object — two sources that MapStruct can't merge automatically. Rather than forcing
 *   everything into @AfterMapping hooks, define an extension function outside the interface.
 *   This keeps the mapper interface clean while making the logic easy to test in isolation.
 *
 * ALSO SHOWN HERE:
 *   - Manual toEntity: entity uses protected set + domain constructor
 *   - Manual updateEntityFromCalculation: delegates to entity.updateFromCalculation()
 *   - CIIComputationResult extracted to its own model class (not nested in mapper)
 *   - CIIRatingPolicy extracts domain interpretation (description/recommendation) from mapper
 *   - The is-prefix fix: isDemoData via expression
 */
@Mapper(componentModel = "spring")
abstract class CIIRecordMapper {

    @Mapping(target = "isDemoData", expression = "java(entity.isDemoData())")
    abstract fun toResponse(entity: CIIRecord): CIIRecordResponse

    abstract fun toResponseList(entities: List<CIIRecord>): List<CIIRecordResponse>

    fun toEntity(request: CalculateCIIRequest): CIIRecord = CIIRecord(
        vesselId = request.vesselId,
        calculationYear = request.calculationYear,
        vesselType = request.vesselType,
        dwt = request.dwt,
        distanceTravelled = request.distanceTravelled,
        fuelConsumptionMt = request.fuelConsumptionMt
    )

    fun updateEntityFromCalculation(result: CIIComputationResult, entity: CIIRecord) {
        entity.updateFromCalculation(
            attainedCii = result.attainedCii,
            requiredCii = result.requiredCii,
            referenceCii = result.referenceCii,
            ciiRatio = result.ciiRatio,
            rating = result.rating,
            boundarySuperior = result.boundarySuperior,
            boundaryLower = result.boundaryLower,
            boundaryUpper = result.boundaryUpper,
            boundaryInferior = result.boundaryInferior,
            reductionNeededPercent = result.reductionNeededPercent
        )
    }
}

/**
 * Extension function: combines CalculateCIIRequest + CIIComputationResult -> CIICalculationResponse.
 * Lives outside the class to keep the mapper uncluttered. Called from the service layer.
 */
fun CIIRecordMapper.toCalculationResponse(
    request: CalculateCIIRequest,
    result: CIIComputationResult
): CIICalculationResponse = CIICalculationResponse(
    vesselId = request.vesselId,
    calculationYear = request.calculationYear,
    vesselType = request.vesselType,
    dwt = request.dwt,
    attainedCii = result.attainedCii,
    requiredCii = result.requiredCii,
    referenceCii = result.referenceCii,
    ciiRatio = result.ciiRatio,
    rating = result.rating,
    ratingDescription = CIIRatingPolicy.description(result.rating),
    boundarySuperior = result.boundarySuperior,
    boundaryLower = result.boundaryLower,
    boundaryUpper = result.boundaryUpper,
    boundaryInferior = result.boundaryInferior,
    reductionNeededPercent = result.reductionNeededPercent,
    recommendation = CIIRatingPolicy.recommendation(result.rating)
)
