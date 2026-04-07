package com.mapstructpatterns.dto.response

import com.mapstructpatterns.model.enums.CertificateStatus
import com.mapstructpatterns.model.enums.DataSource
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

data class CertificateResponse(
    val id: UUID?,

    val vesselId: UUID,

    val vesselName: String?,

    val certificateType: String,

    val certificateNumber: String,

    val issuedBy: String,

    val issueDate: LocalDate,

    val expiryDate: LocalDate,

    val daysUntilExpiry: Long,

    val isExpiringSoon: Boolean,

    val status: CertificateStatus,

    val notes: String?,

    val isDemoData: Boolean,

    val dataSource: DataSource,

    val createdAt: Instant?,

    val updatedAt: Instant?
)
