package ca.on.hojat.gamenews.core.data.api.igdb.games.entities

import ca.on.hojat.gamenews.core.data.api.igdbcalypse.serialization.annotations.Apicalypse
import ca.on.hojat.gamenews.core.data.api.igdbcalypse.serialization.annotations.ApicalypseClass
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@ApicalypseClass
@Serializable
data class ApiCompany(
    @Apicalypse(Schema.ID)
    @SerialName(Schema.ID)
    val id: Int,
    @Apicalypse(Schema.NAME)
    @SerialName(Schema.NAME)
    val name: String,
    @Apicalypse(Schema.WEBSITE_URL)
    @SerialName(Schema.WEBSITE_URL)
    val websiteUrl: String,
    @Apicalypse(Schema.LOGO)
    @SerialName(Schema.LOGO)
    val logo: ApiImage? = null,
    @Apicalypse(Schema.DEVELOPED_GAMES)
    @SerialName(Schema.DEVELOPED_GAMES)
    val developedGames: List<Int> = listOf(),
) {

    object Schema {
        const val ID = "id"
        const val NAME = "name"
        const val WEBSITE_URL = "url"
        const val LOGO = "logo"
        const val DEVELOPED_GAMES = "developed"
    }
}
