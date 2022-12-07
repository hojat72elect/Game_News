package ca.on.hojat.gamenews.shared.core

import ca.on.hojat.gamenews.shared.core.formatters.GameRatingFormatterImpl
import ca.on.hojat.gamenews.shared.core.providers.StringProvider
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

internal class GameRatingFormatterImplTest {

    private lateinit var stringProvider: FakeStringProvider
    private lateinit var SUT: GameRatingFormatterImpl

    @Before
    fun setup() {
        stringProvider = FakeStringProvider()
        SUT = GameRatingFormatterImpl(stringProvider)
    }

    @Test
    fun `Returns not available string when rating is null`() {
        SUT.formatRating(null)

        assertThat(stringProvider.isRatingNotAvailable).isTrue()
    }

    @Test
    fun `Returns properly formatted string without coercing rating`() {
        val rating = 50.0

        SUT.formatRating(rating)

        assertThat(stringProvider.isRatingAvailable).isTrue()
        assertThat(stringProvider.rating).isEqualTo(rating.toInt())
    }

    @Test
    fun `Returns properly formatted string with rating coerced to min rating`() {
        val rating = -50.0

        SUT.formatRating(rating)

        assertThat(stringProvider.isRatingAvailable).isTrue()
        assertThat(stringProvider.rating).isEqualTo(0)
    }

    @Test
    fun `Returns properly formatted string with rating coerced to max rating`() {
        val rating = 150.0

        SUT.formatRating(rating)

        assertThat(stringProvider.isRatingAvailable).isTrue()
        assertThat(stringProvider.rating).isEqualTo(100)
    }

    private class FakeStringProvider : StringProvider {

        var isRatingAvailable = false
        var isRatingNotAvailable = false

        var rating = 0

        override fun getString(id: Int, vararg args: Any): String {
            isRatingAvailable = (id == R.string.rating_template)
            isRatingNotAvailable = (id == R.string.not_available_abbr)

            if (args.isNotEmpty()) rating = (args.first() as Int)

            return ""
        }

        override fun getQuantityString(id: Int, quantity: Int, vararg formatArgs: Any): String {
            return ""
        }
    }
}