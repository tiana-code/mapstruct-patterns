package com.mapstructpatterns.dto.response

import java.util.UUID

data class OrganizationSummaryResponse(
    val id: UUID,

    val name: String,

    val code: String
)
