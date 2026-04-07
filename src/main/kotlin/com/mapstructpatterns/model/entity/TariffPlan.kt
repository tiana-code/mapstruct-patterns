package com.mapstructpatterns.model.entity

import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.Table
import java.math.BigDecimal
import java.util.UUID

@Entity
@Table(name = "tariff_plans")
open class TariffPlan protected constructor() {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null
        protected set

    @Column(nullable = false)
    var name: String? = null
        protected set

    @Column(nullable = false)
    var priceMonthly: BigDecimal = BigDecimal.ZERO
        protected set

    @Column(nullable = false)
    var vesselLimit: Int = 10
        protected set

    @Column(nullable = false)
    var userLimit: Int = 5
        protected set

    @ElementCollection
    @CollectionTable(name = "tariff_plan_features", joinColumns = [JoinColumn(name = "tariff_plan_id")])
    @Column(name = "feature")
    protected val featuresInternal: MutableList<String> = mutableListOf()

    val features: List<String>
        get() = featuresInternal.toList()

    constructor(
        name: String,
        priceMonthly: BigDecimal = BigDecimal.ZERO,
        vesselLimit: Int = 10,
        userLimit: Int = 5
    ) : this() {
        this.name = name
        this.priceMonthly = priceMonthly
        this.vesselLimit = vesselLimit
        this.userLimit = userLimit
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is TariffPlan) return false
        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}
