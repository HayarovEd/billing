package com.impuls.billing.ui.payment_screen

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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            text = stringResource(R.string.choose_payment),
            style = MaterialTheme.typography.titleMedium
        )
        
        Spacer(modifier = Modifier.height(10.dp))
        
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            items(TypePayment.entries) { type ->
                ItemPaymentMethod(
                    commission = type.comission,
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
        
        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            onClick = updateScreen,
            enabled = chosenMethod != null,
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