package com.therxmv.ershu.data.source.remote

import com.therxmv.ershu.data.models.AllCallsScheduleModel
import com.therxmv.ershu.data.models.AllFacultiesModel
import com.therxmv.ershu.data.models.AllSpecialtiesModel
import com.therxmv.ershu.data.models.CallScheduleModel
import com.therxmv.ershu.data.models.ScheduleModel
import com.therxmv.ershu.data.source.local.database.ERSHUDatabaseApi
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class ERSHUService(
    private val httpClient: HttpClient,
    private val ershuDatabaseApi: ERSHUDatabaseApi,
) : ERSHUApi {

    private val apiUrl = BaseUrlProvider.getUrl()

    override suspend fun getAllFaculties() = try {
        Result.Success(
            httpClient
                .get("$apiUrl/all_faculties.json")
                .body<AllFacultiesModel>()
                .also {
                    ershuDatabaseApi.setAllFaculties(it)
                }
        )
    } catch (e: Exception) {
        e.printStackTrace()
        Result.Failure(
            ershuDatabaseApi.getAllFaculties() ?: AllFacultiesModel()
        )
    }

    override suspend fun getAllSpecialties(facultyPath: String) = try {
        Result.Success(
            httpClient
                .get("$apiUrl/$facultyPath/all_years.json")
                .body<AllSpecialtiesModel>()
                .also {
                    ershuDatabaseApi.setAllSpecialties(it, facultyPath)
                }
        )
    } catch (e: Exception) {
        e.printStackTrace()
        Result.Failure(
            ershuDatabaseApi.getAllSpecialties(facultyPath) ?: AllSpecialtiesModel()
        )
    }

    override suspend fun getSchedule(facultyPath: String, year: String, specialty: String) = try {
        Result.Success(
            httpClient
                .get("$apiUrl/$facultyPath/${year}/${specialty}.json")
                .body<ScheduleModel>()
                .apply {
                    week = week.map { it.distinct() }
                }
                .also {
                    ershuDatabaseApi.setSchedule(it, specialty)
                }
        )
    } catch (e: Exception) {
        e.printStackTrace()
        Result.Failure(
            ershuDatabaseApi.getSchedule(specialty) ?: ScheduleModel()
        )
    }

    override suspend fun getCallSchedule() = try {
        val first = httpClient
            .get("$apiUrl/fmi_schedule/time1.json")
            .body<CallScheduleModel>()

        val second = httpClient
            .get("$apiUrl/fmi_schedule/time2.json")
            .body<CallScheduleModel>()

        Result.Success(
            AllCallsScheduleModel(first, second).also {
                ershuDatabaseApi.setAllCalls(it)
            }
        )
    } catch (e: Exception) {
        e.printStackTrace()
        getLocalCallSchedule()
    }

    override suspend fun getLocalCallSchedule() = Result.Failure(
        ershuDatabaseApi.getAllCalls() ?: AllCallsScheduleModel()
    )
}