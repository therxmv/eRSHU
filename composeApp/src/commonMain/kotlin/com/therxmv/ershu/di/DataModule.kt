package com.therxmv.ershu.di

import com.therxmv.ershu.data.source.remote.ERSHUApi
import com.therxmv.ershu.data.source.remote.ERSHUService
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import org.koin.dsl.module

val dataModule = module {
    single<ERSHUApi> { ERSHUService() }

    single {
        HttpClient {
            install(ContentNegotiation) {
                json()
            }
        }
    }
}