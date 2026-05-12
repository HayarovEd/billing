package com.impuls.billing.ui.promise_screen

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.impuls.billing.R
import com.impuls.billing.domain.model.PPSettings
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.ObserveAsEvents
import com.impuls.billing.ui.uikit.UiIconButton
import com.impuls.billing.ui.uikit.UiText
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

@OptIn(ExperimentalMaterial3Api::class)
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
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.promise_payment),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    UiIconButton(
                        modifier = modifier
                            .padding(horizontal = 15.dp),
                        icon = ImageVector.vectorResource(R.drawable.arrow_back_24dp),
                        onClick = onBack,
                    )
                }
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
                    val errorText = rs.message?.asString() ?: stringResource(R.string.unknown_error)
                    ErrorPromisePaymentCard(
                        errorText = errorText
                    )
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
                        isEnableUpdateButton = state.activeSetPromiseButton,
                        updateAmount = {
                            onAction(PromiseScreenAction.UpdateAmount(it))
                        },
                        setPromise = {
                            onAction(PromiseScreenAction.SetPromisePayment)
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .basicMarquee(),
                    text = stringResource(R.string.history_promise_payment),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(state.promisePayments) {pm->

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

@Preview(showSystemUi = true)
@Composable
private fun Preview2() {
    val snackBarState = remember { SnackbarHostState() }
    BillingTheme {
        PromiseScreen(
            state = PromiseScreenState(
                resultUiState = ResultUiState.Error(
                    message = UiText.DynamicString("error")
                )
            ),
            snakeBarHostState = snackBarState,
            onAction = {},
            onBack = {}
        )
    }
}