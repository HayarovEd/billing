package com.impuls.billing.ui.home_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.impuls.billing.R
import com.impuls.billing.domain.model.ClientVroup
import com.impuls.billing.domain.model.ModifierData
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.ObserveAsEvents
import com.impuls.billing.ui.uikit.UiIconButton
import org.koin.androidx.compose.koinViewModel
import kotlin.random.Random

@Composable
fun HomeScreenRoot(
    viewModel: HomeScreenViewModel = koinViewModel(),
    onNavigateToLogin: () -> Unit,
    onNavigateToBack: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snakeBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            HomeScreenEvent.NavigateEvent -> {
                onNavigateToLogin()
            }

            is HomeScreenEvent.OnMessage -> snakeBarHostState.showSnackbar(
                event.message.asString(
                    context
                )
            )
        }
    }

    HomeScreen(
        state = state,
        snakeBarHostState = snakeBarHostState,
        onAction = viewModel::onAction,
        onNavigateToBack = onNavigateToBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeScreenState,
    snakeBarHostState: SnackbarHostState,
    onAction: (HomeScreenAction) -> Unit,
    onNavigateToBack: () -> Unit,
) {
    val windowSize = LocalWindowInfo.current.containerDpSize
    val windowWidth = windowSize.width
    Scaffold(
        snackbarHost = { SnackbarHost(snakeBarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    if (state.selectedVGroupId != null) {
                        Text(
                            text = state.vGroups.first { it.vgId == state.selectedVGroupId }.login,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                },
                navigationIcon = {
                    UiIconButton(
                        icon = ImageVector.vectorResource(R.drawable.arrow_back_24dp),
                        onClick = onNavigateToBack
                    )
                }
            )
        }
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
                    .fillMaxSize()
                    .padding(15.dp)
            ) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(state.vGroups) { vg ->
                        TariffCardUL(
                            data = vg,
                            isSelected = vg.vgId == state.selectedVGroupId,
                            width = if (state.vGroups.size == 1) (windowWidth * 0.9f) else windowWidth * 0.8f,
                            onClick = {
                                onAction(HomeScreenAction.SelectVgroup(vg.vgId))
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
    val snakeBarHostState = remember { SnackbarHostState() }
    val vgroups = (1..5).map {
        ClientVroup(
            agrmId = 45,
            vgId = it.toLong(),
            blocked = it % 2 == 0,
            tarifId = it * 10L,
            tarifDescr = "$it",
            login = "login $it",
            serviceVolume = 1000L,
            serviceRent = Random.nextDouble(100.0, 500.0),
            addresses = emptyList(),
            speed = 100,
            agentDescr = "Netflow ООО НПО ИМПУЛЬС",
            currentModifier = ModifierData(
                type = "ddds",
                value = Random.nextDouble(0.8, 1.0),
            )
        )
    }
    BillingTheme {
        HomeScreen(
            state = HomeScreenState(
                agrmId = 0,
                vGroups = vgroups
            ),
            snakeBarHostState = snakeBarHostState,
            onAction = {},
            onNavigateToBack = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview1() {
    val snakeBarHostState = remember { SnackbarHostState() }
    val vgroups = (1..1).map {
        ClientVroup(
            agrmId = 45,
            vgId = it.toLong(),
            blocked = it % 2 == 0,
            tarifId = it * 10L,
            tarifDescr = "$it",
            login = "login $it",
            serviceVolume = 1000L,
            agentDescr = "Netflow ООО НПО ИМПУЛЬС",
            serviceRent = Random.nextDouble(100.0, 500.0),
            addresses = emptyList(),
            speed = 100,
            currentModifier = ModifierData(
                type = "ddds",
                value = Random.nextDouble(0.8, 1.0),
            )
        )
    }
    BillingTheme {
        HomeScreen(
            state = HomeScreenState(
                agrmId = 0,
                vGroups = vgroups
            ),
            snakeBarHostState = snakeBarHostState,
            onAction = {},
            onNavigateToBack = {}
        )
    }
}

