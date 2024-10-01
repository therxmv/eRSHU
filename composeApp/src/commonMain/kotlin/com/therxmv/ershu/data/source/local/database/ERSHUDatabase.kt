package com.therxmv.ershu.data.source.local.database

import com.therxmv.ershu.data.models.AllCallsScheduleModel
import com.therxmv.ershu.data.models.AllFacultiesModel
import com.therxmv.ershu.data.models.AllSpecialtiesModel
import com.therxmv.ershu.data.models.CallScheduleModel
import com.therxmv.ershu.data.models.RatingModel
import com.therxmv.ershu.data.models.ScheduleModel
import com.therxmv.ershu.data.source.local.DatabaseDriverFactory
import com.therxmv.ershu.data.source.local.mapper.toDomain
import com.therxmv.ershu.db.ERSHUDatabase
import com.therxmv.ershu.db.Lesson

class ERSHUDatabase(
    databaseDriverFactory: DatabaseDriverFactory,
) : ERSHUDatabaseApi {

    companion object {
        private const val FIRST_SHIFT = "first"
        private const val SECOND_SHIFT = "second"
    }

    private val database = ERSHUDatabase(databaseDriverFactory.createDriver())

    override fun getAllSpecialties(faculty: String?) = AllSpecialtiesModel(
        database.specialtyQueries
            .getAllSpecialties(faculty)
            .executeAsList()
            .groupBy { it.year }.values.toList()
            .map { it.map { item -> item.toDomain() } }
    )

    override fun setAllSpecialties(allSpecialtiesModel: AllSpecialtiesModel, faculty: String?) {
        clearSpecialties(faculty)
        allSpecialtiesModel.allYears.forEachIndexed { index, list ->
            if (list.isEmpty()) {
                database.specialtyQueries.setSpecialty("${index + 1}", "", faculty)
            } else {
                list.forEach { item ->
                    database.specialtyQueries.setSpecialty("${index + 1}", item.specialtyName, faculty)
                }
            }
        }
    }

    override fun clearSpecialties(faculty: String?) {
        database.specialtyQueries.clearSpecialties(faculty)
    }

    override fun getSchedule(specialty: String) = ScheduleModel(
        database.lessonQueries
            .getAllSchedule(specialty)
            .executeAsList()
            .groupBy { it.dayOfWeek }.values.toList()
            .map { it.validate().map { item -> item.toDomain() } }
    )

    override fun setSchedule(scheduleModel: ScheduleModel, specialty: String) {
        clearSchedule(specialty)
        scheduleModel.week.forEachIndexed { index, list ->
            if (list.isEmpty()) {
                database.lessonQueries.setSchedule(
                    name = null,
                    number = null,
                    link = null,
                    dayOfWeek = "${index + 1}",
                    specialty = specialty,
                    lessonId = null,
                )
            } else {
                list.forEach { item ->
                    database.lessonQueries.setSchedule(
                        name = item.lessonName,
                        number = item.lessonNumber,
                        link = item.link,
                        dayOfWeek = "${index + 1}",
                        specialty = specialty,
                        lessonId = item.lessonId,
                    )
                }
            }
        }
    }

    override fun clearSchedule(specialty: String) {
        database.lessonQueries.clearSchedule(specialty)
    }

    override fun getAllCalls() = AllCallsScheduleModel(
        first = CallScheduleModel(
            database.callsQueries.getAllCalls(FIRST_SHIFT).executeAsList().map { it.time.orEmpty() }
        ),
        second = CallScheduleModel(
            database.callsQueries.getAllCalls(SECOND_SHIFT).executeAsList().map { it.time.orEmpty() }
        ),
    )

    override fun setAllCalls(allCallsScheduleModel: AllCallsScheduleModel) {
        clearCalls()

        allCallsScheduleModel.first.time.forEach {
            database.callsQueries.setCall(FIRST_SHIFT, it)
        }

        allCallsScheduleModel.second.time.forEach {
            database.callsQueries.setCall(SECOND_SHIFT, it)
        }
    }

    override fun clearCalls() {
        database.callsQueries.clearCalls()
    }

    override fun getAllFaculties() = AllFacultiesModel(
        database.facultyQueries.getAllFaculties().executeAsList().map { it.toDomain() }
    )

    override fun setAllFaculties(allFacultiesModel: AllFacultiesModel) {
        clearFaculties()
        allFacultiesModel.allFaculties.forEach {
            database.facultyQueries.setFaculty(it.facultyName, it.folderName)
        }
    }

    override fun clearFaculties() {
        database.facultyQueries.clearFaculties()
    }

    override fun getRating(specialty: String) = RatingModel(
        database.ratingQueries.getRating(specialty).executeAsList().map { it.toDomain() }
    )

    override fun setRating(ratingModel: RatingModel, specialty: String) {
        clearRating(specialty)
        ratingModel.list.forEach {
            database.ratingQueries.setRating(
                name = it.name,
                credits = it.credits.toLong(),
                specialty = specialty,
            )
        }
    }

    override fun clearRating(specialty: String) {
        database.ratingQueries.clearRating(specialty)
    }

    private fun List<Lesson>.validate() = if (this.size <= 1) {
        this.filter { it.name.isNullOrEmpty().not() }
    } else {
        this
    }
}