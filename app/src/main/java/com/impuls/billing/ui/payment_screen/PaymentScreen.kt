package com.impuls.billing.ui.payment_screen

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.impuls.billing.R
import com.impuls.billing.domain.model.payment.CompanyPayment
import com.impuls.billing.domain.model.payment.ImageData
import com.impuls.billing.domain.model.payment.ResultCheck
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.ObserveAsEvents
import com.impuls.billing.ui.uikit.UiIconButton
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
                event.message.asString(context)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PaymentScreen(
    modifier: Modifier = Modifier,
    state: PaymentScreenState,
    snackbarHostState: SnackbarHostState,
    onAction: (PaymentScreenAction) -> Unit,
    navigateToBack: () -> Unit,
) {
    val context = LocalContext.current

    val handleBack = {
        onAction(
            PaymentScreenAction.UpdatePaymentScreenType(
                backSwitcher(
                    methodType = state.paymentScreenType,
                    onBack = navigateToBack
                )
            )
        )
    }

    BackHandler(onBack = handleBack)

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.add_balance),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    UiIconButton(
                        modifier = Modifier.padding(horizontal = 15.dp),
                        icon = ImageVector.vectorResource(R.drawable.arrow_back_24dp),
                        onClick = handleBack,
                    )
                }
            )
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .padding(horizontal = 15.dp),
            contentAlignment = Alignment.Center
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                state.resultCheck?.let { check ->
                    if (check.code == 0) {
                        AnimatedContent(
                            targetState = state.paymentScreenType,
                            transitionSpec = {
                                if (targetState.ordinal > initialState.ordinal) {
                                    slideInHorizontally { it } + fadeIn() togetherWith slideOutHorizontally { -it } + fadeOut()
                                } else {
                                    slideInHorizontally { -it } + fadeIn() togetherWith slideOutHorizontally { it } + fadeOut()
                                }
                            },
                            label = "PaymentScreenTransition"
                        ) { screenType ->
                            when (screenType) {
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
                                        },
                                        onBack = handleBack
                                    )
                                }

                                PaymentScreenType.CHOOSE_PAYMENT_METHOD -> {
                                    ChooseMethodUi(
                                        imageData = state.imageData,
                                        chosenMethod = state.chosenMethod,
                                        amount = state.amountString,
                                        onBack = handleBack,
                                        updateScreen = {
                                            if (state.chosenMethod == TypePayment.CARD) {
                                                state.paymentCard?.wayToPay?.url?.let {
                                                    val intent = Intent(Intent.ACTION_VIEW, it.toUri()).apply {
                                                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                    }
                                                    context.startActivity(intent)
                                                }
                                            } else {
                                                onAction(PaymentScreenAction.Payment)
                                            }
                                        },
                                        updateSelectedMethod = {
                                            onAction(PaymentScreenAction.UpdateMethodPayment(it))
                                        }
                                    )
                                }

                                PaymentScreenType.CARD -> {
                                    PaymentCardUi(
                                        url = state.paymentCard?.wayToPay?.url,
                                        onBack = handleBack
                                    )
                                }

                                PaymentScreenType.ENTER_PHONE -> {
                                    PhoneTypeUi(
                                        imageData = state.imageData,
                                        phone = state.paymentPhone,
                                        amount = state.amountString,
                                        updatePhone = {
                                            onAction(PaymentScreenAction.UpdatePhone(it))
                                        },
                                        updateScreen = {
                                            onAction(PaymentScreenAction.Payment)
                                        },
                                        onBack = handleBack
                                    )
                                }

                                PaymentScreenType.SBER_PAY -> {
                                    SberPayTypeUi(
                                        imageData = state.imageData,
                                        amount = state.amountString,
                                        checkPayment = {
                                            onAction(PaymentScreenAction.CheckPayment)
                                        },
                                        onBack = handleBack
                                    )
                                }

                                PaymentScreenType.SBP -> {
                                    SbpPayTypeUi(
                                        imageData = state.imageData,
                                        amount = state.amountString,
                                        doPayment = {
                                            onAction(PaymentScreenAction.SendSpbLink)
                                        },
                                        onBack = handleBack
                                    )
                                }
                            }
                        }
                    } else {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = check.message ?: stringResource(R.string.unknown_error),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.error,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp),
                                    onClick = navigateToBack,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
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
