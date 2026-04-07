package com.mapstructpatterns.model.entity

import com.mapstructpatterns.model.enums.DataSource
import com.mapstructpatterns.model.enums.VoyageEventType
import jakarta.persistence.Column
import jakarta.validation.constraints.NotNull
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
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "voyage_events")
open class VoyageEvent protected constructor() {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voyage_id", nullable = false)
    var voyage: Voyage? = null
        protected set

    @field:NotNull
    @Column(nullable = false)
    var vesselId: UUID? = null
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var eventType: VoyageEventType = VoyageEventType.OTHER
        protected set

    var eventTime: Instant? = null
        protected set

    var latitude: Double? = null
        protected set

    var longitude: Double? = null
        protected set

    var description: String? = null
        protected set

    @Column(columnDefinition = "TEXT")
    var metadata: String? = null
        protected set

    @Column(nullable = false)
    var isDemoData: Boolean = false
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var dataSource: DataSource = DataSource.REAL
        protected set

    var userId: UUID? = null
        protected set

    var createdAt: Instant? = null
        protected set

    constructor(
        vesselId: UUID,
        eventType: VoyageEventType,
        eventTime: Instant? = null,
        latitude: Double? = null,
        longitude: Double? = null,
        description: String? = null,
        metadata: String? = null,
        isDemoData: Boolean = false,
        dataSource: DataSource = DataSource.REAL,
        userId: UUID? = null,
        createdAt: Instant? = null
    ) : this() {
        this.vesselId = vesselId
        this.eventType = eventType
        this.eventTime = eventTime
        this.latitude = latitude
        this.longitude = longitude
        this.description = description
        this.metadata = metadata
        this.isDemoData = isDemoData
        this.dataSource = dataSource
        this.userId = userId
        this.createdAt = createdAt
    }

    internal fun attachToVoyage(voyage: Voyage) {
        this.voyage = voyage
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is VoyageEvent) return false
        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}
