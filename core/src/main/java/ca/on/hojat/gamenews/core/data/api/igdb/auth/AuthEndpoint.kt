package ca.on.hojat.gamenews.core.data.api.igdb.auth

import ca.on.hojat.gamenews.core.data.api.common.ApiResult
import ca.on.hojat.gamenews.core.data.api.igdb.auth.AuthService
import ca.on.hojat.gamenews.core.data.api.igdb.auth.entities.ApiGrantType
import ca.on.hojat.gamenews.core.data.api.igdb.auth.entities.ApiOauthCredentials

interface AuthEndpoint {
    suspend fun getOauthCredentials(): ApiResult<ApiOauthCredentials>
}

class AuthEndpointImpl(
    private val authService: AuthService,
    private val clientId: String,
    private val clientSecret: String
) : AuthEndpoint {

    override suspend fun getOauthCredentials(): ApiResult<ApiOauthCredentials> {
        return authService.getOauthCredentials(
            clientId = clientId,
            clientSecret = clientSecret,
            grantType = ApiGrantType.CLIENT_CREDENTIALS.rawType
        )
    }
}
