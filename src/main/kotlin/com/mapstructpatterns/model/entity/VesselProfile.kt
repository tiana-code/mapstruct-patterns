package com.mapstructpatterns.model.entity

import com.mapstructpatterns.model.enums.DataSource
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import java.util.UUID

@Entity
@Table(name = "vessel_profiles")
open class VesselProfile protected constructor() : BaseAuditableEntity() {

    @field:NotBlank
    @Column(nullable = false, unique = true)
    var profileCode: String? = null
        protected set

    @Column(nullable = false)
    var vesselId: UUID? = null
        protected set

    @field:NotBlank
    @Column(nullable = false)
    var vesselName: String? = null
        protected set

    var imoNumber: String? = null
        protected set

    var mmsi: String? = null
        protected set

    @field:NotBlank
    @Column(nullable = false)
    var vesselType: String? = null
        protected set

    var flag: String? = null
        protected set

    var grossTonnage: Double? = null
        protected set

    var netTonnage: Double? = null
        protected set

    var dwt: Double? = null
        protected set

    var lengthOverall: Double? = null
        protected set

    var beam: Double? = null
        protected set

    var draft: Double? = null
        protected set

    var yearBuilt: Int? = null
        protected set

    var engineType: String? = null
        protected set

    var enginePowerKw: Int? = null
        protected set

    @Column(nullable = false)
    var active: Boolean = true
        protected set

    constructor(
        vesselId: UUID,
        profileCode: String,
        vesselName: String,
        vesselType: String,
        imoNumber: String? = null,
        mmsi: String? = null,
        flag: String? = null,
        grossTonnage: Double? = null,
        netTonnage: Double? = null,
        dwt: Double? = null,
        lengthOverall: Double? = null,
        beam: Double? = null,
        draft: Double? = null,
        yearBuilt: Int? = null,
        engineType: String? = null,
        enginePowerKw: Int? = null,
        userId: UUID? = null,
        isDemoData: Boolean = false,
        dataSource: DataSource = DataSource.REAL
    ) : this() {
        require(profileCode.isNotBlank()) { "Profile code must not be blank" }
        require(vesselName.isNotBlank()) { "Vessel name must not be blank" }
        require(vesselType.isNotBlank()) { "Vessel type must not be blank" }
        require(grossTonnage == null || grossTonnage > 0) { "Gross tonnage must be positive" }
        require(netTonnage == null || netTonnage > 0) { "Net tonnage must be positive" }
        require(dwt == null || dwt > 0) { "DWT must be positive" }
        require(lengthOverall == null || lengthOverall > 0) { "Length must be positive" }
        require(beam == null || beam > 0) { "Beam must be positive" }
        require(draft == null || draft > 0) { "Draft must be positive" }
        require(yearBuilt == null || yearBuilt > 1800) { "Year built must be realistic" }
        require(enginePowerKw == null || enginePowerKw > 0) { "Engine power must be positive" }

        this.vesselId = vesselId
        this.profileCode = profileCode
        this.vesselName = vesselName
        this.vesselType = vesselType
        this.imoNumber = imoNumber
        this.mmsi = mmsi
        this.flag = flag
        this.grossTonnage = grossTonnage
        this.netTonnage = netTonnage
        this.dwt = dwt
        this.lengthOverall = lengthOverall
        this.beam = beam
        this.draft = draft
        this.yearBuilt = yearBuilt
        this.engineType = engineType
        this.enginePowerKw = enginePowerKw
        this.userId = userId
        this.isDemoData = isDemoData
        this.dataSource = dataSource
    }

    fun update(
        vesselName: String? = null,
        flag: String? = null,
        grossTonnage: Double? = null,
        netTonnage: Double? = null,
        dwt: Double? = null,
        lengthOverall: Double? = null,
        beam: Double? = null,
        draft: Double? = null,
        yearBuilt: Int? = null,
        engineType: String? = null,
        enginePowerKw: Int? = null
    ) {
        vesselName?.let {
            require(it.isNotBlank()) { "Vessel name must not be blank" }
            this.vesselName = it
        }
        flag?.let { this.flag = it }
        grossTonnage?.let {
            require(it > 0) { "Gross tonnage must be positive" }
            this.grossTonnage = it
        }
        netTonnage?.let {
            require(it > 0) { "Net tonnage must be positive" }
            this.netTonnage = it
        }
        dwt?.let {
            require(it > 0) { "DWT must be positive" }
            this.dwt = it
        }
        lengthOverall?.let {
            require(it > 0) { "Length must be positive" }
            this.lengthOverall = it
        }
        beam?.let {
            require(it > 0) { "Beam must be positive" }
            this.beam = it
        }
        draft?.let {
            require(it > 0) { "Draft must be positive" }
            this.draft = it
        }
        yearBuilt?.let {
            require(it > 1800) { "Year built must be realistic" }
            this.yearBuilt = it
        }
        engineType?.let { this.engineType = it }
        enginePowerKw?.let {
            require(it > 0) { "Engine power must be positive" }
            this.enginePowerKw = it
        }
    }

    fun deactivate() {
        active = false
    }

    fun activate() {
        active = true
    }
}
