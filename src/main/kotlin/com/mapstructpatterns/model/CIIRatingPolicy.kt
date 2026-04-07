package com.mapstructpatterns.model

import com.mapstructpatterns.model.enums.CIIRating

object CIIRatingPolicy {

    fun description(rating: CIIRating): String = when (rating) {
        CIIRating.A -> "Superior performance - well above required level"
        CIIRating.B -> "Lower performance - above required level"
        CIIRating.C -> "Moderate performance - at required level"
        CIIRating.D -> "Inferior performance - below required level, improvement plan required"
        CIIRating.E -> "Significantly inferior - well below required level, urgent action needed"
    }

    fun recommendation(rating: CIIRating): String? = when (rating) {
        CIIRating.A,
        CIIRating.B -> null
        CIIRating.C -> "Monitor performance to maintain compliance"
        CIIRating.D -> "Develop and implement a Ship Energy Efficiency Management Plan (SEEMP) Part III"
        CIIRating.E -> "Immediate corrective action required. Consider operational changes, route optimization, or technical modifications"
    }
}
