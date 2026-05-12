package com.impuls.billing.ui.promise_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.impuls.billing.R
import com.impuls.billing.domain.model.PPSettings
import com.impuls.billing.domain.utils.toCurrentLocalDate
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.ProfileRow
import com.impuls.billing.ui.uikit.UiTextField
import com.impuls.billing.ui.uikit.asAmountTextError
import com.impuls.billing.ui.uikit.toDebitUi2
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import kotlin.time.Clock

@Composable
fun PromiseCard(
    modifier: Modifier = Modifier,
    promisePayment: Double,
    amount: String,
    todayDate: LocalDate = Clock.System.now().toCurrentLocalDate(),
    isEnableUpdateButton: Boolean,
    ppSettings: PPSettings?,
    textError: AmountTextError,
    updateAmount: (String) -> Unit,
    setPromise: () -> Unit,
) {
    val context = LocalContext.current
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        ppSettings?.let { pp ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                if (pp.promiseAvailable) {
                    ProfileRow(
                        title = stringResource(R.string.period_promise_payment),
                        content = todayDate
                            .plus(
                                value = pp.promiseTill,
                                unit = DateTimeUnit.DAY
                            )
                            .toDebitUi2(context)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    ProfileRow(
                        title = stringResource(R.string.min_promise_amount),
                        content = "${pp.promiseMin} ${stringResource(R.string.rub_unit)}"
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    ProfileRow(
                        title = stringResource(R.string.max_promise_amount),
                        content = "${pp.promiseMax} ${stringResource(R.string.rub_unit)}"
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    ProfileRow(
                        title = stringResource(R.string.recommended_promise),
                        content = "$promisePayment ${stringResource(R.string.rub_unit)}"
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    ) {
                        Column(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(5.dp),
                        ) {
                            Text(
                                modifier = Modifier,
                                text = "${stringResource(R.string.amount)}:",
                                style = MaterialTheme.typography.labelMedium
                            )
                            Spacer(modifier = modifier.height(3.dp))
                            UiTextField(
                                modifier = Modifier,
                                isError = textError != AmountTextError.NO_ERROR,
                                error = textError.asAmountTextError().asString(),
                                content = amount,
                                keyboardType = KeyboardType.Decimal,
                                label = "",
                                onClickContent = { updateAmount(it) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = setPromise,
                        enabled = amount.isNotBlank() && textError == AmountTextError.NO_ERROR &&isEnableUpdateButton,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
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
                                text = stringResource(R.string.do_payment),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                } else {
                    Text(
                        modifier = modifier.fillMaxWidth(),
                        text = stringResource(R.string.promise_payment_unavailable),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.error,
                        textDecoration = TextDecoration.Underline,
                        textAlign = TextAlign.Center
                    )
                }
            }

        }

    }
}

@Preview
@Composable
private fun Preview() {
    BillingTheme {
        PromiseCard(
            promisePayment = 600.0,
            amount = "",
            textError = AmountTextError.MORE_RECOMMENDED,
            isEnableUpdateButton = false,
            ppSettings = PPSettings(
                promiseAvailable = true,
                promiseBlockDays = 5,
                promiseFee = 0.0,
                promiseLimit = 1.0,
                promiseMax = 1000.0,
                promiseMin = 10.0,
                promiseOnDays = 10,
                promiseRent = true,
                promiseTill = 10
            ),
            updateAmount = { },
            setPromise = {}
        )
    }
}

@Preview
@Composable
private fun Preview1() {
    BillingTheme {
        PromiseCard(
            promisePayment = 600.0,
            amount = "",
            textError = AmountTextError.NO_ERROR,
            isEnableUpdateButton = true,
            updateAmount = {},
            ppSettings = PPSettings(
                promiseAvailable = false,
                promiseBlockDays = 5,
                promiseFee = 0.0,
                promiseLimit = 1.0,
                promiseMax = 1000.0,
                promiseMin = 10.0,
                promiseOnDays = 10,
                promiseRent = true,
                promiseTill = 10
            ),
            setPromise = {},
        )
    }
}

@Preview
@Composable
private fun Preview2() {
    BillingTheme {
        PromiseCard(
            promisePayment = 600.0,
            amount = "100",
            textError = AmountTextError.NO_ERROR,
            isEnableUpdateButton = true,
            ppSettings = PPSettings(
                promiseAvailable = true,
                promiseBlockDays = 5,
                promiseFee = 0.0,
                promiseLimit = 1.0,
                promiseMax = 1000.0,
                promiseMin = 10.0,
                promiseOnDays = 10,
                promiseRent = false,
                promiseTill = 10
            ),
            updateAmount = {},
            setPromise = {},
        )
    }
}