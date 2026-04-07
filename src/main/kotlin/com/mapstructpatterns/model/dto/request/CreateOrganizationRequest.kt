package com.mapstructpatterns.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class CreateOrganizationRequest(
    @field:NotBlank
    val name: String,

    @field:NotBlank
    val code: String,

    @field:Email
    val contactEmail: String?,

    val contactPhone: String? = null,

    val address: String? = null
)
