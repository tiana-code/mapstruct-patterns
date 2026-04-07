package com.mapstructpatterns.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
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
@Table(name = "voyage_points")
open class VoyagePoint protected constructor() {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voyage_id", nullable = false)
    var voyage: Voyage? = null
        protected set

    @Column(nullable = false)
    var latitude: Double = 0.0
        protected set

    @Column(nullable = false)
    var longitude: Double = 0.0
        protected set

    var speedKnots: Double? = null
        protected set

    var headingDeg: Double? = null
        protected set

    var timestamp: Instant? = null
        protected set

    constructor(
        latitude: Double = 0.0,
        longitude: Double = 0.0,
        speedKnots: Double? = null,
        headingDeg: Double? = null,
        timestamp: Instant? = null
    ) : this() {
        this.latitude = latitude
        this.longitude = longitude
        this.speedKnots = speedKnots
        this.headingDeg = headingDeg
        this.timestamp = timestamp
    }

    internal fun attachToVoyage(voyage: Voyage) {
        this.voyage = voyage
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is VoyagePoint) return false
        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}
