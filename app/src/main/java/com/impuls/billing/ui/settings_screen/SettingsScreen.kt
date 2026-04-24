package com.impuls.billing.ui.settings_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreenRoot(
    viewModel: SettingsScreenViewModel = koinViewModel ()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {

            else -> {}
        }
    }

    SettingsScreenScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun SettingsScreenScreen(
    modifier: Modifier = Modifier,
    state: SettingsScreenState,
    onAction: (SettingsScreenAction) -> Unit,
) {

}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    BillingTheme {
        SettingsScreenScreen(
            state = SettingsScreenState(),
            onAction = {}
        )
    }
}
