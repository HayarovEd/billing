package com.impuls.billing.ui.payment_history

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun HistoryPaymentScreenRoot(
    viewModel: HistoryPaymentScreenViewModel = koinViewModel ()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {

            else -> {}
        }
    }

    HistoryPaymentScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun HistoryPaymentScreen(
    modifier: Modifier = Modifier,
    state: HistoryPaymentScreenState,
    onAction: (HistoryPaymentScreenAction) -> Unit,
) {

}

@Preview
@Composable
private fun Preview() {
    BillingTheme {
        HistoryPaymentScreen(
            state = HistoryPaymentScreenState(
                agrmId = 0
            ),
            onAction = {}
        )
    }
}