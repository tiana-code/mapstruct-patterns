package com.mapstructpatterns.dto.request

import com.mapstructpatterns.model.enums.VoyageEventType
import jakarta.validation.constraints.NotNull
import java.time.Instant

data class CreateVoyageEventRequest(
    @field:NotNull
    val eventType: VoyageEventType,

    @field:NotNull
    val eventTime: Instant,

    val latitude: Double? = null,

    val longitude: Double? = null,

    val description: String? = null,

    val metadata: Map<String, Any>? = null
)
