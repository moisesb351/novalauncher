package com.nova.launcher

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.Dp

/** Iconos de trazo fino (cuadrícula de 24x24), los mismos del diseño. */
object NovaIcons {
    const val Mic = "M12 3a3 3 0 0 0-3 3v6a3 3 0 0 0 6 0V6a3 3 0 0 0-3-3z M5 11a7 7 0 0 0 14 0 M12 18v3"
    const val Wifi = "M2.5 9a14 14 0 0 1 19 0 M5.5 12.5a9.5 9.5 0 0 1 13 0 M8.5 16a5 5 0 0 1 7 0 M12 19.5h.01"
    const val Sliders = "M4 7h10 M18 7h2 M4 17h2 M10 17h10 M16 5v4 M8 15v4"
    const val Sparkle = "M12 3l1.8 5.2L19 10l-5.2 1.8L12 17l-1.8-5.2L5 10l5.2-1.8z"
    const val Grid = "M4 4h6v6H4z M14 4h6v6h-6z M4 14h6v6H4z M14 14h6v6h-6z"
    const val Back = "M15 5l-7 7 7 7"
    const val Check = "M5 12.5l4.5 4.5L19 7.5"
}

@Composable
fun StrokeIcon(
    pathData: String,
    iconSize: Dp,
    tint: Color,
    modifier: Modifier = Modifier,
    strokeWidth: Float = 1.5f
) {
    val path = remember(pathData) { PathParser().parsePathString(pathData).toPath() }
    Canvas(modifier = modifier.size(iconSize)) {
        val factor = this.size.width / 24f
        scale(factor, factor, Offset.Zero) {
            drawPath(
                path = path,
                color = tint,
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }
    }
}
