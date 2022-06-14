/*
 * Copyright 2021 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.paulrybitskyi.gamedge.commons.ui.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter.State
import com.paulrybitskyi.gamedge.commons.ui.images.defaultImageRequest
import com.paulrybitskyi.gamedge.commons.ui.images.secondaryImage
import com.paulrybitskyi.gamedge.commons.ui.theme.GamedgeTheme

val DefaultCoverWidth = 112.dp
val DefaultCoverHeight = 153.dp

@Composable
fun GameCover(
    title: String?,
    imageUrl: String?,
    onCoverClicked: () -> Unit,
    modifier: Modifier = Modifier,
    hasRoundedShape: Boolean = true,
) {
    GamedgeCard(
        onClick = onCoverClicked,
        modifier = Modifier
            .size(width = DefaultCoverWidth, height = DefaultCoverHeight)
            .then(modifier),
        shape = if (hasRoundedShape) GamedgeTheme.shapes.medium else RectangleShape,
        backgroundColor = Color.Transparent,
    ) {
        Box {
            var imageState by remember { mutableStateOf<State>(State.Empty) }
            // Not using derivedStateOf here because rememberSaveable does not support derivedStateOf?
            // https://stackoverflow.com/questions/71986944/custom-saver-remembersaveable-using-derivedstateof
            val shouldDisplayTitle = rememberSaveable(title, imageState) {
                (title != null) &&
                (imageState !is State.Success)
            }

            AsyncImage(
                model = defaultImageRequest(imageUrl) {
                    secondaryImage(R.drawable.game_cover_placeholder)
                },
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                onState = { state ->
                    imageState = state
                },
                contentScale = ContentScale.Crop,
            )

            if (shouldDisplayTitle) {
                Text(
                    text = checkNotNull(title),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = GamedgeTheme.spaces.spacing_4_0),
                    color = GamedgeTheme.colors.onPrimary,
                    textAlign = TextAlign.Center,
                    style = GamedgeTheme.typography.caption,
                )
            }
        }
    }
}

@Preview
@Composable
private fun GameCoverWithTitlePreview() {
    GamedgeTheme {
        GameCover(
            title = "Ghost of Tsushima: Director's Cut",
            imageUrl = null,
            onCoverClicked = {},
        )
    }
}

@Preview
@Composable
private fun GameCoverWithoutTitlePreview() {
    GamedgeTheme {
        GameCover(
            title = null,
            imageUrl = null,
            onCoverClicked = {},
        )
    }
}