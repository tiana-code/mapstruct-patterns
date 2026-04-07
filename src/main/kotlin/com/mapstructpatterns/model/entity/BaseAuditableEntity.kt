package com.mapstructpatterns.model.entity

import com.mapstructpatterns.model.enums.DataSource
import jakarta.persistence.Column
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import jakarta.persistence.Version
import jakarta.validation.constraints.NotNull
import java.time.Instant
import java.util.UUID

@MappedSuperclass
abstract class BaseAuditableEntity protected constructor() {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null
        protected set

    @Column(nullable = false)
    var isDemoData: Boolean = false
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var dataSource: DataSource = DataSource.REAL
        protected set

    @field:NotNull
    @Column(nullable = false)
    var userId: UUID? = null
        protected set

    @Version
    var version: Long? = null
        protected set

    @Column(nullable = false, updatable = false)
    var createdAt: Instant? = null
        protected set

    @Column(nullable = false)
    var updatedAt: Instant? = null
        protected set

    @PrePersist
    protected fun onCreate() {
        val now = Instant.now()
        createdAt = now
        updatedAt = now
    }

    @PreUpdate
    protected fun onUpdate() {
        updatedAt = Instant.now()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as BaseAuditableEntity
        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}
