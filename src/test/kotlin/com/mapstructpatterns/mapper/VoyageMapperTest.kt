package com.mapstructpatterns.mapper

import com.mapstructpatterns.dto.request.UpdateVoyageRequest
import com.mapstructpatterns.model.entity.Voyage
import com.mapstructpatterns.model.entity.VoyagePoint
import com.mapstructpatterns.model.enums.VoyageStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.UUID

class VoyageMapperTest {

    private val mapper: VoyageMapper = object : VoyageMapper() {}

    @Test
    fun `toStatisticsResponse returns null when no statistics populated`() {
        val voyage = Voyage(
            vesselId = UUID.randomUUID(),
            voyageNumber = "V-001",
            departurePort = "Rotterdam",
            arrivalPort = "Hamburg"
        )

        val result = mapper.toStatisticsResponse(voyage)

        assertNull(result)
    }

    @Test
    fun `toStatisticsResponse includes dataPointsCount from points list size`() {
        val voyage = Voyage(
            vesselId = UUID.randomUUID(),
            voyageNumber = "V-002",
            departurePort = "Rotterdam",
            arrivalPort = "Hamburg"
        )
        voyage.updateStatistics(totalDistanceNm = 1500.0, avgSpeedKnots = 12.5)
        voyage.addPoint(VoyagePoint())
        voyage.addPoint(VoyagePoint())
        voyage.addPoint(VoyagePoint())

        val result = mapper.toStatisticsResponse(voyage)

        assertNotNull(result)
        assertEquals(3, result!!.dataPointsCount)
        assertEquals(1500.0, result.totalDistanceNm)
    }

    @Test
    fun `toStatisticsResponse calculates sailingHours from departure to arrival`() {
        val now = Instant.parse("2026-04-05T10:00:00Z")
        val departure = now.minusSeconds(3600 * 10) // 10 hours ago
        val arrival = now

        val voyage = Voyage(
            vesselId = UUID.randomUUID(),
            voyageNumber = "V-003",
            departurePort = "Rotterdam",
            arrivalPort = "Hamburg",
            departureTime = departure
        )
        voyage.updateStatistics(totalDistanceNm = 120.0)
        voyage.updateDetails(arrivalTime = arrival)

        val result = mapper.toStatisticsResponse(voyage)

        assertNotNull(result)
        assertEquals(10.0, result!!.sailingHours)
    }

    @Test
    fun `updateFromRequest updates only provided fields`() {
        val voyage = Voyage(
            vesselId = UUID.randomUUID(),
            voyageNumber = "V-001",
            departurePort = "Rotterdam",
            arrivalPort = "Hamburg"
        )

        val request = UpdateVoyageRequest(
            status = VoyageStatus.IN_PROGRESS
        )

        mapper.updateFromRequest(voyage, request)

        assertEquals(VoyageStatus.IN_PROGRESS, voyage.status)
        assertEquals("Rotterdam", voyage.departurePort)
        assertEquals("V-001", voyage.voyageNumber)
    }
}
