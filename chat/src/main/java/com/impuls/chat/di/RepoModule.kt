package com.impuls.chat.di


import android.system.Os.bind
import com.impuls.chat.data.repository.DataStoreRepositoryImpl
import com.impuls.chat.data.repository.NotificationRepositoryImpl
import com.impuls.chat.data.repository.RemoteRepositoryImpl
import com.impuls.chat.data.repository.WebSocketChatRepositoryImpl
import com.impuls.chat.domain.repository.AuthUseCase
import com.impuls.chat.domain.repository.DataStoreChatRepository
import com.impuls.chat.domain.repository.NotificationRepository
import com.impuls.chat.domain.repository.RemoteChatRepository
import com.impuls.chat.domain.repository.WebSocketChatRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repoChatModule = module {
    singleOf(::RemoteRepositoryImpl) { bind<RemoteChatRepository>() }
    singleOf(::DataStoreRepositoryImpl) { bind<DataStoreChatRepository>() }
    singleOf(::WebSocketChatRepositoryImpl) { bind<WebSocketChatRepository>() }
    singleOf(::NotificationRepositoryImpl) { bind<NotificationRepository>() }


    factoryOf (::AuthUseCase) { bind<AuthUseCase>() }
}
