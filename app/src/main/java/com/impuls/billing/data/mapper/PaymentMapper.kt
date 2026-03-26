package com.impuls.billing.data.mapper

import com.impuls.billing.data.remote.check_account.SuccessCheckLsResponse
import com.impuls.billing.data.remote.payment.base_data.CompanyPaymentResponse
import com.impuls.billing.data.remote.payment_card.PaymentCardResponse
import com.impuls.billing.domain.model.payment.Comission
import com.impuls.billing.domain.model.payment.CompanyPayment
import com.impuls.billing.domain.model.payment.Mask
import com.impuls.billing.domain.model.payment.PaymentCard
import com.impuls.billing.domain.model.payment.ReceiptCard
import com.impuls.billing.domain.model.payment.ResultCard
import com.impuls.billing.domain.model.payment.ResultCheck
import com.impuls.billing.domain.model.payment.Term
import com.impuls.billing.domain.model.payment.WayToPayCard

fun CompanyPaymentResponse.convertCompanyPayment(): CompanyPayment {
    return CompanyPayment(
        amountMode = amountMode,
        bankAccount = bankAccount,
        bic = bic,
        comissions = comissionDtos.map {
            Comission(
                comissionType = it.comissionType,
                maxAmount = it.maxAmount,
                maxPayAmount = it.maxPayAmount,
                minAmount = it.minAmount,
                minPayAmount = it.minPayAmount,
                payCurrency = it.payCurrency,
                payType = it.payType,
                termDto = Term(
                    recipient = it.termDto.recipient
                ),
                value = it.value
            )
        },
        currency = currency,
        dynCom = dynCom,
        inn = inn,
        kpp = kpp,
        logoUrl = logoUrl,
        maskDtos = maskDtos.map {
            Mask(
                editable = it.editable,
                maskType = it.maskType,
                maxLength = it.maxLength,
                minLength = it.minLength,
                name = it.name,
                note = it.note,
                propertyType = it.propertyType,
                sortNumber = it.sortNumber,
                viewName = it.viewName,
                visible = it.visible
            )
        },
        multiCurrency = multiCurrency,
        name = name,
        offline = offline,
        orgCode = orgCode,
        orgName = orgName,
        pfAds = pfAds,
        readOnlyAmount = readOnlyAmount,
        receivingCurrency = receivingCurrency,
        serviceCode = serviceCode,
        serviceName = serviceName,
        serviceType = serviceType,
        tags = tags
    )
}

fun SuccessCheckLsResponse.convertToResultCheck(): ResultCheck {
    return ResultCheck(
        code = result.code,
        message = result.message,
        details = result.details
    )
}

fun PaymentCardResponse.toPaymentCard(): PaymentCard {
    return PaymentCard(
        failUrl = failUrl,
        receipt = ReceiptCard(
            createDate = receipt.createDate,
            fiscalUrl = receipt.fiscalUrl,
            fnsUrl = receipt.fnsUrl,
            infoUrl = receipt.infoUrl,
            regPayNum = receipt.regPayNum
        ),
        result = ResultCard(
            code = result.code,
            details = result.details,
            message = result.message
        ),
        successUrl = successUrl,
        wayToPay = WayToPayCard(
            methodType = wayToPay.methodType,
            type = wayToPay.type,
            url = wayToPay.url
        )
    )
}