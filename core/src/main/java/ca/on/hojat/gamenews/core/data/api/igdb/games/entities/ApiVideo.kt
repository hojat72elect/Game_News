package ca.on.hojat.gamenews.core.data.api.igdb.games.entities

import ca.on.hojat.gamenews.core.data.api.igdbcalypse.serialization.annotations.Apicalypse
import ca.on.hojat.gamenews.core.data.api.igdbcalypse.serialization.annotations.ApicalypseClass
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@ApicalypseClass
@Serializable
data class ApiVideo(
    @Apicalypse(Schema.ID)
    @SerialName(Schema.ID)
    val id: String,
    @Apicalypse(Schema.NAME)
    @SerialName(Schema.NAME)
    val name: String? = null,
) {

    object Schema {
        const val ID = "video_id"
        const val NAME = "name"
    }
}
