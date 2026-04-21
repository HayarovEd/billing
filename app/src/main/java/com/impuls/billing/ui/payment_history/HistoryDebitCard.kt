package com.impuls.billing.ui.payment_history

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.impuls.billing.domain.model.HistoryDebit
import com.impuls.billing.domain.utils.toCurrentLocalDateTime
import com.impuls.billing.ui.theme.BillingTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.impuls.billing.R
import com.impuls.billing.ui.uikit.toDebitUi
import kotlin.time.Clock

@Composable
fun HistoryDebitCard(
    modifier: Modifier = Modifier,
    history: HistoryDebit,
) {
    val context = LocalContext.current
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Левая часть: иконка и дата
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Иконка дебета
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.trending_down_24dp),
                    contentDescription = "Debit",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(24.dp)
                )

                Column {
                    Text(
                        text = stringResource(R.string.debet),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = history.yearMonth.toDebitUi(context),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
            Text(
                text = "-${"%.2f".format(history.amount)} ${stringResource(R.string.rub_unit)}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    BillingTheme() {
        HistoryDebitCard(
            history = HistoryDebit(
                amount = 456.00,
                yearMonth = Clock.System.now().toCurrentLocalDateTime()
            )
        )
    }
}