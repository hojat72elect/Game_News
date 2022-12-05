package com.paulrybitskyi.gamedge.igdb.api.games.entities

import com.paulrybitskyi.gamedge.igdb.apicalypse.serialization.annotations.Apicalypse
import com.paulrybitskyi.gamedge.igdb.apicalypse.serialization.annotations.ApicalypseClass
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@ApicalypseClass
@Serializable
data class ApiPlatform(
    @Apicalypse(Schema.ABBREVIATION)
    @SerialName(Schema.ABBREVIATION)
    val abbreviation: String? = null,
    @Apicalypse(Schema.NAME)
    @SerialName(Schema.NAME)
    val name: String,
) {

    object Schema {
        const val ABBREVIATION = "abbreviation"
        const val NAME = "name"
    }
}
