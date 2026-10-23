package com.nova.launcher

import android.text.format.DateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private const val MAX_HOME_APPS = 6

@Composable
fun HomeScreen(
    apps: List<AppEntry>,
    onOpenApps: () -> Unit,
    onOpenCustomize: () -> Unit
) {
    val context = LocalContext.current
    val d = LocalDim.current
    val accent = LocalAccent.current

    val nowMillis = rememberNow().value
    val is24h = DateFormat.is24HourFormat(context)

    val timeText = remember(nowMillis, is24h) {
        SimpleDateFormat(if (is24h) "HH:mm" else "h:mm", Locale.getDefault()).format(Date(nowMillis))
    }
    val dateText = remember(nowMillis) {
        SimpleDateFormat("EEEE · d MMMM", Locale.forLanguageTag("es")).format(Date(nowMillis)).uppercase()
    }
    val greeting = remember(nowMillis) {
        val hour = Calendar.getInstance().apply { timeInMillis = nowMillis }.get(Calendar.HOUR_OF_DAY)
        when (hour) {
            in 5..11 -> "Buenos días"
            in 12..19 -> "Buenas tardes"
            else -> "Buenas noches"
        }
    }

    val rowApps = remember(apps) { apps.take(MAX_HOME_APPS) }
    var focusedLabel by remember { mutableStateOf<String?>(null) }
    val firstTile = remember { FocusRequester() }
    val headline = focusedLabel ?: rowApps.firstOrNull()?.label ?: "Nova"

    LaunchedEffect(rowApps.isNotEmpty()) {
        repeat(5) {
            delay(150)
            if (runCatching { firstTile.requestFocus() }.isSuccess) return@LaunchedEffect
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = d.d(96), top = d.d(64))
        ) {
            Text(
                text = timeText,
                color = NovaTheme.text,
                fontSize = d.t(104),
                fontWeight = FontWeight.ExtraLight
            )
            Spacer(modifier = Modifier.height(d.d(10)))
            Text(
                text = dateText,
                color = NovaTheme.muted,
                fontSize = d.t(22),
                letterSpacing = d.t(5)
            )
        }

        NovaPill(
            label = "Gemini",
            glyph = NovaIcons.Mic,
            highlighted = true,
            onClick = { NovaActions.launchAssistant(context) },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = d.d(96), top = d.d(80))
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = d.d(96), top = d.d(286))
                .width(d.d(1500))
        ) {
            Text(
                text = greeting.uppercase(),
                color = accent,
                fontSize = d.t(22),
                letterSpacing = d.t(7),
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.height(d.d(16)))
            Text(
                text = headline,
                color = NovaTheme.text,
                fontSize = d.t(80),
                fontWeight = FontWeight.Light,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(d.d(10)))
            Text(
                text = "Pulsa OK para abrir",
                color = NovaTheme.muted,
                fontSize = d.t(28),
                fontWeight = FontWeight.Light
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = d.d(96), top = d.d(520))
                .height(d.d(360)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(d.d(32))
        ) {
            rowApps.forEachIndexed { index, app ->
                NovaTile(
                    label = app.label,
                    icon = app.icon,
                    modifier = if (index == 0) Modifier.focusRequester(firstTile) else Modifier,
                    onFocused = { focusedLabel = app.label },
                    onClick = { NovaActions.launchApp(context, app.packageName) }
                )
            }
            NovaTile(
                label = "Todas",
                glyph = NovaIcons.Grid,
                modifier = if (rowApps.isEmpty()) Modifier.focusRequester(firstTile) else Modifier,
                onFocused = { focusedLabel = "Todas las apps" },
                onClick = onOpenApps
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = d.d(96), bottom = d.d(80)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(d.d(20))
        ) {
            NovaPill(
                label = "Ajustes",
                glyph = NovaIcons.Sliders,
                onClick = { NovaActions.openSettings(context) }
            )
            NovaPill(
                label = "Red",
                glyph = NovaIcons.Wifi,
                onClick = { NovaActions.openNetworkSettings(context) }
            )
            NovaPill(
                label = "Personalizar",
                glyph = NovaIcons.Sparkle,
                highlighted = true,
                onClick = onOpenCustomize
            )
        }
    }
}

@Composable
private fun rememberNow(): State<Long> {
    val now = remember { mutableStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            val current = System.currentTimeMillis()
            now.value = current
            delay(60_000L - (current % 60_000L) + 50L)
        }
    }
    return now
}
