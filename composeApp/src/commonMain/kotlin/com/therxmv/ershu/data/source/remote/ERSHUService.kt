package com.therxmv.ershu.data.source.remote

import com.therxmv.ershu.data.models.AllCallsScheduleModel
import com.therxmv.ershu.data.models.AllSpecialtiesModel
import com.therxmv.ershu.data.models.CallScheduleModel
import com.therxmv.ershu.data.models.ScheduleModel
import com.therxmv.ershu.data.source.local.database.ERSHUDatabaseApi
import com.therxmv.ershu.data.source.remote.BaseUrlProvider.HEADER_NAME
import com.therxmv.ershu.data.source.remote.BaseUrlProvider.getHeader
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header

class ERSHUService(
    private val httpClient: HttpClient,
    private val ershuDatabaseApi: ERSHUDatabaseApi,
) : ERSHUApi {

    private val apiUrl = BaseUrlProvider.getUrl()

    override suspend fun getAllSpecialties() = try {
        Result.Success(
            httpClient
                .get("$apiUrl/all_specialty") {
                    header(HEADER_NAME, getHeader())
                }
                .body<AllSpecialtiesModel>()
                .also {
                    ershuDatabaseApi.setAllSpecialties(it)
                }
        )
    } catch (e: Exception) {
        Result.Failure(
            ershuDatabaseApi.getAllSpecialties() ?: AllSpecialtiesModel()
        )
    }

    override suspend fun getSchedule(year: String, specialty: String) = try {
        Result.Success(
            httpClient
                .get("$apiUrl/${year}/${specialty}") {
                    header(HEADER_NAME, getHeader())
                }
                .body<ScheduleModel>()
                .apply {
                    week = week.map { it.distinct() }
                }
                .also {
                    ershuDatabaseApi.setSchedule(it, specialty)
                }
        )
    } catch (e: Exception) {
        Result.Failure(
            ershuDatabaseApi.getSchedule(specialty) ?: ScheduleModel()
        )
    }

    override suspend fun getCallSchedule() = try {
        val first = httpClient
            .get("$apiUrl/time/1") {
                header(HEADER_NAME, getHeader())
            }
            .body<CallScheduleModel>()

        val second = httpClient
            .get("$apiUrl/time/2") {
                header(HEADER_NAME, getHeader())
            }
            .body<CallScheduleModel>()

        Result.Success(
            AllCallsScheduleModel(first, second).also {
                ershuDatabaseApi.setAllCalls(it)
            }
        )
    } catch (e: Exception) {
        Result.Failure(
            ershuDatabaseApi.getAllCalls() ?: AllCallsScheduleModel()
        )
    }
}