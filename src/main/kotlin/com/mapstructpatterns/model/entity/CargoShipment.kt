package com.mapstructpatterns.model.entity

import com.mapstructpatterns.model.enums.DataSource
import com.mapstructpatterns.model.enums.ShipmentStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "cargo_shipments")
open class CargoShipment protected constructor() : BaseAuditableEntity() {

    @Column(nullable = false)
    var vesselId: UUID? = null
        protected set

    @field:NotBlank
    @Column(nullable = false)
    var billOfLading: String? = null
        protected set

    var containerIds: String? = null
        protected set

    @field:NotBlank
    @Column(nullable = false)
    var originPortName: String? = null
        protected set

    @field:NotBlank
    @Column(nullable = false)
    var destinationPortName: String? = null
        protected set

    var originPortLat: Double? = null
        protected set

    var originPortLon: Double? = null
        protected set

    var destinationPortLat: Double? = null
        protected set

    var destinationPortLon: Double? = null
        protected set

    @field:NotBlank
    @Column(nullable = false)
    var cargoType: String? = null
        protected set

    var weightTons: Double? = null
        protected set

    var etd: Instant? = null
        protected set

    var eta: Instant? = null
        protected set

    var ata: Instant? = null
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: ShipmentStatus = ShipmentStatus.BOOKED
        protected set

    constructor(
        vesselId: UUID,
        billOfLading: String,
        originPortName: String,
        destinationPortName: String,
        cargoType: String,
        containerIds: String? = null,
        weightTons: Double? = null,
        etd: Instant? = null,
        eta: Instant? = null,
        status: ShipmentStatus = ShipmentStatus.BOOKED,
        userId: UUID? = null,
        isDemoData: Boolean = false,
        dataSource: DataSource = DataSource.REAL
    ) : this() {
        require(billOfLading.isNotBlank()) { "Bill of lading must not be blank" }
        require(originPortName.isNotBlank()) { "Origin port must not be blank" }
        require(destinationPortName.isNotBlank()) { "Destination port must not be blank" }
        require(cargoType.isNotBlank()) { "Cargo type must not be blank" }
        require(weightTons == null || weightTons > 0) { "Weight must be positive" }

        this.vesselId = vesselId
        this.billOfLading = billOfLading
        this.originPortName = originPortName
        this.destinationPortName = destinationPortName
        this.cargoType = cargoType
        this.containerIds = containerIds
        this.weightTons = weightTons
        this.etd = etd
        this.eta = eta
        this.status = status
        this.userId = userId
        this.isDemoData = isDemoData
        this.dataSource = dataSource
    }

    fun transitionTo(newStatus: ShipmentStatus) {
        this.status = newStatus
    }

    fun recordArrival(ata: Instant) {
        this.ata = ata
        this.status = ShipmentStatus.ARRIVED
    }
}
