package com.therxmv.ershu.data.source.remote

import com.therxmv.ershu.data.models.AllCallsScheduleModel
import com.therxmv.ershu.data.models.AllSpecialtiesModel
import com.therxmv.ershu.data.models.CallScheduleModel
import com.therxmv.ershu.data.models.ScheduleModel
import com.therxmv.ershu.data.source.local.database.ERSHUDatabase
import com.therxmv.ershu.data.source.local.database.ERSHUDatabaseApi
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json

class ERSHUService(
    private val ershuDatabaseApi: ERSHUDatabaseApi = ERSHUDatabase(),
) : ERSHUApi {

    private val apiUrl = BaseUrlProvider.getUrl()

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    override suspend fun getAllSpecialties() = try {
        httpClient
            .get("$apiUrl/all_specialty")
            .body<AllSpecialtiesModel>()
            .also {
                ershuDatabaseApi.setAllSpecialties(it)
            }
    } catch (e: Exception) {
        e.printStackTrace()
        ershuDatabaseApi.getAllSpecialties() ?: AllSpecialtiesModel()
    }

    override suspend fun getSchedule(year: String, specialty: String) = try {
        httpClient
            .get("$apiUrl/${year}/${specialty}")
            .body<ScheduleModel>()
            .apply {
                week = week.map { it.distinct() }
            }
            .also {
                ershuDatabaseApi.setSchedule(it, specialty)
            }
    } catch (e: Exception) {
        e.printStackTrace()
        ershuDatabaseApi.getSchedule(specialty) ?: ScheduleModel()
    }

    override suspend fun getCallSchedule() = try {
        val first = httpClient
            .get("$apiUrl/time/1")
            .body<CallScheduleModel>()

        val second = httpClient
            .get("$apiUrl/time/2")
            .body<CallScheduleModel>()

        AllCallsScheduleModel(first, second).also {
            ershuDatabaseApi.setAllCalls(it)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        ershuDatabaseApi.getAllCalls() ?: AllCallsScheduleModel()
    }
}