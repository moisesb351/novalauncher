package com.nova.launcher

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.delay

@Composable
fun AppsScreen(
    apps: List<AppEntry>,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val d = LocalDim.current
    val firstTile = remember { FocusRequester() }

    LaunchedEffect(apps.isNotEmpty()) {
        repeat(5) {
            delay(150)
            if (runCatching { firstTile.requestFocus() }.isSuccess) return@LaunchedEffect
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        NovaPill(
            label = "Inicio",
            glyph = NovaIcons.Back,
            compact = true,
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = d.d(96), top = d.d(64))
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = d.d(96), top = d.d(150))
        ) {
            Text(
                text = "Todas las apps",
                color = NovaTheme.text,
                fontSize = d.t(64),
                fontWeight = FontWeight.Light
            )
            Spacer(modifier = Modifier.height(d.d(10)))
            Text(
                text = "${apps.size} apps instaladas",
                color = NovaTheme.muted,
                fontSize = d.t(26),
                fontWeight = FontWeight.Light
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .fillMaxSize()
                .padding(start = d.d(96), end = d.d(96), top = d.d(300)),
            contentPadding = PaddingValues(vertical = d.d(30)),
            horizontalArrangement = Arrangement.spacedBy(d.d(40)),
            verticalArrangement = Arrangement.spacedBy(d.d(40))
        ) {
            itemsIndexed(items = apps, key = { _, app -> app.packageName }) { index, app ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    NovaTile(
                        label = app.label,
                        icon = app.icon,
                        growOnFocus = false,
                        modifier = if (index == 0) Modifier.focusRequester(firstTile) else Modifier,
                        onClick = { NovaActions.launchApp(context, app.packageName) }
                    )
                }
            }
        }
    }
}
