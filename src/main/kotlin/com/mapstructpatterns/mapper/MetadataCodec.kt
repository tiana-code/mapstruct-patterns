package com.mapstructpatterns.mapper

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class MetadataCodec(private val objectMapper: ObjectMapper) {

    private val log = LoggerFactory.getLogger(MetadataCodec::class.java)

    fun serialize(metadata: Map<String, Any>?): String? =
        metadata?.let { objectMapper.writeValueAsString(it) }

    fun deserialize(json: String?): Map<String, Any>? =
        json?.let {
            try {
                objectMapper.readValue<Map<String, Any>>(it)
            } catch (ex: Exception) {
                log.warn("Failed to deserialize metadata JSON, returning empty map: {}", ex.message)
                emptyMap()
            }
        }
}
