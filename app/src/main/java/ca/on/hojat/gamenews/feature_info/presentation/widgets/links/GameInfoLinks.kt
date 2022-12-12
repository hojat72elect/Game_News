package ca.on.hojat.gamenews.feature_info.presentation.widgets.links

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.LocalMinimumTouchTargetEnforcement
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ca.on.hojat.gamenews.shared.domain.games.entities.WebsiteCategory
import ca.on.hojat.gamenews.shared.ui.theme.GamedgeTheme
import ca.on.hojat.gamenews.shared.ui.widgets.GamedgeCard
import com.google.accompanist.flowlayout.FlowRow
import ca.on.hojat.gamenews.R
import ca.on.hojat.gamenews.feature_info.presentation.widgets.utils.GameInfoSection
import java.util.Locale

@Composable
internal fun GameInfoLinks(
    links: List<GameInfoLinkUiModel>,
    onLinkClicked: (GameInfoLinkUiModel) -> Unit,
) {
    GameInfoSection(title = stringResource(R.string.game_info_links_title)) { paddingValues ->
        FlowRow(
            modifier = Modifier.padding(paddingValues),
            mainAxisSpacing = GamedgeTheme.spaces.spacing_2_0,
            crossAxisSpacing = GamedgeTheme.spaces.spacing_3_0,
        ) {
            for (link in links) {
                Link(
                    link = link,
                    onLinkClicked = { onLinkClicked(link) },
                )
            }
        }
    }
}

@Composable
private fun Link(
    link: GameInfoLinkUiModel,
    onLinkClicked: () -> Unit,
) {
    CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
        GamedgeCard(
            onClick = onLinkClicked,
            shape = GamedgeTheme.shapes.small,
            backgroundColor = GamedgeTheme.colors.primaryVariant,
            contentColor = GamedgeTheme.colors.onSurface,
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = GamedgeTheme.spaces.spacing_1_5)
                    .padding(
                        start = GamedgeTheme.spaces.spacing_2_5,
                        end = GamedgeTheme.spaces.spacing_3_0,
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(link.iconId),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
                Text(
                    text = link.text,
                    modifier = Modifier.padding(start = GamedgeTheme.spaces.spacing_1_5),
                    style = GamedgeTheme.typography.button,
                )
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun GameInfoLinksPreview() {
    val links = WebsiteCategory.values()
        .filterNot { it == WebsiteCategory.UNKNOWN }
        .mapIndexed { index, websiteCategory ->
            GameInfoLinkUiModel(
                id = index,
                text = websiteCategory.name
                    .replace("_", " ")
                    .lowercase()
                    .replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                    },
                iconId = R.drawable.web,
                url = "url$index",
            )
        }

    GamedgeTheme {
        GameInfoLinks(
            links = links,
            onLinkClicked = {},
        )
    }
}