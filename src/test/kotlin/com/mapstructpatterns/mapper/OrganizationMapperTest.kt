package com.mapstructpatterns.mapper

import com.mapstructpatterns.dto.request.CreateOrganizationRequest
import com.mapstructpatterns.dto.request.UpdateOrganizationRequest
import com.mapstructpatterns.dto.response.SubscriptionSummaryResponse
import com.mapstructpatterns.model.entity.Organization
import com.mapstructpatterns.model.entity.Subscription
import com.mapstructpatterns.model.entity.TariffPlan
import com.mapstructpatterns.model.enums.SubscriptionStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class OrganizationMapperTest {

    @Test
    fun `toEntity creates Organization with uppercased code`() {
        val mapper = object : OrganizationMapper() {
            override fun toSubscriptionSummary(entity: Subscription) = error("stub")
        }

        val request = CreateOrganizationRequest(
            name = "Acme Shipping",
            code = "acme",
            contactEmail = "info@acme.com"
        )

        val entity = mapper.toEntity(request)

        assertEquals("Acme Shipping", entity.name)
        assertEquals("ACME", entity.code)
        assertEquals("info@acme.com", entity.contactEmail)
    }

    @Test
    fun `toDto maps active subscription from entity`() {
        val mapper = object : OrganizationMapper() {
            override fun toSubscriptionSummary(entity: Subscription) =
                SubscriptionSummaryResponse(
                    id = entity.id,
                    tariffPlanName = entity.tariffPlan?.name,
                    status = entity.status,
                    startDate = entity.startDate,
                    endDate = entity.endDate
                )
        }

        val org = Organization(
            name = "Acme Shipping",
            code = "acme"
        )
        val plan = TariffPlan("Enterprise")
        val sub = Subscription(tariffPlan = plan, status = SubscriptionStatus.ACTIVE)
        org.addSubscription(sub)

        val response = mapper.toDto(org, usersCount = 5, vesselsCount = 12)

        assertEquals("Acme Shipping", response.name)
        assertEquals("ACME", response.code)
        assertEquals(5, response.usersCount)
        assertEquals(12, response.vesselsCount)
        assertNotNull(response.activeSubscription)
        assertEquals(SubscriptionStatus.ACTIVE, response.activeSubscription?.status)
    }

    @Test
    fun `toDto returns null activeSubscription when no active subscription`() {
        val mapper = object : OrganizationMapper() {
            override fun toSubscriptionSummary(entity: Subscription) = error("stub")
        }

        val org = Organization(
            name = "Acme Shipping",
            code = "acme"
        )
        val plan = TariffPlan("Enterprise")
        val sub = Subscription(tariffPlan = plan, status = SubscriptionStatus.EXPIRED)
        org.addSubscription(sub)

        val response = mapper.toDto(org)

        assertNull(response.activeSubscription)
    }

    @Test
    fun `updateEntity delegates to entity update`() {
        val mapper = object : OrganizationMapper() {
            override fun toSubscriptionSummary(entity: Subscription) = error("stub")
        }

        val org = Organization(
            name = "Old Name",
            code = "old",
            contactEmail = "old@example.com"
        )

        mapper.updateEntity(org, UpdateOrganizationRequest(name = "New Name"))

        assertEquals("New Name", org.name)
        assertEquals("old@example.com", org.contactEmail)
    }
}
