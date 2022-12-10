package ca.on.hojat.gamenews.feature_news.domain

import app.cash.turbine.test
import ca.on.hojat.gamenews.feature_news.DOMAIN_ARTICLES
import ca.on.hojat.gamenews.shared.testing.domain.DOMAIN_ERROR_UNKNOWN
import ca.on.hojat.gamenews.shared.testing.domain.MainCoroutineRule
import ca.on.hojat.gamenews.shared.testing.domain.coVerifyNotCalled
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.get
import com.google.common.truth.Truth.assertThat
import com.paulrybitskyi.gamedge.feature.news.domain.throttling.ArticlesRefreshingThrottler
import com.paulrybitskyi.gamedge.feature.news.domain.throttling.ArticlesRefreshingThrottlerKeyProvider
import com.paulrybitskyi.gamedge.feature.news.domain.throttling.ArticlesRefreshingThrottlerTools
import com.paulrybitskyi.gamedge.feature.news.domain.usecases.RefreshArticlesUseCase
import com.paulrybitskyi.gamedge.feature.news.domain.usecases.RefreshArticlesUseCaseImpl
import com.paulrybitskyi.gamedge.feature_news.domain.datastores.ArticlesDataStores
import com.paulrybitskyi.gamedge.feature_news.domain.datastores.ArticlesLocalDataStore
import com.paulrybitskyi.gamedge.feature_news.domain.datastores.ArticlesRemoteDataStore
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private val USE_CASE_PARAMS = RefreshArticlesUseCase.Params()

internal class RefreshArticlesUseCaseImplTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var articlesLocalDataStore: ArticlesLocalDataStore
    @MockK
    private lateinit var articlesRemoteDataStore: ArticlesRemoteDataStore
    @MockK
    private lateinit var throttler: ArticlesRefreshingThrottler
    @MockK
    private lateinit var keyProvider: ArticlesRefreshingThrottlerKeyProvider

    private lateinit var SUT: RefreshArticlesUseCaseImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        SUT = RefreshArticlesUseCaseImpl(
            articlesDataStores = ArticlesDataStores(
                local = articlesLocalDataStore,
                remote = articlesRemoteDataStore
            ),
            dispatcherProvider = mainCoroutineRule.dispatcherProvider,
            throttlerTools = ArticlesRefreshingThrottlerTools(
                throttler = throttler,
                keyProvider = keyProvider
            ),
        )

        every { keyProvider.provideArticlesKey(any()) } returns "key"
    }

    @Test
    fun `Emits remote articles when refresh is possible`() {
        runTest {
            coEvery { throttler.canRefreshArticles(any()) } returns true
            coEvery { articlesRemoteDataStore.getArticles(any()) } returns Ok(DOMAIN_ARTICLES)

            SUT.execute(USE_CASE_PARAMS).test {
                assertThat(awaitItem().get()).isEqualTo(DOMAIN_ARTICLES)
                awaitComplete()
            }
        }
    }

    @Test
    fun `Does not emit remote articles when refresh is not possible`() {
        runTest {
            coEvery { throttler.canRefreshArticles(any()) } returns false

            SUT.execute(USE_CASE_PARAMS).test {
                awaitComplete()
            }
        }
    }

    @Test
    fun `Saves remote articles into local data store when refresh is successful`() {
        runTest {
            coEvery { throttler.canRefreshArticles(any()) } returns true
            coEvery { articlesRemoteDataStore.getArticles(any()) } returns Ok(DOMAIN_ARTICLES)

            SUT.execute(USE_CASE_PARAMS).firstOrNull()

            coVerify { articlesLocalDataStore.saveArticles(DOMAIN_ARTICLES) }
        }
    }

    @Test
    fun `Does not save remote articles into local data store when refresh is not possible`() {
        runTest {
            coEvery { throttler.canRefreshArticles(any()) } returns false

            SUT.execute(USE_CASE_PARAMS).firstOrNull()

            coVerifyNotCalled { articlesLocalDataStore.saveArticles(any()) }
        }
    }

    @Test
    fun `Does not save remote articles into local data store when refresh is unsuccessful`() {
        runTest {
            coEvery { throttler.canRefreshArticles(any()) } returns false
            coEvery { articlesRemoteDataStore.getArticles(any()) } returns Err(DOMAIN_ERROR_UNKNOWN)

            SUT.execute(USE_CASE_PARAMS).firstOrNull()

            coVerifyNotCalled { articlesLocalDataStore.saveArticles(any()) }
        }
    }

    @Test
    fun `Updates articles last refresh time when refresh is successful`() {
        runTest {
            coEvery { throttler.canRefreshArticles(any()) } returns true
            coEvery { articlesRemoteDataStore.getArticles(any()) } returns Ok(DOMAIN_ARTICLES)

            SUT.execute(USE_CASE_PARAMS).firstOrNull()

            coVerify { throttler.updateArticlesLastRefreshTime(any()) }
        }
    }

    @Test
    fun `Does not update articles last refresh time when refresh is not possible`() {
        runTest {
            coEvery { throttler.canRefreshArticles(any()) } returns false

            SUT.execute(USE_CASE_PARAMS).firstOrNull()

            coVerifyNotCalled { throttler.updateArticlesLastRefreshTime(any()) }
        }
    }

    @Test
    fun `Does not update articles last refresh time when refresh is unsuccessful`() {
        runTest {
            coEvery { throttler.canRefreshArticles(any()) } returns false
            coEvery { articlesRemoteDataStore.getArticles(any()) } returns Err(DOMAIN_ERROR_UNKNOWN)

            SUT.execute(USE_CASE_PARAMS).firstOrNull()

            coVerifyNotCalled { throttler.updateArticlesLastRefreshTime(any()) }
        }
    }
}