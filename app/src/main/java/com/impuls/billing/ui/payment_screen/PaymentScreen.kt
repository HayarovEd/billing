package com.impuls.billing.ui.payment_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.impuls.billing.R
import com.impuls.billing.domain.model.payment.CompanyPayment
import com.impuls.billing.domain.model.payment.ImageData
import com.impuls.billing.domain.model.payment.ResultCheck
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.ObserveAsEvents
import com.impuls.billing.ui.uikit.openSbpUrl
import org.koin.androidx.compose.koinViewModel

@Composable
fun PaymentScreenRoot(
    viewModel: PaymentScreenViewModel = koinViewModel(),
    navigateToBack: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current


    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is PaymentScreenEvent.OnMessage -> snackbarHostState.showSnackbar(
                event.message.asString(
                    context
                )
            )

            is PaymentScreenEvent.SendLink -> {
                openSbpUrl(context, event.url)
            }
        }
    }

    PaymentScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onAction = viewModel::onAction,
        navigateToBack = navigateToBack,
    )
}

@Composable
private fun PaymentScreen(
    modifier: Modifier = Modifier,
    state: PaymentScreenState,
    snackbarHostState: SnackbarHostState,
    onAction: (PaymentScreenAction) -> Unit,
    navigateToBack: () -> Unit,
) {
    BackHandler {
        onAction(
            PaymentScreenAction.UpdatePaymentScreenType(
                backSwitcher(
                    methodType = state.paymentScreenType,
                    onBack = navigateToBack
                )
            )
        )
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .padding(15.dp),
            contentAlignment = Alignment.Center
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                state.resultCheck?.let { check ->
                    if (check.code == 0) {
                        when (state.paymentScreenType) {
                            PaymentScreenType.ENTER_AMOUNT -> {
                                AmountTypeUi(
                                    companyPayment = state.companyPayment,
                                    imageData = state.imageData,
                                    number = state.number,
                                    amountString = state.amountString,
                                    typeErrorAmount = state.typeErrorAmount,
                                    updateAmount = {
                                        onAction(PaymentScreenAction.UpdateAmount(it))
                                    },
                                    updateScreen = {
                                        onAction(
                                            PaymentScreenAction.UpdatePaymentScreenType(
                                                PaymentScreenType.CHOOSE_PAYMENT_METHOD
                                            )
                                        )
                                    }
                                )
                            }

                            PaymentScreenType.CHOOSE_PAYMENT_METHOD -> {
                                ChooseMethodUi(
                                    imageData = state.imageData,
                                    chosenMethod = state.chosenMethod,
                                    amount = state.amountString,
                                    onBack = {
                                        onAction(
                                            PaymentScreenAction.UpdatePaymentScreenType(
                                                PaymentScreenType.ENTER_AMOUNT
                                            )
                                        )
                                    },
                                    updateScreen = {
                                        onAction(PaymentScreenAction.Payment)
                                    },
                                    updateSelectedMethod = {
                                        onAction(PaymentScreenAction.UpdateMethodPayment(it))
                                    }
                                )
                            }

                            PaymentScreenType.CARD -> {
                                PaymentCardUi(
                                    url = state.paymentCard?.wayToPay?.url,
                                    onBack = {
                                        onAction(
                                            PaymentScreenAction.UpdatePaymentScreenType(
                                                PaymentScreenType.CHOOSE_PAYMENT_METHOD
                                            )
                                        )
                                    }
                                )
                            }

                            PaymentScreenType.ENTER_PHONE -> {
                                PhoneTypeUi(
                                    imageData = state.imageData,
                                    phone = state.paymentPhone,
                                    amount = state.amountString,
                                    updatePhone = {
                                        onAction(
                                            PaymentScreenAction.UpdatePhone(it)
                                        )
                                    },
                                    updateScreen = {
                                        onAction(PaymentScreenAction.Payment)
                                    },
                                    onBack = {
                                        onAction(
                                            PaymentScreenAction.UpdatePaymentScreenType(
                                                PaymentScreenType.CHOOSE_PAYMENT_METHOD
                                            )
                                        )
                                    }
                                )
                            }

                            PaymentScreenType.SBER_PAY -> {
                                SberPayTypeUi(
                                    imageData = state.imageData,
                                    amount = state.amountString,
                                    checkPayment = {
                                        onAction(PaymentScreenAction.CheckPayment)
                                    },
                                    onBack = {
                                        onAction(
                                            PaymentScreenAction.UpdatePaymentScreenType(
                                                PaymentScreenType.CHOOSE_PAYMENT_METHOD
                                            )
                                        )
                                    }
                                )
                            }

                            PaymentScreenType.SBP -> {
                                SbpPayTypeUi(
                                    imageData = state.imageData,
                                    amount = state.amountString,
                                    doPayment = {
                                        onAction(PaymentScreenAction.SendSpbLink)
                                    },
                                    onBack = {
                                        onAction(
                                            PaymentScreenAction.UpdatePaymentScreenType(
                                                PaymentScreenType.CHOOSE_PAYMENT_METHOD
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = check.message ?: "",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp),
                                onClick = navigateToBack,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary,
                                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            ) {
                                Text(
                                    text = stringResource(R.string.go_back),
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    val snackbarHostState = remember { SnackbarHostState() }
    BillingTheme {
        PaymentScreen(
            state = PaymentScreenState(
                resultCheck = ResultCheck(
                    code = 0,
                    details = null,
                    message = null
                ),
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
                )
            ),
            snackbarHostState = snackbarHostState,
            onAction = {},
            navigateToBack = {},
        )
    }
}

@Preview
@Composable
private fun Preview1() {
    val snackbarHostState = remember { SnackbarHostState() }
    BillingTheme {
        PaymentScreen(
            state = PaymentScreenState(
                resultCheck = ResultCheck(
                    code = 1640,
                    details = "Unknown Лицевой счет 5951420261",
                    message = "Неизвестный Лицевой счет 5951420261"
                )
            ),
            snackbarHostState = snackbarHostState,
            onAction = {},
            navigateToBack = {},
        )
    }
}