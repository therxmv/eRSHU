package com.therxmv.ershu.di

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
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import org.koin.dsl.module

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
}