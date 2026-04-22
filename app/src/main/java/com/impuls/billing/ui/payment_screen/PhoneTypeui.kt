package com.impuls.billing.ui.payment_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.impuls.billing.R
import com.impuls.billing.domain.model.payment.ImageData
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.PhoneVisualTransformation
import com.impuls.billing.ui.uikit.UiIconButton
import com.impuls.billing.ui.uikit.UiTextField2

private const val mask = "+0(000)000-00-00"
private const val maskNumber = '0'

@Composable
fun PhoneTypeUi(
    modifier: Modifier = Modifier,
    imageData: ImageData?,
    phone: String,
    amount: String,
    updatePhone: (String) -> Unit,
    onBack: () -> Unit,
    updateScreen: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(15.dp)
    ) {
        UiIconButton(
            icon = ImageVector.vectorResource(R.drawable.arrow_back_24dp),
            onClick = onBack,
        )

        Spacer(modifier = Modifier.height(10.dp))

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
            Spacer(modifier = Modifier.height(10.dp))
            TitleCompany(
                name = "$amount ${stringResource(R.string.rub_symbol)}",
                image = it.image,
                colorString = it.color
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.sberpay_payment),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(R.string.enter_number_phone),
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(10.dp))

        UiTextField2(
            content = phone,
            onClickContent = {
                updatePhone(it)
            },
            label = stringResource(R.string.phone_number),
            keyboardType = KeyboardType.Phone,
            isOnlyDigit = true,
            visualTransformation = PhoneVisualTransformation(mask, maskNumber),
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            onClick = updateScreen,
            enabled = phone.length == 11,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        ) {
            Text(
                text = stringResource(R.string.get_push),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    BillingTheme {
        PhoneTypeUi(
            imageData = ImageData(
                color = "FEAD16",
                image = ""
            ),
            phone = "",
            amount = "349",
            updatePhone = {},
            onBack = {},
            updateScreen = {}
        )
    }
}
