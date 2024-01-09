package com.therxmv.ershu.data.source.remote

import com.therxmv.ershu.data.models.AllCallsScheduleModel
import com.therxmv.ershu.data.models.AllSpecialtiesModel
import com.therxmv.ershu.data.models.CallScheduleModel
import com.therxmv.ershu.data.models.ScheduleModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json

class ERSHUService : ERSHUApi {

    private val apiUrl = BaseUrlProvider.getUrl()

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    override suspend fun getAllSpecialties() = try {
        httpClient
            .get("$apiUrl/all_specialty")
            .body()
    } catch (e: Exception) {
        e.printStackTrace()
        AllSpecialtiesModel()
    }

    override suspend fun getSchedule(year: String, specialty: String) = try {
        httpClient
            .get("$apiUrl/${year}/${specialty}")
            .body<ScheduleModel>()
            .apply {
                week = week.map { it.distinct() }
            }
    } catch (e: Exception) {
        e.printStackTrace()
        ScheduleModel()
    }

    override suspend fun getCallSchedule() = try {
        val first = httpClient
            .get("$apiUrl/time/1")
            .body<CallScheduleModel>()

        val second = httpClient
            .get("$apiUrl/time/2")
            .body<CallScheduleModel>()

        AllCallsScheduleModel(first, second)
    } catch (e: Exception) {
        e.printStackTrace()
        AllCallsScheduleModel()
    }
}