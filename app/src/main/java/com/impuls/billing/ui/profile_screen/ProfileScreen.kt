package com.impuls.billing.ui.profile_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.impuls.billing.R
import com.impuls.billing.domain.model.Account
import com.impuls.billing.domain.model.Agreement
import com.impuls.billing.domain.model.BillDeliveryType
import com.impuls.billing.domain.model.ClientAccount
import com.impuls.billing.domain.model.PaymentMethodType
import com.impuls.billing.domain.model.UserType
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreenRoot(
    viewModel: ProfileScreenViewModel = koinViewModel(),
    onNavigateToPayment: (String) -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToMore: (Long) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snakeBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is ProfileScreenEvent.OnMessage -> snakeBarHostState.showSnackbar(
                event.message.asString(
                    context
                )
            )

            ProfileScreenEvent.NavigateEvent -> {
                onNavigateToLogin()
            }
        }
    }

    ProfileScreenScreen(
        state = state,
        snakeBarHostState = snakeBarHostState,
        onAction = viewModel::onAction,
        onNavigateToPayment = onNavigateToPayment,
        onNavigateToMore = onNavigateToMore
    )
}

@Composable
private fun ProfileScreenScreen(
    modifier: Modifier = Modifier,
    state: ProfileScreenState,
    snakeBarHostState: SnackbarHostState,
    onAction: (ProfileScreenAction) -> Unit,
    onNavigateToPayment: (String) -> Unit,
    onNavigateToMore: (Long) -> Unit,
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
            Column(
                modifier = modifier
                    .padding(contentPadding)
                    .fillMaxSize()
                    .padding(15.dp)
            ) {
                state.account?.let { acc ->
                    UserCard(
                        account = acc.account,
                        count = state.count,
                        onUpdatePhone = {
                            onAction(ProfileScreenAction.UpdatePhone(it))
                        },
                        onUpdateEmail = {
                            onAction(ProfileScreenAction.UpdateEmail(it))
                        },
                        onRecentCodeEmail = {
                            onAction(ProfileScreenAction.ResentCodeEmail)
                        },
                        onRecentCodePhone = {
                            onAction(ProfileScreenAction.ResentCodePhone)
                        },
                        onConfirmCodeEmail = {
                            onAction(ProfileScreenAction.ConfirmEmail(it))
                        },
                        onConfirmCodePhone = {
                            onAction(ProfileScreenAction.ConfirmPhone(it))
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    when (acc.account.type) {
                        UserType.UL -> {
                            Text(
                                text = stringResource(R.string.my_accounts),
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = modifier.height(10.dp))
                            LazyColumn (
                                modifier = modifier
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                            ) {
                                items(acc.agreements) {
                                    AgreementCard(
                                        agreement = it,
                                        onClickPayment = {
                                            onNavigateToPayment(it.code)
                                        },
                                        onClickInfo = {
                                            onNavigateToMore(it.agrmId)
                                        }
                                    )
                                }
                            }
                        }
                        UserType.FL -> {
                            acc.agreements.firstOrNull()?.let { agrm->
                                AgreementCardFL(
                                    agreement = agrm,
                                    onClickPayment = {
                                        onNavigateToPayment(agrm.code)
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(
    showSystemUi = true
)
@Composable
private fun Preview() {
    val snackBarState = remember { SnackbarHostState() }

    val clientAccount = ClientAccount(
        billDeliveryName = "Email",
        account = Account(
            uid = 8273,
            doctype = 0,
            billDelivery = BillDeliveryType.COURIER,
            category = 0,
            type = UserType.UL,
            oksm = 643,
            wrongActive = 0,
            archive = false,
            ownerShip = 0,
            mobileIsConfirmed = false,
            emailIsConfirmed = false,
            offerIsAccepted = false,
            soleProprietor = false,
            login = "pycutbvk45",
            password = "342py23854",
            description = "Не использовать.",
            name = "ЯЯ_Биллинг(Т_СТ)",
            phone = "",
            fax = "",
            email = "",
            mobile = "89026355319",
            bankName = "",
            branchBankName = "",
            treasuryName = "",
            treasuryAccount = "",
            bik = "",
            setti = "",
            corr = "",
            kpp = "",
            inn = "",
            ogrn = "",
            okpo = "",
            okved = "",
            abonentName = "",
            abonentSurname = "",
            abonentPatronymic = ""
        ),
        agreements = listOf(
            Agreement(
                agrmId = 8220,
                uid = 8273,
                operId = 7357,
                curId = 1,
                bNotify = 0,
                vGroups = 20,
                penaltyMethod = 0,
                monthBlockDay = 0,
                balance = -56977.602534999998,
                credit = 900000.0,
                promiseCredit = 0.0,
                installments = 0.0,
                balanceStatus = 1,
                friendAgrmId = 0,
                paymentMethod = PaymentMethodType.CREDIT,
                blockDays = 0,
                blockMonths = 0,
                orderPayDay = 0,
                blockOrders = 0,
                blockAmount = 0.0,
                ownerId = 0,
                isDefault = 0,
                number = "Т_СТ",
                date = "2017-03-14",
                closeDon = null,
                bCheck = "0000-00-00",
                symbol = "руб",
                username = "ЯЯ_Биллинг(Т_СТ)",
                initialBalance = 0.0,
                code = ""
            )
        )
    )


    BillingTheme {
        ProfileScreenScreen(
            state = ProfileScreenState(
                account = clientAccount
            ),
            snakeBarHostState = snackBarState,
            onAction = {},
            onNavigateToPayment = {},
            onNavigateToMore = {},
        )
    }
}

@Preview(
    showSystemUi = true
)
@Composable
private fun Preview1() {
    val snackBarState = remember { SnackbarHostState() }

    val clientAccount = ClientAccount(
        billDeliveryName = "Email",
        account = Account(
            uid = 8273,
            doctype = 0,
            billDelivery = BillDeliveryType.MAIL,
            category = 0,
            type = UserType.FL,
            oksm = 643,
            wrongActive = 0,
            archive = false,
            ownerShip = 0,
            mobileIsConfirmed = false,
            emailIsConfirmed = false,
            offerIsAccepted = false,
            soleProprietor = false,
            login = "pycutbvk45",
            password = "342py23854",
            description = "Не использовать.",
            name = "Ivanov Ivan Ivanovich",
            phone = "",
            fax = "",
            email = "",
            mobile = "89026355319",
            bankName = "",
            branchBankName = "",
            treasuryName = "",
            treasuryAccount = "",
            bik = "",
            setti = "",
            corr = "",
            kpp = "",
            inn = "",
            ogrn = "",
            okpo = "",
            okved = "",
            abonentName = "",
            abonentSurname = "",
            abonentPatronymic = ""
        ),
        agreements = listOf(
            Agreement(
                agrmId = 8220,
                uid = 8273,
                operId = 7357,
                curId = 1,
                bNotify = 0,
                vGroups = 20,
                penaltyMethod = 0,
                monthBlockDay = 0,
                balance = 1000.0,
                credit = 900000.0,
                promiseCredit = 0.0,
                installments = 0.0,
                balanceStatus = 1,
                friendAgrmId = 0,
                paymentMethod = PaymentMethodType.ADVANCE,
                blockDays = 0,
                blockMonths = 0,
                orderPayDay = 0,
                blockOrders = 0,
                blockAmount = 0.0,
                ownerId = 0,
                isDefault = 0,
                number = "Т_СТ",
                date = "2017-03-14",
                closeDon = null,
                bCheck = "0000-00-00",
                symbol = "руб",
                username = "ЯЯ_Биллинг(Т_СТ)",
                initialBalance = 0.0,
                code = "5665665"
            )
        )
    )


    BillingTheme {
        ProfileScreenScreen(
            state = ProfileScreenState(
                account = clientAccount
            ),
            snakeBarHostState = snackBarState,
            onAction = {},
            onNavigateToPayment = {},
            onNavigateToMore = {},
        )
    }
}

@Preview(
    showSystemUi = true
)
@Composable
private fun Preview2() {
    val snackBarState = remember { SnackbarHostState() }


    BillingTheme {
        ProfileScreenScreen(
            state = ProfileScreenState(
                isLoading = true
            ),
            snakeBarHostState = snackBarState,
            onAction = {},
            onNavigateToPayment = {},
            onNavigateToMore = {},
        )
    }
}