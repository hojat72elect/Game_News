package ca.on.hojat.gamenews.shared.ui

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut

sealed interface TransitionAnimations {

    companion object {
        const val DefaultAnimationDuration = 300
    }

    fun enter(): EnterTransition
    fun exit(): ExitTransition
    fun popEnter(): EnterTransition
    fun popExit(): ExitTransition
}

object Fading : TransitionAnimations {

    override fun enter(): EnterTransition {
        return fadeIn(
            animationSpec = tween(
                durationMillis = TransitionAnimations.DefaultAnimationDuration,
                easing = LinearEasing,
            ),
        )
    }

    override fun exit(): ExitTransition {
        return fadeOut(
            animationSpec = tween(
                durationMillis = TransitionAnimations.DefaultAnimationDuration,
                easing = LinearEasing,
            ),
        )
    }

    override fun popEnter(): EnterTransition {
        return enter()
    }

    override fun popExit(): ExitTransition {
        return exit()
    }
}

object OvershootScaling : TransitionAnimations {

    private const val FadingAnimationDuration = 100

    private const val MinScale = 0.9f
    private const val MaxScale = 1.1f

    private const val MinAlpha = 0f
    private const val MaxAlpha = 0.9f

    private val overshootInterpolator = OvershootInterpolator()

    override fun enter(): EnterTransition {
        return scaleIn(
            animationSpec = tween(
                durationMillis = TransitionAnimations.DefaultAnimationDuration,
                easing = Easing(overshootInterpolator::getInterpolation),
            ),
            initialScale = MaxScale,
        ) + fadeIn(
            animationSpec = tween(
                durationMillis = FadingAnimationDuration,
                easing = LinearEasing,
            ),
            initialAlpha = MaxAlpha,
        )
    }

    override fun exit(): ExitTransition {
        return scaleOut(
            animationSpec = tween(
                durationMillis = TransitionAnimations.DefaultAnimationDuration,
                easing = Easing(overshootInterpolator::getInterpolation),
            ),
            targetScale = MinScale,
        ) + fadeOut(
            animationSpec = tween(
                durationMillis = FadingAnimationDuration,
                easing = LinearEasing,
            ),
            targetAlpha = MaxAlpha,
        )
    }

    override fun popEnter(): EnterTransition {
        return scaleIn(
            animationSpec = tween(
                durationMillis = TransitionAnimations.DefaultAnimationDuration,
                easing = Easing(overshootInterpolator::getInterpolation),
            ),
            initialScale = MinScale,
        ) + fadeIn(
            animationSpec = tween(
                durationMillis = FadingAnimationDuration,
                easing = LinearEasing,
            ),
            initialAlpha = MaxAlpha,
        )
    }

    override fun popExit(): ExitTransition {
        return scaleOut(
            animationSpec = tween(
                durationMillis = TransitionAnimations.DefaultAnimationDuration,
                easing = Easing(overshootInterpolator::getInterpolation),
            ),
            targetScale = MaxScale,
        ) + fadeOut(
            animationSpec = tween(
                durationMillis = FadingAnimationDuration,
                easing = LinearEasing,
            ),
            targetAlpha = MinAlpha,
        )
    }
}

// Replace sliding animations with fading ones temporarily because
// for sliding to work properly, we need to be able to specify the
// zIndex of the entering and exiting animation.
//
// The GitHub issue: https://github.com/google/accompanist/issues/1160
object HorizontalSliding : TransitionAnimations {

//    private const val MinAlpha = 0.8f
//    private const val MaxOffsetRatio = 0.25f

    override fun enter(): EnterTransition {
        return Fading.enter()
//        return slideInHorizontally(
//            animationSpec = tween(TransitionAnimations.DefaultAnimationDuration),
//            initialOffsetX = HorizontalSliding::calculateMinOffsetX,
//        )
    }

    override fun exit(): ExitTransition {
        return Fading.exit()
//        return fadeOut(
//            animationSpec = tween(TransitionAnimations.DefaultAnimationDuration),
//            targetAlpha = MinAlpha,
//        ) + slideOutHorizontally(
//            animationSpec = tween(TransitionAnimations.DefaultAnimationDuration),
//            targetOffsetX = HorizontalSliding::calculateMaxOffsetX,
//        )
    }

    override fun popEnter(): EnterTransition {
        return Fading.popEnter()
//        return fadeIn(
//            animationSpec = tween(TransitionAnimations.DefaultAnimationDuration),
//            initialAlpha = MinAlpha,
//        ) + slideInHorizontally(
//            animationSpec = tween(TransitionAnimations.DefaultAnimationDuration),
//            initialOffsetX = HorizontalSliding::calculateMaxOffsetX,
//        )
    }

    override fun popExit(): ExitTransition {
        return Fading.popExit()
//        return slideOutHorizontally(
//            animationSpec = tween(TransitionAnimations.DefaultAnimationDuration),
//            targetOffsetX = HorizontalSliding::calculateMinOffsetX,
//        )
    }

//    private fun calculateMinOffsetX(fullWidth: Int): Int {
//        return fullWidth
//    }

//    private fun calculateMaxOffsetX(fullWidth: Int): Int {
//        return (-fullWidth * MaxOffsetRatio).roundToInt()
//    }
}
