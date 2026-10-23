package com.nova.launcher

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class NamedColor(val name: String, val color: Color)

enum class BackgroundStyle(val label: String) {
    Aurora("Aurora"),
    Carbon("Carbón"),
    Horizon("Horizonte")
}

object NovaTheme {
    val bg = Color(0xFF05070B)
    val text = Color(0xFFEAF0F7)
    val textBright = Color(0xFFFFFFFF)
    val textSoft = Color(0xFFC9D3E0)
    val muted = Color(0xFF8E9AAB)

    val accents: List<NamedColor> = listOf(
        NamedColor("Hielo", Color(0xFF6FE9FF)),
        NamedColor("Menta", Color(0xFF7CF2B0)),
        NamedColor("Violeta", Color(0xFFB79CFF)),
        NamedColor("Ámbar", Color(0xFFFFB86B))
    )

    val backgrounds: List<BackgroundStyle> = BackgroundStyle.values().toList()
}

val LocalAccent = staticCompositionLocalOf { Color(0xFF6FE9FF) }

/**
 * Convierte medidas del diseño (pensado en 1920x1080) a dp/sp reales,
 * sea cual sea la densidad que reporte la TV.
 */
class Dim(private val scale: Float) {
    fun d(px: Int): Dp = (px * scale).dp
    fun t(px: Int): TextUnit = (px * scale).sp
}

val LocalDim = staticCompositionLocalOf { Dim(0.5f) }

@Composable
fun ProvideDim(content: @Composable () -> Unit) {
    val widthDp = LocalConfiguration.current.screenWidthDp
    val scale = if (widthDp > 0) widthDp / 1920f else 0.5f
    CompositionLocalProvider(LocalDim provides Dim(scale), content = content)
}

fun DrawScope.drawNovaBackground(style: BackgroundStyle) {
    when (style) {
        BackgroundStyle.Aurora -> {
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0x333CD2FF), Color.Transparent),
                    center = Offset(size.width * 0.8f, size.height * 0.08f),
                    radius = size.width * 0.5f
                )
            )
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0x337058FF), Color.Transparent),
                    center = Offset(size.width * 0.08f, size.height),
                    radius = size.width * 0.55f
                )
            )
        }
        BackgroundStyle.Carbon -> {
            drawRect(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF10151D), Color(0xFF05070B)),
                    start = Offset.Zero,
                    end = Offset(size.width * 0.4f, size.height)
                )
            )
        }
        BackgroundStyle.Horizon -> {
            drawRect(
                brush = Brush.verticalGradient(
                    0.45f to Color.Transparent,
                    1f to Color(0x66FFB86B)
                )
            )
        }
    }
}
