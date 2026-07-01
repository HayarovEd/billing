package com.impuls.billing.ui.profile_screen

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.impuls.billing.R
import com.impuls.billing.domain.model.Account
import com.impuls.billing.domain.model.Agreement
import com.impuls.billing.domain.model.BillDeliveryType
import com.impuls.billing.domain.model.ClientAccount
import com.impuls.billing.domain.model.ClientVroup
import com.impuls.billing.domain.model.ModifierData
import com.impuls.billing.domain.model.PaymentMethodType
import com.impuls.billing.domain.model.TypeBlockVGroup
import com.impuls.billing.domain.model.UserType
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.ContentExitDialog
import com.impuls.billing.ui.uikit.ObserveAsEvents
import com.impuls.billing.ui.uikit.UiIconButton
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreenRoot(
    viewModel: ProfileScreenViewModel = koinViewModel(),
    onNavigateToPayment: (String, Long) -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToMore: (Long) -> Unit,
    onClickPaymentHistory: (Long, Long, Long) -> Unit,
    onNavigateToSettings: (Long) -> Unit,
    onNavigateToBlocking: (Long) -> Unit,
    onClickPromisePayment: (Long) -> Unit,
    onNavigateToMainChat: (String, String, String, String, String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snakeBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val activity = context as Activity
    var isVisibleDialog by retain { mutableStateOf(false) }
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
        isVisibleDialog = isVisibleDialog,
        onNavigateToPayment = onNavigateToPayment,
        onNavigateToSettings = onNavigateToSettings,
        onExitFromApp = {
            activity.finish()
        },
        onClickPaymentHistory = onClickPaymentHistory,
        updateVisibilityDialog = {
            isVisibleDialog = it
        },
        onNavigateToBlocking = onNavigateToBlocking,
        onClickPromisePayment = onClickPromisePayment,
        onNavigateToMainChat = onNavigateToMainChat
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileScreenScreen(
    modifier: Modifier = Modifier,
    state: ProfileScreenState,
    snakeBarHostState: SnackbarHostState,
    isVisibleDialog: Boolean,
    updateVisibilityDialog: (Boolean) -> Unit,
    onNavigateToPayment: (String, Long) -> Unit,
    onExitFromApp: () -> Unit,
    onClickPaymentHistory: (Long, Long, Long) -> Unit,
    onNavigateToSettings: (Long) -> Unit,
    onNavigateToBlocking: (Long) -> Unit,
    onClickPromisePayment: (Long) -> Unit,
    onNavigateToMainChat: (
        name: String,
        login: String,
        password: String,
        email: String,
        groupName: String
    ) -> Unit
) {

    var openCloseDialog by remember { mutableStateOf(false) }


    BackHandler() {
        openCloseDialog = true
    }

    if (openCloseDialog) {
        Dialog(
            onDismissRequest = {
                openCloseDialog = false
            }
        ) {
            ContentExitDialog(
                title = stringResource(R.string.sure_exit),
                onCancel = {
                    openCloseDialog = false
                },
                onConfirm = {
                    openCloseDialog = false
                    onExitFromApp()
                }
            )
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snakeBarHostState) },
        floatingActionButton = {
            UiIconButton(
                icon = ImageVector.vectorResource(R.drawable.chat_24dp),
                buttonColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                onClick = {
                    state.account?.let { acc->
                        onNavigateToMainChat(
                            acc.account.name,
                            acc.account.login,
                            acc.account.password,
                            acc.account.email,
                            "Группа 16" //TODO нужно определиться с группами
                        )
                    }

                }
            )
        }
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
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .padding(15.dp)
            ) {
                state.account?.let { acc ->
                    UserCard(
                        account = acc.account,
                        address = state.clientVGroups?.addresses?.firstOrNull() ?: "",
                        onNavigateToSettings = {
                            onNavigateToSettings(it)
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    when (acc.account.type) {
                        /*UserType.UL -> {
                            Text(
                                text = stringResource(R.string.my_accounts),
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = modifier.height(10.dp))
                            LazyColumn(
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
                        }*/

                        UserType.FL, UserType.UL -> {
                            acc.agreements.firstOrNull()?.let { agrm ->
                                AgreementCardFL(
                                    agreement = agrm,
                                    onClickPayment = {
                                        onNavigateToPayment(agrm.code, agrm.operId)
                                    },
                                    onClickPaymentHistory = {
                                        if (state.vGroups.isNotEmpty()) {
                                            val vg = state.vGroups.first()
                                            onClickPaymentHistory(agrm.agrmId, vg.vgId, vg.tarifId)
                                        }
                                    },
                                    onClickPromisePayment = {
                                        onClickPromisePayment(agrm.agrmId)
                                    }
                                )
                                state.clientVGroups?.let {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    TarifCardFL(
                                        clientVgroup = it,
                                        vGroupPassword = state.vGroupPassword,
                                        onClickUnblock = {
                                            onNavigateToBlocking(it.vgId)
                                        },
                                        onClickPayment = {
                                            onNavigateToPayment(agrm.code, agrm.operId)
                                        }
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            UsbBoxCardFL(
                                modifier = modifier.heightIn(max = 400.dp),
                                vGroupUsbBoxes = state.vGroupUsbBoxes
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            SharedPostsCard(
                                modifier = modifier.heightIn(max = 400.dp),
                                sharedPosts = state.filteredSharedPosts
                            )
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
                account = clientAccount,
            ),
            snakeBarHostState = snackBarState,
            onNavigateToSettings = { _ -> },
            onNavigateToPayment = { _, _ -> },
            onExitFromApp = {},
            onClickPaymentHistory = { _, _, _ -> },
            isVisibleDialog = false,
            updateVisibilityDialog = {},
            onNavigateToBlocking = {},
            onClickPromisePayment = {},
            onNavigateToMainChat = { _, _, _, _, _ -> },
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

    val vgroup = listOf(
        ClientVroup(
            agrmId = 11,
            vgId = 1000,
            blocked = TypeBlockVGroup.ADMIN_BLOCKED,
            tarifId = 107,
            tarifDescr = "descr",
            agentDescr = "",
            login = "login",
            speed = 5000,
            serviceVolume = 0,
            serviceRent = 700.0,
            currentModifier = ModifierData(
                type = "TODO()",
                value = 1.0
            ),
            addresses = listOf()
        )
    )


    BillingTheme {
        ProfileScreenScreen(
            state = ProfileScreenState(
                account = clientAccount,
                vGroups = vgroup
            ),
            snakeBarHostState = snackBarState,
            onNavigateToSettings = { _ -> },
            onNavigateToPayment = { _, _ -> },
            onExitFromApp = {},
            onClickPaymentHistory = { _, _, _ -> },
            isVisibleDialog = false,
            updateVisibilityDialog = {},
            onNavigateToBlocking = {},
            onClickPromisePayment = {},
            onNavigateToMainChat = { _, _, _, _, _ -> },
        )
    }
}

@Preview(
    showSystemUi = true
)
@Composable
private fun Preview1_1() {
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


    BillingTheme(darkTheme = true) {
        ProfileScreenScreen(
            state = ProfileScreenState(
                account = clientAccount,
            ),
            snakeBarHostState = snackBarState,
            onNavigateToSettings = { _ -> },
            onNavigateToPayment = { _, _ -> },
            onExitFromApp = {},
            onClickPaymentHistory = { _, _, _ -> },
            isVisibleDialog = false,
            updateVisibilityDialog = {},
            onNavigateToBlocking = {},
            onClickPromisePayment = {},
            onNavigateToMainChat = { _, _, _, _, _ -> },
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
                isLoading = true,
            ),
            snakeBarHostState = snackBarState,
            onNavigateToSettings = { _ -> },
            onNavigateToPayment = { _, _ -> },
            onExitFromApp = {},
            onClickPaymentHistory = { _, _, _ -> },
            isVisibleDialog = true,
            updateVisibilityDialog = {},
            onNavigateToBlocking = {},
            onClickPromisePayment = {},
            onNavigateToMainChat = { _, _, _, _, _ -> },
        )
    }
}