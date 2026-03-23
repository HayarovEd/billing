package com.impuls.billing.di


import com.impuls.billing.data.repository.DataStoreRepositoryImpl
import com.impuls.billing.data.repository.RemoteRepositoryImpl
import com.impuls.billing.domain.repository.DataStoreRepository
import com.impuls.billing.domain.repository.RemoteRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repoModule = module {
    singleOf(::RemoteRepositoryImpl) { bind<RemoteRepository>() }
    singleOf(::DataStoreRepositoryImpl) { bind<DataStoreRepository>() }
    /*singleOf(::DownloadRepositoryImpl) { bind<DownloadRepository>() }
    singleOf(::ServiceRepositoryImpl) { bind<ServiceRepository>() }
    singleOf(::InstallerImpl) { bind<Installer>() }
    singleOf(::LocalRepositoryImpl) { bind<LocalRepository>() }*/

}
