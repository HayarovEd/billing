package com.impuls.billing.ui.profile_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.impuls.billing.R
import com.impuls.billing.domain.model.Account
import com.impuls.billing.domain.model.Agreement
import com.impuls.billing.domain.model.BillDeliveryType
import com.impuls.billing.domain.model.ClientAccount
import com.impuls.billing.domain.model.PaymentMethodTYpe
import com.impuls.billing.domain.model.UserType
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.ObserveAsEvents
import com.impuls.billing.ui.uikit.ProfileRow
import com.impuls.billing.ui.uikit.ProfileRowWithSave
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreenRoot(
    viewModel: ProfileScreenViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snakeBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is ProfileScreenEvent.OnMessage -> snakeBarHostState.showSnackbar(
                event.message.asString(
                    context
                )
            )
        }
    }

    ProfileScreenScreen(
        state = state,
        drawerState = drawerState,
        snakeBarHostState = snakeBarHostState,
        onAction = viewModel::onAction,
        updateVisisbilityDrawerState = {
            scope.launch {
                drawerState.apply {
                    if (isClosed) open() else close()
                }
            }
        }
    )
}

@Composable
private fun ProfileScreenScreen(
    modifier: Modifier = Modifier,
    drawerState: DrawerState,
    state: ProfileScreenState,
    snakeBarHostState: SnackbarHostState,
    onAction: (ProfileScreenAction) -> Unit,
    updateVisisbilityDrawerState: () -> Unit,
) {
    var visiblePhoneDialog by retain { mutableStateOf(false) }
    var visibleEmailDialog by retain { mutableStateOf(false) }
    val windowSize = LocalWindowInfo.current.containerDpSize
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            // ModalDrawerSheet { /* Drawer content */ }
        },
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
                        .verticalScroll(rememberScrollState())
                        .padding(15.dp)
                ) {
                    state.account?.let { acc ->
                        Text(
                            text = stringResource(R.string.my_accounts),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = modifier.height(10.dp))
                        LazyVerticalGrid(
                            modifier = modifier
                                .fillMaxWidth()
                                .heightIn(max = windowSize.height / 2),
                            columns = GridCells.Fixed(2),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(acc.agreements) {
                                ItemAgreement(
                                    agreement = it,
                                    onClick = {

                                    }
                                )
                            }
                        }
                        Spacer(modifier = modifier.height(10.dp))
                        Text(
                            text = stringResource(R.string.profile),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = modifier.height(10.dp))
                        ProfileRow(
                            title = stringResource(R.string.you_enter_as),
                            content = acc.account.login
                        )
                        Spacer(modifier = modifier.height(5.dp))
                        ProfileRow(
                            title = when (acc.account.type) {
                                UserType.UL -> stringResource(R.string.name_company)
                                UserType.FL -> stringResource(R.string.name_fl)
                            },
                            content = acc.account.name
                        )
                        Spacer(modifier = modifier.height(5.dp))
                        ProfileRow(
                            title = stringResource(R.string.phone),
                            content = acc.account.phone
                        )
                        Spacer(modifier = modifier.height(5.dp))
                        ProfileRowWithSave(
                            title = stringResource(R.string.mobile_phone),
                            description = stringResource(R.string.desc_phone),
                            subTitle = stringResource(R.string.phone_number),
                            content = acc.account.mobile,
                            defaultContent = stringResource(R.string.set_phone),
                            isConfirmed = acc.account.mobileIsConfirmed,
                            isShowDialog = visiblePhoneDialog,
                            count = state.count,
                            onConfirmCode = {
                                onAction(ProfileScreenAction.ConfirmPhone(it))
                            },
                            onRecentCode = {
                                onAction(ProfileScreenAction.ResentCodePhone)
                            },
                            onConfirmContent = {
                                onAction(ProfileScreenAction.UpdatePhone(it))
                            },
                            updateVisibleDialog = {
                                visiblePhoneDialog = it
                            }
                        )
                        Spacer(modifier = modifier.height(5.dp))
                        ProfileRowWithSave(
                            title = stringResource(R.string.email),
                            description = stringResource(R.string.desc_email),
                            subTitle = stringResource(R.string.email_adrress),
                            content = acc.account.email,
                            defaultContent = stringResource(R.string.set_email),
                            isConfirmed = acc.account.emailIsConfirmed,
                            count = state.count,
                            isShowDialog = visibleEmailDialog,
                            onConfirmCode = {
                                onAction(ProfileScreenAction.ConfirmEmail(it))
                            },
                            onRecentCode = {
                                onAction(ProfileScreenAction.ResentCodeEmail)
                            },
                            onConfirmContent = {
                                onAction(ProfileScreenAction.UpdateEmail(it))
                            },
                            updateVisibleDialog = {
                                visibleEmailDialog = it
                            }
                        )
                        /*ProfileRow(
                            title = stringResource(R.string.email),
                            content = acc.account.email
                        )*/
                        if (acc.account.type == UserType.UL) {
                            Spacer(modifier = modifier.height(10.dp))
                            Text(
                                text = stringResource(R.string.bank_req),
                                style = MaterialTheme.typography.titleSmall
                            )
                            Spacer(modifier = modifier.height(10.dp))
                            ProfileRow(
                                title = stringResource(R.string.name_bank),
                                content = acc.account.bankName
                            )
                            Spacer(modifier = modifier.height(5.dp))
                            ProfileRow(
                                title = stringResource(R.string.name_sub_bank),
                                content = acc.account.branchBankName
                            )
                            Spacer(modifier = modifier.height(5.dp))
                            ProfileRow(
                                title = stringResource(R.string.bik),
                                content = acc.account.bik
                            )
                            Spacer(modifier = modifier.height(5.dp))
                            ProfileRow(
                                title = stringResource(R.string.setti),
                                content = acc.account.setti
                            )
                            Spacer(modifier = modifier.height(5.dp))
                            ProfileRow(
                                title = stringResource(R.string.corr),
                                content = acc.account.corr
                            )
                            Spacer(modifier = modifier.height(10.dp))
                            ProfileRow(
                                title = stringResource(R.string.inn),
                                content = acc.account.inn
                            )
                            Spacer(modifier = modifier.height(5.dp))
                            ProfileRow(
                                title = stringResource(R.string.ogrn),
                                content = acc.account.ogrn
                            )
                            Spacer(modifier = modifier.height(5.dp))
                            ProfileRow(
                                title = stringResource(R.string.kpp),
                                content = acc.account.kpp
                            )
                            Spacer(modifier = modifier.height(5.dp))
                            ProfileRow(
                                title = stringResource(R.string.okpo),
                                content = acc.account.okpo
                            )
                            Spacer(modifier = modifier.height(5.dp))
                            ProfileRow(
                                title = stringResource(R.string.okved),
                                content = acc.account.okved
                            )
                            Spacer(modifier = modifier.height(5.dp))
                            ProfileRow(
                                title = stringResource(R.string.name_treasure),
                                content = acc.account.treasuryName
                            )
                            Spacer(modifier = modifier.height(5.dp))
                            ProfileRow(
                                title = stringResource(R.string.treasure_account),
                                content = acc.account.treasuryAccount
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
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
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
                paymentMethod = PaymentMethodTYpe.CREDIT,
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
            drawerState = drawerState,
            snakeBarHostState = snackBarState,
            onAction = {},
            updateVisisbilityDrawerState = {}
        )
    }
}

@Preview(
    showSystemUi = true
)
@Composable
private fun Preview1() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
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
            name = "Ivanov II",
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
                paymentMethod = PaymentMethodTYpe.ADVANCE,
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
            drawerState = drawerState,
            snakeBarHostState = snackBarState,
            onAction = {},
            updateVisisbilityDrawerState = {}
        )
    }
}

@Preview(
    showSystemUi = true
)
@Composable
private fun Preview2() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val snackBarState = remember { SnackbarHostState() }


    BillingTheme {
        ProfileScreenScreen(
            state = ProfileScreenState(
                isLoading = true
            ),
            drawerState = drawerState,
            snakeBarHostState = snackBarState,
            onAction = {},
            updateVisisbilityDrawerState = {}
        )
    }
}