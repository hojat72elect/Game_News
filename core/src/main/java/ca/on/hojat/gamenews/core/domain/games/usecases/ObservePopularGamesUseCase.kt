package ca.on.hojat.gamenews.core.domain.games.usecases

import ca.on.hojat.gamenews.core.domain.common.DispatcherProvider
import ca.on.hojat.gamenews.core.domain.games.ObservableGamesUseCase
import ca.on.hojat.gamenews.core.domain.games.common.ObserveGamesUseCaseParams
import ca.on.hojat.gamenews.core.domain.games.datastores.GamesLocalDataStore
import ca.on.hojat.gamenews.core.domain.entities.Game
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

interface ObservePopularGamesUseCase : ObservableGamesUseCase

@Singleton
class ObservePopularGamesUseCaseImpl @Inject constructor(
    private val gamesLocalDataStore: GamesLocalDataStore,
    private val dispatcherProvider: DispatcherProvider,
) : ObservePopularGamesUseCase {

    override fun execute(params: ObserveGamesUseCaseParams): Flow<List<Game>> {
        return gamesLocalDataStore.observePopularGames(params.pagination)
            .flowOn(dispatcherProvider.main)
    }
}
