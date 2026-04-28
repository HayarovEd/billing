package com.impuls.billing.ui.settings_screen

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.impuls.billing.ui.theme.BillingTheme
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
        snakeBarHostState = snakeBarHostState,
        onAction = viewModel::onAction
    )
}

@Composable
private fun SettingsScreen(
    modifier: Modifier = Modifier,
    state: SettingsScreenState,
    snakeBarHostState: SnackbarHostState,
    onAction: (SettingsScreenAction) -> Unit,
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
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .padding(15.dp)
            ) {
                state.clientInfo?.let {clientInfo->
                    ContactsCard(
                        mobile = clientInfo.mobile,
                        mobileIsConfirmed = clientInfo.mobileIsConfirmed,
                        emailIsConfirmed = clientInfo.emailIsConfirmed,
                        email = clientInfo.email,
                        count = state.count,
                        onUpdatePhone = {
                            onAction(SettingsScreenAction.UpdatePhone(
                                phone = it
                            ))
                        },
                        onUpdateEmail = {
                            onAction(SettingsScreenAction.UpdateEmail(
                                email = it
                            ))
                        },
                        onConfirmCodePhone = {
                            onAction(SettingsScreenAction.ConfirmPhone(
                                code = it
                            ))
                        },
                        onConfirmCodeEmail = {
                            onAction(SettingsScreenAction.ConfirmEmail(
                                code = it
                            ))
                        },
                        onRecentCodePhone = {
                            onAction(SettingsScreenAction.ResentCodeEmail)
                        },
                        onRecentCodeEmail = {
                            onAction(SettingsScreenAction.ResentCodePhone)
                        }
                    )
                    state.clientNotices?.let { clientNotices->
                        Spacer(modifier = Modifier.height(12.dp))
                        NoticesCard(
                            clientInfo = clientInfo,
                            clientNotices = clientNotices,
                            updateCheckSms = { checkSms, type->
                                onAction(SettingsScreenAction.UpdateCheckSms(
                                    isCheckSms = checkSms,
                                    typeId = type
                                ))
                            },
                            updateCheckEmail = { checkEmail, type->
                                onAction(SettingsScreenAction.UpdateCheckEmail(
                                    isCheckEmail = checkEmail,
                                    typeId = type
                                ))
                            },
                            updateVolume = { value, type->
                                onAction(SettingsScreenAction.UpdateValue(
                                    value = value,
                                    typeId = type
                                ))
                            },
                            onClickSave = {
                                onAction(SettingsScreenAction.UpdateNotices)
                            }
                        )
                    }
                }

            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    val snackBarState = remember { SnackbarHostState() }
    BillingTheme {
        SettingsScreen(
            state = SettingsScreenState(),
            snakeBarHostState = snackBarState,
            onAction = {}
        )
    }
}
