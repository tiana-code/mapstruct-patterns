package com.mapstructpatterns.dto.response

import com.mapstructpatterns.model.enums.DataSource
import java.time.Instant
import java.util.UUID

data class OrganizationResponse(
    val id: UUID?,

    val name: String,

    val code: String,

    val contactEmail: String?,

    val contactPhone: String?,

    val address: String?,

    val logoUrl: String?,

    val usersCount: Int,

    val vesselsCount: Int,

    val activeSubscription: SubscriptionSummaryResponse?,

    val isDemoData: Boolean,

    val dataSource: DataSource,

    val createdAt: Instant?,

    val updatedAt: Instant?
)
