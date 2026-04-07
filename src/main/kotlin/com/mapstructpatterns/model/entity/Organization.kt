package com.mapstructpatterns.model.entity

import com.mapstructpatterns.model.enums.DataSource
import com.mapstructpatterns.model.enums.SubscriptionStatus
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank

@Entity
@Table(name = "organizations")
open class Organization protected constructor() : BaseAuditableEntity() {

    @field:NotBlank
    @Column(nullable = false)
    var name: String? = null
        protected set

    @field:NotBlank
    @Column(nullable = false, unique = true)
    var code: String? = null
        protected set

    var contactEmail: String? = null
        protected set

    var contactPhone: String? = null
        protected set

    var address: String? = null
        protected set

    var logoUrl: String? = null
        protected set

    @OneToMany(mappedBy = "organization", cascade = [CascadeType.ALL], orphanRemoval = true)
    protected val subscriptionsInternal: MutableList<Subscription> = mutableListOf()

    val subscriptions: List<Subscription>
        get() = subscriptionsInternal.toList()

    constructor(
        name: String,
        code: String,
        contactEmail: String? = null,
        contactPhone: String? = null,
        address: String? = null,
        isDemoData: Boolean = false,
        dataSource: DataSource = DataSource.REAL
    ) : this() {
        require(name.isNotBlank()) { "Name must not be blank" }
        require(code.isNotBlank()) { "Code must not be blank" }

        this.name = name
        this.code = code.uppercase()
        this.contactEmail = contactEmail
        this.contactPhone = contactPhone
        this.address = address
        this.isDemoData = isDemoData
        this.dataSource = dataSource
    }

    fun update(
        name: String? = null,
        contactEmail: String? = null,
        contactPhone: String? = null,
        address: String? = null
    ) {
        name?.let {
            require(it.isNotBlank()) { "Name must not be blank" }
            this.name = it
        }
        contactEmail?.let { this.contactEmail = it }
        contactPhone?.let { this.contactPhone = it }
        address?.let { this.address = it }
    }

    fun activeSubscription(): Subscription? =
        subscriptionsInternal.find {
            it.status == SubscriptionStatus.ACTIVE || it.status == SubscriptionStatus.TRIAL
        }

    fun addSubscription(subscription: Subscription) {
        subscription.attachToOrganization(this)
        subscriptionsInternal.add(subscription)
    }

    fun removeSubscription(subscription: Subscription) {
        if (subscriptionsInternal.remove(subscription)) {
            subscription.detachFromOrganization()
        }
    }
}
