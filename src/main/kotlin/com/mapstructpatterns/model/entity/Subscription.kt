package com.mapstructpatterns.model.entity

import com.mapstructpatterns.model.enums.SubscriptionStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "subscriptions")
open class Subscription protected constructor() {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    var organization: Organization? = null
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tariff_plan_id", nullable = false)
    var tariffPlan: TariffPlan? = null
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: SubscriptionStatus = SubscriptionStatus.ACTIVE
        protected set

    var startDate: LocalDate? = null
        protected set

    var endDate: LocalDate? = null
        protected set

    constructor(
        tariffPlan: TariffPlan,
        status: SubscriptionStatus = SubscriptionStatus.ACTIVE,
        startDate: LocalDate? = null,
        endDate: LocalDate? = null
    ) : this() {
        this.tariffPlan = tariffPlan
        this.status = status
        this.startDate = startDate
        this.endDate = endDate
    }

    internal fun attachToOrganization(organization: Organization) {
        this.organization = organization
    }

    internal fun detachFromOrganization() {
        this.organization = null
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is Subscription) return false
        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}
