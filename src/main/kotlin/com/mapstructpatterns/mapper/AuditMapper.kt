package com.mapstructpatterns.mapper

import org.mapstruct.IterableMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named
import org.mapstruct.NullValuePropertyMappingStrategy
import com.mapstructpatterns.dto.request.CreateAuditFindingRequest
import com.mapstructpatterns.dto.request.CreateAuditRequest
import com.mapstructpatterns.dto.request.UpdateAuditFindingRequest
import com.mapstructpatterns.dto.request.UpdateAuditRequest
import com.mapstructpatterns.dto.response.AuditFindingResponse
import com.mapstructpatterns.dto.response.AuditResponse
import com.mapstructpatterns.model.entity.Audit
import com.mapstructpatterns.model.entity.AuditFinding
import com.mapstructpatterns.model.enums.FindingStatus
import java.time.LocalDate
import java.util.UUID

/**
 * Pattern 4: @IterableMapping + @Named method variants + nested entity mapping
 *
 * THE PROBLEM:
 *   A list endpoint must return AuditResponse WITHOUT findings (to avoid N+1 loading),
 *   but a detail endpoint returns AuditResponse WITH findings. Both use the same DTO.
 *   MapStruct would default to calling toResponse() for all elements in toResponseList().
 *
 * THE FIX:
 *   - Define two named variants: @Named("toResponseWithFindings") and @Named("toResponseWithoutFindings")
 *   - Use @IterableMapping(qualifiedByName = [...]) to control which variant the list method uses
 *
 * ALSO SHOWN HERE:
 *   - Nested entity mapping: toFindingResponse / toFindingResponseList for AuditFinding
 *   - @Mapping(target = "auditId", source = "audit.id") — navigating nested source paths
 *   - isOverdue() computed field: checks status + dueDate together
 *   - Three is-prefix fixes: isDemoData, isDetained, isOverdue
 *   - Manual toEntity/updateEntity: entity uses protected set + domain constructor,
 *     so MapStruct cannot auto-generate entity creation — done manually via constructor
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
abstract class AuditMapper {

    @Named("toResponseWithFindings")
    @Mapping(target = "vesselName", ignore = true)
    @Mapping(target = "findings", source = "findings")
    @Mapping(target = "isDemoData", expression = "java(entity.isDemoData())")
    @Mapping(target = "isDetained", expression = "java(entity.getDetained())")
    abstract fun toResponse(entity: Audit): AuditResponse

    @Named("toResponseWithoutFindings")
    @Mapping(target = "vesselName", ignore = true)
    @Mapping(target = "findings", ignore = true)
    @Mapping(target = "isDemoData", expression = "java(entity.isDemoData())")
    @Mapping(target = "isDetained", expression = "java(entity.getDetained())")
    abstract fun toResponseWithoutFindings(entity: Audit): AuditResponse

    @IterableMapping(qualifiedByName = ["toResponseWithoutFindings"])
    abstract fun toResponseList(entities: List<Audit>): List<AuditResponse>

    fun toEntity(request: CreateAuditRequest, userId: UUID): Audit = Audit(
        vesselId = request.vesselId,
        auditType = request.auditType,
        auditDate = request.auditDate,
        port = request.port,
        inspectorName = request.inspectorName,
        userId = userId
    ).also { audit ->
        if (request.detained) audit.markDetained()
    }

    fun updateEntity(request: UpdateAuditRequest, entity: Audit) {
        entity.updateDetails(
            auditDate = request.auditDate,
            port = request.port,
            inspectorName = request.inspectorName
        )
        request.detained?.let { detained ->
            if (detained) entity.markDetained() else entity.markReleased()
        }
    }

    @Mapping(target = "auditId", source = "audit.id")
    @Mapping(target = "isOverdue", expression = "java(isOverdue(entity))")
    @Mapping(target = "isDemoData", expression = "java(entity.isDemoData())")
    abstract fun toFindingResponse(entity: AuditFinding): AuditFindingResponse

    abstract fun toFindingResponseList(entities: List<AuditFinding>): List<AuditFindingResponse>

    fun toFindingEntity(request: CreateAuditFindingRequest, userId: UUID): AuditFinding = AuditFinding(
        code = request.code,
        description = request.description,
        userId = userId,
        severity = request.severity,
        dueDate = request.dueDate
    )

    fun updateFindingEntity(request: UpdateAuditFindingRequest, entity: AuditFinding) {
        request.description?.let { entity.changeDescription(it) }
        request.severity?.let { entity.changeSeverity(it) }
        request.dueDate?.let { entity.reschedule(it) }
        request.evidenceUrl?.let { entity.attachEvidence(it) }
        request.status?.let { status ->
            when (status) {
                FindingStatus.CLOSED -> entity.close(
                    request.closedDate ?: LocalDate.now(),
                    request.verifiedBy
                )

                FindingStatus.OPEN -> entity.reopen()
                else -> {}
            }
        }
    }

    fun isOverdue(entity: AuditFinding): Boolean =
        isOverdue(entity, LocalDate.now())

    fun isOverdue(entity: AuditFinding, today: LocalDate): Boolean =
        entity.dueDate != null &&
                entity.dueDate!!.isBefore(today) &&
                entity.status == FindingStatus.OPEN
}
