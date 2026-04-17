package com.impuls.billing.ui.payment_history

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.impuls.billing.R
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.DateRangeCalendar
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate

@Composable
fun ContentHistoryBottomSheet(
    modifier: Modifier = Modifier,
    onClickPeriodToday: () -> Unit,
    onClickPeriodWeek: () -> Unit,
    onClickPeriodMonth: () -> Unit,
    onClickPeriodCustom: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        PeriodSegmentedButton(
            onClick = onClickPeriodToday,
            text = stringResource(R.string.today)
        )
        PeriodSegmentedButton(
            onClick = onClickPeriodWeek,
            text = stringResource(R.string.week)
        )
        PeriodSegmentedButton(
            onClick = onClickPeriodMonth,
            text = stringResource(R.string.month)
        )
        PeriodSegmentedButton(
            onClick = onClickPeriodCustom,
            text = stringResource(R.string.choose_period)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentCalendarBottomSheet(
    modifier: Modifier = Modifier,
    onClickPeriod: (LocalDate, LocalDate) -> Unit,
    onClickCancel: () -> Unit,
) {
    var selectedStartDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedEndDate by remember { mutableStateOf<LocalDate?>(null) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        DateRangeCalendar(
            onRangeSelected = { start, end ->
                selectedStartDate = start?.toKotlinLocalDate()
                selectedEndDate = end?.toKotlinLocalDate()
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Кнопка Отмена
            OutlinedButton(
                onClick = onClickCancel,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline
                )
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.close_24dp),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.cancel),
                    style = MaterialTheme.typography.labelMedium
                )
            }

            // Кнопка Подтвердить/Сохранить
            Button(
                onClick = {
                    selectedStartDate?.let { st->
                        selectedEndDate?.let { en ->
                            onClickPeriod(st, en)
                        }
                    }
                },
                enabled = selectedStartDate!=null&&selectedEndDate!=null,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 2.dp,
                    pressedElevation = 4.dp
                )
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.check_circle_24dp),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.confirm),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}


@Composable
private fun PeriodSegmentedButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
        )
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.keyboard_arrow_right_24dp),
            contentDescription = null,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    BillingTheme {
        ContentHistoryBottomSheet(
            onClickPeriodToday = {},
            onClickPeriodWeek = {},
            onClickPeriodMonth = {},
            onClickPeriodCustom = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview1() {
    BillingTheme {
        ContentCalendarBottomSheet(
            onClickPeriod = {_,_->},
            onClickCancel = {}
        )
    }
}