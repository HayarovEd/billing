package com.impuls.billing.ui.payment_history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.impuls.billing.R
import com.impuls.billing.domain.model.HistoryPayment
import com.impuls.billing.domain.model.HistoryStatusPayment
import com.impuls.billing.domain.utils.toUiFormat
import com.impuls.billing.ui.theme.BillingTheme
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

@Composable
fun ItemHistoryPayment(
    modifier: Modifier = Modifier,
    historyPayment: HistoryPayment,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Верхняя строка: статус + дата
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadge(status = historyPayment.status)
                Text(
                    text = historyPayment.timestamp.toUiFormat(),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.amount),
                    fontSize = 12.sp,
                )
                Text(
                    text = "${historyPayment.amount} ${historyPayment.currency}",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 4.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            DetailRow(label = "Тип операции", value = historyPayment.message)
            /*DetailRow(
                label = "Статус операции",
                value = getStatusText(data.status),
                valueColor = getStatusColor(data.status)
            )*/
        }
    }
}

@Composable
fun StatusBadge(status: HistoryStatusPayment) {
    val (backgroundColor, textColor, text) = when (status) {
        HistoryStatusPayment.APPROVED -> {
            Triple(
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f),
                MaterialTheme.colorScheme.primary,
                stringResource(R.string.success_payment)
            )
        }
        HistoryStatusPayment.APPROVED_ON_FILE -> {
            Triple(
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f),
                MaterialTheme.colorScheme.primary,
                stringResource(R.string.success_payment)
            )
        }
        HistoryStatusPayment.CANCELLED -> {
            Triple(
                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f),
                MaterialTheme.colorScheme.error,
                stringResource(R.string.cancelled_payment)
            )
        }
    }

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = textColor,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun DetailRow(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


@Preview
@Composable
private fun Preview() {
    BillingTheme {
        ItemHistoryPayment(
            historyPayment = HistoryPayment(
                timestamp = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                amount = 1000.0,
                uid = 27790,
                status = HistoryStatusPayment.APPROVED,
                currency = "руб",
                message = "Платеж",
            )
        )
    }
}

@Preview
@Composable
private fun Preview1() {
    BillingTheme {
        ItemHistoryPayment(
            historyPayment = HistoryPayment(
                timestamp = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                amount = 1000.0,
                uid = 27790,
                status = HistoryStatusPayment.CANCELLED,
                currency = "руб",
                message = "Платеж",
            )
        )
    }
}