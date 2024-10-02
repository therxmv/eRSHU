package com.therxmv.ershu.di

import androidx.compose.runtime.Composable
import com.therxmv.ershu.analytics.AnalyticsApi
import com.therxmv.ershu.data.reminders.RemindersApi
import com.therxmv.ershu.data.reminders.event.EventProvider
import com.therxmv.ershu.data.reminders.event.EventProviderApi
import com.therxmv.ershu.data.source.local.DatabaseDriverFactory
import com.therxmv.ershu.data.source.local.database.ERSHUDatabase
import com.therxmv.ershu.data.source.local.database.ERSHUDatabaseApi
import com.therxmv.ershu.data.source.local.profile.ProfileLocalSource
import com.therxmv.ershu.data.source.local.profile.ProfileLocalSourceApi
import com.therxmv.ershu.data.source.local.reminders.RemindersLocalSource
import com.therxmv.ershu.data.source.local.reminders.RemindersLocalSourceApi
import com.therxmv.ershu.data.source.remote.ERSHUApi
import com.therxmv.ershu.data.source.remote.ERSHUService
import com.therxmv.ershu.ui.base.AppbarTitleStore
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.module
import org.koin.mp.KoinPlatform

@Composable
inline fun <reified T> getDependency(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): T {
    val koin = KoinPlatform.getKoin()
    return koin.get(qualifier, parameters)
}

val dataModule = module {
    single<ERSHUApi> {
        ERSHUService(get(), get())
//        ERSHUServiceMock()
    }

    single<ProfileLocalSourceApi> {
        ProfileLocalSource(get())
    }

    single<RemindersLocalSourceApi> {
        RemindersLocalSource(get())
    }

    single<ERSHUDatabaseApi> {
        ERSHUDatabase(get())
    }

    single {
        HttpClient {
            install(ContentNegotiation) {
                json()
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 4000
            }
        }
    }

    single {
        DatabaseDriverFactory()
    }

    single {
        RemindersApi()
    }

    single<EventProviderApi> {
        EventProvider()
    }

    single {
        AnalyticsApi()
    }

    single {
        AppbarTitleStore()
    }
}