package com.impuls.billing.ui.promise_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import com.impuls.billing.domain.model.PromisePayment
import com.impuls.billing.domain.model.StatusPromisePayment
import com.impuls.billing.domain.utils.toUiFormat
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.asStatusPromisePaymentText
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

@Composable
fun PromisePaymentCard(
    payment: PromisePayment,
    modifier: Modifier = Modifier,
) {
    val statusColor = when (payment.status) {
        StatusPromisePayment.EXPIRED -> MaterialTheme.colorScheme.error// Красный
        StatusPromisePayment.PAYED -> MaterialTheme.colorScheme.primary  // Зеленый
        StatusPromisePayment.WORKED -> MaterialTheme.colorScheme.secondary // Желтый
    }


    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Верхняя строка: сумма и статус
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${payment.amount} ${payment.symbol}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = statusColor.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = payment.status.asStatusPromisePaymentText().asString(),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        color = statusColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Комиссия
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.comission),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = "${payment.fee} ${payment.symbol}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant
            )

            // Даты
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.date_request),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = payment.promiseDate.toUiFormat(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stringResource(R.string.validity_period),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = payment.promiseTill.toUiFormat(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (payment.status == StatusPromisePayment.EXPIRED)
                            MaterialTheme.colorScheme.error else Color.Unspecified
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview1() {
    BillingTheme {
        PromisePaymentCard(
            payment = PromisePayment(
                agrmId = 1,
                agrmNum = "1",
                amount = 600.0,
                cancelFio = "",
                cancelPerson = 1,
                currId = 1,
                debt = 200.0,
                fee = 10.0,
                modFio = "",
                modPerson = 1,
                payId = 1,
                promiseDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                promiseTill = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                recordId = 333,
                status = StatusPromisePayment.EXPIRED,
                symbol = "Rub",
                username = ""
            )
        )
    }
}

@Preview
@Composable
private fun Preview2() {
    BillingTheme {
        PromisePaymentCard(
            payment = PromisePayment(
                agrmId = 1,
                agrmNum = "1",
                amount = 600.0,
                cancelFio = "",
                cancelPerson = 1,
                currId = 1,
                debt = 200.0,
                fee = 10.0,
                modFio = "",
                modPerson = 1,
                payId = 1,
                promiseDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                promiseTill = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                recordId = 333,
                status = StatusPromisePayment.WORKED,
                symbol = "Rub",
                username = ""
            )
        )
    }
}

@Preview
@Composable
private fun Preview3() {
    BillingTheme {
        PromisePaymentCard(
            payment = PromisePayment(
                agrmId = 1,
                agrmNum = "1",
                amount = 600.0,
                cancelFio = "",
                cancelPerson = 1,
                currId = 1,
                debt = 200.0,
                fee = 10.0,
                modFio = "",
                modPerson = 1,
                payId = 1,
                promiseDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                promiseTill = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                recordId = 333,
                status = StatusPromisePayment.PAYED,
                symbol = "Rub",
                username = ""
            )
        )
    }
}