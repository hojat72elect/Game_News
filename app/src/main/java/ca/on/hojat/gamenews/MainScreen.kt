package ca.on.hojat.gamenews

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@Preview
@Composable
internal fun MainScreen() {
    val navController = rememberAnimatedNavController()
    val currentScreen by navController.currentScreenAsState()

    Scaffold(
        bottomBar = {
            BottomBar(
                navController = navController,
                currentScreen = currentScreen,
            )
        },
        content = { paddingValues ->
            AppNavigation(
                navController = navController,
                modifier = Modifier.padding(paddingValues),
            )
        },
    )
}
