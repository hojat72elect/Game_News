
package ca.on.hojat.gamenews.feature_category

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import ca.on.hojat.gamenews.shared.domain.games.entities.Game
import ca.on.hojat.gamenews.shared.domain.games.usecases.ObservePopularGamesUseCase
import ca.on.hojat.gamenews.shared.domain.games.usecases.RefreshPopularGamesUseCase
import ca.on.hojat.gamenews.shared.testing.FakeErrorMapper
import ca.on.hojat.gamenews.shared.testing.FakeLogger
import ca.on.hojat.gamenews.shared.testing.FakeStringProvider
import ca.on.hojat.gamenews.shared.testing.domain.DOMAIN_GAMES
import ca.on.hojat.gamenews.shared.testing.domain.MainCoroutineRule
import ca.on.hojat.gamenews.shared.ui.base.events.common.GeneralCommand
import ca.on.hojat.gamenews.shared.ui.widgets.FiniteUiState
import com.github.michaelbull.result.Ok
import com.google.common.truth.Truth.assertThat
import com.paulrybitskyi.gamedge.feature_category.GamesCategory
import com.paulrybitskyi.gamedge.feature_category.GamesCategoryRoute
import com.paulrybitskyi.gamedge.feature_category.GamesCategoryUseCases
import com.paulrybitskyi.gamedge.feature_category.GamesCategoryViewModel
import com.paulrybitskyi.gamedge.feature_category.di.GamesCategoryKey
import com.paulrybitskyi.gamedge.feature_category.widgets.GameCategoryUiModel
import com.paulrybitskyi.gamedge.feature_category.widgets.GameCategoryUiModelMapper
import com.paulrybitskyi.gamedge.feature_category.widgets.finiteUiState
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import javax.inject.Provider

