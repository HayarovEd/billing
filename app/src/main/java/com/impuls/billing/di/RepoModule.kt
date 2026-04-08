package com.impuls.billing.di


import com.impuls.billing.data.repository.CKassaPaymentRepository
import com.impuls.billing.data.repository.DataStoreRepositoryImpl
import com.impuls.billing.data.repository.JwtRepositoryImpl
import com.impuls.billing.data.repository.RemoteRepositoryImpl
import com.impuls.billing.data.repository.ServiceRepositoryImpl
import com.impuls.billing.domain.repository.DataStoreRepository
import com.impuls.billing.domain.repository.JwtRepository
import com.impuls.billing.domain.repository.PaymentRepository
import com.impuls.billing.domain.repository.RemoteRepository
import com.impuls.billing.domain.repository.ServiceRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repoModule = module {
    singleOf(::RemoteRepositoryImpl) { bind<RemoteRepository>() }
    singleOf(::DataStoreRepositoryImpl) { bind<DataStoreRepository>() }
    singleOf(::CKassaPaymentRepository) { bind<PaymentRepository>() }
    singleOf(::ServiceRepositoryImpl) { bind<ServiceRepository>() }
    singleOf(::JwtRepositoryImpl) { bind<JwtRepository>() }
    /*singleOf(::DownloadRepositoryImpl) { bind<DownloadRepository>() }

    singleOf(::InstallerImpl) { bind<Installer>() }
    singleOf(::LocalRepositoryImpl) { bind<LocalRepository>() }*/

}
