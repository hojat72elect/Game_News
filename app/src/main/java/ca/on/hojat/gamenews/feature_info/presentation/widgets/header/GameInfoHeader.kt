@file:Suppress("LongMethod")

package ca.on.hojat.gamenews.feature_info.presentation.widgets.header

import android.content.res.Configuration
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import ca.on.hojat.gamenews.core.common_ui.theme.GameNewsTheme
import ca.on.hojat.gamenews.core.common_ui.theme.lightScrim
import ca.on.hojat.gamenews.core.common_ui.theme.subtitle3
import ca.on.hojat.gamenews.core.common_ui.widgets.Info
import ca.on.hojat.gamenews.R
import ca.on.hojat.gamenews.common_ui.clickable
import ca.on.hojat.gamenews.core.common_ui.widgets.GameCover
import ca.on.hojat.gamenews.feature_info.presentation.widgets.header.artworks.Artworks
import ca.on.hojat.gamenews.feature_info.presentation.widgets.header.artworks.GameInfoArtworkUiModel

private const val ConstraintIdArtworks = "artworks"
private const val ConstraintIdArtworksScrim = "artworks_scrim"
private const val ConstraintIdBackButton = "back_button"
private const val ConstraintIdPageIndicator = "page_indicator"
private const val ConstraintIdBackdrop = "backdrop"
private const val ConstraintIdCoverSpace = "cover_space"
private const val ConstraintIdCover = "cover"
private const val ConstraintIdLikeButton = "like_button"
private const val ConstraintIdFirstTitle = "first_title"
private const val ConstraintIdSecondTitle = "second_title"
private const val ConstraintIdReleaseDate = "release_date"
private const val ConstraintIdDeveloperName = "developer_name"
private const val ConstraintIdRating = "rating"
private const val ConstraintIdLikeCount = "like_count"
private const val ConstraintIdAgeRating = "age_rating"
private const val ConstraintIdGameCategory = "game_category"

private val CoverSpace = 40.dp
private val InfoIconSize = 34.dp

