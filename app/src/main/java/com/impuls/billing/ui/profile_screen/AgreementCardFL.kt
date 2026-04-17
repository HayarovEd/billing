package com.impuls.billing.ui.profile_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.impuls.billing.R
import com.impuls.billing.domain.model.Agreement
import com.impuls.billing.domain.model.PaymentMethodType
import com.impuls.billing.ui.theme.BillingTheme
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale


@Composable
fun AgreementCardFL(
    agreement: Agreement,
    modifier: Modifier = Modifier,
    onClickPayment: () -> Unit,
    onClickPaymentHistory: () -> Unit,
) {
    // Форматирование чисел
    val balanceFormat = DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale.US).apply {
        decimalSeparator = '.'
        groupingSeparator = ' '
    })

    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Заголовок: Номер и ID
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.agreement_number),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = agreement.number,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

            }

            Spacer(modifier = Modifier.height(12.dp))

            // Код договора
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.code_agreement),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = agreement.code,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 4.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Баланс (с отрицательным значением - красный цвет)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.balance),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${balanceFormat.format(agreement.balance)} ${agreement.symbol}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (agreement.balance < 0)
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.End
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Promise Credit
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.promise_credit),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${balanceFormat.format(agreement.promiseCredit)} ${agreement.symbol}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Индикатор статуса
            val statusColor = when {
                agreement.balance < 0 -> MaterialTheme.colorScheme.error
                agreement.balance > agreement.credit * 0.8 -> MaterialTheme.colorScheme.error
                else -> MaterialTheme.colorScheme.tertiary
            }
            if (agreement.balance <0) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = statusColor.copy(alpha = 0.1f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.overdue_dept),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(8.dp),
                        textAlign = TextAlign.Center,
                        color = statusColor
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row( modifier = Modifier
                .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(
                    modifier = Modifier
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    onClick = onClickPaymentHistory
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.credit_card_clock_24dp),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    onClick = onClickPayment,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.payment_card_24dp),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = stringResource(R.string.add_balance),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    BillingTheme {
        AgreementCardFL(
            agreement = Agreement(
                agrmId = 8220,
                uid = 8273,
                operId = 7357,
                curId = 1,
                bNotify = 0,
                vGroups = 20,
                penaltyMethod = 0,
                monthBlockDay = 0,
                balance = -56977.602534999998,
                credit = 900000.0,
                promiseCredit = 0.0,
                installments = 0.0,
                balanceStatus = 1,
                friendAgrmId = 0,
                paymentMethod = PaymentMethodType.CREDIT,
                blockDays = 0,
                blockMonths = 0,
                orderPayDay = 0,
                blockOrders = 0,
                blockAmount = 0.0,
                ownerId = 0,
                isDefault = 0,
                number = "Т_СТ",
                date = "2017-03-14",
                closeDon = null,
                bCheck = "0000-00-00",
                symbol = "руб",
                username = "ЯЯ_Биллинг(Т_СТ)",
                initialBalance = 0.0,
                code = "222"
            ),
            onClickPayment = {},
            onClickPaymentHistory = {}
        )
    }
}