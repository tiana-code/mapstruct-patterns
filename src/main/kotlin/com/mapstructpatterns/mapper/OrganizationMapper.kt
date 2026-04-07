package com.mapstructpatterns.mapper

import org.mapstruct.Mapper
import org.mapstruct.NullValuePropertyMappingStrategy
import com.mapstructpatterns.dto.request.CreateOrganizationRequest
import com.mapstructpatterns.dto.request.UpdateOrganizationRequest
import com.mapstructpatterns.dto.response.OrganizationResponse
import com.mapstructpatterns.dto.response.OrganizationSummaryResponse
import com.mapstructpatterns.dto.response.SubscriptionSummaryResponse
import com.mapstructpatterns.model.entity.Organization
import com.mapstructpatterns.model.entity.Subscription

/**
 * Pattern 7: Manual enrichment + delegated sub-mapper + active subscription lookup
 *
 * MANUAL toDto():
 *   When the response needs additional parameters not present in the entity (usersCount,
 *   vesselsCount), a fully manual method is cleaner than @AfterMapping with injected services.
 *
 * DELEGATED SUB-MAPPER:
 *   OrganizationMapper depends on SubscriptionMapper to map the active subscription.
 *   Instead of duplicating mapping logic, delegate through an abstract helper method.
 *
 * ALSO SHOWN HERE:
 *   - Manual toEntity: entity uses protected set + domain constructor,
 *     code uppercasing happens in the constructor itself (not @AfterMapping)
 *   - Active subscription lookup delegated to entity.activeSubscription()
 *   - Manual updateEntity delegates to entity.update()
 */
@Mapper(
    componentModel = "spring",
    uses = [SubscriptionMapper::class],
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
abstract class OrganizationMapper {
    protected abstract fun toSubscriptionSummary(entity: Subscription): SubscriptionSummaryResponse

    fun toDto(entity: Organization, usersCount: Int = 0, vesselsCount: Int = 0): OrganizationResponse {
        return OrganizationResponse(
            id = entity.id,
            name = requireNotNull(entity.name) { "Organization.name must not be null for mapping" },
            code = requireNotNull(entity.code) { "Organization.code must not be null for mapping" },
            contactEmail = entity.contactEmail,
            contactPhone = entity.contactPhone,
            address = entity.address,
            logoUrl = entity.logoUrl,
            usersCount = usersCount,
            vesselsCount = vesselsCount,
            activeSubscription = entity.activeSubscription()?.let(::toSubscriptionSummary),
            isDemoData = entity.isDemoData,
            dataSource = entity.dataSource,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    fun toSummaryDto(entity: Organization): OrganizationSummaryResponse = OrganizationSummaryResponse(
        id = requireNotNull(entity.id) { "Organization.id must not be null for summary mapping" },
        name = requireNotNull(entity.name) { "Organization.name must not be null for summary mapping" },
        code = requireNotNull(entity.code) { "Organization.code must not be null for summary mapping" }
    )

    fun toEntity(request: CreateOrganizationRequest): Organization = Organization(
        name = request.name,
        code = request.code,
        contactEmail = request.contactEmail,
        contactPhone = request.contactPhone,
        address = request.address
    )

    fun updateEntity(entity: Organization, request: UpdateOrganizationRequest) {
        entity.update(
            name = request.name,
            contactEmail = request.contactEmail,
            contactPhone = request.contactPhone,
            address = request.address
        )
    }
}

@Mapper(componentModel = "spring")
abstract class SubscriptionMapper {

    fun toSummaryDto(entity: Subscription): SubscriptionSummaryResponse = SubscriptionSummaryResponse(
        id = entity.id,
        tariffPlanName = entity.tariffPlan?.name,
        status = entity.status,
        startDate = entity.startDate,
        endDate = entity.endDate
    )
}