@Composable
internal fun GameInfoHeader(
    headerInfo: GameInfoHeaderUiModel,
    onArtworkClicked: (artworkIndex: Int) -> Unit,
    onBackButtonClicked: () -> Unit,
    onCoverClicked: () -> Unit,
    onLikeButtonClicked: () -> Unit,
) {
    val artworks = headerInfo.artworks
    val isPageIndicatorVisible by remember(artworks) { mutableStateOf(artworks.size > 1) }
    var selectedArtworkPage by rememberSaveable { mutableStateOf(0) }
    var secondTitleText by rememberSaveable { mutableStateOf("") }
    val isSecondTitleVisible by remember {
        derivedStateOf {
            secondTitleText.isNotEmpty()
        }
    }

    ConstraintLayout(
        constraintSet = constructExpandedConstraintSet(),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Artworks(
            artworks = artworks,
            isScrollingEnabled = true,
            modifier = Modifier.layoutId(ConstraintIdArtworks),
            onArtworkChanged = { page ->
                selectedArtworkPage = page
            },
            onArtworkClicked = onArtworkClicked,
        )

        Box(
            modifier = Modifier
                .layoutId(ConstraintIdArtworksScrim)
                .background(Color.Transparent),
        )

        Icon(
            painter = painterResource(R.drawable.arrow_left),
            contentDescription = null,
            modifier = Modifier
                .layoutId(ConstraintIdBackButton)
                .statusBarsPadding()
                .size(56.dp)
                .clickable(
                    indication = rememberRipple(
                        bounded = false,
                        radius = 18.dp,
                    ),
                    onClick = onBackButtonClicked,
                )
                .padding(GameNewsTheme.spaces.spacing_2_5)
                .background(
                    color = GameNewsTheme.colors.lightScrim,
                    shape = CircleShape,
                )
                .padding(GameNewsTheme.spaces.spacing_1_5),
            tint = Color.White,
        )

        if (isPageIndicatorVisible) {
            Text(
                text = stringResource(
                    R.string.game_info_header_page_indicator_template,
                    selectedArtworkPage + 1,
                    headerInfo.artworks.size,
                ),
                modifier = Modifier
                    .layoutId(ConstraintIdPageIndicator)
                    .statusBarsPadding()
                    .background(
                        color = GameNewsTheme.colors.lightScrim,
                        shape = RoundedCornerShape(20.dp),
                    )
                    .padding(
                        vertical = GameNewsTheme.spaces.spacing_1_5,
                        horizontal = GameNewsTheme.spaces.spacing_2_0,
                    ),
                color = Color.White,
                style = GameNewsTheme.typography.subtitle3,
            )
        }

        Box(
            modifier = Modifier
                .layoutId(ConstraintIdBackdrop)
                .shadow(
                    elevation = GameNewsTheme.spaces.spacing_0_5,
                    shape = RectangleShape,
                    clip = false,
                )
                .background(
                    color = GameNewsTheme.colors.surface,
                    shape = RectangleShape,
                )
                .clip(RectangleShape),
        )

        Spacer(
            modifier = Modifier
                .layoutId(ConstraintIdCoverSpace)
                .height(CoverSpace),
        )

        GameCover(
            title = null,
            imageUrl = headerInfo.coverImageUrl,
            modifier = Modifier.layoutId(ConstraintIdCover),
            onCoverClicked = if (headerInfo.hasCoverImageUrl) onCoverClicked else null,
        )

        FloatingActionButton(
            onClick = onLikeButtonClicked,
            modifier = Modifier.layoutId(ConstraintIdLikeButton),
            backgroundColor = GameNewsTheme.colors.secondary,
        ) {
            // Animated selector drawables are not currently supported by the Jetpack Compose.
            // https://issuetracker.google.com/issues/212418566
            // Consider to use the R.drawable.heart_animated_selector when the support arrives.

            Icon(
                painter = rememberAnimatedVectorPainter(
                    animatedImageVector = AnimatedImageVector.animatedVectorResource(
                        R.drawable.heart_animated_fill
                    ),
                    atEnd = headerInfo.isLiked,
                ),
                contentDescription = null,
                modifier = Modifier.size(52.dp),
                tint = GameNewsTheme.colors.onSecondary,
            )
        }

        Text(
            text = headerInfo.title,
            modifier = Modifier.layoutId(ConstraintIdFirstTitle),
            color = GameNewsTheme.colors.onPrimary,
            maxLines = 1,
            onTextLayout = { textLayoutResult ->
                if (textLayoutResult.hasVisualOverflow) {
                    val firstTitleWidth = textLayoutResult.size.width.toFloat()
                    val firstTitleOffset = Offset(firstTitleWidth, 0f)
                    val firstTitleVisibleTextEndIndex =
                        textLayoutResult.getOffsetForPosition(firstTitleOffset) + 1

                    secondTitleText = headerInfo.title.substring(firstTitleVisibleTextEndIndex)
                }
            },
            style = GameNewsTheme.typography.h6,
        )

        Box(modifier = Modifier.layoutId(ConstraintIdSecondTitle)) {
            if (isSecondTitleVisible) {
                // Remove font padding once https://issuetracker.google.com/issues/171394808
                // is implemented (includeFontPadding="false" in XML)
                Text(
                    text = secondTitleText,
                    color = GameNewsTheme.colors.onPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = GameNewsTheme.typography.h6,
                )
            }
        }

        Text(
            text = headerInfo.releaseDate,
            modifier = Modifier.layoutId(ConstraintIdReleaseDate),
            color = GameNewsTheme.colors.onSurface,
            style = GameNewsTheme.typography.subtitle3,
        )

        Box(modifier = Modifier.layoutId(ConstraintIdDeveloperName)) {
            if (headerInfo.hasDeveloperName) {
                Text(
                    text = checkNotNull(headerInfo.developerName),
                    color = GameNewsTheme.colors.onSurface,
                    style = GameNewsTheme.typography.subtitle3,
                )
            }
        }

        Info(
            icon = painterResource(R.drawable.star_circle_outline),
            title = headerInfo.rating,
            modifier = Modifier.layoutId(ConstraintIdRating),
            iconSize = InfoIconSize,
            titleTextStyle = GameNewsTheme.typography.caption,
        )
        Info(
            icon = painterResource(R.drawable.account_heart_outline),
            title = headerInfo.likeCount,
            modifier = Modifier.layoutId(ConstraintIdLikeCount),
            iconSize = InfoIconSize,
            titleTextStyle = GameNewsTheme.typography.caption,
        )
        Info(
            icon = painterResource(R.drawable.age_rating_outline),
            title = headerInfo.ageRating,
            modifier = Modifier.layoutId(ConstraintIdAgeRating),
            iconSize = InfoIconSize,
            titleTextStyle = GameNewsTheme.typography.caption,
        )
        Info(
            icon = painterResource(R.drawable.shape_outline),
            title = headerInfo.gameCategory,
            modifier = Modifier.layoutId(ConstraintIdGameCategory),
            iconSize = InfoIconSize,
            titleTextStyle = GameNewsTheme.typography.caption,
        )
    }
}

