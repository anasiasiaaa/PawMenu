package com.example.pawmenu.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Общий каркас экрана: заголовок, прокручиваемое содержимое, кнопка снизу. */
@Composable
fun Page(
    title: String,
    subtitle: String? = null,
    onBack: (() -> Unit)? = null,
    button: String? = null,
    onButton: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        if (onBack != null) TextButton(onClick = onBack) { Text("← Назад") } else Spacer(Modifier.height(16.dp))
        Text(title, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
        if (subtitle != null) Muted(subtitle)
        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp), content = content
        )
        if (button != null) PrimaryButton(button, onButton, Modifier.padding(bottom = 12.dp))
    }
}

@Composable
fun Muted(text: String, modifier: Modifier = Modifier, size: Int = 13) =
    Text(text, modifier, fontSize = size.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

@Composable
fun PrimaryButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) =
    Button(onClick, modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(14.dp)) {
        Text(text, fontWeight = FontWeight.Bold)
    }

@Composable
fun PawCard(
    modifier: Modifier = Modifier, selected: Boolean = false,
    onClick: (() -> Unit)? = null, content: @Composable ColumnScope.() -> Unit
) {
    val c = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(16.dp)
    var m = modifier.fillMaxWidth().clip(shape).background(c.surface)
        .border(if (selected) 2.dp else 1.dp, if (selected) c.primary else c.outline, shape)
    if (onClick != null) m = m.clickable(onClick = onClick)
    Column(m.padding(12.dp), content = content)
}

/** Чип-переключатель (аллергии, предпочтения, реакции). */
@Composable
fun PawChip(text: String, initiallyOn: Boolean = false) {
    var on by remember { mutableStateOf(initiallyOn) }
    val c = MaterialTheme.colorScheme
    Text(
        text, fontSize = 12.sp, fontWeight = FontWeight.SemiBold,
        color = if (on) c.onPrimary else c.onSecondaryContainer,
        modifier = Modifier.clip(CircleShape)
            .background(if (on) c.primary else c.secondaryContainer)
            .clickable { on = !on }.padding(horizontal = 14.dp, vertical = 8.dp)
    )
}

/** Вкладки-таблетки с одиночным выбором (БЖУ / Витамины / Минералы). */
@Composable
fun TabChips(items: List<String>) {
    var sel by remember { mutableStateOf(0) }
    val c = MaterialTheme.colorScheme
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items.forEachIndexed { i, t ->
            Text(
                t, fontSize = 12.sp, fontWeight = FontWeight.SemiBold,
                color = if (i == sel) c.onPrimary else c.onSurface,
                modifier = Modifier.clip(CircleShape)
                    .background(if (i == sel) c.primary else c.surface)
                    .border(1.dp, if (i == sel) c.primary else c.outline, CircleShape)
                    .clickable { sel = i }.padding(horizontal = 14.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 6.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Muted(label); Text(value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun BarRow(label: String, value: String, progress: Float) {
    val c = MaterialTheme.colorScheme
    Column(Modifier.padding(vertical = 4.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Muted(label, size = 12); Text(value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
        Spacer(Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress }, color = c.primary, trackColor = c.outline,
            modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape)
        )
    }
}

@Composable
fun Donut(parts: List<Pair<Float, Color>>, label: String, size: Int = 96) {
    Box(Modifier.size(size.dp), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val s = Stroke(16.dp.toPx())
            var start = -90f
            parts.forEach { (f, col) ->
                drawArc(
                    col, start, f * 360f - 2f, false, style = s,
                    topLeft = Offset(s.width / 2, s.width / 2),
                    size = Size(this.size.width - s.width, this.size.height - s.width)
                )
                start += f * 360f
            }
        }
        Text(label, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
    }
}

@Composable
fun Ring(progress: Float, label: String) {
    val c = MaterialTheme.colorScheme
    Box(Modifier.size(64.dp), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val s = Stroke(8.dp.toPx(), cap = StrokeCap.Round)
            val tl = Offset(s.width / 2, s.width / 2)
            val sz = Size(size.width - s.width, size.height - s.width)
            drawArc(c.outline, 0f, 360f, false, topLeft = tl, size = sz, style = s)
            drawArc(c.primary, -90f, 360f * progress, false, topLeft = tl, size = sz, style = s)
        }
        Text(label, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
    }
}
