package com.mapstructpatterns.model.entity

import com.mapstructpatterns.model.enums.DataSource
import com.mapstructpatterns.model.enums.FindingSeverity
import com.mapstructpatterns.model.enums.FindingStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import jakarta.persistence.Table
import jakarta.persistence.Version
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.Instant
import java.time.LocalDate
import java.util.UUID


@Entity
@Table(name = "audit_findings")
open class AuditFinding protected constructor() {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null
        protected set

    @field:ManyToOne(fetch = FetchType.LAZY)
    @field:JoinColumn(name = "audit_id", nullable = false)
    var audit: Audit? = null
        protected set

    @field:NotBlank
    @Column(nullable = false)
    var code: String? = null
        protected set

    @field:NotBlank
    @Column(nullable = false)
    var description: String? = null
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var severity: FindingSeverity = FindingSeverity.MINOR
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: FindingStatus = FindingStatus.OPEN
        protected set

    var dueDate: LocalDate? = null
        protected set

    var closedDate: LocalDate? = null
        protected set

    var verifiedBy: String? = null
        protected set

    var evidenceUrl: String? = null
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

    constructor(
        code: String,
        description: String,
        userId: UUID,
        severity: FindingSeverity = FindingSeverity.MINOR,
        dueDate: LocalDate? = null
    ) : this() {
        require(code.isNotBlank()) { "Code must not be blank" }
        require(description.isNotBlank()) { "Description must not be blank" }

        this.code = code
        this.description = description
        this.userId = userId
        this.severity = severity
        this.dueDate = dueDate
    }

    fun changeDescription(newDescription: String) {
        require(newDescription.isNotBlank()) { "Description must not be blank" }
        description = newDescription
    }

    fun changeSeverity(newSeverity: FindingSeverity) {
        severity = newSeverity
    }

    fun reschedule(newDueDate: LocalDate?) {
        dueDate = newDueDate
    }

    fun attachEvidence(newEvidenceUrl: String?) {
        require(newEvidenceUrl == null || newEvidenceUrl.isNotBlank()) {
            "Evidence URL must not be blank"
        }
        evidenceUrl = newEvidenceUrl
    }

    fun close(closedDate: LocalDate, verifiedBy: String? = null) {
        require(status != FindingStatus.CLOSED) { "Finding is already closed" }
        require(verifiedBy == null || verifiedBy.isNotBlank()) {
            "Verified by must not be blank"
        }

        status = FindingStatus.CLOSED
        this.closedDate = closedDate
        this.verifiedBy = verifiedBy
    }

    fun reopen() {
        status = FindingStatus.OPEN
        closedDate = null
        verifiedBy = null
    }

    internal fun assignAudit(audit: Audit) {
        this.audit = audit
    }

    internal fun clearAudit() {
        this.audit = null
    }

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
        if (other == null || other !is AuditFinding) return false
        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}
