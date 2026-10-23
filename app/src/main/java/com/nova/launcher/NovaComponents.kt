package com.nova.launcher

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

/** Tarjeta de app: crece y brilla al recibir el foco del control remoto. */
@Composable
fun NovaTile(
    label: String,
    modifier: Modifier = Modifier,
    icon: ImageBitmap? = null,
    glyph: String? = null,
    growOnFocus: Boolean = true,
    onFocused: () -> Unit = {},
    onClick: () -> Unit
) {
    val d = LocalDim.current
    val accent = LocalAccent.current
    var focused by remember { mutableStateOf(false) }
    val interaction = remember { MutableInteractionSource() }

    val normalSize = d.d(200)
    val focusedSize = if (growOnFocus) d.d(260) else normalSize
    val tileSize by animateDpAsState(
        targetValue = if (focused) focusedSize else normalSize,
        animationSpec = tween(durationMillis = 180),
        label = "tileSize"
    )
    val shape = RoundedCornerShape(tileSize * 0.24f)

    Column(
        modifier = modifier
            .onFocusChanged { state ->
                focused = state.isFocused
                if (state.isFocused) onFocused()
            }
            .clickable(interactionSource = interaction, indication = null, onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(tileSize)
                .drawBehind {
                    if (focused) {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(accent.copy(alpha = 0.32f), Color.Transparent),
                                radius = size.minDimension * 0.95f
                            )
                        )
                    }
                }
                .background(
                    color = if (focused) accent.copy(alpha = 0.12f) else Color.White.copy(alpha = 0.05f),
                    shape = shape
                )
                .border(
                    width = if (focused) d.d(2) else d.d(1),
                    color = if (focused) accent else Color.White.copy(alpha = 0.10f),
                    shape = shape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (icon != null) {
                Image(
                    bitmap = icon,
                    contentDescription = null,
                    modifier = Modifier.size(tileSize * 0.54f)
                )
            } else if (glyph != null) {
                StrokeIcon(
                    pathData = glyph,
                    iconSize = tileSize * 0.34f,
                    tint = if (focused) accent else NovaTheme.textSoft,
                    strokeWidth = 1.4f
                )
            }
        }
        Spacer(modifier = Modifier.height(d.d(18)))
        Text(
            text = label,
            modifier = Modifier.width(tileSize),
            color = if (focused) NovaTheme.textBright else NovaTheme.muted,
            fontSize = if (focused) d.t(26) else d.t(22),
            fontWeight = if (focused) FontWeight.Normal else FontWeight.Light,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/** Botón en forma de cápsula (accesos rápidos, Gemini, volver). */
@Composable
fun NovaPill(
    label: String,
    glyph: String,
    modifier: Modifier = Modifier,
    highlighted: Boolean = false,
    compact: Boolean = false,
    onClick: () -> Unit
) {
    val d = LocalDim.current
    val accent = LocalAccent.current
    var focused by remember { mutableStateOf(false) }
    val interaction = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(50)
    val active = focused || highlighted

    val fill = when {
        focused -> accent.copy(alpha = 0.18f)
        highlighted -> accent.copy(alpha = 0.08f)
        else -> Color.White.copy(alpha = 0.06f)
    }
    val outline = when {
        focused -> accent
        highlighted -> accent.copy(alpha = 0.65f)
        else -> Color.White.copy(alpha = 0.12f)
    }

    Row(
        modifier = modifier
            .height(if (compact) d.d(56) else d.d(72))
            .background(color = fill, shape = shape)
            .border(width = if (focused) d.d(2) else d.d(1), color = outline, shape = shape)
            .onFocusChanged { state -> focused = state.isFocused }
            .clickable(interactionSource = interaction, indication = null, onClick = onClick)
            .padding(start = d.d(24), end = d.d(32)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        StrokeIcon(
            pathData = glyph,
            iconSize = if (compact) d.d(26) else d.d(30),
            tint = if (active) accent else NovaTheme.textSoft
        )
        Spacer(modifier = Modifier.width(d.d(14)))
        Text(
            text = label,
            color = if (active) accent else NovaTheme.text,
            fontSize = d.t(22),
            fontWeight = FontWeight.Normal,
            maxLines = 1
        )
    }
}

/** Opción seleccionable con vista previa (fondos y colores de acento). */
@Composable
fun NovaChoice(
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    preview: @Composable BoxScope.() -> Unit
) {
    val d = LocalDim.current
    val accent = LocalAccent.current
    var focused by remember { mutableStateOf(false) }
    val interaction = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(d.d(28))

    Column(
        modifier = modifier
            .onFocusChanged { state -> focused = state.isFocused }
            .clickable(interactionSource = interaction, indication = null, onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .width(d.d(224))
                .height(d.d(140))
                .clip(shape)
                .background(color = NovaTheme.bg, shape = shape)
        ) {
            preview()
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .border(
                        width = if (focused || selected) d.d(2) else d.d(1),
                        color = when {
                            focused -> NovaTheme.textBright
                            selected -> accent
                            else -> Color.White.copy(alpha = 0.12f)
                        },
                        shape = shape
                    )
            )
            if (selected) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(d.d(10))
                        .size(d.d(32))
                        .background(color = accent, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    StrokeIcon(
                        pathData = NovaIcons.Check,
                        iconSize = d.d(20),
                        tint = NovaTheme.bg,
                        strokeWidth = 2.4f
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(d.d(12)))
        Text(
            text = label,
            color = if (focused || selected) NovaTheme.textBright else NovaTheme.muted,
            fontSize = d.t(22),
            fontWeight = if (focused || selected) FontWeight.Normal else FontWeight.Light,
            maxLines = 1
        )
    }
}