internal class GamesCategoryViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule(StandardTestDispatcher())

    private val observePopularGamesUseCase = mockk<ObservePopularGamesUseCase>(relaxed = true)
    private val refreshPopularGamesUseCase = mockk<RefreshPopularGamesUseCase>(relaxed = true)

    private val logger = FakeLogger()
    private val SUT by lazy {
        GamesCategoryViewModel(
            savedStateHandle = setupSavedStateHandle(),
            stringProvider = FakeStringProvider(),
            transitionAnimationDuration = 0L,
            useCases = setupUseCases(),
            uiModelMapper = FakeGameCategoryUiModelMapper(),
            dispatcherProvider = mainCoroutineRule.dispatcherProvider,
            errorMapper = FakeErrorMapper(),
            logger = logger,
        )
    }

    private fun setupSavedStateHandle(): SavedStateHandle {
        return mockk(relaxed = true) {
            every { get<String>(any()) } returns GamesCategory.POPULAR.name
        }
    }

    private fun setupUseCases(): GamesCategoryUseCases {
        return GamesCategoryUseCases(
            observeGamesUseCasesMap = mapOf(
                GamesCategoryKey.Type.POPULAR to Provider { observePopularGamesUseCase },
                GamesCategoryKey.Type.RECENTLY_RELEASED to Provider(::mockk),
                GamesCategoryKey.Type.COMING_SOON to Provider(::mockk),
                GamesCategoryKey.Type.MOST_ANTICIPATED to Provider(::mockk)
            ),
            refreshGamesUseCasesMap = mapOf(
                GamesCategoryKey.Type.POPULAR to Provider { refreshPopularGamesUseCase },
                GamesCategoryKey.Type.RECENTLY_RELEASED to Provider(::mockk),
                GamesCategoryKey.Type.COMING_SOON to Provider(::mockk),
                GamesCategoryKey.Type.MOST_ANTICIPATED to Provider(::mockk)
            )
        )
    }

    @Test
    fun `Emits toolbar title when initialized`() {
        runTest {
            SUT.uiState.test {
                assertThat(awaitItem().title).isNotEmpty()
            }
        }
    }

    @Test
    fun `Emits correct ui states when observing games`() {
        runTest {
            every { observePopularGamesUseCase.execute(any()) } returns flowOf(DOMAIN_GAMES)

            SUT.uiState.test {
                assertThat(awaitItem().finiteUiState).isEqualTo(FiniteUiState.Empty)
                assertThat(awaitItem().finiteUiState).isEqualTo(FiniteUiState.Success)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `Logs error when games observing use case throws error`() {
        runTest {
            every { observePopularGamesUseCase.execute(any()) } returns flow { throw IllegalStateException("error") }
            every { refreshPopularGamesUseCase.execute(any()) } returns flowOf(Ok(DOMAIN_GAMES))

            SUT
            advanceUntilIdle()

            assertThat(logger.errorMessage).isNotEmpty()
        }
    }

    @Test
    fun `Dispatches toast showing command when games observing use case throws error`() {
        runTest {
            every { observePopularGamesUseCase.execute(any()) } returns flow { throw IllegalStateException("error") }
            every { refreshPopularGamesUseCase.execute(any()) } returns flowOf(Ok(DOMAIN_GAMES))

            SUT.commandFlow.test {
                assertThat(awaitItem()).isInstanceOf(GeneralCommand.ShowLongToast::class.java)
            }
        }
    }

    @Test
    fun `Emits correct ui states when refreshing games`() {
        runTest {
            every {
                refreshPopularGamesUseCase.execute(any())
            } returns flow {
                // Refresh, for some reason, emits way too fast.
                // Adding delay to grab all possible states.
                delay(10)
                emit(Ok(DOMAIN_GAMES))
            }

            SUT.uiState.test {
                assertThat(awaitItem().finiteUiState).isEqualTo(FiniteUiState.Empty)
                assertThat(awaitItem().finiteUiState).isEqualTo(FiniteUiState.Loading)
                assertThat(awaitItem().finiteUiState).isEqualTo(FiniteUiState.Empty)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `Logs error when games refreshing use case throws error`() {
        runTest {
            every { observePopularGamesUseCase.execute(any()) } returns flowOf(DOMAIN_GAMES)
            every { refreshPopularGamesUseCase.execute(any()) } returns flow { throw IllegalStateException("error") }

            SUT
            advanceUntilIdle()

            assertThat(logger.errorMessage).isNotEmpty()
        }
    }

    @Test
    fun `Dispatches toast showing command when games refreshing use case throws error`() {
        runTest {
            every { observePopularGamesUseCase.execute(any()) } returns flowOf(DOMAIN_GAMES)
            every { refreshPopularGamesUseCase.execute(any()) } returns flow { throw IllegalStateException("error") }

            SUT.commandFlow.test {
                assertThat(awaitItem()).isInstanceOf(GeneralCommand.ShowLongToast::class.java)
            }
        }
    }

    @Test
    fun `Routes to previous screen when toolbar left button is clicked`() {
        runTest {
            SUT.routeFlow.test {
                SUT.onToolbarLeftButtonClicked()

                assertThat(awaitItem()).isInstanceOf(GamesCategoryRoute.Back::class.java)
            }
        }
    }

    @Test
    fun `Routes to game info screen when game is clicked`() {
        runTest {
            val game = GameCategoryUiModel(
                id = 1,
                title = "title",
                coverUrl = null
            )

            SUT.routeFlow.test {
                SUT.onGameClicked(game)

                val route = awaitItem()

                assertThat(route).isInstanceOf(GamesCategoryRoute.Info::class.java)
                assertThat((route as GamesCategoryRoute.Info).gameId).isEqualTo(game.id)
            }
        }
    }

    private class FakeGameCategoryUiModelMapper : GameCategoryUiModelMapper {

        override fun mapToUiModel(game: Game): GameCategoryUiModel {
            return GameCategoryUiModel(
                id = game.id,
                title = game.name,
                coverUrl = null,
            )
        }
    }
}