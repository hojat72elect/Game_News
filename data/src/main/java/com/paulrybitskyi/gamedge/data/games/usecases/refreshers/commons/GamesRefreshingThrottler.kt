/*
 * Copyright 2020 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.paulrybitskyi.gamedge.data.games.usecases.refreshers.commons

import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import com.paulrybitskyi.gamedge.core.providers.TimestampProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit


internal interface GamesRefreshingThrottler {

    suspend fun canRefreshGames(key: String): Boolean

    suspend fun updateGamesLastRefreshTime(key: String)

    suspend fun canRefreshCompanyDevelopedGames(key: String): Boolean

    suspend fun canRefreshSimilarGames(key: String): Boolean

}


internal class GamesRefreshingThrottlerImpl(
    private val gamesPreferences: DataStore<Preferences>,
    private val timestampProvider: TimestampProvider
) : GamesRefreshingThrottler {


    private companion object {

        val DEFAULT_GAMES_REFRESH_TIMEOUT = TimeUnit.MINUTES.toMillis(10L)
        val COMPANY_DEVELOPED_GAMES_REFRESH_TIMEOUT = TimeUnit.DAYS.toMillis(7L)
        val SIMILAR_GAMES_REFRESH_TIMEOUT = TimeUnit.DAYS.toMillis(7L)

    }


    override suspend fun canRefreshGames(key: String): Boolean {
        return canRefreshGames(
            key = preferencesKey(key),
            refreshTimeout = DEFAULT_GAMES_REFRESH_TIMEOUT
        )
    }


    override suspend fun updateGamesLastRefreshTime(key: String) {
        updateGamesLastRefreshTime(preferencesKey(key))
    }


    override suspend fun canRefreshCompanyDevelopedGames(key: String): Boolean {
        return canRefreshGames(
            key = preferencesKey(key),
            refreshTimeout = COMPANY_DEVELOPED_GAMES_REFRESH_TIMEOUT
        )
    }


    override suspend fun canRefreshSimilarGames(key: String): Boolean {
        return canRefreshGames(
            key = preferencesKey(key),
            refreshTimeout = SIMILAR_GAMES_REFRESH_TIMEOUT
        )
    }


    private suspend fun canRefreshGames(key: Preferences.Key<Long>, refreshTimeout: Long): Boolean {
        return gamesPreferences.data
            .map { it[key] ?: 0L }
            .map { timestampProvider.getUnixTimestamp() > (it + refreshTimeout) }
            .first()
    }


    private suspend fun updateGamesLastRefreshTime(key: Preferences.Key<Long>) {
        gamesPreferences.edit {
            it[key] = timestampProvider.getUnixTimestamp()
        }
    }


}