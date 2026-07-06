package com.impuls.billing.di


import com.impuls.billing.data.repository.AccountRepositoryImpl
import com.impuls.billing.data.repository.CKassaPaymentRepository
import com.impuls.billing.data.repository.DataStoreRepositoryImpl
import com.impuls.billing.data.repository.JwtRepositoryImpl
import com.impuls.billing.data.repository.AuthRepositoryImpl
import com.impuls.billing.data.repository.BillingPaymentRepositoryImpl
import com.impuls.billing.data.repository.BlockingRepositoryImpl
import com.impuls.billing.data.repository.HomeRepositoryImpl
import com.impuls.billing.data.repository.ServiceRepositoryImpl
import com.impuls.billing.domain.repository.AccountRepository
import com.impuls.billing.domain.repository.DataStoreRepository
import com.impuls.billing.domain.repository.JwtRepository
import com.impuls.billing.domain.repository.PaymentRepository
import com.impuls.billing.domain.repository.AuthRepository
import com.impuls.billing.domain.repository.BillingPaymentRepository
import com.impuls.billing.domain.repository.BlockingRepository
import com.impuls.billing.domain.repository.HomeRepository
import com.impuls.billing.domain.repository.ServiceRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repoModule = module {
    singleOf(::AuthRepositoryImpl) { bind<AuthRepository>() }
    singleOf(::DataStoreRepositoryImpl) { bind<DataStoreRepository>() }
    singleOf(::CKassaPaymentRepository) { bind<PaymentRepository>() }
    singleOf(::JwtRepositoryImpl) { bind<JwtRepository>() }
    singleOf(::AccountRepositoryImpl) { bind<AccountRepository>() }
    singleOf(::HomeRepositoryImpl) {bind<HomeRepository>()}
    singleOf(::BillingPaymentRepositoryImpl) {bind<BillingPaymentRepository>()}
    singleOf(::BlockingRepositoryImpl) {bind<BlockingRepository>()}
    singleOf(::ServiceRepositoryImpl) {bind<ServiceRepository>()}
}
