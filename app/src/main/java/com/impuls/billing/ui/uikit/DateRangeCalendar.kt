package com.impuls.billing.ui.uikit


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.impuls.billing.ui.theme.BillingTheme
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import kotlinx.datetime.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Preview(showBackground = true)
@Composable
private fun Preview2() {
    BillingTheme {
        DateRangeCalendar(
            onRangeSelected = {_,_->}
        )
    }
}


@Composable
fun DateRangeCalendar(
    onRangeSelected: (startDate: LocalDate?, endDate: LocalDate?) -> Unit,
    modifier: Modifier = Modifier
) {
    // Настройка диапазона отображаемых месяцев (например, 2 года назад до 2 лет вперёд)
    val currentMonth = YearMonth.now()
    val startMonth = currentMonth.minusMonths(24)
    val endMonth = currentMonth.plusMonths(24)

    // Состояние календаря с горизонтальной прокруткой [citation:3][citation:6]
    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        outDateStyle = OutDateStyle.EndOfGrid
    )

    // Состояние для выбранного диапазона дат
    var selectedStartDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedEndDate by remember { mutableStateOf<LocalDate?>(null) }

    // Функция обработки выбора даты (реализация выбора диапазона)
    fun selectDate(date: LocalDate) {
        when {
            // Начальная дата не выбрана → выбираем начальную
            selectedStartDate == null -> {
                selectedStartDate = date
                selectedEndDate = null
            }
            // Выбрана только начальная дата
            selectedStartDate != null && selectedEndDate == null -> {
                if (date.isBefore(selectedStartDate)) {
                    // Если новая дата раньше начальной — меняем местами
                    selectedEndDate = selectedStartDate
                    selectedStartDate = date
                } else {
                    // Иначе завершаем диапазон
                    selectedEndDate = date
                }
                onRangeSelected(selectedStartDate, selectedEndDate)
            }
            // Диапазон уже выбран — начинаем новый
            else -> {
                selectedStartDate = date
                selectedEndDate = null
            }
        }
    }

    // Дни недели для заголовка
    val daysOfWeek = remember { daysOfWeek() }

    Column(modifier = modifier.fillMaxWidth()) {

        // Заголовок с днями недели
        DaysOfWeekHeader(daysOfWeek = daysOfWeek)

        // Горизонтальный календарь
        HorizontalCalendar(
            state = state,
            monthHeader = { month ->
                MonthHeader(month = month)
            },
            dayContent = { day ->
                DayCell(
                    date = day.date,
                    isStartDate = day.date == selectedStartDate,
                    isEndDate = day.date == selectedEndDate,
                    isInRange = isDateInRange(day.date, selectedStartDate, selectedEndDate),
                    isCurrentMonth = day.position == DayPosition.MonthDate,
                    onClick = { selectDate(day.date) }
                )
            }
        )
    }
}

@Composable
private fun DaysOfWeekHeader(daysOfWeek: List<DayOfWeek>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        daysOfWeek.forEach { dayOfWeek ->
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = when (dayOfWeek) {
                    DayOfWeek.MONDAY -> "ПН"
                    DayOfWeek.TUESDAY -> "ВТ"
                    DayOfWeek.WEDNESDAY -> "СР"
                    DayOfWeek.THURSDAY -> "ЧТ"
                    DayOfWeek.FRIDAY -> "ПТ"
                    DayOfWeek.SATURDAY -> "СБ"
                    DayOfWeek.SUNDAY -> "ВС"
                },
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun MonthHeader(month: CalendarMonth) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        textAlign = TextAlign.Center,
        text = "${month.yearMonth.month.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())} ${month.yearMonth.year}",
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
private fun DayCell(
    date: LocalDate,
    isStartDate: Boolean,
    isEndDate: Boolean,
    isInRange: Boolean,
    isCurrentMonth: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isStartDate || isEndDate -> MaterialTheme.colorScheme.primaryContainer
        isInRange -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        else -> Color.Transparent
    }

    val textColor = when {
        isStartDate || isEndDate -> MaterialTheme.colorScheme.onPrimaryContainer
        !isCurrentMonth -> MaterialTheme.colorScheme.onSurfaceVariant
        else -> MaterialTheme.colorScheme.onSurface
    }

    val shape = when {
        isStartDate && isEndDate -> RoundedCornerShape(8.dp)
        isStartDate -> RoundedCornerShape(8.dp)
        isEndDate -> RoundedCornerShape(8.dp)
        else -> RoundedCornerShape(0.dp)
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(shape)
            .background(backgroundColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            color = textColor,
            fontSize = 14.sp
        )
    }
}

// Вспомогательные функции
private fun firstDayOfWeek(): DayOfWeek = DayOfWeek.MONDAY

private fun daysOfWeek(): List<DayOfWeek> {
    val days = DayOfWeek.entries
    val firstDay = firstDayOfWeek()
    return days.slice(firstDay.ordinal..<days.size) + days.slice(0..<firstDay.ordinal)
}

private fun isDateInRange(
    date: LocalDate,
    startDate: LocalDate?,
    endDate: LocalDate?
): Boolean {
    return if (startDate != null && endDate != null) {
        date in startDate..endDate
    } else if (startDate != null) {
        date == startDate
    } else {
        false
    }
}