@Composable
private fun constructExpandedConstraintSet(): ConstraintSet {
    val artworksHeight = 240.dp
    val pageIndicatorMargin = GameNewsTheme.spaces.spacing_2_5
    val coverSpaceMargin = CoverSpace
    val coverMarginStart = GameNewsTheme.spaces.spacing_3_5
    val likeBtnMarginEnd = GameNewsTheme.spaces.spacing_2_5
    val titleMarginStart = GameNewsTheme.spaces.spacing_3_5
    val firstTitleMarginEnd = GameNewsTheme.spaces.spacing_1_0
    val secondTitleMarginEnd = GameNewsTheme.spaces.spacing_3_5
    val releaseDateMarginTop = GameNewsTheme.spaces.spacing_2_5
    val releaseDateMarginHorizontal = GameNewsTheme.spaces.spacing_3_5
    val developerNameMarginHorizontal = GameNewsTheme.spaces.spacing_3_5
    val bottomBarrierMargin = GameNewsTheme.spaces.spacing_5_0
    val infoItemMarginBottom = GameNewsTheme.spaces.spacing_3_5

    return ConstraintSet {
        val artworks = createRefFor(ConstraintIdArtworks)
        val artworksScrim = createRefFor(ConstraintIdArtworksScrim)
        val backButton = createRefFor(ConstraintIdBackButton)
        val pageIndicator = createRefFor(ConstraintIdPageIndicator)
        val backdrop = createRefFor(ConstraintIdBackdrop)
        val coverSpace = createRefFor(ConstraintIdCoverSpace)
        val cover = createRefFor(ConstraintIdCover)
        val likeButton = createRefFor(ConstraintIdLikeButton)
        val firstTitle = createRefFor(ConstraintIdFirstTitle)
        val secondTitle = createRefFor(ConstraintIdSecondTitle)
        val releaseDate = createRefFor(ConstraintIdReleaseDate)
        val developerName = createRefFor(ConstraintIdDeveloperName)
        val bottomBarrier = createBottomBarrier(cover, developerName, margin = bottomBarrierMargin)
        val rating = createRefFor(ConstraintIdRating)
        val likeCount = createRefFor(ConstraintIdLikeCount)
        val ageRating = createRefFor(ConstraintIdAgeRating)
        val gameCategory = createRefFor(ConstraintIdGameCategory)

        constrain(artworks) {
            width = Dimension.fillToConstraints
            height = Dimension.value(artworksHeight)
            top.linkTo(parent.top)
            centerHorizontallyTo(parent)
        }
        constrain(artworksScrim) {
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
            centerVerticallyTo(artworks)
            centerHorizontallyTo(artworks)
        }
        constrain(backButton) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
        }
        constrain(pageIndicator) {
            top.linkTo(parent.top, pageIndicatorMargin)
            end.linkTo(parent.end, pageIndicatorMargin)
        }
        constrain(backdrop) {
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
            top.linkTo(artworks.bottom)
            bottom.linkTo(parent.bottom)
            centerHorizontallyTo(parent)
        }
        constrain(coverSpace) {
            start.linkTo(parent.start)
            bottom.linkTo(artworks.bottom, coverSpaceMargin)
        }
        constrain(cover) {
            top.linkTo(coverSpace.bottom)
            start.linkTo(parent.start, coverMarginStart)
        }
        constrain(likeButton) {
            top.linkTo(artworks.bottom)
            bottom.linkTo(artworks.bottom)
            end.linkTo(parent.end, likeBtnMarginEnd)
        }
        constrain(firstTitle) {
            width = Dimension.fillToConstraints
            top.linkTo(artworks.bottom, titleMarginStart)
            start.linkTo(cover.end, titleMarginStart)
            end.linkTo(likeButton.start, firstTitleMarginEnd)
        }
        constrain(secondTitle) {
            width = Dimension.fillToConstraints
            top.linkTo(firstTitle.bottom)
            start.linkTo(cover.end, titleMarginStart)
            end.linkTo(parent.end, secondTitleMarginEnd)
        }
        constrain(releaseDate) {
            width = Dimension.fillToConstraints
            top.linkTo(secondTitle.bottom, releaseDateMarginTop)
            start.linkTo(cover.end, releaseDateMarginHorizontal)
            end.linkTo(parent.end, releaseDateMarginHorizontal)
        }
        constrain(developerName) {
            width = Dimension.fillToConstraints
            top.linkTo(releaseDate.bottom)
            start.linkTo(cover.end, developerNameMarginHorizontal)
            end.linkTo(parent.end, developerNameMarginHorizontal)
        }
        constrain(rating) {
            width = Dimension.fillToConstraints
            top.linkTo(bottomBarrier)
            bottom.linkTo(parent.bottom, infoItemMarginBottom)
            linkTo(start = parent.start, end = likeCount.start, bias = 0.25f)
        }
        constrain(likeCount) {
            width = Dimension.fillToConstraints
            top.linkTo(bottomBarrier)
            bottom.linkTo(parent.bottom, infoItemMarginBottom)
            linkTo(start = rating.end, end = ageRating.start, bias = 0.25f)
        }
        constrain(ageRating) {
            width = Dimension.fillToConstraints
            top.linkTo(bottomBarrier)
            bottom.linkTo(parent.bottom, infoItemMarginBottom)
            linkTo(start = likeCount.end, end = gameCategory.start, bias = 0.25f)
        }
        constrain(gameCategory) {
            width = Dimension.fillToConstraints
            top.linkTo(bottomBarrier)
            bottom.linkTo(parent.bottom, infoItemMarginBottom)
            linkTo(start = ageRating.end, end = parent.end, bias = 0.25f)
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun GameInfoHeaderPreview() {
    GameNewsTheme {
        GameInfoHeader(
            headerInfo = GameInfoHeaderUiModel(
                artworks = listOf(GameInfoArtworkUiModel.DefaultImage),
                isLiked = true,
                coverImageUrl = null,
                title = "Elden Ring",
                releaseDate = "Feb 25, 2022 (in a month)",
                developerName = "FromSoftware",
                rating = "N/A",
                likeCount = "92",
                ageRating = "N/A",
                gameCategory = "Main",
            ),
            onArtworkClicked = {},
            onBackButtonClicked = {},
            onCoverClicked = {},
            onLikeButtonClicked = {},
        )
    }
}
