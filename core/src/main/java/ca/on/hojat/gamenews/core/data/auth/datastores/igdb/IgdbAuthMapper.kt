package ca.on.hojat.gamenews.core.data.auth.datastores.igdb

import ca.on.hojat.gamenews.core.data.api.igdb.auth.entities.ApiOauthCredentials
import ca.on.hojat.gamenews.core.domain.auth.entities.OauthCredentials
import javax.inject.Inject

class IgdbAuthMapper @Inject constructor() {

    fun mapToApiOauthCredentials(oauthCredentials: OauthCredentials): ApiOauthCredentials {
        return ApiOauthCredentials(
            accessToken = oauthCredentials.accessToken,
            tokenType = oauthCredentials.tokenType,
            tokenTtl = oauthCredentials.tokenTtl
        )
    }

    fun mapToDomainOauthCredentials(apiOauthCredentials: ApiOauthCredentials): OauthCredentials {
        return OauthCredentials(
            accessToken = apiOauthCredentials.accessToken,
            tokenType = apiOauthCredentials.tokenType,
            tokenTtl = apiOauthCredentials.tokenTtl
        )
    }
}
