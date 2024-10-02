package com.therxmv.ershu.data.source.remote

import com.therxmv.ershu.data.models.AllCallsScheduleModel
import com.therxmv.ershu.data.models.AllFacultiesModel
import com.therxmv.ershu.data.models.AllSpecialtiesModel
import com.therxmv.ershu.data.models.CallScheduleModel
import com.therxmv.ershu.data.models.ExamCalendarModel
import com.therxmv.ershu.data.models.LessonModel
import com.therxmv.ershu.data.models.RatingModel
import com.therxmv.ershu.data.models.ScheduleModel
import com.therxmv.ershu.data.source.local.database.ERSHUDatabaseApi
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.request.get

class ERSHUService(
    private val httpClient: HttpClient,
    private val ershuDatabaseApi: ERSHUDatabaseApi,
) : ERSHUApi {

    companion object {
        private const val SCHEDULE = "schedule"
        private const val RATING = "rating"
        private const val RES = "recours"
        private const val EXAMS = "exams"
    }

    private val apiUrl = BaseUrlProvider.getUrl()

    override suspend fun getAllFaculties() = try {
        Result.Success(
            httpClient
                .get("$apiUrl/$RES/all_faculties.json")
                .body<AllFacultiesModel>()
                .also {
                    ershuDatabaseApi.setAllFaculties(it)
                }
        )
    } catch (e: Exception) {
        e.handleException {
            ershuDatabaseApi.getAllFaculties() ?: AllFacultiesModel()
        }
    }

    override suspend fun getAllSpecialties(facultyPath: String) = try {
        Result.Success(
            httpClient
                .get("$apiUrl/$facultyPath/$SCHEDULE/all_years.json")
                .body<AllSpecialtiesModel>()
                .also {
                    ershuDatabaseApi.setAllSpecialties(it, facultyPath)
                }
        )
    } catch (e: Exception) {
        e.handleException {
            ershuDatabaseApi.getAllSpecialties(facultyPath) ?: AllSpecialtiesModel()
        }
    }

    override suspend fun getSchedule(facultyPath: String, year: String, specialty: String) = try {
        Result.Success(
            httpClient
                .get("$apiUrl/$facultyPath/$SCHEDULE/$year/$specialty.json")
                .body<ScheduleModel>()
                .apply {
                    week = week.mapSchedule()
                }
                .also {
                    ershuDatabaseApi.setSchedule(it, specialty)
                }
        )
    } catch (e: Exception) {
        e.handleException {
            ershuDatabaseApi.getSchedule(specialty) ?: ScheduleModel()
        }
    }

    override suspend fun getCallSchedule() = try {
        val first = httpClient
            .get("$apiUrl/$RES/time1.json")
            .body<CallScheduleModel>()

        val second = httpClient
            .get("$apiUrl/$RES/time2.json")
            .body<CallScheduleModel>()

        Result.Success(
            AllCallsScheduleModel(first, second).also {
                ershuDatabaseApi.setAllCalls(it)
            }
        )
    } catch (e: Exception) {
        e.handleException {
            ershuDatabaseApi.getAllCalls() ?: AllCallsScheduleModel()
        }
    }

    override suspend fun getRatingBySpecialty(faculty: String, year: String, specialty: String) = try {
        Result.Success(
            httpClient
                .get("$apiUrl/$faculty/$RATING/$year/$specialty.json")
                .body<RatingModel>()
                .also {
                    ershuDatabaseApi.setRating(it, specialty)
                }
        )
    } catch (e: Exception) {
        e.handleException {
            ershuDatabaseApi.getRating(specialty) ?: RatingModel()
        }
    }

    override suspend fun getExamCalendar(faculty: String, year: String, specialty: String): Result<ExamCalendarModel> = try {
        Result.Success(
            httpClient
                .get("$apiUrl/$faculty/$EXAMS/$year/$specialty.json")
                .body<ExamCalendarModel>()
        )
    } catch (e: Exception) {
        e.handleException {
            ExamCalendarModel()
        }
    }

    private fun <T> Exception.handleException(defaultValue: () -> T): Result<T> {
        printStackTrace()
        val isInvalidRequest = this is NoTransformationFoundException
        return Result.Failure(
            value = defaultValue(),
            isBadRequest = isInvalidRequest,
        )
    }

    private fun List<List<LessonModel>>.mapSchedule() = this.mapIndexed { day, list ->
        list.distinct().mapIndexed { index, item ->
            val number = item.lessonNumber ?: (index + 1)

            item.copy(
                lessonId = "$day-$number-${item.lessonName}"
            )
        }
    }
}