package com.mapstructpatterns.model.entity

import com.mapstructpatterns.model.enums.AuditResult
import com.mapstructpatterns.model.enums.AuditType
import com.mapstructpatterns.model.enums.DataSource
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "audits")
open class Audit protected constructor() : BaseAuditableEntity() {

    @field:NotNull
    @Column(nullable = false)
    var vesselId: UUID? = null
        protected set

    @field:NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var auditType: AuditType? = null
        protected set

    @field:NotNull
    @Column(nullable = false)
    var auditDate: LocalDate? = null
        protected set

    @field:NotBlank
    @Column(nullable = false)
    var port: String? = null
        protected set

    @field:NotBlank
    @Column(nullable = false)
    var inspectorName: String? = null
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var result: AuditResult = AuditResult.PASS
        protected set

    @Column(nullable = false)
    var detained: Boolean = false
        protected set

    @Column(nullable = false)
    var active: Boolean = true
        protected set

    @OneToMany(mappedBy = "audit", cascade = [CascadeType.ALL], orphanRemoval = true)
    protected val findingsInternal: MutableList<AuditFinding> = mutableListOf()

    val findings: List<AuditFinding>
        get() = findingsInternal.toList()

    val deficienciesCount: Int
        get() = findingsInternal.size

    constructor(
        vesselId: UUID,
        auditType: AuditType,
        auditDate: LocalDate,
        port: String,
        inspectorName: String,
        userId: UUID,
        isDemoData: Boolean = false,
        dataSource: DataSource = DataSource.REAL
    ) : this() {
        require(port.isNotBlank()) { "Port must not be blank" }
        require(inspectorName.isNotBlank()) { "Inspector name must not be blank" }

        this.vesselId = vesselId
        this.auditType = auditType
        this.auditDate = auditDate
        this.port = port
        this.inspectorName = inspectorName
        this.userId = userId
        this.isDemoData = isDemoData
        this.dataSource = dataSource
    }

    fun updateDetails(
        auditDate: LocalDate? = null,
        port: String? = null,
        inspectorName: String? = null
    ) {
        auditDate?.let { this.auditDate = it }
        port?.let {
            require(it.isNotBlank()) { "Port must not be blank" }
            this.port = it
        }
        inspectorName?.let {
            require(it.isNotBlank()) { "Inspector name must not be blank" }
            this.inspectorName = it
        }
    }

    fun addFinding(finding: AuditFinding) {
        if (findingsInternal.contains(finding)) return
        finding.assignAudit(this)
        findingsInternal.add(finding)
        recalculateResult()
    }

    fun removeFinding(finding: AuditFinding) {
        if (findingsInternal.remove(finding)) {
            finding.clearAudit()
            recalculateResult()
        }
    }

    fun markDetained() {
        detained = true
        result = AuditResult.DETAINED
    }

    fun markReleased() {
        detained = false
        recalculateResult()
    }

    fun deactivate() {
        active = false
    }

    protected fun recalculateResult() {
        result = when {
            detained -> AuditResult.DETAINED
            deficienciesCount > 0 -> AuditResult.PASS_WITH_DEFICIENCIES
            else -> AuditResult.PASS
        }
    }
}
