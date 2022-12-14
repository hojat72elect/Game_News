package ca.on.hojat.gamenews.feature_info.domain

import ca.on.hojat.gamenews.core.domain.entities.Game
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject

interface GameLikeCountCalculator {
    fun calculateLikeCount(game: Game): Int
}

@BindType
internal class GameLikeCountCalculatorImpl @Inject constructor() : GameLikeCountCalculator {

    override fun calculateLikeCount(game: Game): Int {
        val followerCount = (game.followerCount ?: 0)
        val hypeCount = (game.hypeCount ?: 0)

        return (followerCount + hypeCount)
    }
}
