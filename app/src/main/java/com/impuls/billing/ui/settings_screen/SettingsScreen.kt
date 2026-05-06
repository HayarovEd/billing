package com.impuls.billing.ui.settings_screen

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.impuls.billing.R
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.ContentExitDialog
import com.impuls.billing.ui.uikit.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreenRoot(
    viewModel: SettingsScreenViewModel = koinViewModel(),
    onNavigateToLogin: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snakeBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    var isVisibleDialog by retain { mutableStateOf(false) }
    var isVisibleLogOffDialog by retain { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    var visiblePhoneDialog by retain { mutableStateOf(false) }
    var visibleEmailDialog by retain { mutableStateOf(false) }
    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            SettingsScreenEvent.NavigateEvent -> onNavigateToLogin()
            is SettingsScreenEvent.OnMessage -> {
                snakeBarHostState.showSnackbar(
                    event.message.asString(
                        context
                    )
                )
            }
        }
    }

    SettingsScreen(
        state = state,
        scrollState = scrollState,
        isVisibleDialog = isVisibleDialog,
        isVisibleLogOffDialog = isVisibleLogOffDialog,
        snakeBarHostState = snakeBarHostState,
        onAction = viewModel::onAction,
        updateVisibilityDialog = {
            isVisibleDialog = it
        },
        updateVisibilityLogoffDialog = {
            isVisibleLogOffDialog = it
        }
    )
}

@Composable
private fun SettingsScreen(
    modifier: Modifier = Modifier,
    scrollState: ScrollState,
    state: SettingsScreenState,
    isVisibleDialog: Boolean,
    isVisibleLogOffDialog: Boolean,
    updateVisibilityDialog: (Boolean) -> Unit,
    updateVisibilityLogoffDialog: (Boolean) -> Unit,
    snakeBarHostState: SnackbarHostState,
    onAction: (SettingsScreenAction) -> Unit,
) {
    if (isVisibleLogOffDialog) {
        Dialog(
            onDismissRequest = {
                updateVisibilityLogoffDialog(false)
            },
            content = {
                ContentExitDialog(
                    title = stringResource(R.string.logoff_profile),
                    onConfirm = {
                        updateVisibilityLogoffDialog(false)
                        onAction(SettingsScreenAction.LogOff)
                    },
                    onCancel = {
                        updateVisibilityLogoffDialog(false)
                    }
                )
            }
        )
    }
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
                    .verticalScroll(scrollState)
                    .fillMaxSize()
                    .padding(15.dp)
            ) {
                state.clientInfo?.let { clientInfo ->
                    ContactsCard(
                        mobile = clientInfo.mobile,
                        mobileIsConfirmed = clientInfo.mobileIsConfirmed,
                        emailIsConfirmed = clientInfo.emailIsConfirmed,
                        email = clientInfo.email,
                        count = state.count,
                        onUpdatePhone = {
                            onAction(
                                SettingsScreenAction.UpdatePhone(
                                    phone = it
                                )
                            )
                        },
                        onUpdateEmail = {
                            onAction(
                                SettingsScreenAction.UpdateEmail(
                                    email = it
                                )
                            )
                        },
                        onConfirmCodePhone = {
                            onAction(
                                SettingsScreenAction.ConfirmPhone(
                                    code = it
                                )
                            )
                        },
                        onConfirmCodeEmail = {
                            onAction(
                                SettingsScreenAction.ConfirmEmail(
                                    code = it
                                )
                            )
                        },
                        onRecentCodePhone = {
                            onAction(SettingsScreenAction.ResentCodeEmail)
                        },
                        onRecentCodeEmail = {
                            onAction(SettingsScreenAction.ResentCodePhone)
                        }
                    )
                    state.clientNotices?.let { clientNotices ->
                        Spacer(modifier = Modifier.height(12.dp))
                        NoticesCard(
                            clientInfo = clientInfo,
                            clientNotices = clientNotices,
                            updateCheckSms = { checkSms, type ->
                                onAction(
                                    SettingsScreenAction.UpdateCheckSms(
                                        isCheckSms = checkSms,
                                        typeId = type
                                    )
                                )
                            },
                            updateCheckEmail = { checkEmail, type ->
                                onAction(
                                    SettingsScreenAction.UpdateCheckEmail(
                                        isCheckEmail = checkEmail,
                                        typeId = type
                                    )
                                )
                            },
                            updateVolume = { value, type ->
                                onAction(
                                    SettingsScreenAction.UpdateValue(
                                        value = value,
                                        typeId = type
                                    )
                                )
                            },
                            onClickSave = {
                                onAction(SettingsScreenAction.UpdateNotices)
                            }
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .height(12.dp))
                    ChangePasswordCard(
                        isVisibleDialog = isVisibleDialog,
                        isUpdatingPassword = state.isUpdatingPassword,
                        updateVisibilityDialog = updateVisibilityDialog,
                        onUpdatePassword = { old, new, confirm ->
                            onAction(
                                SettingsScreenAction.UpdatePassword(
                                    old = old,
                                    new = new,
                                    confirm = confirm
                                )
                            )
                        },
                        onClickLogoff = {
                            updateVisibilityLogoffDialog(true)
                        }
                    )
                }

            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    val snackBarState = remember { SnackbarHostState() }
    val mockClientInfo = com.impuls.billing.domain.model.ClientInfo(
        email = "example@mail.ru",
        emailIsConfirmed = true,
        kontPerson = "Иванов Иван Иванович",
        mobile = "79001234567",
        mobileIsConfirmed = true,
        offerIsAccepted = true,
        passIsTemporary = false,
        password = "password",
        phone = "79001234567",
        triggerContactDataReminder = false
    )
    val mockNotices = listOf(
        com.impuls.billing.domain.model.Notice(
            type = 1,
            name = "Порог предупреждения о балансе",
            descr = "Уведомление при достижении порога",
            sms = true,
            email = true,
            messenger = false,
            value = "100"
        ),
        com.impuls.billing.domain.model.Notice(
            type = 3,
            name = "Ежемесячный отчет",
            descr = "Отчет за месяц",
            sms = false,
            email = true,
            messenger = false,
            value = "0"
        )
    )
    val mockState = SettingsScreenState(
        clientInfo = mockClientInfo,
        clientNotices = com.impuls.billing.domain.model.ClientNotices(
            notices = mockNotices,
            uid = 123
        ),
        isLoading = false
    )
    val scrollState = rememberScrollState()
    BillingTheme {
        SettingsScreen(
            state = mockState,
            scrollState = scrollState,
            snakeBarHostState = snackBarState,
            isVisibleLogOffDialog = false,
            onAction = {},
            isVisibleDialog = false,
            updateVisibilityDialog = {},
            updateVisibilityLogoffDialog = {}
        )
    }
}
