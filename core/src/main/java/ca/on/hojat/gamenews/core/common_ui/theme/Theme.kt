package ca.on.hojat.gamenews.core.common_ui.theme

import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalElevationOverlay
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

private const val DefaultContentAlpha = 1f

object GameNewsTheme {

    val colors: Colors
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.colors

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography

    val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.shapes

    val spaces: Spaces
        @Composable
        @ReadOnlyComposable
        get() = LocalSpaces.current
}

@Composable
fun GameNewsTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
        CompositionLocalProvider(LocalElevationOverlay provides null) {
            MaterialTheme(
                colors = if (useDarkTheme) darkColors() else lightColors(),
                typography = typography,
                shapes = shapes,
            ) {
                CompositionLocalProvider(LocalRippleTheme provides GameNewsRippleTheme) {
                    CompositionLocalProvider(LocalContentAlpha provides DefaultContentAlpha) {
                        content()
                    }
                }
            }
        }
    }
}

@Immutable
private object GameNewsRippleTheme : RippleTheme {

    @Composable
    override fun defaultColor() = RippleTheme.defaultRippleColor(
        contentColor = Color.Black,
        lightTheme = true,
    )

    @Composable
    override fun rippleAlpha() = RippleTheme.defaultRippleAlpha(
        contentColor = Color.Black,
        lightTheme = true,
    )
}
