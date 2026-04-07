package com.mapstructpatterns.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.NullValuePropertyMappingStrategy
import com.mapstructpatterns.dto.request.CreateCertificateRequest
import com.mapstructpatterns.dto.request.UpdateCertificateRequest
import com.mapstructpatterns.dto.response.CertificateResponse
import com.mapstructpatterns.model.entity.Certificate
import com.mapstructpatterns.model.enums.CertificateStatus
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.UUID

/**
 * Pattern 2: Abstract class mapper with computed/derived fields
 *
 * Use `abstract class` instead of `interface` when you need helper methods
 * that are referenced from `expression = "java(...)"`. Interface default
 * methods work too, but abstract class gives cleaner access to `this`.
 *
 * SHOWN HERE:
 *   - Derived fields: daysUntilExpiry and isExpiringSoon computed at mapping time
 *   - expression = "java(calculateDaysUntilExpiry(entity))" calls a Kotlin method
 *   - The `is`-prefix fix applied to isDemoData
 *   - Manual toEntity/updateEntity: entity uses protected set + domain constructor
 *   - vesselName is ignored on entity->response (enriched later from a separate service call)
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
abstract class CertificateMapper {

    @Mapping(target = "vesselName", ignore = true)
    @Mapping(target = "daysUntilExpiry", expression = "java(calculateDaysUntilExpiry(entity))")
    @Mapping(target = "isExpiringSoon", expression = "java(isExpiringSoon(entity))")
    @Mapping(target = "isDemoData", expression = "java(entity.isDemoData())")
    abstract fun toResponse(entity: Certificate): CertificateResponse

    abstract fun toResponseList(entities: List<Certificate>): List<CertificateResponse>

    fun toEntity(request: CreateCertificateRequest, userId: UUID? = null): Certificate = Certificate(
        vesselId = request.vesselId,
        certificateType = request.certificateType,
        certificateNumber = request.certificateNumber,
        issuedBy = request.issuedBy,
        issueDate = request.issueDate,
        expiryDate = request.expiryDate,
        notes = request.notes,
        userId = userId
    )

    fun updateEntity(request: UpdateCertificateRequest, entity: Certificate) {
        entity.update(
            certificateNumber = request.certificateNumber,
            issuedBy = request.issuedBy,
            issueDate = request.issueDate,
            expiryDate = request.expiryDate,
            status = request.status?.let {
                try {
                    CertificateStatus.valueOf(it)
                } catch (_: Exception) {
                    null
                }
            },
            notes = request.notes
        )
    }

    fun calculateDaysUntilExpiry(entity: Certificate): Long =
        calculateDaysUntilExpiry(entity, LocalDate.now())

    fun calculateDaysUntilExpiry(entity: Certificate, currentDate: LocalDate): Long =
        ChronoUnit.DAYS.between(currentDate, entity.expiryDate)

    fun isExpiringSoon(entity: Certificate): Boolean =
        isExpiringSoon(entity, LocalDate.now())

    fun isExpiringSoon(entity: Certificate, currentDate: LocalDate): Boolean {
        val daysUntilExpiry = calculateDaysUntilExpiry(entity, currentDate)
        return daysUntilExpiry in 0..30
    }
}
