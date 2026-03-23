package com.impuls.billing.ui.profile_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreenRoot(
    viewModel: ProfileScreenViewModel = koinViewModel ()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {

            else -> {}
        }
    }

    ProfileScreenScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun ProfileScreenScreen(
    modifier: Modifier = Modifier,
    state: ProfileScreenState,
    onAction: (ProfileScreenAction) -> Unit,
) {

}

@Preview
@Composable
private fun Preview() {
    BillingTheme {
        ProfileScreenScreen(
            state = ProfileScreenState(),
            onAction = {}
        )
    }
}