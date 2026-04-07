package com.mapstructpatterns.dto.request

data class UpdateOrganizationRequest(
    val name: String? = null,

    val contactEmail: String? = null,

    val contactPhone: String? = null,

    val address: String? = null
)
