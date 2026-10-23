package com.nova.launcher

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.delay

@Composable
fun CustomizeScreen(
    settings: NovaSettings,
    onBack: () -> Unit
) {
    val d = LocalDim.current
    val accent = LocalAccent.current
    val firstChoice = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        repeat(5) {
            delay(150)
            if (runCatching { firstChoice.requestFocus() }.isSuccess) return@LaunchedEffect
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
                text = "Personalizar",
                color = NovaTheme.text,
                fontSize = d.t(64),
                fontWeight = FontWeight.Light
            )
            Spacer(modifier = Modifier.height(d.d(10)))
            Text(
                text = "Fondo y color de acento",
                color = NovaTheme.muted,
                fontSize = d.t(26),
                fontWeight = FontWeight.Light
            )

            Spacer(modifier = Modifier.height(d.d(56)))
            Text(
                text = "01 · FONDO",
                color = accent,
                fontSize = d.t(22),
                letterSpacing = d.t(6)
            )
            Spacer(modifier = Modifier.height(d.d(20)))
            Row(horizontalArrangement = Arrangement.spacedBy(d.d(24))) {
                NovaTheme.backgrounds.forEachIndexed { index, style ->
                    NovaChoice(
                        label = style.label,
                        selected = settings.backgroundIndex == index,
                        modifier = if (index == 0) Modifier.focusRequester(firstChoice) else Modifier,
                        onClick = { settings.selectBackground(index) }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .drawBehind { drawNovaBackground(style) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(d.d(52)))
            Text(
                text = "02 · ACENTO",
                color = accent,
                fontSize = d.t(22),
                letterSpacing = d.t(6)
            )
            Spacer(modifier = Modifier.height(d.d(20)))
            Row(horizontalArrangement = Arrangement.spacedBy(d.d(24))) {
                NovaTheme.accents.forEachIndexed { index, named ->
                    NovaChoice(
                        label = named.name,
                        selected = settings.accentIndex == index,
                        onClick = { settings.selectAccent(index) }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(named.color)
                        )
                    }
                }
            }
        }
    }
}
