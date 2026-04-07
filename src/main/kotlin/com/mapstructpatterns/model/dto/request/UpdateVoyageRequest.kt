package com.mapstructpatterns.dto.request

import com.mapstructpatterns.model.enums.VoyageStatus
import java.time.Instant

data class UpdateVoyageRequest(
    val voyageNumber: String? = null,

    val departurePort: String? = null,

    val arrivalPort: String? = null,

    val departureTime: Instant? = null,

    val arrivalTime: Instant? = null,

    val status: VoyageStatus? = null
)
