package com.impuls.billing.ui.payment_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import com.impuls.billing.domain.model.payment.CompanyPayment
import com.impuls.billing.domain.model.payment.ImageData
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.ProfileRow
import com.impuls.billing.ui.uikit.UiIconButton
import com.impuls.billing.ui.uikit.UiTextField2

@Composable
fun AmountTypeUi(
    modifier: Modifier = Modifier,
    companyPayment: CompanyPayment?,
    imageData: ImageData?,
    number: String?,
    amountString: String,
    typeErrorAmount: TypeErrorAmount,
    updateAmount: (String) -> Unit,
    updateScreen: () -> Unit,
    onBack: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(15.dp), // Консистентный отступ 15.dp
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        UiIconButton(
            icon = ImageVector.vectorResource(R.drawable.arrow_back_24dp),
            onClick = onBack,
        )
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

        // Карточка компании
        if (companyPayment != null && imageData != null) {
            TitleCompany(
                name = companyPayment.name,
                image = imageData.image,
                colorString = imageData.color,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Лицевой счет
        ProfileRow(
            title = stringResource(R.string.ladger_account),
            content = number ?: "—",
            modifier = Modifier.fillMaxWidth()
        )

        // Поле ввода суммы
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(R.string.amount_to_pay),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            UiTextField2(
                content = amountString,
                onClickContent = {
                    updateAmount(it)
                },
                textSize = 22.sp,
                label = "0.00",
                leadingText = "₽",
                keyboardType = KeyboardType.Decimal,
                isError = typeErrorAmount != TypeErrorAmount.NO_ERROR && typeErrorAmount != TypeErrorAmount.BLANK,
            )

            // Сообщение об ошибке (не показываем BLANK до попытки ввода)
            if (typeErrorAmount != TypeErrorAmount.NO_ERROR && typeErrorAmount != TypeErrorAmount.BLANK) {
                Text(
                    text = when (typeErrorAmount) {
                        TypeErrorAmount.NOT_DIGIT -> stringResource(R.string.no_digit)
                        TypeErrorAmount.LESS_RANGE -> stringResource(R.string.less_range)
                        TypeErrorAmount.BIG_RANGE -> stringResource(R.string.big_range)
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Кнопка "Далее"
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            onClick = updateScreen,
            enabled = typeErrorAmount == TypeErrorAmount.NO_ERROR && amountString.isNotBlank(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        ) {
            Text(
                text = stringResource(R.string.next),
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
        AmountTypeUi(
            imageData = ImageData(
                color = "FEAD16",
                image = ""
            ),
            companyPayment = CompanyPayment(
                amountMode = "pay_amount",
                commissions = emptyList(),
                currency = "RUB",
                dynCom = "OFFLINE",
                masks = emptyList(),
                multiCurrency = false,
                name = "НПО ЦОДИТ, Телинт, Инновационный центр Верхнекамья, НПО Импульс",
                offline = false,
                pfAds = "{\"success\":{\"link\":\"https://share.flocktory.com/exchange/login?ssid=5092&bid=14416\",\"logoMob\":\"floctory/flocktory_mob.svg\",\"logoWeb\":\"floctory/flocktory_web.svg\"}}",
                readOnlyAmount = false,
                receivingCurrency = "RUB",
                serviceCode = "111-16915-2",
                serviceName = "Оплата услуг",
                serviceType = "INTERNET",
                tags = ""
            ),
            number = "6666",
            amountString = "",
            typeErrorAmount = TypeErrorAmount.BLANK,
            updateAmount = {},
            updateScreen = {},
            onBack = {}
        )
    }
}
