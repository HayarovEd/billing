package com.impuls.billing.ui.payment_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.impuls.billing.R
import com.impuls.billing.domain.model.payment.CompanyPayment
import com.impuls.billing.domain.model.payment.ImageData
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.ProfileRow
import com.impuls.billing.ui.uikit.UiTextField

@Composable
fun AmountTypeUi(
    modifier: Modifier = Modifier,
    companyPayment: CompanyPayment?,
    imageData: ImageData?,
    number: String?,
    amountString:String,
    typeErrorAmount: TypeErrorAmount,
    updateAmount: (String) -> Unit,
    updateScreen: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(15.dp)
    ) {
        Text(
            text = stringResource(R.string.add_balance),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = modifier.height(10.dp))
        Text(
            text = stringResource(R.string.add_balance_desc),
            style = MaterialTheme.typography.labelMedium
        )
        companyPayment?.let { company ->
            imageData?.let {
                TitleCompany(
                    name = company.name,
                    image = it.image,
                    colorString = it.color
                )
            }
        }
        Spacer(modifier = modifier.height(10.dp))
        ProfileRow(
            title = stringResource(R.string.ladger_account),
            content = number ?: ""
        )
        Spacer(modifier = modifier.height(10.dp))
        UiTextField(
            content = amountString,
            label = stringResource(R.string.amount_to_pay),
            isOnlyDigit = true,
            keyboardType = KeyboardType.Number,
            error = when (typeErrorAmount) {
                TypeErrorAmount.NO_ERROR -> ""
                TypeErrorAmount.NOT_DIGIT -> stringResource(R.string.no_digit)
                TypeErrorAmount.LESS_RANGE -> stringResource(R.string.less_range)
                TypeErrorAmount.BIG_RANGE -> stringResource(R.string.big_range)
                TypeErrorAmount.BLANK -> stringResource(R.string.text_not_blank)
            },
            isError = typeErrorAmount != TypeErrorAmount.NO_ERROR,
            onClickContent = {
                updateAmount(it)
            }
        )
        Spacer(modifier = modifier.height(10.dp))
        Button(
            modifier = modifier.fillMaxWidth(),
            onClick = updateScreen,
            shape = RoundedCornerShape(5.dp),
            enabled = typeErrorAmount == TypeErrorAmount.NO_ERROR,
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground
            )
        ) {
            Text(
                text = stringResource(R.string.next),
                style = MaterialTheme.typography.labelSmall
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
                bankAccount = "40702810604000000852",
                bic = "40702810604000000852",
                comissions = emptyList(),
                currency = "RUB",
                dynCom = "OFFLINE",
                inn = "5903061324",
                kpp = "590301001",
                logoUrl = "",
                maskDtos = emptyList(),
                multiCurrency = false,
                name = "НПО ЦОДИТ, Телинт, Инновационный центр Верхнекамья, НПО Импульс",
                offline = false,
                orgCode = 16915,
                orgName = "ООО НАУЧНО-ПРОИЗВОДСТВЕННОЕ ОБЪЕДИНЕНИЕ ИМПУЛЬС",
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
            typeErrorAmount = TypeErrorAmount.NOT_DIGIT,
            updateAmount = {},
            updateScreen = {}
        )
    }
}