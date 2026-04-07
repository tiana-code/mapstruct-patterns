package com.mapstructpatterns.mapper

import org.mapstruct.Mapper
import com.mapstructpatterns.dto.request.CreateVoyageRequest
import com.mapstructpatterns.dto.request.UpdateVoyageRequest
import com.mapstructpatterns.dto.response.VoyageResponse
import com.mapstructpatterns.dto.response.VoyageStatisticsResponse
import com.mapstructpatterns.model.entity.Voyage
import java.time.Duration
import java.time.Instant
import java.util.UUID

/**
 * Pattern 5: Abstract class mapper with manual response mapping and explicit server-side defaults
 *
 * WHEN TO USE MANUAL MAPPING:
 *   When the response DTO includes a nested sub-object (VoyageStatisticsResponse) computed from
 *   multiple fields of the entity, MapStruct cannot auto-derive it. A manual toResponse() method
 *   is cleaner than forcing @AfterMapping that mutates a partially-built DTO.
 *
 * ALSO SHOWN HERE:
 *   - Manual toEntity: entity uses protected set + domain constructor
 *   - Manual update method with null-safety via entity.updateDetails()
 *   - toStatisticsResponse() null-guards: returns null if no statistics exist yet
 *   - sailingHours can be evaluated against an explicit reference time for deterministic tests
 */
@Mapper(componentModel = "spring")
abstract class VoyageMapper {

    fun toEntity(request: CreateVoyageRequest, userId: UUID? = null): Voyage = Voyage(
        vesselId = request.vesselId,
        voyageNumber = request.voyageNumber,
        departurePort = request.departurePort,
        arrivalPort = request.arrivalPort,
        departureTime = request.departureTime,
        userId = userId
    )

    fun toResponse(voyage: Voyage): VoyageResponse = VoyageResponse(
        id = requireNotNull(voyage.id) { "Voyage.id must not be null for response mapping" },
        vesselId = requireNotNull(voyage.vesselId) { "Voyage.vesselId must not be null" },
        voyageNumber = requireNotNull(voyage.voyageNumber) { "Voyage.voyageNumber must not be null" },
        departurePort = requireNotNull(voyage.departurePort) { "Voyage.departurePort must not be null" },
        arrivalPort = requireNotNull(voyage.arrivalPort) { "Voyage.arrivalPort must not be null" },
        departureTime = voyage.departureTime,
        arrivalTime = voyage.arrivalTime,
        status = voyage.status,
        statistics = toStatisticsResponse(voyage),
        isDemoData = voyage.isDemoData,
        dataSource = voyage.dataSource,
        userId = voyage.userId,
        createdAt = voyage.createdAt,
        updatedAt = voyage.updatedAt
    )

    fun toStatisticsResponse(voyage: Voyage, referenceTime: Instant = Instant.now()): VoyageStatisticsResponse? {
        if (voyage.totalDistanceNm == null && voyage.avgSpeedKnots == null) return null

        val sailingHours = if (voyage.departureTime != null && voyage.arrivalTime != null) {
            Duration.between(voyage.departureTime, voyage.arrivalTime).toHours().toDouble()
        } else if (voyage.departureTime != null) {
            Duration.between(voyage.departureTime, referenceTime).toHours().toDouble()
        } else {
            null
        }

        return VoyageStatisticsResponse(
            totalDistanceNm = voyage.totalDistanceNm,
            totalFuelConsumptionMt = voyage.totalFuelConsumptionMt,
            avgSpeedKnots = voyage.avgSpeedKnots,
            maxSpeedKnots = voyage.maxSpeedKnots,
            minSpeedKnots = voyage.minSpeedKnots,
            avgSfoc = voyage.avgSfoc,
            sailingHours = sailingHours,
            dataPointsCount = voyage.points.size
        )
    }

    fun updateFromRequest(voyage: Voyage, request: UpdateVoyageRequest): Voyage {
        voyage.updateDetails(
            voyageNumber = request.voyageNumber,
            departurePort = request.departurePort,
            arrivalPort = request.arrivalPort,
            departureTime = request.departureTime,
            arrivalTime = request.arrivalTime,
            status = request.status
        )
        return voyage
    }
}
