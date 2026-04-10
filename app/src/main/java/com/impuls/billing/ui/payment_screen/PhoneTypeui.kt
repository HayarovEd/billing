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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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

private const val mask = "+0(000)000-00-00"
private  const val maskNumber = '0'


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
            text = stringResource(R.string.enter_number_phone),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = modifier.height(10.dp))
        OutlinedTextField(
            value = phone,
            onValueChange = { newValue ->
                if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                    updatePhone(newValue.take(mask.count { it == maskNumber }))
                }
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = "",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                keyboardType = KeyboardType.Phone,
            ),
            visualTransformation  = PhoneVisualTransformation(mask, maskNumber),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                errorBorderColor = MaterialTheme.colorScheme.error,
            )
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