package com.impuls.billing.ui.promise_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun PromiseScreenRoot(
    viewModel: PromiseScreenViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {

            else -> {}
        }
    }

    PromiseScreenScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun PromiseScreenScreen(
    modifier: Modifier = Modifier,
    state: PromiseScreenState,
    onAction: (PromiseScreenAction) -> Unit,
) {

}

@Preview
@Composable
private fun Preview() {
    BillingTheme {
        PromiseScreenScreen(
            state = PromiseScreenState(),
            onAction = {}
        )
    }
}