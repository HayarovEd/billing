package com.impuls.billing.ui.promise_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.impuls.billing.R
import com.impuls.billing.domain.model.PPSettings
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.ObserveAsEvents
import com.impuls.billing.ui.uikit.UiIconButton
import org.koin.androidx.compose.koinViewModel

@Composable
fun PromiseScreenRoot(
    viewModel: PromiseScreenViewModel = koinViewModel(),
    onNavigateToLogin: () -> Unit,
    onBack: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snakeBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            PromiseScreenEvent.NavigateEvent -> onNavigateToLogin()
            is PromiseScreenEvent.OnMessage -> {
                snakeBarHostState.showSnackbar(
                    event.message.asString(
                        context
                    )
                )
            }
        }
    }

    PromiseScreen(
        state = state,
        onAction = viewModel::onAction,
        snakeBarHostState = snakeBarHostState,
        onBack = onBack,
    )
}

@Composable
private fun PromiseScreen(
    modifier: Modifier = Modifier,
    state: PromiseScreenState,
    snakeBarHostState: SnackbarHostState,
    onAction: (PromiseScreenAction) -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snakeBarHostState) },
        topBar = {
            UiIconButton(
                modifier = modifier
                    .statusBarsPadding()
                    .padding(horizontal = 15.dp),
                icon = ImageVector.vectorResource(R.drawable.arrow_back_24dp),
                onClick = onBack,
            )
        }
    ) { contentPadding ->
        Column(
            modifier = modifier
                .padding(contentPadding)
                .fillMaxSize()
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (val rs = state.resultUiState) {
                is ResultUiState.Error -> {

                }
                ResultUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is ResultUiState.Success -> {
                    PromiseCard(
                        promisePayment = rs.promisePayment,
                        amount = state.amount,
                        ppSettings = rs.ppSettings,
                        textError = state.amountError,
                        updateAmount = {
                            onAction(PromiseScreenAction.UpdateAmount(it))
                        },
                        setPromise = {

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
    BillingTheme {
        PromiseScreen(
            state = PromiseScreenState(),
            snakeBarHostState = snackBarState,
            onAction = {},
            onBack = {},
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview1() {
    val snackBarState = remember { SnackbarHostState() }
    BillingTheme {
        PromiseScreen(
            state = PromiseScreenState(
                resultUiState = ResultUiState.Success(
                    promisePayment = 600.0,
                    ppSettings = PPSettings(
                        promiseAvailable = true,
                        promiseBlockDays = 5,
                        promiseFee = 0.0,
                        promiseLimit = 1.0,
                        promiseMax = 1000.0,
                        promiseMin = 10.0,
                        promiseOnDays = 10,
                        promiseRent = true,
                        promiseTill = 10
                    ),
                )
            ),
            snakeBarHostState = snackBarState,
            onAction = {},
            onBack = {}
        )
    }
}