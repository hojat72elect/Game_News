package ca.on.hojat.gamenews.core.common_ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val typography = Typography(
    defaultFontFamily = FontFamily.SansSerif,
    h5 = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
    ),
    h6 = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        letterSpacing = 0.50.sp,
    ),
    subtitle1 = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 17.sp,
        letterSpacing = 0.50.sp,
        lineHeight = 24.sp,
    ),
    subtitle2 = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        letterSpacing = 0.50.sp,
    ),
    body1 = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
        letterSpacing = 0.50.sp,
    ),
    body2 = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = 0.50.sp,
        lineHeight = 20.sp,
    ),
    button = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = 0.50.sp,
    ),
    caption = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = 0.50.sp,
    ),
)

private val SUBTITLE_3 = TextStyle(
    fontWeight = FontWeight.Medium,
    fontSize = 15.sp,
    letterSpacing = 0.50.sp,
)

@Suppress("unused")
val Typography.subtitle3: TextStyle
    get() = SUBTITLE_3
