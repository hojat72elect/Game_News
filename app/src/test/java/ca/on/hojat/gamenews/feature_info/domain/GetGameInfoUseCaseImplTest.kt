package ca.on.hojat.gamenews.feature_info.domain

import app.cash.turbine.test
import ca.on.hojat.gamenews.feature_info.GAME_INFO
import ca.on.hojat.gamenews.feature_info.INVOLVED_COMPANY
import ca.on.hojat.gamenews.shared.domain.common.DomainException
import ca.on.hojat.gamenews.shared.testing.domain.DOMAIN_ERROR_UNKNOWN
import ca.on.hojat.gamenews.shared.testing.domain.DOMAIN_GAME
import ca.on.hojat.gamenews.shared.testing.domain.DOMAIN_GAMES
import ca.on.hojat.gamenews.shared.testing.domain.MainCoroutineRule
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.google.common.truth.Truth.assertThat
import com.paulrybitskyi.gamedge.feature_info.domain.usecases.GetCompanyDevelopedGamesUseCase
import com.paulrybitskyi.gamedge.feature_info.domain.usecases.GetGameInfoUseCase
import com.paulrybitskyi.gamedge.feature_info.domain.usecases.GetGameInfoUseCaseImpl
import com.paulrybitskyi.gamedge.feature_info.domain.usecases.GetGameUseCase
import com.paulrybitskyi.gamedge.feature_info.domain.usecases.GetSimilarGamesUseCase
import com.paulrybitskyi.gamedge.feature_info.domain.usecases.likes.ObserveGameLikeStateUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private val USE_CASE_PARAMS = GetGameInfoUseCase.Params(gameId = 0)

internal class GetGameInfoUseCaseImplTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var getGameUseCase: GetGameUseCase
    @MockK
    private lateinit var observeGameLikeStateUseCase: ObserveGameLikeStateUseCase
    @MockK
    private lateinit var getCompanyDevelopedGamesUseCase: GetCompanyDevelopedGamesUseCase
    @MockK
    private lateinit var getSimilarGamesUseCase: GetSimilarGamesUseCase

    private lateinit var SUT: GetGameInfoUseCaseImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        SUT = GetGameInfoUseCaseImpl(
            getGameUseCase = getGameUseCase,
            observeGameLikeStateUseCase = observeGameLikeStateUseCase,
            getCompanyDevelopedGamesUseCase = getCompanyDevelopedGamesUseCase,
            getSimilarGamesUseCase = getSimilarGamesUseCase,
            dispatcherProvider = mainCoroutineRule.dispatcherProvider,
        )
    }

    @Test
    fun `Emits game info successfully`() {
        runTest {
            val game = DOMAIN_GAME.copy(
                involvedCompanies = listOf(INVOLVED_COMPANY.copy(isDeveloper = true)),
                similarGames = listOf(1, 2, 3),
            )
            val expectedGameInfo = GAME_INFO.copy(game = game)

            coEvery { getGameUseCase.execute(any()) } returns flowOf(Ok(game))
            every { observeGameLikeStateUseCase.execute(any()) } returns flowOf(true)
            coEvery { getCompanyDevelopedGamesUseCase.execute(any()) } returns flowOf(
                Ok(
                    DOMAIN_GAMES
                )
            )
            coEvery { getSimilarGamesUseCase.execute(any()) } returns flowOf(Ok(DOMAIN_GAMES))

            SUT.execute(USE_CASE_PARAMS).test {
                assertThat(awaitItem()).isEqualTo(expectedGameInfo)
                awaitComplete()
            }
        }
    }

    @Test
    fun `Emits error when game retrieval fails`() {
        runTest {
            coEvery { getGameUseCase.execute(any()) } returns flowOf(Err(DOMAIN_ERROR_UNKNOWN))

            SUT.execute(USE_CASE_PARAMS).test {
                assertThat(awaitError()).isInstanceOf(DomainException::class.java)
            }
        }
    }

    @Test
    fun `Emits game info with empty company games`() {
        runTest {
            val game = DOMAIN_GAME.copy(involvedCompanies = emptyList())

            coEvery { getGameUseCase.execute(any()) } returns flowOf(Ok(game))
            every { observeGameLikeStateUseCase.execute(any()) } returns flowOf(true)

            SUT.execute(USE_CASE_PARAMS).test {
                assertThat(awaitItem().companyGames).isEmpty()
                awaitComplete()
            }
        }
    }

    @Test
    fun `Emits game info with empty similar games`() {
        runTest {
            val game = DOMAIN_GAME.copy(similarGames = emptyList())

            coEvery { getGameUseCase.execute(any()) } returns flowOf(Ok(game))
            every { observeGameLikeStateUseCase.execute(any()) } returns flowOf(true)

            SUT.execute(USE_CASE_PARAMS).test {
                assertThat(awaitItem().similarGames).isEmpty()
                awaitComplete()
            }
        }
    }
}