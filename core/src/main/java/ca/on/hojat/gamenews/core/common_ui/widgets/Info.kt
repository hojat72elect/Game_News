package ca.on.hojat.gamenews.core.common_ui.widgets

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ca.on.hojat.gamenews.core.R
import ca.on.hojat.gamenews.core.common_ui.theme.GameNewsTheme

@Composable
fun Info(
    icon: Painter,
    title: String,
    modifier: Modifier = Modifier,
    iconSize: Dp = 100.dp,
    iconColor: Color = GameNewsTheme.colors.onBackground,
    titleTextColor: Color = GameNewsTheme.colors.onBackground,
    titleTextStyle: TextStyle = GameNewsTheme.typography.subtitle1,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = icon,
            modifier = Modifier.size(iconSize),
            contentDescription = null,
            tint = iconColor,
        )
        Spacer(Modifier.height(GameNewsTheme.spaces.spacing_1_0))
        Text(
            text = title,
            color = titleTextColor,
            textAlign = TextAlign.Center,
            style = titleTextStyle,
        )
    }
}

@Preview(
    widthDp = 300,
    showBackground = true,
    backgroundColor = 0xfaf9f7,
)
@Preview(
    widthDp = 300,
    showBackground = true,
    backgroundColor = 0x1c2028,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun InfoWithSingleLineTitlePreview() {
    GameNewsTheme {
        Info(
            icon = painterResource(R.drawable.heart),
            title = "Lorem ipsum dolor sit amet",
        )
    }
}

@Preview(
    widthDp = 300,
    showBackground = true,
    backgroundColor = 0xfaf9f7,
)
@Preview(
    widthDp = 300,
    showBackground = true,
    backgroundColor = 0x1c2028,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun InfoWithMultiLineTitlePreview() {
    GameNewsTheme {
        Info(
            icon = painterResource(R.drawable.heart),
            title = "Lorem ipsum dolor sit amet\nLorem ipsum dolor sit amet",
        )
    }
}
