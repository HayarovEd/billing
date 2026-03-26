package com.impuls.billing.ui.payment_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.impuls.billing.R
import com.impuls.billing.domain.model.payment.CompanyPayment
import com.impuls.billing.domain.model.payment.ImageData
import com.impuls.billing.domain.model.payment.ResultCheck
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun PaymentScreenRoot(
    viewModel: PaymentScreenViewModel = koinViewModel(),
    navigateToBack: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val snakeBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is PaymentScreenEvent.OnMessage -> snakeBarHostState.showSnackbar(
                event.message.asString(
                    context
                )
            )
        }
    }

    PaymentScreen(
        state = state,
        snakeBarHostState = snakeBarHostState,
        onAction = viewModel::onAction,
        navigateToBack = navigateToBack,
    )
}

@Composable
private fun PaymentScreen(
    modifier: Modifier = Modifier,
    state: PaymentScreenState,
    snakeBarHostState: SnackbarHostState,
    onAction: (PaymentScreenAction) -> Unit,
    navigateToBack: () -> Unit,
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snakeBarHostState) },
    ) { contentPadding ->
        if (state.isLoading) {
            Box(
                modifier = modifier
                    .padding(contentPadding)
                    .fillMaxSize()
                    .padding(15.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            state.resultCheck?.let { check ->
                if (check.code == 0) {
                    Column(
                        modifier = modifier
                            .padding(contentPadding)
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
                        state.companyPayment?.let { company->
                            state.imageData?.let {
                                TitleCompany(
                                    name = company.name,
                                    image = it.image,
                                    colorString = it.color
                                )
                            }
                        }

                    }
                } else {
                    Box(
                        modifier = modifier
                            .padding(contentPadding)
                            .fillMaxSize()
                            .padding(15.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = check.message ?: "",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = modifier.height(10.dp))
                            Button(
                                modifier = modifier.fillMaxWidth(),
                                onClick = navigateToBack,
                                shape = RoundedCornerShape(5.dp),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            ) {
                                Text(
                                    text = stringResource(R.string.go_back),
                                    style = MaterialTheme.typography.labelSmall
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
    val snackBarState = remember { SnackbarHostState() }
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
            snakeBarHostState = snackBarState,
            onAction = {},
            navigateToBack = {},
        )
    }
}

@Preview
@Composable
private fun Preview1() {
    val snackBarState = remember { SnackbarHostState() }
    BillingTheme {
        PaymentScreen(
            state = PaymentScreenState(
                resultCheck = ResultCheck(
                    code = 1640,
                    details = "Unknown Лицевой счет 5951420261",
                    message = "Неизвестный Лицевой счет 5951420261"
                )
            ),
            snakeBarHostState = snackBarState,
            onAction = {},
            navigateToBack = {},
        )
    }
}