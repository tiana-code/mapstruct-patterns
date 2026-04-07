package com.mapstructpatterns.model.entity

import com.mapstructpatterns.model.enums.CIIRating
import com.mapstructpatterns.model.enums.DataSource
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import java.util.UUID

@Entity
@Table(name = "cii_records")
open class CIIRecord protected constructor() : BaseAuditableEntity() {

    @Column(nullable = false)
    var vesselId: UUID? = null
        protected set

    @Column(nullable = false)
    var calculationYear: Int = 0
        protected set

    @field:NotBlank
    @Column(nullable = false)
    var vesselType: String? = null
        protected set

    @Column(nullable = false)
    var dwt: Double = 0.0
        protected set

    @Column(nullable = false)
    var distanceTravelled: Double = 0.0
        protected set

    @Column(nullable = false)
    var fuelConsumptionMt: Double = 0.0
        protected set

    var attainedCii: Double? = null
        protected set

    var requiredCii: Double? = null
        protected set

    var referenceCii: Double? = null
        protected set

    var ciiRatio: Double? = null
        protected set

    @Enumerated(EnumType.STRING)
    var rating: CIIRating? = null
        protected set

    var boundarySuperior: Double? = null
        protected set

    var boundaryLower: Double? = null
        protected set

    var boundaryUpper: Double? = null
        protected set

    var boundaryInferior: Double? = null
        protected set

    var reductionNeededPercent: Double? = null
        protected set

    constructor(
        vesselId: UUID,
        calculationYear: Int,
        vesselType: String,
        dwt: Double,
        distanceTravelled: Double,
        fuelConsumptionMt: Double,
        userId: UUID? = null,
        isDemoData: Boolean = false,
        dataSource: DataSource = DataSource.REAL
    ) : this() {
        require(vesselType.isNotBlank()) { "Vessel type must not be blank" }
        require(calculationYear > 0) { "Calculation year must be positive" }
        require(dwt > 0) { "DWT must be positive" }
        require(distanceTravelled > 0) { "Distance travelled must be positive" }
        require(fuelConsumptionMt > 0) { "Fuel consumption must be positive" }

        this.vesselId = vesselId
        this.calculationYear = calculationYear
        this.vesselType = vesselType
        this.dwt = dwt
        this.distanceTravelled = distanceTravelled
        this.fuelConsumptionMt = fuelConsumptionMt
        this.userId = userId
        this.isDemoData = isDemoData
        this.dataSource = dataSource
    }

    fun updateFromCalculation(
        attainedCii: Double,
        requiredCii: Double,
        referenceCii: Double,
        ciiRatio: Double,
        rating: CIIRating,
        boundarySuperior: Double,
        boundaryLower: Double,
        boundaryUpper: Double,
        boundaryInferior: Double,
        reductionNeededPercent: Double
    ) {
        require(attainedCii > 0) { "Attained CII must be positive" }
        require(requiredCii > 0) { "Required CII must be positive" }

        this.attainedCii = attainedCii
        this.requiredCii = requiredCii
        this.referenceCii = referenceCii
        this.ciiRatio = ciiRatio
        this.rating = rating
        this.boundarySuperior = boundarySuperior
        this.boundaryLower = boundaryLower
        this.boundaryUpper = boundaryUpper
        this.boundaryInferior = boundaryInferior
        this.reductionNeededPercent = reductionNeededPercent
    }
}
