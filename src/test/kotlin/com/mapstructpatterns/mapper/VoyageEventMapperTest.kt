package com.mapstructpatterns.mapper

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mapstructpatterns.dto.request.CreateVoyageEventRequest
import com.mapstructpatterns.model.entity.Voyage
import com.mapstructpatterns.model.enums.DataSource
import com.mapstructpatterns.model.enums.VoyageEventType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.UUID

class VoyageEventMapperTest {

    private val codec = MetadataCodec(jacksonObjectMapper())

    private val mapper = object : VoyageEventMapper() {}.also {
        val field = VoyageEventMapper::class.java.getDeclaredField("metadataCodec")
        field.isAccessible = true
        field.set(it, codec)
    }

    private fun testVoyage(): Voyage = Voyage(
        vesselId = UUID.randomUUID(),
        voyageNumber = "V-001",
        departurePort = "Rotterdam",
        arrivalPort = "Hamburg",
        userId = UUID.randomUUID()
    )

    @Test
    fun `toEntity creates VoyageEvent with metadata serialized`() {
        val voyage = testVoyage()
        val request = CreateVoyageEventRequest(
            eventType = VoyageEventType.DEPARTURE,
            eventTime = Instant.parse("2026-04-05T08:00:00Z"),
            latitude = 51.9,
            longitude = 4.5,
            description = "Left port",
            metadata = mapOf("pilot" to "J. Smith", "tugboats" to 2)
        )

        val event = mapper.toEntity(request, voyage)

        assertEquals(VoyageEventType.DEPARTURE, event.eventType)
        assertEquals(51.9, event.latitude)
        assertNotNull(event.metadata)
        assertTrue(event.metadata!!.contains("pilot"))
        assertEquals(voyage, event.voyage)
        assertEquals(voyage.vesselId, event.vesselId)
    }

    @Test
    fun `toEntity inherits isDemoData and dataSource from voyage`() {
        val voyage = Voyage(
            vesselId = UUID.randomUUID(),
            voyageNumber = "V-002",
            departurePort = "Singapore",
            arrivalPort = "Shanghai",
            isDemoData = true,
            dataSource = DataSource.DEMO
        )
        val request = CreateVoyageEventRequest(
            eventType = VoyageEventType.ANCHOR,
            eventTime = Instant.now()
        )

        val event = mapper.toEntity(request, voyage)

        assertTrue(event.isDemoData)
        assertEquals(DataSource.DEMO, event.dataSource)
    }

    @Test
    fun `toEntity handles null metadata`() {
        val voyage = testVoyage()
        val request = CreateVoyageEventRequest(
            eventType = VoyageEventType.BUNKERING,
            eventTime = Instant.now()
        )

        val event = mapper.toEntity(request, voyage)

        assertNull(event.metadata)
    }

    @Test
    fun `MetadataCodec deserializes valid JSON`() {
        val json = """{"key":"value","count":42}"""
        val result = codec.deserialize(json)

        assertNotNull(result)
        assertEquals("value", result!!["key"])
        assertEquals(42, result["count"])
    }

    @Test
    fun `MetadataCodec returns empty map for malformed JSON`() {
        val result = codec.deserialize("{broken json")

        assertNotNull(result)
        assertTrue(result!!.isEmpty())
    }

    @Test
    fun `MetadataCodec returns null for null input`() {
        assertNull(codec.deserialize(null))
    }

    @Test
    fun `MetadataCodec serializes map to JSON`() {
        val json = codec.serialize(mapOf("a" to 1, "b" to "two"))

        assertNotNull(json)
        assertTrue(json!!.contains("\"a\""))
        assertTrue(json.contains("\"two\""))
    }

    @Test
    fun `generated impl maps voyage event correctly`() {
        val voyageEventMapperImpl = VoyageEventMapperImpl().also {
            val field = VoyageEventMapper::class.java.getDeclaredField("metadataCodec")
            field.isAccessible = true
            field.set(it, codec)
        }
        val voyage = testVoyage()
        val request = CreateVoyageEventRequest(
            eventType = VoyageEventType.DEPARTURE,
            eventTime = Instant.parse("2026-04-05T08:00:00Z"),
            latitude = 51.9,
            longitude = 4.5,
            description = "Left port",
            metadata = mapOf("pilot" to "J. Smith")
        )

        val event = voyageEventMapperImpl.toEntity(request, voyage)

        assertEquals(VoyageEventType.DEPARTURE, event.eventType)
        assertEquals(51.9, event.latitude)
        assertNotNull(event.metadata)
        assertTrue(event.metadata!!.contains("pilot"))
        assertEquals(voyage, event.voyage)
    }
}
