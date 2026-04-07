package com.mapstructpatterns.model.entity

import com.mapstructpatterns.model.enums.DataSource
import com.mapstructpatterns.model.enums.VoyageStatus
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "voyages")
open class Voyage protected constructor() : BaseAuditableEntity() {

    @Column(nullable = false)
    var vesselId: UUID? = null
        protected set

    @field:NotBlank
    @Column(nullable = false)
    var voyageNumber: String? = null
        protected set

    @field:NotBlank
    @Column(nullable = false)
    var departurePort: String? = null
        protected set

    @field:NotBlank
    @Column(nullable = false)
    var arrivalPort: String? = null
        protected set

    var departureTime: Instant? = null
        protected set

    var arrivalTime: Instant? = null
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: VoyageStatus = VoyageStatus.PLANNED
        protected set

    var totalDistanceNm: Double? = null
        protected set

    var totalFuelConsumptionMt: Double? = null
        protected set

    var avgSpeedKnots: Double? = null
        protected set

    var maxSpeedKnots: Double? = null
        protected set

    var minSpeedKnots: Double? = null
        protected set

    var avgSfoc: Double? = null
        protected set

    @OneToMany(mappedBy = "voyage", cascade = [CascadeType.ALL], orphanRemoval = true)
    protected val pointsInternal: MutableList<VoyagePoint> = mutableListOf()

    val points: List<VoyagePoint>
        get() = pointsInternal.toList()

    @OneToMany(mappedBy = "voyage", cascade = [CascadeType.ALL], orphanRemoval = true)
    protected val eventsInternal: MutableList<VoyageEvent> = mutableListOf()

    val events: List<VoyageEvent>
        get() = eventsInternal.toList()

    constructor(
        vesselId: UUID,
        voyageNumber: String,
        departurePort: String,
        arrivalPort: String,
        departureTime: Instant? = null,
        userId: UUID? = null,
        isDemoData: Boolean = false,
        dataSource: DataSource = DataSource.REAL
    ) : this() {
        require(voyageNumber.isNotBlank()) { "Voyage number must not be blank" }
        require(departurePort.isNotBlank()) { "Departure port must not be blank" }
        require(arrivalPort.isNotBlank()) { "Arrival port must not be blank" }

        this.vesselId = vesselId
        this.voyageNumber = voyageNumber
        this.departurePort = departurePort
        this.arrivalPort = arrivalPort
        this.departureTime = departureTime
        this.userId = userId
        this.isDemoData = isDemoData
        this.dataSource = dataSource
    }

    fun updateDetails(
        voyageNumber: String? = null,
        departurePort: String? = null,
        arrivalPort: String? = null,
        departureTime: Instant? = null,
        arrivalTime: Instant? = null,
        status: VoyageStatus? = null
    ) {
        voyageNumber?.let {
            require(it.isNotBlank()) { "Voyage number must not be blank" }
            this.voyageNumber = it
        }
        departurePort?.let {
            require(it.isNotBlank()) { "Departure port must not be blank" }
            this.departurePort = it
        }
        arrivalPort?.let {
            require(it.isNotBlank()) { "Arrival port must not be blank" }
            this.arrivalPort = it
        }
        departureTime?.let { this.departureTime = it }
        arrivalTime?.let { this.arrivalTime = it }
        status?.let { this.status = it }
    }

    fun updateStatistics(
        totalDistanceNm: Double? = null,
        totalFuelConsumptionMt: Double? = null,
        avgSpeedKnots: Double? = null,
        maxSpeedKnots: Double? = null,
        minSpeedKnots: Double? = null,
        avgSfoc: Double? = null
    ) {
        totalDistanceNm?.let {
            require(it >= 0) { "Total distance must not be negative" }
            this.totalDistanceNm = it
        }
        totalFuelConsumptionMt?.let {
            require(it >= 0) { "Total fuel consumption must not be negative" }
            this.totalFuelConsumptionMt = it
        }
        avgSpeedKnots?.let {
            require(it >= 0) { "Average speed must not be negative" }
            this.avgSpeedKnots = it
        }
        maxSpeedKnots?.let { this.maxSpeedKnots = it }
        minSpeedKnots?.let { this.minSpeedKnots = it }
        avgSfoc?.let { this.avgSfoc = it }
    }

    fun addPoint(point: VoyagePoint) {
        point.attachToVoyage(this)
        pointsInternal.add(point)
    }

    fun addEvent(event: VoyageEvent) {
        event.attachToVoyage(this)
        eventsInternal.add(event)
    }
}
