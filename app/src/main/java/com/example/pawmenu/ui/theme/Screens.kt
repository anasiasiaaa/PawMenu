package com.example.pawmenu.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ---------- Онбординг ----------

@Composable
fun WelcomeScreen(onStart: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.weight(1f))
        Text("🐾", fontSize = 64.sp)
        Text("PawMenu", fontSize = 36.sp, fontWeight = FontWeight.ExtraBold)
        Spacer(Modifier.height(8.dp))
        Muted("Правильное питание для долгой и счастливой жизни вашего питомца")
        Spacer(Modifier.height(24.dp))
        Text("🐶🐱🐰🐴", fontSize = 44.sp)
        Spacer(Modifier.weight(1f))
        PrimaryButton("Начать", onStart)
        TextButton(onClick = {}) { Text("Уже есть аккаунт? Войти") }
    }
}

@Composable
fun AnimalScreen(onBack: () -> Unit, onNext: () -> Unit) {
    var sel by remember { mutableStateOf(0) }
    val items = listOf("🐶" to "Собака", "🐱" to "Кошка", "🐰" to "Кролик", "🐴" to "Лошадь")
    Page("Какое у вас животное?", "Выберите, чтобы настроить рацион", onBack, "Далее", onNext) {
        items.chunked(2).forEachIndexed { r, row ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                row.forEachIndexed { i, (emoji, name) ->
                    val idx = r * 2 + i
                    PawCard(Modifier.weight(1f), selected = sel == idx, onClick = { sel = idx }) {
                        Column(Modifier.fillMaxWidth().padding(vertical = 12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(emoji, fontSize = 44.sp)
                            Text(name, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

/** onNext == null → экран открыт из вкладки «Питомцы», кнопки «Далее» нет. */
@Composable
fun PetProfileScreen(onBack: (() -> Unit)? = null, onNext: (() -> Unit)? = null) {
    val c = MaterialTheme.colorScheme
    Page("Профиль питомца", null, onBack, if (onNext != null) "Далее" else null, { onNext?.invoke() }) {
        Box(Modifier.align(Alignment.CenterHorizontally).size(88.dp).clip(CircleShape).background(c.secondaryContainer), Alignment.Center) {
            Text("🐶", fontSize = 46.sp)
        }
        Text("Боня ✎", Modifier.align(Alignment.CenterHorizontally), fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
        Muted("Собака · 7 лет · 7 кг", Modifier.align(Alignment.CenterHorizontally))
        PawCard {
            InfoRow("Порода", "Джек-рассел терьер")
            InfoRow("Активность", "Высокая")
            InfoRow("Стерилизация", "Нет")
            InfoRow("Цель", "Поддержание веса")
        }
        PawCard(onClick = {}) { Text("＋ Добавить ещё питомца", color = c.primary, fontWeight = FontWeight.SemiBold) }
    }
}

@Composable
fun FeedTypeScreen(onBack: () -> Unit, onNext: () -> Unit) {
    var sel by remember { mutableStateOf(0) }
    val items = listOf(
        Triple("🥣", "Готовый корм", "Сухой / влажный"),
        Triple("🥗", "Натуральное", "Свежие продукты и домашние блюда"),
        Triple("🥩", "BARF", "Сырое биологически подходящее питание")
    )
    Page("Тип питания", "Выберите, чем вы кормите питомца", onBack, "Далее", onNext) {
        items.forEachIndexed { i, (e, t, s) ->
            PawCard(selected = sel == i, onClick = { sel = i }) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(e, fontSize = 32.sp)
                    Column(Modifier.weight(1f)) { Text(t, fontWeight = FontWeight.Bold); Muted(s, size = 12) }
                    RadioButton(selected = sel == i, onClick = { sel = i })
                }
            }
        }
        Hint("♥ Тип питания можно изменить в настройках")
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AllergiesScreen(onBack: () -> Unit, onNext: () -> Unit) {
    Page("Аллергии и непереносимости", "Отметьте, что не подходит питомцу", onBack, "Сохранить", onNext) {
        Text("Аллергии", fontWeight = FontWeight.Bold)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Курица", "Говядина", "Рыба", "Яйца", "Пшеница", "＋").forEachIndexed { i, t -> PawChip(t, i == 0) }
        }
        Text("Непереносимости", fontWeight = FontWeight.Bold)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Лактоза", "Глютен", "Кукуруза", "Соя", "＋").forEach { PawChip(it) }
        }
        Hint("⚠ Корма с этими ингредиентами будут скрыты из подбора")
    }
}

@Composable
fun Hint(text: String) {
    Text(
        text, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSecondaryContainer,
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer).padding(12.dp)
    )
}

// ---------- Основное приложение ----------

@Composable
fun RationScreen(button: String, onBack: (() -> Unit)?, onDetails: () -> Unit, onSave: () -> Unit) {
    val c = MaterialTheme.colorScheme
    Page("Рекомендуемый рацион", null, onBack, button, onSave) {
        TabChips(listOf("БЖУ", "Микроэлементы", "Калории"))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Donut(listOf(0.28f to c.tertiary, 0.18f to c.error, 0.54f to c.primary), "58%")
            Column(Modifier.weight(1f)) {
                InfoRow("Белки", "28%"); InfoRow("Жиры", "18%"); InfoRow("Углеводы", "54%")
            }
        }
        Text("Пример дневного рациона", fontWeight = FontWeight.Bold)
        PawCard {
            listOf("Завтрак" to "120 г · 304 ккал", "Обед" to "150 г · 381 ккал", "Ужин" to "120 г · 304 ккал", "Перекус" to "50 г · 127 ккал")
                .forEach { (a, b) -> InfoRow(a, b) }
        }
        OutlinedButton(onClick = onDetails, Modifier.fillMaxWidth()) { Text("Состав рациона") }
    }
}

@Composable
fun CompositionScreen(onBack: () -> Unit) {
    Page("Состав рациона", null, onBack) {
        TabChips(listOf("БЖУ", "Витамины", "Минералы"))
        PawCard {
            BarRow("Белок", "22 г (28%)", 0.7f); BarRow("Жиры", "12 г (18%)", 0.45f)
            BarRow("Углеводы", "36 г (54%)", 0.8f); BarRow("Кальций", "800 мг (80%)", 0.8f)
            BarRow("Фосфор", "600 мг (75%)", 0.75f); BarRow("Железо", "8 мг (60%)", 0.6f)
        }
        OutlinedButton(onClick = {}, Modifier.fillMaxWidth()) { Text("Подробнее о микроэлементах") }
    }
}

@Composable
fun FoodPickerScreen(onBack: () -> Unit) {
    val c = MaterialTheme.colorScheme
    Page("Подбор корма", null, onBack) {
        TabChips(listOf("Готовый корм", "Натуральное", "BARF"))
        TabChips(listOf("Все", "Сухой", "Влажный"))
        PawCard {
            listOf("Royal Canin" to true, "Hill’s" to true, "Purina Pro Plan" to true, "Brit Care" to true, "Acana" to false).forEach { (n, ok) ->
                Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(c.surfaceVariant), Alignment.Center) { Text("🥫", fontSize = 20.sp) }
                    Column(Modifier.weight(1f)) {
                        Text(n, fontWeight = FontWeight.SemiBold)
                        Text(if (ok) "Подходит ✓" else "Есть курица ⚠", fontSize = 12.sp, color = if (ok) c.tertiary else c.error)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NotesScreen(onBack: () -> Unit) {
    Page("Особенности и заметки", null, onBack, "Сохранить", onBack) {
        Text("Индивидуальные особенности", fontWeight = FontWeight.Bold)
        PawCard { Text("Чувствительный ЖКТ, нужен корм без курицы", fontSize = 13.sp) }
        Text("Предпочтения", fontWeight = FontWeight.Bold)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) { PawChip("Любит говядину", true); PawChip("Не любит рыбу") }
        Text("Заметки", fontWeight = FontWeight.Bold)
        PawCard { Text("Ест медленно, периодически аллергия на пыльцу", fontSize = 13.sp) }
        Hint("♥ Новый корм вводится постепенно в течение 7–10 дней")
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DiaryScreen(onBack: () -> Unit) {
    Page("Дневник кормлений", null, onBack, "＋ Добавить кормление") {
        TabChips(listOf("Пн", "Вт", "Ср", "Чт", "Пт"))
        listOf("Завтрак · 120 г" to 1, "Обед · 150 г" to 0, "Ужин · 120 г" to 0).forEach { (t, n) ->
            var portions by remember { mutableStateOf(n) }
            PawCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(t, Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                    TextButton(onClick = { if (portions > 0) portions-- }) { Text("−", fontSize = 20.sp) }
                    Text("$portions")
                    TextButton(onClick = { portions++ }) { Text("＋", fontSize = 20.sp) }
                }
            }
        }
        Text("Реакция на корм", fontWeight = FontWeight.Bold)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("😋 Съел всё", "😐 Неохотно", "🤢 Рвота", "🔴 Зуд").forEachIndexed { i, t -> PawChip(t, i == 0) }
        }
    }
}

@Composable
fun HomeScreen(onPet: () -> Unit, onDiary: () -> Unit, onFood: () -> Unit, onRation: () -> Unit, onNotes: () -> Unit, onStub: () -> Unit) {
    val c = MaterialTheme.colorScheme
    Page("Привет, Анастасия!", "Забота о питомцах начинается с правильного питания") {
        PawCard(onClick = onPet) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(Modifier.size(44.dp).clip(CircleShape).background(c.secondaryContainer), Alignment.Center) { Text("🐶", fontSize = 24.sp) }
                Column(Modifier.weight(1f)) { Text("Боня", fontWeight = FontWeight.Bold); Muted("Джек-рассел терьер · 7 лет", size = 12) }
                Text("›", fontSize = 22.sp)
            }
        }
        Text("Сегодня", fontWeight = FontWeight.Bold)
        PawCard(onClick = onDiary) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Ring(0.75f, "75%")
                Column(Modifier.weight(1f)) { Text("Калории", fontWeight = FontWeight.Bold); Muted("из нормы · открыть дневник", size = 12) }
            }
        }
        listOf(
            listOf(Triple("⭐", "Рекомендации", onRation), Triple("🥫", "Корм", onFood)),
            listOf(Triple("🧪", "Анализы", onStub), Triple("📝", "Заметки", onNotes))
        ).forEach { row ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                row.forEach { (e, t, go) ->
                    PawCard(Modifier.weight(1f), onClick = go) {
                        Column(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(e, fontSize = 28.sp); Text(t, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(mode: ThemeMode, onMode: (ThemeMode) -> Unit) {
    val c = MaterialTheme.colorScheme
    Page("Профиль") {
        Text("Тема оформления", fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(ThemeMode.SYSTEM to "Авто", ThemeMode.LIGHT to "Светлая", ThemeMode.DARK to "Тёмная").forEach { (m, t) ->
                val on = m == mode
                Text(
                    t, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = if (on) c.onPrimary else c.onSurface,
                    modifier = Modifier.clip(CircleShape).background(if (on) c.primary else c.surface)
                        .border(1.dp, if (on) c.primary else c.outline, CircleShape)
                        .clickable { onMode(m) }.padding(horizontal = 14.dp, vertical = 8.dp)
                )
            }
        }
        Muted("Остальные настройки — заглушка")
    }
}

@Composable
fun StubScreen(title: String, onBack: () -> Unit) =
    Page(title, null, onBack) { Muted("Экран-заглушка") }
