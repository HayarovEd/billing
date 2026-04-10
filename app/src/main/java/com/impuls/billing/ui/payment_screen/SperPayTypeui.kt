package com.impuls.billing.ui.payment_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.impuls.billing.R
import com.impuls.billing.domain.model.payment.ImageData
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.UiIconButton


@Composable
fun SberPayTypeUi(
    modifier: Modifier = Modifier,
    imageData: ImageData?,
    amount: String,
    checkPayment: () -> Unit,
    onBack: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(15.dp)
    ) {
        Text(
            text = stringResource(R.string.add_balance),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        // Описание
        Text(
            text = stringResource(R.string.add_balance_desc),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        imageData?.let {
            TitleCompany(
                name = "$amount ${stringResource(R.string.rub_symbol)}",
                image = it.image,
                colorString = it.color
            )
        }
        Spacer(modifier = modifier.height(10.dp))
        UiIconButton(
            icon = ImageVector.vectorResource(R.drawable.arrow_back_24dp),
            onClick = onBack,
        )
        Text(
            text = stringResource(R.string.sberpay_payment),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = modifier.height(10.dp))
        Text(
            text = stringResource(R.string.sberpay_desc),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = modifier.height(10.dp))
        Text(
            text = stringResource(R.string.sberpay_warning),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = modifier.height(10.dp))
        IconButton(
            modifier = modifier
                .align(Alignment.CenterHorizontally),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            ),
            onClick = checkPayment
        ) {
            Icon(
                modifier = modifier.padding(4.dp),
                imageVector = ImageVector.vectorResource(R.drawable.autorenew_48dp),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    BillingTheme {
        SberPayTypeUi(
            imageData = ImageData(
                color = "FEAD16",
                image = ""
            ),
            amount = "349",
            onBack = {},
            checkPayment = {}
        )
    }
}