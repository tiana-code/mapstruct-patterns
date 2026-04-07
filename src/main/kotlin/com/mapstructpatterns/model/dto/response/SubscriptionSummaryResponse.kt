package com.mapstructpatterns.dto.response

import com.mapstructpatterns.model.enums.SubscriptionStatus
import java.time.LocalDate
import java.util.UUID

data class SubscriptionSummaryResponse(
    val id: UUID?,

    val tariffPlanName: String?,

    val status: SubscriptionStatus,

    val startDate: LocalDate?,

    val endDate: LocalDate?
)
