package com.mapstructpatterns.mapper

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.mapstruct.Mapper
import com.mapstructpatterns.dto.request.CreateVoyageEventRequest
import com.mapstructpatterns.dto.response.VoyageEventResponse
import com.mapstructpatterns.model.entity.Voyage
import com.mapstructpatterns.model.entity.VoyageEvent
import org.springframework.beans.factory.annotation.Autowired
import java.time.Instant

/**
 * Pattern 6: Spring-managed dependency inside a mapper for JSON conversion
 *
 * WHEN TO INJECT INTO A MAPPER:
 *   When mapping requires a Spring-managed service — here an ObjectMapper to serialize/deserialize
 *   a JSON metadata field stored as String in the entity but exposed as Map<String,Any> in the DTO.
 *
 * HOW IT WORKS:
 *   MapStruct with componentModel = "spring" generates a @Component class.
 *   Using `abstract class` allows manual methods to reuse the injected ObjectMapper.
 *
 * ALSO SHOWN HERE:
 *   - Two-argument toEntity(request, parentEntity): entity created via domain constructor,
 *     then attached to parent voyage
 *   - JSON round-trip: metadata stored as String entity field, deserialized on read
 *   - Silent fallback: malformed JSON metadata returns emptyMap() instead of throwing
 *   - isDemoData inherited from parent voyage (not from request)
 */
@Mapper(componentModel = "spring")
abstract class VoyageEventMapper {

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    fun toEntity(
        request: CreateVoyageEventRequest,
        voyage: Voyage,
        createdAt: Instant = Instant.now()
    ): VoyageEvent = VoyageEvent(
        vesselId = requireNotNull(voyage.vesselId) { "Voyage.vesselId must not be null" },
        eventType = request.eventType,
        eventTime = request.eventTime,
        latitude = request.latitude,
        longitude = request.longitude,
        description = request.description,
        metadata = request.metadata?.let { objectMapper.writeValueAsString(it) },
        isDemoData = voyage.isDemoData,
        dataSource = voyage.dataSource,
        userId = voyage.userId,
        createdAt = createdAt
    ).also { event ->
        event.attachToVoyage(voyage)
    }

    fun toResponse(event: VoyageEvent): VoyageEventResponse = VoyageEventResponse(
        id = requireNotNull(event.id) { "VoyageEvent.id must not be null for response mapping" },
        voyageId = requireNotNull(requireNotNull(event.voyage) { "VoyageEvent.voyage must not be null" }.id) { "Voyage.id must not be null" },
        vesselId = requireNotNull(event.vesselId) { "VoyageEvent.vesselId must not be null" },
        eventType = event.eventType,
        eventTime = event.eventTime,
        latitude = event.latitude,
        longitude = event.longitude,
        description = event.description,
        metadata = event.metadata?.let {
            try {
                objectMapper.readValue<Map<String, Any>>(it)
            } catch (_: Exception) {
                emptyMap()
            }
        },
        isDemoData = event.isDemoData,
        dataSource = event.dataSource,
        userId = event.userId,
        createdAt = event.createdAt
    )
}
