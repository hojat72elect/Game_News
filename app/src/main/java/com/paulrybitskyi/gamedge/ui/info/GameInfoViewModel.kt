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

package com.paulrybitskyi.gamedge.ui.info

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.paulrybitskyi.gamedge.commons.ui.widgets.info.GameInfoUiState
import com.paulrybitskyi.gamedge.commons.ui.widgets.info.model.GameInfoCompanyModel
import com.paulrybitskyi.gamedge.commons.ui.widgets.info.model.GameInfoLinkModel
import com.paulrybitskyi.gamedge.commons.ui.widgets.info.model.GameInfoVideoModel
import com.paulrybitskyi.gamedge.commons.ui.widgets.info.model.games.GameInfoRelatedGameModel
import com.paulrybitskyi.gamedge.core.Logger
import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.core.utils.combine
import com.paulrybitskyi.gamedge.core.utils.onError
import com.paulrybitskyi.gamedge.core.utils.resultOrError
import com.paulrybitskyi.gamedge.domain.games.commons.Pagination
import com.paulrybitskyi.gamedge.domain.games.entities.Company
import com.paulrybitskyi.gamedge.domain.games.entities.Game
import com.paulrybitskyi.gamedge.domain.games.entities.extensions.developerCompany
import com.paulrybitskyi.gamedge.domain.games.entities.extensions.hasDevelopedGames
import com.paulrybitskyi.gamedge.domain.games.entities.extensions.hasSimilarGames
import com.paulrybitskyi.gamedge.domain.games.usecases.*
import com.paulrybitskyi.gamedge.domain.games.usecases.observers.ObserveGameLikeStateUseCase
import com.paulrybitskyi.gamedge.ui.base.BaseViewModel
import com.paulrybitskyi.gamedge.ui.base.events.commons.GeneralCommands
import com.paulrybitskyi.gamedge.ui.info.mapping.GameInfoUiStateFactory
import com.paulrybitskyi.gamedge.utils.ErrorMapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


private const val PARAM_GAME_ID = "game_id"


internal class GameInfoViewModel @ViewModelInject constructor(
    private val infoUseCases: GameInfoUseCases,
    private val infoUiStateFactory: GameInfoUiStateFactory,
    private val dispatcherProvider: DispatcherProvider,
    private val errorMapper: ErrorMapper,
    private val logger: Logger,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {


    private var isLoadingData = false

    private val gameId = checkNotNull(savedStateHandle.get<Int>(PARAM_GAME_ID))

    private val relatedGamesUseCasePagination = Pagination()

    private val _infoUiState = MutableLiveData<GameInfoUiState>(GameInfoUiState.Empty)

    val infoUiState: LiveData<GameInfoUiState>
        get() = _infoUiState


    fun loadData(resultEmissionDelay: Long) {
        if(isLoadingData) return

        viewModelScope.launch {
            loadDataInternal(resultEmissionDelay)
        }
    }


    private suspend fun loadDataInternal(resultEmissionDelay: Long) {
        loadGame()
            .flatMapConcat { game ->
                combine(
                    flowOf(game),
                    isGameLiked(game),
                    loadCompanyGames(game),
                    loadSimilarGames(game)
                )
            }
            .map { (game, isGameLiked, companyGames, similarGames) ->
                infoUiStateFactory.createWithResultState(
                    game,
                    isGameLiked,
                    companyGames,
                    similarGames
                )
            }
            .flowOn(dispatcherProvider.computation)
            .onError {
                logger.error(logTag, "Failed to load game info data.", it)
                dispatchCommand(GeneralCommands.ShowLongToast(errorMapper.mapToMessage(it)))
                emit(infoUiStateFactory.createWithEmptyState())
            }
            .onStart {
                isLoadingData = true
                emit(infoUiStateFactory.createWithLoadingState())
                delay(resultEmissionDelay)
            }
            .onCompletion { isLoadingData = false }
            .collect(_infoUiState::setValue)
    }


    private suspend fun loadGame(): Flow<Game> {
        return infoUseCases.getGameUseCase
            .execute(GetGameUseCase.Params(gameId))
            .resultOrError()
    }


    private suspend fun isGameLiked(game: Game): Flow<Boolean> {
        return infoUseCases.observeGameLikeStateUseCase
            .execute(ObserveGameLikeStateUseCase.Params(game.id))
    }


    private suspend fun loadCompanyGames(game: Game): Flow<List<Game>> {
        val company = game.developerCompany
            ?.takeIf(Company::hasDevelopedGames)
            ?: return flowOf(emptyList())

        return infoUseCases.getCompanyDevelopedGamesUseCase
            .execute(GetCompanyDevelopedGamesUseCase.Params(company, relatedGamesUseCasePagination))
    }


    private suspend fun loadSimilarGames(game: Game): Flow<List<Game>> {
        if(!game.hasSimilarGames) return flowOf(emptyList())

        return infoUseCases.getSimilarGamesUseCase
            .execute(GetSimilarGamesUseCase.Params(game, relatedGamesUseCasePagination))
    }


    fun onBackButtonClicked() {
        route(GameInfoRoutes.Back)
    }


    fun onLikeButtonClicked(gameId: Int) {
        viewModelScope.launch {
            infoUseCases
                .toggleGameLikeStateUseCase
                .execute(ToggleGameLikeStateUseCase.Params(gameId))
        }
    }


    fun onVideoClicked(video: GameInfoVideoModel) {
        openUrl(video.videoUrl)
    }


    fun onLinkClicked(link: GameInfoLinkModel) {
        openUrl(link.payload as String)
    }


    fun onCompanyClicked(company: GameInfoCompanyModel) {
        openUrl(company.websiteUrl)
    }


    private fun openUrl(url: String) {
        dispatchCommand(GameInfoCommands.OpenUrl(url))
    }


    fun onRelatedGameClicked(game: GameInfoRelatedGameModel) {
        route(GameInfoRoutes.Info(gameId = game.id))
    }


}