package com.impuls.billing.ui.uikit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.impuls.billing.R
import com.impuls.billing.ui.theme.BillingTheme
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import androidx.compose.ui.platform.LocalLocale

@Preview(showBackground = true)
@Composable
private fun PreviewCalendar() {
    BillingTheme {
        DateRangeCalendar(
            onRangeSelected = { _, _ -> }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSingleCalendar() {
    BillingTheme {
        DateCalendar(
            currentDate = LocalDate.now(),
            updateCurrentDate = {}
        )
    }
}

@Composable
fun DateRangeCalendar(
    onRangeSelected: (startDate: LocalDate?, endDate: LocalDate?) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(24) }
    val endMonth = remember { currentMonth.plusMonths(24) }
    val scope = rememberCoroutineScope()

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        outDateStyle = OutDateStyle.EndOfGrid
    )

    var selectedStartDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedEndDate by remember { mutableStateOf<LocalDate?>(null) }

    fun selectDate(date: LocalDate) {
        if (date <= LocalDate.now()) {
            when {
                selectedStartDate == null -> {
                    selectedStartDate = date
                    selectedEndDate = null
                }

                selectedStartDate != null && selectedEndDate == null -> {
                    if (date.isBefore(selectedStartDate)) {
                        selectedEndDate = selectedStartDate
                        selectedStartDate = date
                    } else {
                        selectedEndDate = date
                    }
                    onRangeSelected(selectedStartDate, selectedEndDate)
                }

                else -> {
                    selectedStartDate = date
                    selectedEndDate = null
                }
            }
        }
    }

    val daysOfWeek = remember { daysOfWeek() }

    Column(modifier = modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.surface)) {
        MonthHeader(
            month = state.firstVisibleMonth,
            onPreviousMonth = {
                scope.launch {
                    state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.minusMonths(1))
                }
            },
            onNextMonth = {
                scope.launch {
                    state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.plusMonths(1))
                }
            }
        )

        DaysOfWeekHeader(daysOfWeek = daysOfWeek)

        HorizontalCalendar(
            state = state,
            dayContent = { day ->
                val isInRange = isDateInRange(day.date, selectedStartDate, selectedEndDate)

                DayCell(
                    date = day.date,
                    isStartDate = day.date == selectedStartDate,
                    isEndDate = day.date == selectedEndDate,
                    isInRange = isInRange,
                    isCurrentMonth = day.position == DayPosition.MonthDate,
                    onClick = { selectDate(day.date) }
                )
            }
        )
    }
}


@Composable
fun DateCalendar(
    modifier: Modifier = Modifier,
    currentDate: LocalDate,
    updateCurrentDate: (LocalDate) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val currentMonth = YearMonth.of(currentDate.year, currentDate.month)
    val state = rememberCalendarState(
        startMonth = currentMonth.minusMonths(24),
        endMonth = currentMonth.plusMonths(24),
        firstVisibleMonth = currentMonth,
        outDateStyle = OutDateStyle.EndOfGrid
    )

    var selectedDate by remember { mutableStateOf(currentDate) }
    fun selectDate(date: LocalDate) {
        if (date >= LocalDate.now()) {
            selectedDate = date
            updateCurrentDate(selectedDate)
        }
    }

    val daysOfWeek = remember { daysOfWeek() }

    Column(modifier = modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.surface)) {
        MonthHeader(
            month = state.firstVisibleMonth,
            onPreviousMonth = {
                scope.launch {
                    state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.minusMonths(1))
                }
            },
            onNextMonth = {
                scope.launch {
                    state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.plusMonths(1))
                }
            }
        )

        DaysOfWeekHeader(daysOfWeek = daysOfWeek)

        HorizontalCalendar(
            state = state,
            dayContent = { day ->
                DaySingleCell(
                    date = day.date,
                    isSelectedDate = day.date == selectedDate,
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
            .padding(bottom = 8.dp)
    ) {
        daysOfWeek.forEach { dayOfWeek ->
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, LocalLocale.current.platformLocale)
                    .uppercase(),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun MonthHeader(
    month: CalendarMonth,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onPreviousMonth) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.keyboard_arrow_left_24dp),
                contentDescription = "Previous Month",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Text(
            text = "${
                month.yearMonth.month.getDisplayName(
                    TextStyle.FULL_STANDALONE,
                    LocalLocale.current.platformLocale
                ).replaceFirstChar { it.uppercase() }
            } ${month.yearMonth.year}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        IconButton(onClick = onNextMonth) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.keyboard_arrow_right_24dp),
                contentDescription = "Next Month",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
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
    val isToday = date == LocalDate.now()

    // Определяем форму для эффекта "ленты"
    val shape = when {
        isStartDate && isEndDate -> CircleShape
        isStartDate -> RoundedCornerShape(topStart = 50.dp, bottomStart = 50.dp)
        isEndDate -> RoundedCornerShape(topEnd = 50.dp, bottomEnd = 50.dp)
        isInRange -> RoundedCornerShape(0.dp)
        else -> RoundedCornerShape(8.dp)
    }

    val backgroundColor = when {
        isStartDate || isEndDate -> MaterialTheme.colorScheme.primary
        isInRange -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
        else -> Color.Transparent
    }

    val textColor = when {
        isStartDate || isEndDate -> MaterialTheme.colorScheme.onPrimary
        !isCurrentMonth -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
        isToday -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.onSurface
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(vertical = 2.dp)
            .background(backgroundColor, shape)
            .clip(shape)
            .clickable(enabled = date <= LocalDate.now()) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = date.dayOfMonth.toString(),
                color = textColor,
                fontSize = 14.sp,
                fontWeight = if (isStartDate || isEndDate || isToday) FontWeight.Bold else FontWeight.Normal
            )
            if (isToday && !isStartDate && !isEndDate) {
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                )
            }
        }
    }
}


@Composable
private fun DaySingleCell(
    date: LocalDate,
    isSelectedDate: Boolean,
    isCurrentMonth: Boolean,
    onClick: () -> Unit
) {
    val isToday = date == LocalDate.now()


    val shape = when {
        isSelectedDate -> CircleShape
        else -> RoundedCornerShape(8.dp)
    }

    val backgroundColor = when {
        isSelectedDate -> MaterialTheme.colorScheme.primary
        else -> Color.Transparent
    }

    val textColor = when {
        isSelectedDate -> MaterialTheme.colorScheme.onPrimary
        !isCurrentMonth -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
        isToday -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.onSurface
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(vertical = 2.dp)
            .background(backgroundColor, shape)
            .clip(shape)
            .clickable(enabled = date >= LocalDate.now()) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = date.dayOfMonth.toString(),
                color = textColor,
                fontSize = 14.sp,
                fontWeight = if (isSelectedDate || isToday) FontWeight.Bold else FontWeight.Normal
            )
            if (isToday) {
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                )
            }
        }
    }
}

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
    } else {
        false
    }
}
