package com.mapstructpatterns.mapper

import com.mapstructpatterns.model.entity.CargoShipment
import com.mapstructpatterns.model.enums.ShipmentStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.UUID

class CargoShipmentMapperTest {

    private val mapper: CargoShipmentMapper = object : CargoShipmentMapper() {
        override fun toResponse(entity: CargoShipment) = error("stub")
        override fun toResponseList(entities: List<CargoShipment>) = error("stub")
    }

    @Test
    fun `mapStatusDescription returns correct label for each status`() {
        assertEquals("Shipment booked, awaiting loading", mapper.mapStatusDescription(ShipmentStatus.BOOKED))
        assertEquals("Cargo loaded onto vessel", mapper.mapStatusDescription(ShipmentStatus.LOADED))
        assertEquals("Vessel en route to destination", mapper.mapStatusDescription(ShipmentStatus.IN_TRANSIT))
        assertEquals("Vessel approaching destination port", mapper.mapStatusDescription(ShipmentStatus.APPROACHING))
        assertEquals("Vessel arrived at destination", mapper.mapStatusDescription(ShipmentStatus.ARRIVED))
        assertEquals("Cargo delivered to consignee", mapper.mapStatusDescription(ShipmentStatus.DELIVERED))
        assertEquals("Shipment cancelled", mapper.mapStatusDescription(ShipmentStatus.CANCELLED))
    }

    @Test
    fun `mapStatusDescription covers all ShipmentStatus enum values`() {
        ShipmentStatus.values().forEach { status ->
            val description = mapper.mapStatusDescription(status)
            assert(description.isNotBlank()) { "Missing description for status: $status" }
        }
    }

    @Test
    fun `entity status name is correctly converted to string`() {
        val entity = CargoShipment(
            vesselId = UUID.randomUUID(),
            billOfLading = "BL-001",
            originPortName = "Rotterdam",
            destinationPortName = "Hamburg",
            cargoType = "GENERAL",
            status = ShipmentStatus.IN_TRANSIT
        )

        assertEquals("IN_TRANSIT", entity.status.name)
    }

    @Test
    fun `generated impl maps all fields correctly`() {
        val impl = CargoShipmentMapperImpl()
        val entity = CargoShipment(
            vesselId = UUID.randomUUID(),
            billOfLading = "BL-002",
            originPortName = "Singapore",
            destinationPortName = "Shanghai",
            cargoType = "CONTAINERS",
            status = ShipmentStatus.LOADED
        )

        val response = impl.toResponse(entity)

        assertEquals("Singapore", response.originPort)
        assertEquals("Shanghai", response.destinationPort)
        assertEquals("LOADED", response.status)
        assertEquals("Cargo loaded onto vessel", response.statusDescription)
    }

    @Test
    fun `generated impl toResponseList maps each element`() {
        val impl = CargoShipmentMapperImpl()
        val entities = listOf(
            CargoShipment(
                vesselId = UUID.randomUUID(),
                billOfLading = "BL-003",
                originPortName = "Tokyo",
                destinationPortName = "Busan",
                cargoType = "BULK",
                status = ShipmentStatus.DELIVERED
            )
        )

        val responses = impl.toResponseList(entities)

        assertEquals(1, responses.size)
        assertEquals("Cargo delivered to consignee", responses[0].statusDescription)
    }
}
