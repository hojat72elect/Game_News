package ca.on.hojat.gamenews.feature_info.domain

import app.cash.turbine.test
import ca.on.hojat.gamenews.feature_info.GET_SIMILAR_GAMES_USE_CASE_PARAMS
import ca.on.hojat.gamenews.core.domain.games.datastores.GamesLocalDataStore
import ca.on.hojat.gamenews.core.common_testing.domain.DOMAIN_GAMES
import ca.on.hojat.gamenews.core.common_testing.domain.MainCoroutineRule
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.get
import com.google.common.truth.Truth.assertThat
import ca.on.hojat.gamenews.feature_info.domain.usecases.RefreshSimilarGamesUseCase
import ca.on.hojat.gamenews.feature_info.domain.usecases.GetSimilarGamesUseCaseImpl
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class GetSimilarGamesUseCaseImplTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var refreshSimilarGamesUseCase: RefreshSimilarGamesUseCase
    @MockK
    private lateinit var gamesLocalDataStore: GamesLocalDataStore

    private lateinit var sut: GetSimilarGamesUseCaseImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        sut = GetSimilarGamesUseCaseImpl(
            refreshSimilarGamesUseCase = refreshSimilarGamesUseCase,
            gamesLocalDataStore = gamesLocalDataStore,
            dispatcherProvider = mainCoroutineRule.dispatcherProvider,
        )
    }

    @Test
    fun `Emits games that refresh use case successfully emits`() {
        runTest {
            coEvery { refreshSimilarGamesUseCase.execute(any()) } returns flowOf(Ok(DOMAIN_GAMES))

            sut.execute(GET_SIMILAR_GAMES_USE_CASE_PARAMS).test {
                assertThat(awaitItem().get()).isEqualTo(DOMAIN_GAMES)
                awaitComplete()
            }
        }
    }

    @Test
    fun `Emits games from local data store if refresh use case does not emit`() {
        runTest {
            coEvery { refreshSimilarGamesUseCase.execute(any()) } returns flowOf()
            coEvery { gamesLocalDataStore.getSimilarGames(any(), any()) } returns DOMAIN_GAMES

            sut.execute(GET_SIMILAR_GAMES_USE_CASE_PARAMS).test {
                assertThat(awaitItem().get()).isEqualTo(DOMAIN_GAMES)
                awaitComplete()
            }
        }
    }
}
