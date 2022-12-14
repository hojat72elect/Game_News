package ca.on.hojat.gamenews.core.data.api.gamespot

import ca.on.hojat.gamenews.core.common_testing.startSafe
import com.github.michaelbull.result.get
import com.github.michaelbull.result.getError
import com.google.common.truth.Truth.assertThat
import ca.on.hojat.gamenews.core.data.api.gamespot.articles.entities.ApiArticle
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import ca.on.hojat.gamenews.core.data.api.common.Error
import ca.on.hojat.gamenews.core.data.api.gamespot.articles.ArticlesService

@HiltAndroidTest
internal class ArticlesServiceTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var mockWebServer: MockWebServer

    @Inject
    lateinit var articlesService: ArticlesService

    @Before
    fun setup() {
        hiltRule.inject()
        mockWebServer.startSafe()
    }

    @Test
    fun http_error_is_returned_when_articles_endpoint_returns_bad_request_response() {
        runBlocking {
            mockWebServer.enqueue(MockResponse().setResponseCode(400))

            val error = articlesService.getArticles(emptyMap()).getError()

            assertThat(error is Error.HttpError).isTrue()
        }
    }

    @Test
    fun http_error_with_400_code_is_returned_when_articles_endpoint_returns_bad_request_response() {
        runBlocking {
            mockWebServer.enqueue(MockResponse().setResponseCode(400))

            val error = articlesService.getArticles(emptyMap()).getError()

            assertThat(error is Error.HttpError).isTrue()
            assertThat((error as Error.HttpError).code).isEqualTo(400)
        }
    }

    @Test
    fun http_error_with_proper_error_message_is_returned_when_articles_endpoint_returns_bad_request_response() {
        runBlocking {
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(400)
                    .setBody("{\"error\": \"Invalid API Key\"}")
            )

            val error = articlesService.getArticles(emptyMap()).getError()

            assertThat(error is Error.HttpError).isTrue()
            assertThat((error as Error.HttpError).message).isEqualTo("Invalid API Key")
        }
    }

    @Test
    fun http_error_with_unknown_error_message_is_returned_when_articles_endpoint_returns_bad_request_response() {
        runBlocking {
            val errorBody = "{\"message\": \"Invalid API Key\"}"

            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(400)
                    .setBody(errorBody)
            )

            val error = articlesService.getArticles(emptyMap()).getError()

            assertThat(error is Error.HttpError).isTrue()
            assertThat((error as Error.HttpError).message).isNotEmpty()
        }
    }

    @Test
    fun parsed_articles_are_returned_when_articles_endpoint_returns_successful_response() {
        runBlocking {
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody(
                        """
                        {
                            "results": [
                                {
                                    "publish_date": "2021-03-06 10:26:00",
                                    "id": 6488493,
                                    "title": "Will GTA Online Get Cross-Play?",
                                    "lede": "lede1",
                                    "site_detail_url": "site_detail_url1"
                                },
                                {
                                    "publish_date": "2021-03-06 10:00:00",
                                    "id": 6488487,
                                    "title": "How Games Make It Fun To Be The Villain",
                                    "lede": "lede2",
                                    "site_detail_url": "site_detail_url2"
                                }
                            ]
                        }
                        """.trimIndent()
                    )
            )

            val parsedArticles = articlesService.getArticles(emptyMap()).get()!!.results
            val expectedArticles = listOf(
                ApiArticle(
                    id = 6488493,
                    publicationDate = "2021-03-06 10:26:00",
                    title = "Will GTA Online Get Cross-Play?",
                    lede = "lede1",
                    siteDetailUrl = "site_detail_url1"
                ),
                ApiArticle(
                    id = 6488487,
                    publicationDate = "2021-03-06 10:00:00",
                    title = "How Games Make It Fun To Be The Villain",
                    lede = "lede2",
                    siteDetailUrl = "site_detail_url2"
                )
            )

            assertThat(parsedArticles).isEqualTo(expectedArticles)
        }
    }

    @Test
    fun unknown_error_is_returned_when_articles_endpoint_returns_successful_response_with_no_body() {
        runBlocking {
            mockWebServer.enqueue(MockResponse().setResponseCode(200))

            val error = articlesService.getArticles(emptyMap()).getError()

            assertThat(error is Error.UnknownError).isTrue()
        }
    }

    @Test
    fun unknown_error_is_returned_when_articles_endpoint_returns_successful_response_with_bad_json() {
        runBlocking {
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody(
                        """
                        {
                            "results": 50
                        }
                        """.trimIndent()
                    )
            )

            val error = articlesService.getArticles(emptyMap()).getError()

            assertThat(error is Error.UnknownError).isTrue()
        }
    }

    @Test
    fun network_error_is_returned_when_network_is_disconnected_while_fetching_articles() {
        runBlocking {
            mockWebServer.enqueue(
                MockResponse()
                    .setSocketPolicy(SocketPolicy.DISCONNECT_AT_START)
            )

            val error = articlesService.getArticles(emptyMap()).getError()

            assertThat(error is Error.NetworkError).isTrue()
        }
    }

    @After
    fun cleanup() {
        mockWebServer.shutdown()
    }
}
