package com.impuls.billing.ui.login_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreenRoot(
    viewModel: LoginScreenViewModel = koinViewModel ()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {

            else -> {}
        }
    }

    LoginScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun LoginScreen(
    modifier: Modifier = Modifier,
    state: LoginScreenState,
    onAction: (LoginScreenAction) -> Unit,
) {

}

@Preview
@Composable
private fun Preview() {
    BillingTheme{
        LoginScreen(
            state = LoginScreenState(),
            onAction = {}
        )
    }
}


