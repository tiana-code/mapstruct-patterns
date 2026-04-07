package com.mapstructpatterns.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named
import com.mapstructpatterns.dto.request.CreateCargoShipmentRequest
import com.mapstructpatterns.dto.response.CargoTrackingResponse
import com.mapstructpatterns.model.entity.CargoShipment
import com.mapstructpatterns.model.enums.ShipmentStatus

/**
 * Pattern 8: Enum-to-string mapping + @constant + collection join expression
 *
 * ENUM-TO-STRING:
 *   `expression = "java(entity.getStatus().name())"` — explicit enum.name() call.
 *   Prefer this over relying on MapStruct's default toString() because it is:
 *   - explicit about intent
 *   - immune to enum overriding toString()
 *   - visible to code reviewers
 *
 * COLLECTION JOIN:
 *   `containerIds` is stored as a comma-joined String in the entity but sent as List<String>
 *   from the request. The manual toEntity handles the null-safe join.
 *
 * @Named + qualifiedByName for rich status descriptions:
 *   The `statusDescription` field is a human-readable label mapped from the ShipmentStatus enum.
 *   @Named allows reuse of the method in other mappers via `qualifiedByName`.
 *
 * ALSO SHOWN HERE:
 *   - Manual toEntity: entity uses protected set + domain constructor
 */
@Mapper(config = GlobalMapperConfig::class)
abstract class CargoShipmentMapper {

    fun toEntity(request: CreateCargoShipmentRequest): CargoShipment = CargoShipment(
        vesselId = request.vesselId,
        billOfLading = request.billOfLading,
        originPortName = request.originPortName,
        destinationPortName = request.destinationPortName,
        cargoType = request.cargoType,
        containerIds = request.containerIds?.takeIf { it.isNotEmpty() }?.joinToString(","),
        weightTons = request.weightTons,
        etd = request.etd,
        eta = request.eta
    )

    @Mapping(target = "originPort", source = "originPortName")
    @Mapping(target = "destinationPort", source = "destinationPortName")
    @Mapping(target = "status", expression = "java(entity.getStatus().name())")
    @Mapping(target = "statusDescription", source = "status", qualifiedByName = ["mapStatusDescription"])
    abstract fun toResponse(entity: CargoShipment): CargoTrackingResponse

    abstract fun toResponseList(entities: List<CargoShipment>): List<CargoTrackingResponse>

    @Named("mapStatusDescription")
    fun mapStatusDescription(status: ShipmentStatus): String = when (status) {
        ShipmentStatus.BOOKED -> "Shipment booked, awaiting loading"
        ShipmentStatus.LOADED -> "Cargo loaded onto vessel"
        ShipmentStatus.IN_TRANSIT -> "Vessel en route to destination"
        ShipmentStatus.APPROACHING -> "Vessel approaching destination port"
        ShipmentStatus.ARRIVED -> "Vessel arrived at destination"
        ShipmentStatus.DELIVERED -> "Cargo delivered to consignee"
        ShipmentStatus.CANCELLED -> "Shipment cancelled"
    }
}
