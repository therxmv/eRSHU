package com.therxmv.ershu.data.source.local.database

import com.therxmv.ershu.data.models.AllCallsScheduleModel
import com.therxmv.ershu.data.models.AllSpecialtiesModel
import com.therxmv.ershu.data.models.CallScheduleModel
import com.therxmv.ershu.data.models.LessonModel
import com.therxmv.ershu.data.models.ScheduleModel
import com.therxmv.ershu.data.models.SpecialtyModel
import com.therxmv.ershu.data.source.local.DatabaseDriverFactory
import com.therxmv.ershu.db.ERSHUDatabase
import com.therxmv.ershu.db.Lesson
import com.therxmv.ershu.db.Specialty

class ERSHUDatabase(
    databaseDriverFactory: DatabaseDriverFactory = DatabaseDriverFactory(),
) : ERSHUDatabaseApi {

    companion object {
        private const val DEFAULT_NAME = "user_name"

        private const val FIRST_SHIFT = "first"
        private const val SECOND_SHIFT = "second"
    }

    private val database = ERSHUDatabase(databaseDriverFactory.createDriver())

    override fun getProfileInfo() = database.profileQueries
        .getUserInfo(DEFAULT_NAME)
        .executeAsOneOrNull()

    override fun setProfileInfo(year: String?, specialty: String?) {
        clearUserInfo()
        database.profileQueries.setUserInfo(name = DEFAULT_NAME, year = year, specialty = specialty)
    }

    override fun clearUserInfo() {
        database.profileQueries.clearUserInfo()
    }

    override fun getAllSpecialties() = AllSpecialtiesModel(
        database.specialtyQueries
            .getAllSpecialties()
            .executeAsList()
            .groupBy { it.year }.values.toList()
            .map { it.map { item -> item.toDomain() } }
    )

    override fun setAllSpecialties(allSpecialtiesModel: AllSpecialtiesModel) {
        clearSpecialties()
        allSpecialtiesModel.allYears.forEachIndexed { index, list ->
            if (list.isEmpty()) {
                database.specialtyQueries.setSpecialty("${index + 1}", "")
            } else {
                list.forEach { item ->
                    database.specialtyQueries.setSpecialty("${index + 1}", item.specialtyName)
                }
            }
        }
    }

    override fun clearSpecialties() {
        database.specialtyQueries.clearSpecialties()
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
                database.lessonQueries.setSchedule(null, null, null, "${index + 1}", specialty)
            } else {
                list.forEach { item ->
                    database.lessonQueries.setSchedule(item.lessonName, item.lessonNumber, item.link, "${index + 1}", specialty)
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

    private fun List<Lesson>.validate() = if (this.size <= 1) {
        this.filter { it.name.isNullOrEmpty().not() }
    } else {
        this
    }

    private fun Specialty.toDomain() = SpecialtyModel(this.name.orEmpty())

    private fun Lesson.toDomain() = LessonModel(this.name, this.number.orEmpty(), this.link)
}