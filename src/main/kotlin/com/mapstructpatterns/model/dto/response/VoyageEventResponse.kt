package com.mapstructpatterns.dto.response

import com.mapstructpatterns.model.enums.DataSource
import com.mapstructpatterns.model.enums.VoyageEventType
import java.time.Instant
import java.util.UUID

data class VoyageEventResponse(
    val id: UUID,

    val voyageId: UUID,

    val vesselId: UUID,

    val eventType: VoyageEventType,

    val eventTime: Instant?,

    val latitude: Double?,

    val longitude: Double?,

    val description: String?,

    val metadata: Map<String, Any>?,

    val isDemoData: Boolean,

    val dataSource: DataSource,

    val userId: UUID?,

    val createdAt: Instant?
)
