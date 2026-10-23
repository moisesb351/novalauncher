package com.nova.launcher

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

enum class Screen { Home, Apps, Customize }

@Composable
fun NovaApp(
    settings: NovaSettings,
    reloadKey: Int,
    homeSignal: Int
) {
    val context = LocalContext.current
    var screen by remember { mutableStateOf(Screen.Home) }
    var apps by remember { mutableStateOf<List<AppEntry>>(emptyList()) }

    LaunchedEffect(reloadKey) {
        apps = withContext(Dispatchers.Default) { AppRepository.loadApps(context) }
    }

    LaunchedEffect(homeSignal) {
        screen = Screen.Home
    }

    // En Inicio, Atrás no hace nada (un launcher no debe cerrarse); en las demás pantallas vuelve a Inicio.
    BackHandler {
        if (screen != Screen.Home) screen = Screen.Home
    }

    val accent = NovaTheme.accents[settings.accentIndex].color
    val style = NovaTheme.backgrounds[settings.backgroundIndex]

    ProvideDim {
        CompositionLocalProvider(LocalAccent provides accent) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(NovaTheme.bg)
                    .drawBehind { drawNovaBackground(style) }
            ) {
                when (screen) {
                    Screen.Home -> HomeScreen(
                        apps = apps,
                        onOpenApps = { screen = Screen.Apps },
                        onOpenCustomize = { screen = Screen.Customize }
                    )
                    Screen.Apps -> AppsScreen(
                        apps = apps,
                        onBack = { screen = Screen.Home }
                    )
                    Screen.Customize -> CustomizeScreen(
                        settings = settings,
                        onBack = { screen = Screen.Home }
                    )
                }
            }
        }
    }
}
