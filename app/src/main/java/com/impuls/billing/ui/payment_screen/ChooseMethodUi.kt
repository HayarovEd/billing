package com.impuls.billing.ui.payment_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.impuls.billing.R
import com.impuls.billing.domain.model.payment.ImageData
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.TitleMethodContent
import com.impuls.billing.ui.uikit.TitleMethodContentCard
import com.impuls.billing.ui.uikit.UiIconButton

@Composable
fun ChooseMethodUi(
    modifier: Modifier = Modifier,
    imageData: ImageData?,
    chosenMethod: TypePayment?,
    amount: String,
    onBack: () -> Unit,
    updateScreen: () -> Unit,
    updateSelectedMethod: (TypePayment) -> Unit,
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
            text = stringResource(R.string.choose_payment),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = modifier.height(10.dp))
        LazyColumn(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            items(TypePayment.entries) { type->
                ItemPaymentMethod(
                    comission = type.comission,
                    isChosenMethod = chosenMethod == type,
                    titleContent = {
                        if (type == TypePayment.CARD) {
                            TitleMethodContentCard(
                                name = stringResource(R.string.bank_card),
                                icons = type.icons
                            )
                        } else {
                            TitleMethodContent(
                                icons = type.icons,
                                imageWidth = 86.dp
                            )
                        }
                    },
                    updateSelect = {
                        updateSelectedMethod(type)
                    }
                )
            }
        }
        Spacer(modifier = modifier.height(10.dp))
        Button(
            modifier = modifier.fillMaxWidth(),
            onClick = updateScreen,
            enabled = chosenMethod!=null,
            shape = RoundedCornerShape(5.dp),
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
        ChooseMethodUi(
            imageData = ImageData(
                color = "FEAD16",
                image = ""
            ),
            chosenMethod = TypePayment.SBP,
            amount = "100",
            onBack = {},
            updateScreen = {},
            updateSelectedMethod = {},
        )
    }
}