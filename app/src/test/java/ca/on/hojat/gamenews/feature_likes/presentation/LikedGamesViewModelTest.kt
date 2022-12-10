package ca.on.hojat.gamenews.feature_likes.presentation

import app.cash.turbine.test
import ca.on.hojat.gamenews.shared.domain.games.entities.Game
import ca.on.hojat.gamenews.shared.testing.FakeErrorMapper
import ca.on.hojat.gamenews.shared.testing.FakeLogger
import ca.on.hojat.gamenews.shared.testing.FakeStringProvider
import ca.on.hojat.gamenews.shared.testing.domain.DOMAIN_GAMES
import ca.on.hojat.gamenews.shared.testing.domain.MainCoroutineRule
import ca.on.hojat.gamenews.shared.ui.base.events.common.GeneralCommand
import ca.on.hojat.gamenews.shared.ui.widgets.FiniteUiState
import ca.on.hojat.gamenews.shared.ui.widgets.games.GameUiModel
import ca.on.hojat.gamenews.shared.ui.widgets.games.GameUiModelMapper
import ca.on.hojat.gamenews.shared.ui.widgets.games.finiteUiState
import com.google.common.truth.Truth.assertThat
import com.paulrybitskyi.gamedge.feature_likes.domain.ObserveLikedGamesUseCase
import com.paulrybitskyi.gamedge.feature_likes.presentation.LikedGamesRoute
import com.paulrybitskyi.gamedge.feature_likes.presentation.LikedGamesViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

internal class LikedGamesViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule(StandardTestDispatcher())

    private val observeLikedGamesUseCase = mockk<ObserveLikedGamesUseCase>(relaxed = true)

    private val logger = FakeLogger()
    private val SUT by lazy {
        LikedGamesViewModel(
            observeLikedGamesUseCase = observeLikedGamesUseCase,
            uiModelMapper = FakeGameUiModelMapper(),
            dispatcherProvider = mainCoroutineRule.dispatcherProvider,
            stringProvider = FakeStringProvider(),
            errorMapper = FakeErrorMapper(),
            logger = logger,
        )
    }

    @Test
    fun `Emits correct ui states when loading data`() {
        runTest {
            every { observeLikedGamesUseCase.execute(any()) } returns flowOf(DOMAIN_GAMES)

            SUT.uiState.test {
                val emptyState = awaitItem()
                val loadingState = awaitItem()
                val resultState = awaitItem()

                assertThat(emptyState.finiteUiState).isEqualTo(FiniteUiState.Empty)
                assertThat(loadingState.finiteUiState).isEqualTo(FiniteUiState.Loading)
                assertThat(resultState.finiteUiState).isEqualTo(FiniteUiState.Success)
                assertThat(resultState.games).hasSize(DOMAIN_GAMES.size)
            }
        }
    }

    @Test
    fun `Logs error when liked games loading fails`() {
        runTest {
            every { observeLikedGamesUseCase.execute(any()) } returns flow {
                throw IllegalStateException(
                    "error"
                )
            }

            SUT
            advanceUntilIdle()

            assertThat(logger.errorMessage).isNotEmpty()
        }
    }

    @Test
    fun `Dispatches toast showing command when liked games loading fails`() {
        runTest {
            every { observeLikedGamesUseCase.execute(any()) } returns flow {
                throw IllegalStateException(
                    "error"
                )
            }

            SUT.commandFlow.test {
                assertThat(awaitItem()).isInstanceOf(GeneralCommand.ShowLongToast::class.java)
            }
        }
    }

    @Test
    fun `Routes to search screen when search button is clicked`() {
        runTest {
            SUT.routeFlow.test {
                SUT.onSearchButtonClicked()

                assertThat(awaitItem()).isInstanceOf(LikedGamesRoute.Search::class.java)
            }
        }
    }

    @Test
    fun `Routes to info screen when game is clicked`() {
        runTest {
            val game = GameUiModel(
                id = 1,
                coverImageUrl = null,
                name = "",
                releaseDate = "",
                developerName = null,
                description = null,
            )

            SUT.routeFlow.test {
                SUT.onGameClicked(game)

                val route = awaitItem()

                assertThat(route).isInstanceOf(LikedGamesRoute.Info::class.java)
                assertThat((route as LikedGamesRoute.Info).gameId).isEqualTo(game.id)
            }
        }
    }

    private class FakeGameUiModelMapper : GameUiModelMapper {

        override fun mapToUiModel(game: Game): GameUiModel {
            return GameUiModel(
                id = game.id,
                coverImageUrl = null,
                name = game.name,
                releaseDate = "release_date",
                developerName = null,
                description = null,
            )
        }
    }
}