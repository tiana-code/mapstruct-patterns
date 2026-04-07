package com.mapstructpatterns.mapper

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
 *   When mapping requires a Spring-managed service — here a MetadataCodec to serialize/deserialize
 *   a JSON metadata field stored as String in the entity but exposed as Map<String,Any> in the DTO.
 *
 * HOW IT WORKS:
 *   MapStruct with GlobalMapperConfig generates a Spring @Component class.
 *   Constructor injection via the config's injectionStrategy = CONSTRUCTOR.
 *   JSON logic extracted into MetadataCodec — mapper stays focused on shape transformation.
 *
 * ALSO SHOWN HERE:
 *   - Two-argument toEntity(request, parentEntity): entity created via domain constructor,
 *     then attached to parent voyage
 *   - MetadataCodec handles JSON round-trip with logged fallback for malformed data
 *   - isDemoData inherited from parent voyage (not from request)
 */
@Mapper(config = GlobalMapperConfig::class)
abstract class VoyageEventMapper {

    @Autowired
    protected lateinit var metadataCodec: MetadataCodec

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
        metadata = metadataCodec.serialize(request.metadata),
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
        metadata = metadataCodec.deserialize(event.metadata),
        isDemoData = event.isDemoData,
        dataSource = event.dataSource,
        userId = event.userId,
        createdAt = event.createdAt
    )
}
