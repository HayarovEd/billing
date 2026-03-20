package com.impuls.billing.di

import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.impuls.billing.domain.utils.APP_PREFERENCES
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.xml.xml
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import nl.adaptivity.xmlutil.serialization.XML
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val baseModule = module {
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { androidContext().preferencesDataStoreFile(APP_PREFERENCES) }
        )
    }
    single<HttpClient> {
        HttpClient(OkHttp) {
            install(HttpTimeout) {
                connectTimeoutMillis = 100000
                requestTimeoutMillis = 100000
                socketTimeoutMillis = 100000
            }
            defaultRequest {
                header(HttpHeaders.ContentType, "text/xml; charset=utf-8")
            }
            install(HttpCookies) {

            }
            install(ContentNegotiation) {
                xml(format = XML {
                    autoPolymorphic = true
                })
            }
            install(Logging) {
                logger = Logger.DEFAULT
            }
        }
    }

    /*single<TeleTVDatabase> {
        Room.databaseBuilder(
            androidContext(),
            TeleTVDatabase::class.java,
            TELE_TV_DB
        )
            .build()
    }*/
}