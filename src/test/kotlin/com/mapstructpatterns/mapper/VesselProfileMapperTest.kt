package com.mapstructpatterns.mapper

import com.mapstructpatterns.dto.request.UpdateVesselProfileRequest
import com.mapstructpatterns.model.entity.VesselProfile
import com.mapstructpatterns.model.enums.DataSource
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.UUID

class VesselProfileMapperTest {
    private val mapper: VesselProfileMapper = VesselProfileMapperImpl()

    @Test
    fun `toResponse maps isActive and isDemoData correctly via is-prefix workaround`() {
        val entity = VesselProfile(
            vesselId = UUID.randomUUID(),
            profileCode = "PRF-001",
            vesselName = "MV Example",
            vesselType = "CARGO",
            userId = UUID.randomUUID()
        )

        val response = mapper.toResponse(entity)

        assertTrue(response.isActive)
        assertFalse(response.isDemoData)
        assertEquals(DataSource.REAL, response.dataSource)
    }

    @Test
    fun `toResponse maps isDemoData true correctly`() {
        val entity = VesselProfile(
            vesselId = UUID.randomUUID(),
            profileCode = "PRF-002",
            vesselName = "MV Demo",
            vesselType = "TANKER",
            userId = UUID.randomUUID(),
            isDemoData = true,
            dataSource = DataSource.DEMO
        )
        entity.deactivate()

        val response = mapper.toResponse(entity)

        assertFalse(response.isActive)
        assertTrue(response.isDemoData)
        assertEquals(DataSource.DEMO, response.dataSource)
    }

    @Test
    fun `updateEntity ignores null fields from request`() {
        val entity = VesselProfile(
            vesselId = UUID.randomUUID(),
            profileCode = "PRF-003",
            vesselName = "Original Name",
            vesselType = "CARGO",
            flag = "PA",
            dwt = 50000.0
        )

        val request = UpdateVesselProfileRequest(vesselName = "Updated Name")
        mapper.updateEntity(request, entity)

        assertEquals("Updated Name", entity.vesselName)
        assertEquals("PA", entity.flag)
        assertEquals(50000.0, entity.dwt)
    }
}
