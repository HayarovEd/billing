package com.impuls.billing.ui.profile_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.impuls.billing.R
import com.impuls.billing.domain.model.Agreement
import com.impuls.billing.domain.model.PaymentMethodType
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.ProfileRow

@Composable
fun ItemAgreement(
    modifier: Modifier = Modifier,
    agreement: Agreement,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 15.dp),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary),
        onClick = onClick
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = agreement.number,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = modifier.height(5.dp))
            ProfileRow(
                title = stringResource(R.string.balance),
                content = "${"%.2f".format(agreement.balance)} ${agreement.symbol}",
                styleTitle =  MaterialTheme.typography.titleMedium,
                styleContent =  MaterialTheme.typography.titleMedium.copy(color = if (agreement.balance<=0.0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary),
                horizontalArrangement = Arrangement.Center
            )
            Spacer(modifier = modifier.height(5.dp))
            ProfileRow(
                title = stringResource(R.string.code_payment),
                content = agreement.code,
                horizontalArrangement = Arrangement.Center
            )
        }
    }
}

@Preview (showBackground = true)
@Composable
private fun Preview() {
    BillingTheme {
        ItemAgreement(
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
            onClick = {}
        )
    }
}