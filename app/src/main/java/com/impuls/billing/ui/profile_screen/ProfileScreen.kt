package com.impuls.billing.ui.profile_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.impuls.billing.domain.model.ClientAccount
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.ObserveAsEvents
import com.impuls.billing.ui.uikit.ProfileRow
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
            is ProfileScreenEvent.OnMessage ->  snakeBarHostState.showSnackbar(event.message.asString(context))
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
                Box(modifier = modifier
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
                    Text(
                        text = stringResource(R.string.profile),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = modifier.height(10.dp))
                    state.account?.let { acc->
                        ProfileRow(
                            title = if (acc.account.type==0) stringResource(R.string.name_company) else stringResource(R.string.name_fl),
                            content = acc.account.name
                        )
                        Spacer(modifier = modifier.height(5.dp))
                        ProfileRow(
                            title = stringResource(R.string.phone),
                            content = acc.account.phone
                        )
                        Spacer(modifier = modifier.height(5.dp))
                        ProfileRow(
                            title = stringResource(R.string.mobile_phone),
                            content = acc.account.mobile
                        )
                        Spacer(modifier = modifier.height(5.dp))
                        ProfileRow(
                            title = stringResource(R.string.email),
                            content = acc.account.email
                        )
                        if (acc.account.type==0) {
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
            billDelivery = 4,
            category = 0,
            type = 0,
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
        agreement = Agreement(
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
            paymentMethod = 2,
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
            initialBalance = 0.0
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
            billDelivery = 4,
            category = 0,
            type = 1,
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
        agreement = Agreement(
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
            paymentMethod = 2,
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
            initialBalance = 0.0
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