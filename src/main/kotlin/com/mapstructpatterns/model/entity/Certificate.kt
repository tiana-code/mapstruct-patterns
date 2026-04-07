package com.mapstructpatterns.model.entity

import com.mapstructpatterns.model.enums.CertificateStatus
import com.mapstructpatterns.model.enums.DataSource
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "certificates")
open class Certificate protected constructor() : BaseAuditableEntity() {

    @field:NotNull
    @Column(nullable = false)
    var vesselId: UUID? = null
        protected set

    @field:NotBlank
    @Column(nullable = false)
    var certificateType: String? = null
        protected set

    @field:NotBlank
    @Column(nullable = false)
    var certificateNumber: String? = null
        protected set

    @field:NotBlank
    @Column(nullable = false)
    var issuedBy: String? = null
        protected set

    @field:NotNull
    @Column(nullable = false)
    var issueDate: LocalDate? = null
        protected set

    @field:NotNull
    @Column(nullable = false)
    var expiryDate: LocalDate? = null
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: CertificateStatus = CertificateStatus.VALID
        protected set

    var notes: String? = null
        protected set

    @Column(nullable = false)
    var active: Boolean = true
        protected set

    constructor(
        vesselId: UUID,
        certificateType: String,
        certificateNumber: String,
        issuedBy: String,
        issueDate: LocalDate,
        expiryDate: LocalDate,
        notes: String? = null,
        userId: UUID? = null,
        isDemoData: Boolean = false,
        dataSource: DataSource = DataSource.REAL
    ) : this() {
        require(certificateType.isNotBlank()) { "Certificate type must not be blank" }
        require(certificateNumber.isNotBlank()) { "Certificate number must not be blank" }
        require(issuedBy.isNotBlank()) { "Issued by must not be blank" }
        require(!expiryDate.isBefore(issueDate)) { "Expiry date must not be before issue date" }

        this.vesselId = vesselId
        this.certificateType = certificateType
        this.certificateNumber = certificateNumber
        this.issuedBy = issuedBy
        this.issueDate = issueDate
        this.expiryDate = expiryDate
        this.notes = notes
        this.userId = userId
        this.isDemoData = isDemoData
        this.dataSource = dataSource
    }

    fun update(
        certificateNumber: String? = null,
        issuedBy: String? = null,
        issueDate: LocalDate? = null,
        expiryDate: LocalDate? = null,
        status: CertificateStatus? = null,
        notes: String? = null
    ) {
        certificateNumber?.let {
            require(it.isNotBlank()) { "Certificate number must not be blank" }
            this.certificateNumber = it
        }
        issuedBy?.let {
            require(it.isNotBlank()) { "Issued by must not be blank" }
            this.issuedBy = it
        }
        issueDate?.let { this.issueDate = it }
        expiryDate?.let { this.expiryDate = it }
        status?.let { this.status = it }
        notes?.let { this.notes = it }
    }

    fun suspend() {
        status = CertificateStatus.SUSPENDED
    }

    fun withdraw() {
        status = CertificateStatus.WITHDRAWN
    }
}
