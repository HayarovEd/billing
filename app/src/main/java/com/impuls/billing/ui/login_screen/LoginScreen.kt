package com.impuls.billing.ui.login_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.impuls.billing.R
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.ObserveAsEvents
import com.impuls.billing.ui.uikit.UiTextField
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreenRoot(
    viewModel: LoginScreenViewModel = koinViewModel(),
    onNavigateToAccounts: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snakeBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    var isShowPassword by retain { mutableStateOf(false) }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            LoginScreenEvent.NavigateEvent -> onNavigateToAccounts()
            is LoginScreenEvent.OnMessage -> {
                snakeBarHostState.showSnackbar(event.message.asString(context))
            }
        }
    }

    LoginScreen(
        state = state,
        snakeBarHostState = snakeBarHostState,
        isShowPassword = isShowPassword,
        onAction = viewModel::onAction,
        updateIsShowPassword = {
            isShowPassword = !isShowPassword
        }
    )
}

@Composable
private fun LoginScreen(
    modifier: Modifier = Modifier,
    state: LoginScreenState,
    isShowPassword: Boolean,
    snakeBarHostState: SnackbarHostState,
    onAction: (LoginScreenAction) -> Unit,
    updateIsShowPassword: () -> Unit,
) {
    var isOpenLoginDialog by retain { mutableStateOf(false) }
    var isOpenPasswordDialog by retain { mutableStateOf(false) }

    if (isOpenLoginDialog) {
        Dialog(
            onDismissRequest = {
                isOpenLoginDialog = false
            }
        ) {
            ForgetLoginDialogContent(
                onCloseClick = {
                    isOpenLoginDialog = false
                }
            )
        }
    }

    if (isOpenPasswordDialog) {
        Dialog(
            onDismissRequest = {
                isOpenPasswordDialog = false
            }
        ) {
            ForgetPasswordDialogContent(
                onCloseClick = {
                    isOpenPasswordDialog = false
                },
                onRecoverClick = { login, number->

                }
            )
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snakeBarHostState) },
    ) { paddingValues ->
        Box(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .align(alignment = Alignment.Center)
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                UiTextField(
                    content = state.login,
                    label = stringResource(id = R.string.email_required),
                    onClickContent = {
                        onAction(LoginScreenAction.UpdateLogin(it))
                    }
                )
                Spacer(
                    modifier = modifier.height(15.dp)
                )
                UiTextField(
                    content = state.password,
                    label = stringResource(id = R.string.password_required),
                    trailingIcon = if (isShowPassword) ImageVector.vectorResource(R.drawable.baseline_visibility_off_24) else ImageVector.vectorResource(
                        R.drawable.baseline_visibility_24
                    ),
                    visualTransformation = if (isShowPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    onClickContent = {
                        onAction(LoginScreenAction.UpdatePassword(it))
                    },
                    onClickTrailingIcon = updateIsShowPassword
                )
                Spacer(
                    modifier = modifier.height(15.dp)
                )
                Button(
                    modifier = modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    shape = MaterialTheme.shapes.medium,
                    onClick = {
                        onAction(LoginScreenAction.OnLogin)
                    }) {
                    Text(
                        color = MaterialTheme.colorScheme.background,
                        text = stringResource(id = R.string.enter),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
                Spacer(modifier = modifier.height(15.dp))
                TextButton(
                    onClick = {
                        isOpenLoginDialog = true
                    }
                ) {
                    Text(
                        color = MaterialTheme.colorScheme.primary,
                        text = "${stringResource(id = R.string.fargot_login)}?",
                        style = MaterialTheme.typography.bodyLarge,
                        textDecoration = TextDecoration.Underline
                    )
                }
                Spacer(modifier = modifier.height(5.dp))
                TextButton(
                    onClick = {
                        isOpenPasswordDialog = true
                    }
                ) {
                    Text(
                        color = MaterialTheme.colorScheme.primary,
                        text = "${stringResource(id = R.string.fargot_password)}?",
                        style = MaterialTheme.typography.bodyLarge,
                        textDecoration = TextDecoration.Underline
                    )
                }
            }
        }
    }
}

@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
private fun Preview() {
    val snackBarState = remember { SnackbarHostState() }
    BillingTheme {
        LoginScreen(
            state = LoginScreenState(),
            snakeBarHostState = snackBarState,
            isShowPassword = true,
            onAction = {},
            updateIsShowPassword = {}
        )
    }
}


@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
private fun Preview1() {
    val snackBarState = remember { SnackbarHostState() }
    BillingTheme(
        darkTheme = true,
    ) {
        LoginScreen(
            state = LoginScreenState(
                login = "login1",
                password = "pass"
            ),
            snakeBarHostState = snackBarState,
            isShowPassword = false,
            onAction = {},
            updateIsShowPassword = {}
        )
    }
}



