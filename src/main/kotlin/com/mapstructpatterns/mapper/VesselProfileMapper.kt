package com.mapstructpatterns.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import com.mapstructpatterns.dto.request.CreateVesselProfileRequest
import com.mapstructpatterns.dto.request.UpdateVesselProfileRequest
import com.mapstructpatterns.dto.response.VesselProfileResponse
import com.mapstructpatterns.dto.response.VesselProfileSummaryResponse
import com.mapstructpatterns.model.entity.VesselProfile
import java.util.UUID

/**
 * Pattern 1: Kotlin `is`-prefix workaround + manual entity creation with domain constructor
 *
 * THE PROBLEM:
 *   MapStruct's Java annotation processor inspects Kotlin `isActive` as a JavaBean property
 *   named `active` (stripping the `is` prefix). However, Kotlin's primary constructor keeps
 *   the parameter name `isActive`. The generated code ends up as:
 *
 *       new VesselProfileResponse(... isActive = false ...)   // always false!
 *
 *   This happens because MapStruct looks for a setter `setActive()` or constructor param `active`,
 *   finds neither matching `isActive`, and silently defaults to false.
 *
 * THE FIX:
 *   Use `expression = "java(entity.getActive())"` to call the actual getter explicitly.
 *   The same applies to any `is`-prefixed boolean: isDemoData, isIgMember, isEnabled, etc.
 *
 * ALSO SHOWN HERE:
 *   - Manual toEntity/updateEntity: entity uses protected set + domain constructor,
 *     so MapStruct cannot auto-generate entity creation or update
 *   - @Named + qualifiedByName for reusable named conversion methods
 *   - Collection mapping: toResponseList delegates to toResponse per element
 */
@Mapper(componentModel = "spring")
abstract class VesselProfileMapper {

    @Mapping(target = "isActive", expression = "java(entity.getActive())")
    @Mapping(target = "isDemoData", expression = "java(entity.isDemoData())")
    abstract fun toResponse(entity: VesselProfile): VesselProfileResponse

    abstract fun toSummary(entity: VesselProfile): VesselProfileSummaryResponse

    abstract fun toResponseList(entities: List<VesselProfile>): List<VesselProfileResponse>

    abstract fun toSummaryList(entities: List<VesselProfile>): List<VesselProfileSummaryResponse>

    fun toEntity(request: CreateVesselProfileRequest, userId: UUID? = null): VesselProfile {
        return VesselProfile(
            vesselId = request.vesselId,
            profileCode = request.profileCode,
            vesselName = request.vesselName,
            vesselType = request.vesselType,
            imoNumber = request.imoNumber,
            mmsi = request.mmsi,
            flag = request.flag,
            grossTonnage = request.grossTonnage,
            netTonnage = request.netTonnage,
            dwt = request.dwt,
            lengthOverall = request.lengthOverall,
            beam = request.beam,
            draft = request.draft,
            yearBuilt = request.yearBuilt,
            engineType = request.engineType,
            enginePowerKw = request.enginePowerKw,
            userId = userId
        )
    }

    @Suppress("unused")
    fun updateEntity(request: UpdateVesselProfileRequest, entity: VesselProfile) {
        entity.update(
            vesselName = request.vesselName,
            flag = request.flag,
            grossTonnage = request.grossTonnage,
            netTonnage = request.netTonnage,
            dwt = request.dwt,
            lengthOverall = request.lengthOverall,
            beam = request.beam,
            draft = request.draft,
            yearBuilt = request.yearBuilt,
            engineType = request.engineType,
            enginePowerKw = request.enginePowerKw
        )
    }
}
