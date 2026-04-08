package com.mapstructpatterns.policy

import com.mapstructpatterns.model.enums.FindingStatus
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class AuditFindingStatusPolicy {

    fun isOverdue(status: FindingStatus?, dueDate: Instant?, referenceDate: Instant = Instant.now()): Boolean {
        if (status == null || dueDate == null) return false
        return status != FindingStatus.CLOSED
                && status != FindingStatus.VERIFIED
                && referenceDate.isAfter(dueDate)
    }
}
