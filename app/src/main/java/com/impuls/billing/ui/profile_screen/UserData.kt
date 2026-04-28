package com.impuls.billing.ui.profile_screen

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.impuls.billing.R.drawable
import com.impuls.billing.R.string
import com.impuls.billing.domain.model.Account
import com.impuls.billing.domain.model.Agreement
import com.impuls.billing.domain.model.BillDeliveryType
import com.impuls.billing.domain.model.ClientAccount
import com.impuls.billing.domain.model.PaymentMethodType
import com.impuls.billing.domain.model.UserType
import com.impuls.billing.domain.utils.formatPhoneNumber
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.UiIconButton


@Composable
fun UserCard(
    account: Account,
    address: String,
    modifier: Modifier = Modifier,
    onNavigateToSettings: (Long) -> Unit,
) {

    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = account.name.take(2),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            modifier = Modifier.basicMarquee(),
                            text = account.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    UiIconButton(
                        icon = ImageVector.vectorResource(drawable.settings_24dp),
                        onClick = {
                            onNavigateToSettings(account.uid)
                        }
                    )
                }
            }
            Text(
                text = address,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(10.dp))

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Email с верификацией
            InfoRowWithStatus(
                icon = ImageVector.vectorResource(
                    drawable.mobile_check_24dp
                ),
                label = stringResource(string.phone),
                value = formatPhoneNumber(account.mobile),
                isConfirmed = account.mobileIsConfirmed,
                defaultContent = stringResource(string.set_phone),
                onNavigateToSettings = { onNavigateToSettings(account.uid) },
                onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant,
                successColor = MaterialTheme.colorScheme.primary,
                errorColor = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Телефон с верификацией
            InfoRowWithStatus(
                icon = ImageVector.vectorResource(
                    drawable.alternate_email_24dp
                ),
                label = stringResource(string.email),
                value = account.email,
                isConfirmed = account.emailIsConfirmed,
                defaultContent = stringResource(string.set_email),
                onNavigateToSettings = { onNavigateToSettings(account.uid) },
                onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant,
                successColor = MaterialTheme.colorScheme.primary,
                errorColor = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun InfoRowWithStatus(
    icon: ImageVector,
    label: String,
    value: String,
    isConfirmed: Boolean,
    defaultContent: String,
    onSurfaceVariant: Color,
    successColor: Color,
    errorColor: Color,
    onNavigateToSettings: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                enabled = value.isBlank(),
                onClick = onNavigateToSettings
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = onSurfaceVariant
            )
            Text(
                text = label,
                fontSize = 14.sp,
                color = onSurfaceVariant
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value.ifBlank { defaultContent },
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = if (label == "Телефон")
                    FontFamily.Monospace
                else null
            )

            Icon(
                imageVector = if (isConfirmed) ImageVector.vectorResource(
                    drawable.check_circle_24dp
                ) else ImageVector.vectorResource(
                    drawable.close_24dp
                ),
                contentDescription = "",
                modifier = Modifier.size(18.dp),
                tint = if (isConfirmed) successColor else errorColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUserCard() {
    val clientAccount = ClientAccount(
        billDeliveryName = "Email",
        account = Account(
            uid = 8273,
            doctype = 0,
            billDelivery = BillDeliveryType.COURIER,
            category = 0,
            type = UserType.UL,
            oksm = 643,
            wrongActive = 0,
            archive = false,
            ownerShip = 0,
            mobileIsConfirmed = false,
            emailIsConfirmed = false,
            offerIsAccepted = false,
            soleProprietor = false,
            login = "pycutbvk45",
            password = "342py23854",
            description = "Не использовать.",
            name = "ЯЯ_Биллинг(Т_СТ)",
            phone = "",
            fax = "",
            email = "",
            mobile = "89026355319",
            bankName = "",
            branchBankName = "",
            treasuryName = "",
            treasuryAccount = "",
            bik = "",
            setti = "",
            corr = "",
            kpp = "",
            inn = "",
            ogrn = "",
            okpo = "",
            okved = "",
            abonentName = "",
            abonentSurname = "",
            abonentPatronymic = ""
        ),
        agreements = listOf(
            Agreement(
                agrmId = 8220,
                uid = 8273,
                operId = 7357,
                curId = 1,
                bNotify = 0,
                vGroups = 20,
                penaltyMethod = 0,
                monthBlockDay = 0,
                balance = -56977.602534999998,
                credit = 900000.0,
                promiseCredit = 0.0,
                installments = 0.0,
                balanceStatus = 1,
                friendAgrmId = 0,
                paymentMethod = PaymentMethodType.CREDIT,
                blockDays = 0,
                blockMonths = 0,
                orderPayDay = 0,
                blockOrders = 0,
                blockAmount = 0.0,
                ownerId = 0,
                isDefault = 0,
                number = "Т_СТ",
                date = "2017-03-14",
                closeDon = null,
                bCheck = "0000-00-00",
                symbol = "руб",
                username = "ЯЯ_Биллинг(Т_СТ)",
                initialBalance = 0.0,
                code = ""
            )
        )
    )
    BillingTheme {
        UserCard(
            account = clientAccount.account,
            address = "Россия,край Пермский,р-н Пермский,,д Заборье,ул Зелёная,дом 7,,,,",
            onNavigateToSettings = {}
        )
    }
}